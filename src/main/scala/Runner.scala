package com.petergrimshaw.Kmeans

import com.typesafe.scalalogging.{Logger, LazyLogging}

object Runner extends App with LazyLogging {

  val readFormat: String = args(0)
  val filePath: String = args(1)
  val hasHeader: Boolean = args(2).toBoolean
  val plotOutput: Boolean = args(3).toBoolean

  val dataPoints = DataReader.read(readFormat, filePath, hasHeader)
  dataPoints match {
    case Some(dataPoints) => { Engine.execute(2, dataPoints, "runfp") match {
      case Some((centroids: Array[Centroid], dataPoints: Array[DataPoint])) =>
        logger.info("Output Centroids")
        centroids.foreach(cent => logger.info(s"${cent}"))
        logger.info(s"Output data points")
        dataPoints.foreach(dp => logger.info(s"${dp}"))
        if (plotOutput) Plotter.plot(dataPoints)
      case None => logger.info("No datapoints or centroids returned")
    }
  }
    case None => logger.info("Unable to read in datapoints from file")
        }

}
