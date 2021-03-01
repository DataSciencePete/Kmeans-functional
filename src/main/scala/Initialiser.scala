package com.petergrimshaw.Kmeans

import scala.util.{Try, Success, Failure}
import scala.annotation.tailrec
import com.typesafe.scalalogging.Logger

/*
This object contains functions used to select initial centroids for the clustering.

initRunFP method:
The idea behind this method is to make the candidate centroids as far apart as possible. The method selects the first k points to be centroids initially. It then updates these k candidate centroids according to the following rule applied to each of the k candidate centroids. If the candidate centroid is further from the nearest existing centroid than the distance between the two closest existing centroids then use the candidate centroid instead of the closer of the two to the existing centroids.
*/


object Initialiser {

  val logger = Logger("kmeans_logger")

  def execute(initType: String, dataPoints: Array[DataPoint], k: Int): Option[Array[Centroid]] = {
    initType match {
      case "runfp" => Some(initRunFP(dataPoints, k))
      case _ => { logger.info("Invalid initialisation method")
        None }
    }
  }

  // Iterate through the set of datapoints recursively and update the centroids based on each daapoint to run the algorithm.
  def initRunFP(dataPoints: Array[DataPoint], k: Int): Array[Centroid] = {

    @tailrec def _initRunFP(candCents: Array[DataPoint], dataPoints: List[DataPoint]): (Array[DataPoint], List[DataPoint]) = {
        dataPoints match {
          case x :: xs => {
            val updatedCandCents: Array[DataPoint] = updateCandidateCentroids(x, candCents)
            if (xs.length == 0) { (updatedCandCents, xs) } else {
              _initRunFP(updatedCandCents, xs)
            }
          }
          case Nil => (candCents, dataPoints)
        }
    }

    // Select initial candidate centroids as the first k datapoints and iterate through the datapoints to improve initial centroids to get back initialised centroids
    val initCandCents = dataPoints.take(k)
    val initialisedCents = _initRunFP(initCandCents, dataPoints.toList)
    initialisedCents._1.zipWithIndex.map{ case (cent: DataPoint, ind: Int) => Centroid(cent.position, ind) }
    }

  def updateCandidateCentroids(candCent: DataPoint, cents: Array[DataPoint]): Array[DataPoint] = if (getDistanceClosestCentroid(candCent, cents) > getDistanceClosestCentroidPair(cents)) {

    logger.debug(s"Candidate centroid at position ${candCent.position} is distance ${(getDistanceClosestCentroid(candCent, cents))} from the closest centroid and the closest centroids are ${getDistanceClosestCentroidPair(cents)} apart")

    cents.map{
      case existingCent: DataPoint => {
        // See if the existing centroid being mapped is one of the two closest together
        val closestCentroidPair = getClosestCentroidPair(cents)
        val inClosestCentroidPair = Initialiser.evalClosestCentroidPair(closestCentroidPair, existingCent)
        if ( inClosestCentroidPair.reduce( (x, y) => x || y ) ){
          // Is the existing centroid also closest to the candidate centroid
          if (Initialiser.existingCentroidIsClosest(closestCentroidPair, existingCent, candCent)) { candCent } else { existingCent }
        } else { existingCent }
      }
    }
  } else {
  // If the candidate point is not further than the closest pair keep existing centroids
  cents }

  def existingCentroidIsClosest(closestCentroidPair: Array[DataPoint], existingCent: DataPoint, candCent: DataPoint): Boolean = {
    val closest = Initialiser.isClosestOfCentroidPair(closestCentroidPair, existingCent, candCent)
    val matches = Initialiser.evalClosestCentroidPair(closestCentroidPair, existingCent)
    closest.zip(matches).map( clsMtch => clsMtch._1 && clsMtch._2 ).reduce( (x, y) => x || y )
  }

  // Returns an array indicating whether each of the closest pair matches the given centroid
  def evalClosestCentroidPair(closestCentroidPair: Array[DataPoint], existingCent: DataPoint): Array[Boolean] = {
    closestCentroidPair.map{ cent =>
      cent.position == existingCent.position }
  }

  // Evalutes whether the existing centroid is the closest of the closest pair to the candidate centroid. If it is the closest it will be substituted later.
  def isClosestOfCentroidPair(closestCentroidPair: Array[DataPoint], existingCent: DataPoint, candCent: DataPoint): Array[Boolean] = {
    closestCentroidPair.map{ cent => DistanceUtil.getDistance(cent, candCent) == DistanceUtil.getDistance(existingCent, candCent) }
  }


  def getDistanceClosestCentroid(dp: DataPoint, cents: Array[DataPoint]): Double = {
    val closestCentroid: DataPoint = DataPoint.getClosestCentroid(dp, cents)
    DistanceUtil.getDistance(dp, closestCentroid)
  }

  def getClosestCentroidPair(cents: Array[DataPoint]): Array[DataPoint] = {

    // Get all pairwise combinations of centroid to compute the closest
    val centPairs = cents.toSeq.combinations(2)

    val centPairsDists = centPairs.map{
      case centPair: Seq[DataPoint] =>
      (DistanceUtil.getDistance( centPair(0), centPair(1) ), centPair(0), centPair(1) )}
    val minDistCentPair = centPairsDists.minBy(_._1)
    Array(minDistCentPair._2, minDistCentPair._3)
    }

  def getDistanceClosestCentroidPair(cents: Array[DataPoint]): Double = {
    val closestCentroidPair = getClosestCentroidPair(cents)
    DistanceUtil.getDistance(closestCentroidPair(0), closestCentroidPair(1))
  }

}
