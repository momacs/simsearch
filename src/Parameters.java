/// Contents: Parameters for Simulation
/// Author:   John M. Aronis
/// Date:     May 2020

import java.util.ArrayList ;
import java.util.Random ;

public class Parameters {

  public static int POPULATION ;
  public static double THETA ;
  public static int NUMBER_OF_DAYS ;

  public static double MIN_FRACTION_SUSCEPTIBLE = 0.5 ;
  public static double MAX_FRACTION_SUSCEPTIBLE = 1.0 ;
  public static double FRACTION_SUSCEPTIBLE_MOVE = 0.05 ;
  public        double FRACTION_SUSCEPTIBLE ;

  public static int MIN_EXPOSED = 0 ;
  public static int MAX_EXPOSED = 0 ;
  public static int EXPOSED_MOVE = 0 ;
  public        int EXPOSED ;

  public static int MIN_INFECTED = 1 ;
  public static int MAX_INFECTED = 10 ;
  public static int INFECTED_MOVE = 1 ;
  public        int INFECTED ;

  public static double MIN_R0 = 1.5 ;
  public static double MAX_R0 = 3.0 ; 
  public static double R0_MOVE = 0.1 ;
  public        double R0 ;

  public static int MIN_LATENT = 1 ;
  public static int MAX_LATENT = 5 ;
  public static int LATENT_MOVE = 1 ;
  public        int LATENT ;

  public static int MIN_INFECTIOUS = 1 ;
  public static int MAX_INFECTIOUS = 5 ;
  public static int INFECTIOUS_MOVE = 1 ;
  public        int INFECTIOUS ;

  public static int MIN_BASELINE = 10 ;
  public static int MAX_BASELINE = 10 ;
  public static int BASELINE_MOVE = 0 ;
  public        int BASELINE ;

  public static int MIN_START_DAY = 0 ;
  public static int MAX_START_DAY = 60 ;
  public static int START_DAY_MOVE = 3 ;
  public        int START_DAY ;

  private static Random R = new Random() ;

  public Parameters(int population, double theta, int days) {
    POPULATION = population ;
    THETA = theta ;
    NUMBER_OF_DAYS = days ;
    FRACTION_SUSCEPTIBLE = randomStart(MIN_FRACTION_SUSCEPTIBLE,MAX_FRACTION_SUSCEPTIBLE) ;
    EXPOSED = randomStart(MIN_EXPOSED,MAX_EXPOSED) ;
    INFECTED = randomStart(MIN_INFECTED,MAX_INFECTED) ;
    R0 = randomStart(MIN_R0,MAX_R0) ;
    LATENT = randomStart(MIN_LATENT,MAX_LATENT) ;
    INFECTIOUS = randomStart(MIN_INFECTIOUS,MAX_INFECTIOUS) ;
    BASELINE = randomStart(MIN_BASELINE,MAX_BASELINE) ;
    START_DAY = randomStart(MIN_START_DAY,MAX_START_DAY) ;
  }

  public Parameters(int population, double theta, int days, double fraction, int exposed, int infected, double r0, int latent, int infectious, int baseline, int start) {
    POPULATION = population ;
    THETA = theta ;
    NUMBER_OF_DAYS = days ;
    FRACTION_SUSCEPTIBLE = fraction ;
    EXPOSED = exposed ;
    INFECTED = infected ;
    R0 = r0 ;
    LATENT = latent ;
    INFECTIOUS = infectious ;
    BASELINE = baseline ;
    START_DAY = start ;
  }
  
  private Parameters copy() {
    Parameters P = new Parameters(this.POPULATION,this.THETA,this.NUMBER_OF_DAYS) ;
    P.FRACTION_SUSCEPTIBLE = this.FRACTION_SUSCEPTIBLE ;
    P.EXPOSED = this.EXPOSED ;
    P.INFECTED = this.INFECTED ;
    P.R0 = this.R0 ;
    P.LATENT = this.LATENT ;
    P.INFECTIOUS = this.INFECTIOUS ;
    P.BASELINE = this.BASELINE ;
    P.START_DAY = this.START_DAY ;
    return P ;
  }

