///
/// Contents: Basic matrix algebra and statistics.
/// Date:     May 2020
///

public class Matrix {

  ///
  /// Instance variables:
  ///

  private final int numberOfRows ;
  private final int numberOfColumns ;
  private final double[][] data ;

  ///
  /// Basic constructors and access:
  ///

  public Matrix(double[][] data) {
    numberOfRows = data.length ;
    numberOfColumns = data[0].length ;
    this.data = new double[numberOfRows][numberOfColumns] ;
    for (int i = 0 ; i < numberOfRows ; i++) {
      for (int j = 0 ; j < numberOfColumns ; j++) this.data[i][j] = data[i][j] ;
    }
  }

  public Matrix(Matrix A) { this(A.data) ; }

  public Matrix(int numberOfRows, int numberOfColumns) {
      this.numberOfRows = numberOfRows ;
      this.numberOfColumns = numberOfColumns ;
      data = new double[numberOfRows][numberOfColumns] ;
  }

  public static Matrix identity(int numberOfColumns) {
    Matrix I = new Matrix(numberOfColumns, numberOfColumns) ;
    for (int i = 0 ; i < numberOfColumns ; i++) I.data[i][i] = 1 ;
    return I ;
  }

  public int rows() { return numberOfRows ; }

  public int columns() { return numberOfColumns ; }

  public double value(int row, int column) { return data[row][column] ; }

  public void print() {
    for (int row=0 ; row<numberOfRows ; row++) {
      for (int column=0 ; column<numberOfColumns ; column++) {
        System.out.printf( "%10.4f ", data[row][column] ) ;
      }
      System.out.println() ;
    }
  }

  ///
  /// Basic matrix operations:
  ///

  private void swap(int i, int j) {
    double[] temp = data[i] ;
    data[i] = data[j] ;
    data[j] = temp ;
  }

  public Matrix transpose() {
    Matrix A = new Matrix(numberOfColumns, numberOfRows) ;
    for (int i = 0 ; i < numberOfRows ; i++) {
      for (int j = 0 ; j < numberOfColumns ; j++) A.data[j][i] = this.data[i][j] ;
    }
    return A ;
  }

  public Matrix plus(Matrix B) {
    Matrix A = this ;
    Matrix C = new Matrix(numberOfRows, numberOfColumns) ;
    for (int i = 0 ; i < numberOfRows ; i++) {
      for (int j = 0 ; j < numberOfColumns ; j++) C.data[i][j] = A.data[i][j] + B.data[i][j] ;
    }
    return C ;
  }

  public Matrix minus(Matrix B) {
    Matrix A = this ;
    Matrix C = new Matrix(numberOfRows, numberOfColumns) ;
    for (int i = 0 ; i < numberOfRows ; i++) {
      for (int j = 0 ; j < numberOfColumns ; j++) C.data[i][j] = A.data[i][j] - B.data[i][j] ;
    }
    return C ;
  }

  public boolean eq(Matrix B) {
    Matrix A = this ;
    for (int i = 0 ; i < numberOfRows ; i++) {
      for (int j = 0 ; j < numberOfColumns ; j++) if (A.data[i][j] != B.data[i][j]) return false ;
    }
    return true ;
  }

  public Matrix times(Matrix B) {
    Matrix A = this ;
    // A.numberOfColumns=columns, B.numberOfRows=rows
    if (A.numberOfColumns != B.numberOfRows) throw new RuntimeException("Illegal matrix dimensions.") ;
    Matrix C = new Matrix(A.numberOfRows, B.numberOfColumns) ;
    for (int i = 0 ; i < C.numberOfRows ; i++) {
      for (int j = 0 ; j < C.numberOfColumns ; j++) {
        for (int k = 0 ; k < A.numberOfColumns ; k++) { C.data[i][j] += (A.data[i][k] * B.data[k][j]) ; }
      }
    }
    return C ;
  }

