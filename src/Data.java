/// Contents: Data for SimSearch
/// Author:   John M. Aronis
/// Date:     May 2020

import java.io.* ;
import java.util.StringTokenizer ;
import java.util.ArrayList ;

public class Data {

  private String fileName ;
  private boolean isCumulative ;
  private ArrayList<String> dates ;
  private ArrayList<Double> cases ;

  public Data(String fileName, boolean isCumulative) {
    String line, values[] ;
    this.fileName = fileName ;
    this.isCumulative = isCumulative ;
    this.dates = new ArrayList<String>() ;
    this.cases = new ArrayList<Double>() ;
    try {
      FileReader fileReader = new FileReader(fileName) ;
      BufferedReader inputFile = new BufferedReader(fileReader) ;
      double previous = 0.0 ;
      while ( (line=inputFile.readLine()) != null ) {
        if ( line.length()==0 || line.charAt(0)=='#' ) continue ;
        values=line.split(" ") ;
        dates.add( values[0] ) ;
        if ( isCumulative ) {
          cases.add( Double.parseDouble(values[1]) - previous ) ;
          previous = Double.parseDouble(values[1]) ;
        } else {
          cases.add( Double.parseDouble(values[1]) ) ;
        }
      }
    } catch (IOException e) { System.out.println("ERROR READING NATURE FILE") ; }
  }

  public String date(int day) { return dates.get(day) ; }

  public int day(String date) {
    for (int day=0 ; day<dates.size() ; day++) { if ( dates.get(day).equals(date) ) return day ; }
    return -1 ;
  }

  public int numberOfDays() { return cases.size() ; }

  public int numberOfDaysWithData() {
    int result=0 ;
    for (int day=0 ; day<cases.size() ; day++) { if ( !Double.isNaN(cases.get(day)) ) result++ ; }
    return result ;
  }

  public double cases(int day) { return cases.get(day) ; }

  public int peakDay() {
    int peakDay = 0 ;
    double peakCases = 0.0 ;
    for (int day=0 ; day<numberOfDaysWithData() ; day++) {
      if ( cases(day)>peakCases ) { peakCases = cases(day) ; peakDay = day ; }
    }
    return peakDay ;
  }

  public void print() {
      System.out.println( "# DATA FILE:                " + fileName ) ;
      System.out.println( "# CUMULATIVE:               " + isCumulative ) ;
      System.out.println( "# NUMBER OF DAYS:           " + numberOfDays() ) ;
      System.out.println( "# NUMBER OF DAYS WITH DATA: " + numberOfDaysWithData() ) ;
      System.out.println( "# PEAK DAY:                 " + peakDay() ) ;
      System.out.println( "# PEAK DATE:                " + date(peakDay()) ) ;
      System.out.println( "# PEAK CASES:               " + cases(peakDay()) ) ;

  }

}

/// End-of-File