  private Parameters moveUp(int feature) {
    Parameters P = this.copy() ;
    if ( feature==0 && canMoveUp(this.FRACTION_SUSCEPTIBLE,FRACTION_SUSCEPTIBLE_MOVE,MAX_FRACTION_SUSCEPTIBLE) ) {
         P.FRACTION_SUSCEPTIBLE = moveUp(this.FRACTION_SUSCEPTIBLE,FRACTION_SUSCEPTIBLE_MOVE) ;
         return P ;
    }
    if ( feature==1 && canMoveUp(this.EXPOSED,EXPOSED_MOVE,MAX_EXPOSED) ) {
         P.EXPOSED = moveUp(this.EXPOSED,EXPOSED_MOVE) ;
         return P ;
    }
    if ( feature==2 && canMoveUp(this.INFECTED,INFECTED_MOVE,MAX_INFECTED) ) {
         P.INFECTED = moveUp(this.INFECTED,INFECTED_MOVE) ;
         return P ;
    }
    if ( feature==3 && canMoveUp(this.R0,R0_MOVE,MAX_R0) ) {
         P.R0 = moveUp(this.R0,R0_MOVE) ;
         return P ;
    }
    if ( feature==4 && canMoveUp(this.LATENT,LATENT_MOVE,MAX_LATENT) ) {
         P.LATENT = moveUp(this.LATENT,LATENT_MOVE) ;
         return P ;
    }
    if ( feature==5 && canMoveUp(this.INFECTIOUS,INFECTIOUS_MOVE,MAX_INFECTIOUS) ) {
         P.INFECTIOUS = moveUp(this.INFECTIOUS,INFECTIOUS_MOVE) ;
         return P ;
    }
    if ( feature==6 && canMoveUp(this.BASELINE,BASELINE_MOVE,MAX_BASELINE) ) {
         P.BASELINE = moveUp(this.BASELINE,BASELINE_MOVE) ;
         return P ;
    }
    if ( feature==7 && canMoveUp(this.START_DAY,START_DAY_MOVE,MAX_START_DAY) ) {
         P.START_DAY = moveUp(this.START_DAY,START_DAY_MOVE) ;
         return P ;
    }
    return null ;
  }

  private Parameters moveDown(int feature) {
    Parameters P = this.copy() ;
    if ( feature==0 && canMoveDown(this.FRACTION_SUSCEPTIBLE,FRACTION_SUSCEPTIBLE_MOVE,MIN_FRACTION_SUSCEPTIBLE) ) {
         P.FRACTION_SUSCEPTIBLE = moveDown(this.FRACTION_SUSCEPTIBLE,FRACTION_SUSCEPTIBLE_MOVE) ;
         return P ;
    }
    if ( feature==1 && canMoveDown(this.EXPOSED,EXPOSED_MOVE,MIN_EXPOSED) ) {
         P.EXPOSED = moveDown(this.EXPOSED,EXPOSED_MOVE) ;
         return P ;
    }
    if ( feature==2 && canMoveDown(this.INFECTED,INFECTED_MOVE,MIN_INFECTED) ) {
         P.INFECTED = moveDown(this.INFECTED,INFECTED_MOVE) ;
         return P ;
    }
    if ( feature==3 && canMoveDown(this.R0,R0_MOVE,MIN_R0) ) {
         P.R0 = moveDown(this.R0,R0_MOVE) ;
         return P ;
    }
    if ( feature==4 && canMoveDown(this.LATENT,LATENT_MOVE,MIN_LATENT) ) {
         P.LATENT = moveDown(this.LATENT,LATENT_MOVE) ;
         return P ;
    }
    if ( feature==5 && canMoveDown(this.INFECTIOUS,INFECTIOUS_MOVE,MIN_INFECTIOUS) ) {
         P.INFECTIOUS = moveDown(this.INFECTIOUS,INFECTIOUS_MOVE) ;
         return P ;
    }
    if ( feature==6 && canMoveDown(this.BASELINE,BASELINE_MOVE,MIN_BASELINE) ) {
         P.BASELINE = moveDown(this.BASELINE,BASELINE_MOVE) ;
         return P ;
    }
    if ( feature==7 && canMoveDown(this.START_DAY,START_DAY_MOVE,MIN_START_DAY) ) {
         P.START_DAY = moveDown(this.START_DAY,START_DAY_MOVE) ;
         return P ;
    }
    return null ;
  }