  // return x = A^-1 b, assuming A is square and has full rank
  public Matrix solve(Matrix rhs) {
    if (numberOfRows != numberOfColumns || rhs.numberOfRows != numberOfColumns || rhs.numberOfColumns != 1) throw new RuntimeException("Illegal matrix dimensions.") ;
    Matrix A = new Matrix(this) ;
    Matrix b = new Matrix(rhs) ;
    for (int i = 0 ; i < numberOfColumns ; i++) {
      int max = i ;
      for (int j = i + 1 ; j < numberOfColumns ; j++) {
        if (Math.abs(A.data[j][i]) > Math.abs(A.data[max][i])) max = j ;
      }
      A.swap(i, max) ;
      b.swap(i, max) ;
      if (A.data[i][i] == 0.0) throw new RuntimeException("Matrix is singular.") ;
      for (int j = i + 1 ; j < numberOfColumns ; j++) {
        b.data[j][0] -= b.data[i][0] * A.data[j][i] / A.data[i][i] ;
      }
      for (int j = i + 1 ; j < numberOfColumns ; j++) {
        double m = A.data[j][i] / A.data[i][i] ;
        for (int k = i+1 ; k < numberOfColumns ; k++) A.data[j][k] -= A.data[i][k] * m ;
        A.data[j][i] = 0.0 ;
      }
    }
    Matrix x = new Matrix(numberOfColumns, 1) ;
    for (int j = numberOfColumns - 1 ; j >= 0 ; j--) {
      double t = 0.0 ;
      for (int k = j + 1 ; k < numberOfColumns ; k++) t += A.data[j][k] * x.data[k][0] ;
      x.data[j][0] = (b.data[j][0] - t) / A.data[j][j] ;
    }
    return x ;
  }

  public double determinant() { return determinant(this.data) ; }

  private static double determinant (double[][] matrix) {
    double temporary[][] ;
    double result = 0 ;
    if (matrix.length == 1) {
      result = matrix[0][0] ;
      return (result) ;
    }
    if (matrix.length == 2) {
      result = ((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0])) ;
      return (result) ;
    }
    for (int i = 0 ; i < matrix[0].length ; i++) {
      temporary = new double[matrix.length - 1][matrix[0].length - 1] ;
      for (int j = 1 ; j < matrix.length ; j++) {
        for (int k = 0 ; k < matrix[0].length ; k++) {
          if (k < i) { temporary[j - 1][k] = matrix[j][k] ; }
	  else if (k > i) { temporary[j - 1][k - 1] = matrix[j][k] ; }
        }
      }
      result += matrix[0][i] * Math.pow (-1, (double) i) * determinant(temporary) ;
    }
    return (result) ;
  }

  ///
  /// Statistics:
  ///

  public double mean(int column) {
    double result = 0.0 ;
    for (int row=0 ; row<numberOfRows ; row++) { result += value(row,column) ; }
    return result/numberOfRows ;
  }

  public Matrix means() {
    double[][] means = new double[1][numberOfColumns] ;
    for (int column=0 ; column<numberOfColumns ; column++) { means[0][column] = mean(column) ; }
    return new Matrix(means) ;
  }

  public double variance(int column) {
    double mean=mean(column), result=0.0 ;
    for (int row=0 ; row<numberOfRows ; row++) { result += (value(row,column)-mean)*(value(row,column)-mean) ; }
    return result/numberOfRows ;
  }

  public Matrix variances() {
    double[][] variances = new double[1][numberOfColumns] ;
    for (int column=0 ; column<numberOfColumns ; column++) { variances[0][column] = variance(column) ; }
    return new Matrix(variances) ;
  }

  public double covariance(int column1, int column2) {
    double mean1=mean(column1), mean2=mean(column2), result=0.0 ;
    for (int row=0 ; row<numberOfRows ; row++) { result += (value(row,column1)-mean1) * (value(row,column2)-mean2) ; }
    return result/numberOfRows ;
  }

  public Matrix covariance() {
    double[][] covariances = new double[numberOfColumns][numberOfColumns] ;
    for (int column1=0 ; column1<numberOfColumns ; column1++) {
      for (int column2=0 ; column2<numberOfColumns ; column2++) { covariances[column1][column2] = covariance(column1,column2) ; }
    }
    return new Matrix(covariances) ;
  }

}

/// End-of-File
