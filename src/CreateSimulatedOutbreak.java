public class CreateSimulatedOutbreak {

    static int POPULATION = 2000000 ;
    static double THETA = 0.1 ;
    static int NUMBER_OF_DAYS = 366 ;
    static double FRACTION_SUSCEPTIBLE = 0.9458262177465363 ;
    static int EXPOSED = 0 ;
    static int INFECTED = 100 ;
    static double R0 = 2.2358551748014124 ;
    static int LATENT = 2 ;
    static int INFECTIOUS = 2 ;
    static int BASELINE = 10 ;
    static int START_DAY = 1 ;
    static int NUMBER_OF_SIMULATIONS = 5 ;

  public static void main(String[] args) {
    Data data ;
    Parameters parameters ;
    SimulatedOutbreaks simulations ;

    System.out.println( "# SIMULATED OUTREAK" ) ;
    System.out.println( "#" ) ;
    
    parameters = new Parameters(POPULATION,THETA,NUMBER_OF_DAYS,FRACTION_SUSCEPTIBLE,EXPOSED,INFECTED,R0,LATENT,INFECTIOUS,BASELINE,START_DAY) ;
    parameters.print() ;
    System.out.println( "#" ) ;

    simulations = new SimulatedOutbreaks(parameters,NUMBER_OF_SIMULATIONS) ;
    simulations.print() ;
    System.out.println( "#" ) ;

    for (int day=0 ; day<simulations.numberOfDays() ; day++) {
      System.out.println( "DAY" + day + " " + simulations.mean(day) ) ;
    }
    
  }

}
