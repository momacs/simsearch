/// Contents: SimSearch
/// Author:   John M. Aronis
/// Date:     May 2020

import java.util.ArrayList ;

public class SimSearch {

  public static void main(String[] args) {
    String dataFile ;
    boolean isCumulative ;
    int population, numberOfSimulations, forecastDay, numberOfRestarts, interval ;
    Data data ;
    Parameters absoluteBestParameters, restartBestParameters ;
    double theta, absoluteBestScore, restartBestScore, currentScore, correctionFactor ;
    SimulatedOutbreaks absoluteBestSimulations, restartBestSimulations, currentSimulations ;
    boolean searching ;
    ArrayList<Parameters> moves ;

    System.out.println( "# ==================== COMMAND LINE PARAMETERS ====================" ) ;
    if (args.length!=8) {
      System.out.println( "ARGS: dataFile cumulative/absolute population theta numberOfSimulations forecastDay numberOfRestarts interval" ) ;
      return ;
    }
    dataFile            = args[0] ;
    isCumulative        = (args[1].equals("cumulative")?true:false) ;
    population          = Integer.parseInt(args[2]) ;
    theta               = Double.parseDouble(args[3]) ;
    numberOfSimulations = Integer.parseInt(args[4]) ;
    forecastDay         = Integer.parseInt(args[5]) ;
    numberOfRestarts    = Integer.parseInt(args[6]) ;
    interval            = Integer.parseInt(args[7]) ;

    System.out.println( "#" ) ;
    System.out.println( "# dataFile:            " + dataFile ) ;
    System.out.println( "# isCumulative:        " + isCumulative ) ;
    System.out.println( "# population:          " + population ) ;
    System.out.println( "# theta:               " + theta ) ;
    System.out.println( "# numberOfSimulations: " + numberOfSimulations ) ;
    System.out.println( "# forecastDay:         " + forecastDay ) ;
    System.out.println( "# numberOfRestarts:    " + numberOfRestarts ) ;
    System.out.println( "# interval:            " + interval ) ;
    System.out.println( "#" ) ;

    System.out.println( "# ==================== DATA FILE ====================" ) ;
    System.out.println( "#" ) ;
    data = new Data(dataFile, isCumulative) ;
    data.print() ;
    System.out.println( "#" ) ;

    System.out.println( "# ==================== PARAMETER RANGES ====================" ) ;

    System.out.println( "#" ) ;
    Parameters.printParameterRanges() ;
    System.out.println( "#" ) ;

    System.out.println( "# ==================== SEARCHING ====================" ) ;

    System.out.println( "#" ) ;

    absoluteBestParameters = null ;
    absoluteBestSimulations = null ;
    absoluteBestScore = -Double.MAX_VALUE ;

    for (int restart=0 ; restart<numberOfRestarts ; restart++) {

      System.out.printf( "# RESTART: %1d ", restart ) ;
    
      restartBestParameters = new Parameters(population, theta, data.numberOfDays()) ;
      restartBestSimulations = new SimulatedOutbreaks(restartBestParameters, numberOfSimulations) ;
      restartBestScore = Score.leastSquares(data, restartBestSimulations, forecastDay, interval) ;

      searching = true ;
      while (searching) {
        searching = false ;
        moves = restartBestParameters.moves() ;
        for (Parameters currentParameters : moves) {
          currentSimulations = new SimulatedOutbreaks(currentParameters, numberOfSimulations) ;
          currentScore = Score.leastSquares(data, currentSimulations, forecastDay, interval) ;
          if (currentScore>restartBestScore) {
            restartBestParameters=currentParameters ;
	    restartBestSimulations = currentSimulations ;
            restartBestScore=currentScore ;
    	    searching=true ;
    	    System.out.printf( " %10.2f ", restartBestScore ) ;
  	  } else { System.out.printf( "." ) ; }
        } // for
      } // while

      System.out.println() ;

      if ( restartBestScore>absoluteBestScore ) {
	absoluteBestParameters = restartBestParameters ;
	absoluteBestSimulations = restartBestSimulations ;
        absoluteBestScore = restartBestScore ;
      }

    } // for restart

    correctionFactor = data.cases(forecastDay) / absoluteBestSimulations.mean(forecastDay) ;

    System.out.println( "#" ) ;

    System.out.println( "# ==================== BEST PARAMETERS FOUND ====================" ) ;
    System.out.println( "#" ) ;
    absoluteBestParameters.print() ;
    System.out.println( "#" ) ;
    absoluteBestSimulations.print() ;
    System.out.println( "#" ) ;
    System.out.println( "# SCORE: " + absoluteBestScore ) ;
    System.out.println( "#" ) ;

    System.out.println( "# ==================== SUMMARY ====================" ) ;
    System.out.println( "#" ) ;
    System.out.println( "# SUMMARY: current day/date/cases, data peakDay/peakDate/peakCases, forecast peakDay/peakDate/peakCases" ) ;
    System.out.println( "# SUMMARY: "  +
                        forecastDay + "/" +
		        data.date(forecastDay) + "/" +
		        data.cases(forecastDay) + "   " +
                        data.peakDay() + "/" +
		        data.date(data.peakDay()) + "/" +
		        data.cases(data.peakDay()) + "   " +
                        absoluteBestSimulations.peakDay() + "/" +
		        data.date(absoluteBestSimulations.peakDay()) + "/" +
		        correctionFactor*absoluteBestSimulations.mean(absoluteBestSimulations.peakDay())
		      ) ;
    System.out.println( "#" ) ;

    System.out.println( "# ==================== FORECAST ====================" ) ;
    System.out.println( "#" ) ;

    System.out.print( "# Day Date ActualCases ForecastCases" ) ;
    for (int simulation=0 ; simulation<absoluteBestSimulations.numberOfSimulations() ; simulation++) {
      System.out.print( " Simulation" + simulation ) ;
    }
    System.out.println() ;
    for (int day=0 ; day<data.numberOfDays() ; day++) {
      System.out.printf( "%5d %10s %10.2f %10.2f",
                         day,
			 data.date(day),
			 data.cases(day),
			 correctionFactor*absoluteBestSimulations.mean(day)
                       ) ;
      for (int simulation=0 ; simulation<absoluteBestSimulations.numberOfSimulations() ; simulation++) {
        System.out.printf( " %10.2f", correctionFactor*absoluteBestSimulations.cases(simulation,day) ) ;
      }
      System.out.println() ;
    }

  }

}

/// End-of-File
