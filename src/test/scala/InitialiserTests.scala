package com.petergrimshaw.Kmeans

import org.scalatest.{FlatSpec, Matchers}

class InitialiserTests extends FlatSpec with Matchers {

  val centroids = Array(
    DataPoint(Array(1,4), 0, Array("","")),
    DataPoint(Array(2,3), 1, Array("","")),
    DataPoint(Array(3,3), 2, Array("","")),
    DataPoint(Array(3,4), 3, Array("",""))
  )

  "A candidate centroid further from the existing set of centroids than their closest distance" should
  "be included in the set of centroids" in {

    val candCent1 = DataPoint(Array(4,1), 4, Array("",""))
    val candCent2 = DataPoint(Array(2,4), 5, Array("",""))

    val updatedCentroidsPositions1 = Initialiser.updateCandidateCentroids(candCent1, centroids).map( cent => cent.position)
    val updatedCentroidsPositions2 = Initialiser.updateCandidateCentroids(candCent2,
      centroids).map( cent => cent.position)
    updatedCentroidsPositions1 should contain (candCent1.position)
    updatedCentroidsPositions2 should not contain (candCent2.position)
  }

  "A set of datapoints" should
  "be initialised to the furthest apart as initial centroids" in {

    val initCentroids = Initialiser.initRunFP(centroids, 2)
    initCentroids.map( cent => cent.position) should contain (Array(1,4))
    initCentroids.map( cent => cent.position) should not contain (Array(3,4))
  }


}
