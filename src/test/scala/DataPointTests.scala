package com.petergrimshaw.Kmeans

import org.scalatest.FlatSpec

class DataPointTests extends FlatSpec {

  "Data point instantiated and distance checked against another data point" should
  "return the correct distance" in {
    val p1 = DataPoint(Array(1.0,1.0),1, Array("",""))
    val p2 = DataPoint(Array(3.0,3.0),1, Array("",""))
    assert(DistanceUtil.getDistance(p1, p2)==Math.sqrt(8.0))
  }

  "Data point passed an array of centroids in its getClosestCentroid method" should
  "return the closest centroid" in {
    val p1 = DataPoint(Array(1.0,1.0),1, Array("",""))
    val p2 = DataPoint(Array(3.0,3.0),1, Array("",""))
    val c1 = Centroid(Array(2.0,2.0),1)
    val c2 = Centroid(Array(2.0,2.5),1)
    val testCentroids = Array(c1,c2)
    assert(DataPoint.getClosestCentroid(p1, testCentroids) == c1 && DataPoint.getClosestCentroid(p2, testCentroids) == c2)
  }


}
