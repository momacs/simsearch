/// Contents: Scoring methods for SimSearch
/// Author:   John M. Aronis
/// Date:     May 2020

import java.util.ArrayList ;

public class Score {

  public static double leastSquares(Data data, SimulatedOutbreaks simulations, int currentDay, int interval) {
    double difference, score = 0.0 ;
    for (int day=0 ; day<currentDay ; day+=interval) {
      difference = data.cases(day)-simulations.mean(day) ;
      score += difference*difference ;
    }
    return -score ;
  }

  public static double covariance(Data data, SimulatedOutbreaks simulations, int currentDay, int interval) {
    Matrix D, S, mu, xMinusMu, xMinusMuTranspose, covariance ;
    double exponent, determinant, denominator ;
    int n ;
    D = extractMatrixFromData(data, currentDay, interval) ;
    S = extractMatrixFromSimulations(simulations, currentDay, interval) ;
    mu = S.means() ;
    xMinusMu = D.minus(mu) ;
    xMinusMuTranspose = xMinusMu.transpose() ;
    covariance = S.covariance() ;
    exponent = (-1.0/2.0) * (xMinusMu.times(covariance.solve(xMinusMuTranspose)).value(0,0)) ;
    determinant = covariance.determinant() ;
    n = D.columns() ;
    denominator = Math.sqrt(Math.pow(2.0*Math.PI,n)*determinant) ;
    return (Math.log(1.0)-Math.log(denominator)) + (exponent*Math.log(Math.E)) ;
  }

  public static double noCovariance(Data data, SimulatedOutbreaks simulations, int currentDay, int interval) {
    double result = 0.0, mu, variance, sigma, x, exponent, denominator ;
    for (int d=0 ; d<currentDay ; d+=interval) {
      mu = simulations.mean(d) ;
      variance = simulations.variance(d) ;
      sigma = Math.sqrt(variance) ;
      x = data.cases(d) ;
      denominator = sigma*Math.sqrt(2.0*Math.PI) ;
      exponent = -( ((x-mu)*(x-mu)) / (2.0*variance) ) ;
result += (Math.log(1.0)-Math.log(denominator)) + (exponent*Math.log(Math.E)) ;
    }
    return result ;
  }

  private static Matrix extractMatrixFromData(Data data, int currentDay, int interval) {
    int numberOfSamples = currentDay/interval ;
    double[][] values = new double[1][numberOfSamples] ;
    for (int sample=0 ; sample<numberOfSamples ; sample++) {
      values[0][sample] = data.cases( sample*interval ) ;
    }
    return new Matrix(values) ;
  }

  private static Matrix extractMatrixFromSimulations(SimulatedOutbreaks simulations, int currentDay, int interval) {
    int numberOfSamples = currentDay/interval ;
    double[][] values = new double[simulations.numberOfSimulations()][numberOfSamples] ;
    for (int simulation=0 ; simulation<simulations.numberOfSimulations() ; simulation++) {
      for (int sample=0 ; sample<numberOfSamples ; sample++) {
        values[simulation][sample] = simulations.cases( simulation, sample*interval ) ;
      }
    }
    return new Matrix(values) ;
  }

}

/// End-of-File
