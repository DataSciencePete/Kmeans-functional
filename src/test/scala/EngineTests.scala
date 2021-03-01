package com.petergrimshaw.Kmeans

import org.scalatest.FlatSpec

class EngineTests extends FlatSpec {

  "A set of datapoints instantiated and passed to the assignClosestCentroids method" should
  "be assigned to their closest centroids" in {

    val testPointPositions = Array(Array(1.0,3.0,4.0),Array(12.0,7.0,9.0),Array(1.0,1.0,1.0),Array(0.0,0.0,0.0),
      Array(6.0,1.0,2.0))
    val initialPointIndices = Array(1,1,1,1,1)
    val centroids = Array(Centroid(Array(0,0,0),0), Centroid(Array(10,10,10),1), Centroid(Array(6,2,2),2))

    val dps = testPointPositions.zip(initialPointIndices).map{ case (pos: Array[Double],ind: Int) =>
        DataPoint(pos,ind, Array("","")) }

    val correctPointIndices = Array(0,1,0,0,2)

    assert(Engine.assignClosestCentroids(centroids, dps).map( dp => dp.clusterIndex ).toSeq
    == correctPointIndices.toSeq)
  }

  "A set of datapoints and centroids passed to the execute method" should
  "return known correct means and clusters" in {

    val dataPoints = Array(
      DataPoint(Array(1.9,1.9), 0, Array("","")),
      DataPoint(Array(0.9,1.1), 0, Array("","")),
      DataPoint(Array(1.8,2.0), 0, Array("","")),
      DataPoint(Array(0.8,1.0), 0, Array("","")),
      DataPoint(Array(1.1,0.9), 0, Array("","")),
      DataPoint(Array(2.0,1.9), 0, Array("","")),
      DataPoint(Array(1.0,0.9), 0, Array("","")),
      DataPoint(Array(1.9,1.8), 0, Array("",""))
    )
    val centroids = Array(Centroid(Array(1.0,1.0),1), Centroid(Array(2.0,2.0),2))

    // Run the clustering with 5 iterations
    val results = Engine.expectMax(centroids,dataPoints, 5)
    val resCentroid = results._1
    val resClusterMem = results._2.map(dp => dp.clusterIndex)

    val correctCentPos = Array(DataPoint(Array(0.95,0.975), 0, Array("","")), DataPoint(Array(1.9,1.9), 0, Array("","")))
    val correctClustInds = Array(2,1,2,1,1,2,1,2)

    assert(resCentroid.zip(correctCentPos).map{ case (cent: Centroid, pos: DataPoint)
        => DistanceUtil.isCloseTo(cent, pos, 0.1)}.forall(_ == true) &&
      resClusterMem.toSeq == correctClustInds.toSeq)

  }


}
