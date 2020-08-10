/// Contents: Simulated outbreaks
/// Author:   John M. Aronis
/// Date:     May 2020

import java.io.* ;
import java.util.StringTokenizer ;
import java.util.Random ;

public class SimulatedOutbreaks {

  private static Random R = new Random() ;
  private double[][] simulations ;

  public SimulatedOutbreaks(Parameters parameters, int numberOfSimulations)
  {
    int population = parameters.POPULATION ;
    double theta = parameters.THETA ;
    int numberOfDays = parameters.NUMBER_OF_DAYS ;

    double fractionSusceptible = parameters.FRACTION_SUSCEPTIBLE ;
    int exposed = parameters.EXPOSED ;
    int infected = parameters.INFECTED ;
    double r0 = parameters.R0 ;
    int latent = parameters.LATENT ;
    int infectious = parameters.INFECTIOUS ;
    int baseline = parameters.BASELINE ;
    int startDay = parameters.START_DAY ;

    simulations = runPRAM(population, numberOfDays, (int)(fractionSusceptible*population), exposed, infected, r0, latent, infectious, numberOfSimulations) ;
    simulations = setStartDay(simulations, startDay) ;
    simulations = addBaseline(simulations, baseline) ;
    simulations = selectCases(simulations, theta) ;

  }

private void printSimulations(double[][] simulations, int interval) {
  for (int s=0 ; s<simulations.length ; s++) {
    for (int d=0 ; d<simulations[0].length ; d+=interval) { System.out.printf( "%10.2f ", simulations[s][d] ) ; }
    System.out.println() ;
  }
}

  private double[][] runPRAM(int population, int numberOfDays, int susceptible, int exposed, int infected, double R0, int latent, int infectious, int numberOfSimulations)
  {
    double[][] result = new double[numberOfSimulations][numberOfDays] ;
    String args, inputLine ;
    Process p ;
    args = population + " " + susceptible + " " + exposed + " " + infected + " " + R0 + " " + latent + " " + infectious + " " + numberOfDays + " " + numberOfSimulations ;
    try {
      p = Runtime.getRuntime().exec("./RunPRAM.sh " + args);
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
      for (int simulation=0 ; simulation<numberOfSimulations ; simulation++) {
inputLine = input.readLine() ;
if ( inputLine.split(" ").length == numberOfDays) { result[simulation] = convertStringToArrayOfDoubles(inputLine) ; }
else { result[simulation] = dummySimulation(numberOfDays) ; }
      }
      input.close();  
    } catch (Exception e) { System.out.println("ERROR WHILE CREATING SEIR CURVES") ; } ;
    return result ;
  }

  private double[] convertStringToArrayOfDoubles(String inputString) {
    String[] splitString = inputString.split(" ") ;
    double[] arrayOfDoubles = new double[splitString.length] ;
    for (int i=0 ; i<splitString.length ; i++) { arrayOfDoubles[i] = Double.parseDouble(splitString[i]) ; }
    return arrayOfDoubles ;
  }

private double[] dummySimulation(int numberOfDays) {
  double[] result = new double[numberOfDays] ;
  for (int d=0 ; d<numberOfDays ; d++) result[d]=0.0 ;
  return result ;
}

  private double[][] setStartDay(double[][] simulations, int startDay) {
    double[][] result = new double[numberOfSimulations()][numberOfDays()] ;
    for (int s=0 ; s<numberOfSimulations() ; s++) { for (int d=0 ; d<numberOfDays() ; d++) {
      if (d<startDay) result[s][d] = 0.0 ; else result[s][d] = simulations[s][d-startDay] ;
    }}
    return result ;
  }

  private double[][] addBaseline(double[][] simulations, int baseline) {
    double[][] result = new double[numberOfSimulations()][numberOfDays()] ;
    for (int s=0 ; s<numberOfSimulations() ; s++) { for (int d=0 ; d<numberOfDays() ; d++) {
      result[s][d] = simulations[s][d] + baseline ;
    }}
    return result ;
  }

  private double[][] selectCases(double[][] simulations, double theta) {
    double[][] result = new double[numberOfSimulations()][numberOfDays()] ;
    for (int s=0 ; s<numberOfSimulations() ; s++) { for (int d=0 ; d<numberOfDays() ; d++) {
      result[s][d] = select( (int)simulations[s][d], theta) ;
    }}
    return result ;
  }

  private int select(int n, double p) {
     double log_q = Math.log(1.0 - p);
     int x = 0;
     double sum = 0;
     for(;;) { sum += Math.log(Math.random()) / (n - x) ; if (sum < log_q) return x ; x++ ; }
  }

//  private int select(int n, double p) {
//    int result = 0 ;
//    for (int i=0 ; i<n ; i++) { if (R.nextDouble()<p) result++ ; }
//    return result ;
//  }

  public int numberOfSimulations() { return simulations.length ; }

  public int numberOfDays() { return simulations[0].length ; }

  public double cases(int simulation, int day) { return simulations[simulation][day] ; }

  public double mean(int day) {
    double total = 0.0 ;
    for (int s=0 ; s<numberOfSimulations() ; s++) { total += simulations[s][day] ; }
    return total/numberOfSimulations() ;
  }

  public double variance(int day) {
    double mean=mean(day), result=0.0 ;
    for (int row=0 ; row<numberOfSimulations() ; row++) { result += (cases(row,day)-mean)*(cases(row,day)-mean) ; }
    return result/numberOfSimulations() ;
  }

  public int peakDay() {
    int peakDay = 0 ;
    double peakCases = 0.0 ;
    for (int day=0 ; day<numberOfDays() ; day++) {
      if ( mean(day)>peakCases ) { peakCases = mean(day) ; peakDay = day ; }
    }
    return peakDay ;
  }

  public void print() {
    System.out.println( "# NUMBER OF SIMULATIONS: " + numberOfSimulations() ) ;
    System.out.println( "# NUMBER OF DAYS:        " + numberOfDays() ) ;
    System.out.println( "# PEAK DAY:              " + peakDay() ) ;
    System.out.println( "# PEAK MEAN:             " + mean(peakDay()) ) ;
  }

}

/// End-of-File

