package com.petergrimshaw.Kmeans

import org.scalatest.FlatSpec

class CentroidTests extends FlatSpec {

"Centroid instantiated and passed an array of data points to its computeMean method" should
"return the mean of datapoints with the same cluster index" in {
  val p1 = DataPoint(Array(1.0,1.0),2, Array("",""))
  val p2 = DataPoint(Array(1.0,2.0),2, Array("",""))
  val p3 = DataPoint(Array(10.0,3.0),2, Array("",""))
  val p4 = DataPoint(Array(3.0,3.0),1, Array("",""))

  val dps = Array(p1,p2,p3,p4)
  val c1 = Centroid(Array(2.0,1.0),2)
  assert(Centroid.computeMean(c1, dps).toSeq == Array(4.0,2.0).toSeq)
}


}
