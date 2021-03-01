package com.petergrimshaw.Kmeans

case class DataPoint(position: Array[Double], clusterIndex: Int, fieldNames: Array[String]) extends Point

object DataPoint {

  //Function to retrieve the closest in an array of centroids to a point object
  def getClosestCentroid[T <: Point, U <: Point](dp: T, centroids: Array[U]): U = {
    val orderedCentroids = centroids.map{
        cent => (cent, DistanceUtil.getDistance(cent, dp))}
      .sortWith(_._2 < _._2)
    orderedCentroids(0)._1
  }

}