  public ArrayList<Parameters> moves() {
    ArrayList<Parameters> moves = new ArrayList<Parameters>() ;
    Parameters move ;
    for (int i=0 ; i<8 ; i++ ) { move = this.moveUp(i) ; if (move!=null) moves.add(move) ; }
    for (int i=0 ; i<8 ; i++ ) { move = this.moveDown(i) ; if (move!=null) moves.add(move) ; }
    return moves ;
  }
  
  static int randomStart(int min, int max) { return R.nextInt((max+1)-min)+min ; }

  static boolean canMoveUp(int current, int move, int max) { return current+move<=max ; }

  static int moveUp(int current, int move) { return current+move ; }

  static boolean canMoveDown(int current, int move, int min) { return current-move>=min ; }

  static int moveDown(int current, int move) { return current-move ; }

  static double randomStart(double min, double max) { return min+R.nextDouble()*(max-min) ; }

  static boolean canMoveUp(double current, double move, double max) { return current+move<=max ; }

  static double moveUp(double current, double move) { return current+move ; }

  static boolean canMoveDown(double current, double move, double min) { return current-move>=min ; }

  static double moveDown(double current, double move) { return current-move ; }

  public static void printParameterRanges() {
    System.out.println( "# MIN_FRACTION_SUSCEPTIBLE:  " + MIN_FRACTION_SUSCEPTIBLE ) ;
    System.out.println( "# MAX_FRACTION_SUSCEPTIBLE:  " + MAX_FRACTION_SUSCEPTIBLE ) ;
    System.out.println( "# FRACTION_SUSCEPTIBLE_MOVE: " + FRACTION_SUSCEPTIBLE_MOVE ) ;
    System.out.println( "# MIN_EXPOSED:               " + MIN_EXPOSED ) ;
    System.out.println( "# MAX_EXPOSED:               " + MAX_EXPOSED ) ;
    System.out.println( "# EXPOSED_MOVE:              " + EXPOSED_MOVE ) ;
    System.out.println( "# MIN_INFECTED:              " + MIN_INFECTED ) ;
    System.out.println( "# MAX_INFECTED:              " + MAX_INFECTED ) ;
    System.out.println( "# INFECTED_MOVE:             " + INFECTED_MOVE ) ;
    System.out.println( "# MIN_R0:                    " + MIN_R0 ) ;
    System.out.println( "# MAX_R0:                    " + MAX_R0 ) ;
    System.out.println( "# R0_MOVE:                   " + R0_MOVE ) ;
    System.out.println( "# MIN_LATENT:                " + MIN_LATENT ) ;
    System.out.println( "# MAX_LATENT:                " + MAX_LATENT ) ;
    System.out.println( "# LATENT_MOVE:               " + LATENT_MOVE ) ;
    System.out.println( "# MIN_INFECTIOUS:            " + MIN_INFECTIOUS ) ;
    System.out.println( "# MAX_INFECTIOUS:            " + MAX_INFECTIOUS ) ;
    System.out.println( "# INFECTIOUS_MOVE:           " + INFECTIOUS_MOVE ) ;
    System.out.println( "# MIN_BASELINE:              " + MIN_BASELINE ) ;
    System.out.println( "# MAX_BASELINE:              " + MAX_BASELINE ) ;
    System.out.println( "# BASELINE_MOVE:             " + BASELINE_MOVE ) ;
    System.out.println( "# MIN_START_DAY:             " + MIN_START_DAY ) ;
    System.out.println( "# MAX_START_DAY:             " + MAX_START_DAY ) ;
    System.out.println( "# START_DAY_MOVE:            " + START_DAY_MOVE ) ;
  }

  public void print() {
    System.out.println( "# POPULATION:           " + POPULATION ) ;
    System.out.println( "# THETA:                " + THETA ) ;
    System.out.println( "# NUMBER_OF_DAYS:       " + NUMBER_OF_DAYS ) ;
    System.out.println( "# FRACTION_SUSCEPTIBLE: " + FRACTION_SUSCEPTIBLE ) ;
    System.out.println( "# EXPOSED:              " + EXPOSED ) ;
    System.out.println( "# INFECTED:             " + INFECTED ) ;
    System.out.println( "# R0:                   " + R0 ) ;
    System.out.println( "# LATENT:               " + LATENT ) ;
    System.out.println( "# INFECTIOUS:           " + INFECTIOUS ) ;
    System.out.println( "# BASELINE:             " + BASELINE ) ;
    System.out.println( "# START_DAY:            " + START_DAY ) ;
  }
  
}

/// End-of-File

