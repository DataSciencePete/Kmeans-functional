package com.petergrimshaw.Kmeans

case class Centroid(position: Array[Double], clusterIndex: Int = 0) extends Point

object Centroid {

  def computeMean(cent: Centroid, dps: Array[DataPoint]): Array[Double] = {
      val assignedDataPoints = dps.filter(_.clusterIndex == cent.clusterIndex).map(dp =>
        dp.position)
      val numDimensions = dps(0).position.length
      //Check the length of all passed arrays are equal
      assert( dps.map(dp => dp.position.length).foldLeft(numDimensions)( (x,y) => if (x==y) y else 0)
        == numDimensions)
      //Initialise an array of zeroes with length matching dps parameter
      val zeroArr = Array.fill[Double](numDimensions)(0.0)
      //Sum each dimension of the arrays
      val dpsSummed = assignedDataPoints.foldLeft(zeroArr)( (x: Array[Double],y: Array[Double])
        => x.zip(y).map{ case (x: Double , y: Double ) => x + y } )
      //Return the mean value in each dimension of the array
      dpsSummed.map(x => x/assignedDataPoints.length.toDouble )
  }

}
