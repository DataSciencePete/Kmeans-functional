package com.petergrimshaw.Kmeans

import scala.annotation.tailrec
import com.typesafe.scalalogging.Logger
import com.typesafe.config.ConfigFactory

object Engine {

  val logger = Logger("kmeans_logger")

  //Assign each data point to its closest centroid
  def assignClosestCentroids(centroids: Array[Centroid], dps: Array[DataPoint]): Array[DataPoint] = {
      dps.map(pt => (DataPoint.getClosestCentroid(pt, centroids), pt)).map{
        case (cnt: Centroid, pt: DataPoint) => DataPoint(pt.position, cnt.clusterIndex, pt.fieldNames)
        }
      }

  //Adjust the centroids to be the mean of all data points assigned to them
  def adjustCentroids(centroids: Array[Centroid], dps: Array[DataPoint]): Array[Centroid] = {
    centroids.map(cnt => Centroid(Centroid.computeMean(cnt, dps),cnt.clusterIndex))
  }

  // Main method to assign initial cluster means and then run clustering
  def execute(kNumClusters: Int, dataPoints: Array[DataPoint], initType: String): Option[(Array[Centroid], Array[DataPoint])] = {
    Initialiser.execute(initType, dataPoints, kNumClusters) match {
        case Some(initCentroids) => {
          logger.info(s"Initialised centroids using ${initType} algorithm")
          initCentroids.foreach( cent => logger.info(s"Initial centroid ${cent}") )
          val conf = ConfigFactory.load("kmeans.conf")
          val maxIter = conf.getInt("maxIter")
          Some(expectMax(initCentroids, dataPoints, maxIter))
        }
        case None => None
        }
      }

  //Method to run the Kmeans expectation maximisation algorithm
  def expectMax(centroids: Array[Centroid], dps: Array[DataPoint], maxIter: Int): (Array[Centroid],Array[DataPoint]) = {

    @tailrec def _expectMax(centroids: Array[Centroid], dps: Array[DataPoint], currIter: Int):
      (Array[Centroid],Array[DataPoint]) = {
        logger.info(s"Beginning iteration $currIter")

        val newDps = assignClosestCentroids(centroids,dps)
        logger.info(s"Iteration $currIter assigned closest centroid to data points")

        val newCentroids = adjustCentroids(centroids,newDps)
        val newCentroidMeansString = newCentroids.map(cnt => cnt.position).toString
        logger.info(s"Iteration $currIter centroid means $newCentroidMeansString")

        //Function to check for convergence (no change in cluster asignment)
        def checkConvergence(newDps: Array[DataPoint], oldDps: Array[DataPoint]): Boolean = {
          newDps.map(dp => dp.clusterIndex).zip(dps.map(dp => dp.clusterIndex)).map{
            case (newInd: Int, oldInd: Int) => newInd==oldInd }.reduce( (x,y) => x==y )
        }

        if(checkConvergence(newDps,dps) || currIter >= maxIter){
          logger.info(s"Converged after ${currIter} iterations")
          (newCentroids,newDps)
        } else {
          _expectMax(newCentroids,newDps,currIter+1)
        }
    }

    //Execute tail recursive function starting from iteration 1
    _expectMax(centroids,dps,1)
  }

}
