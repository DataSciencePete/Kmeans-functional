package com.petergrimshaw.Kmeans

import com.github.tototoshi.csv.CSVReader
import com.typesafe.scalalogging.Logger
import scala.util.{Try, Success, Failure}
import collection.JavaConverters._

object DataReader {

  val logger = Logger("kmeans_logger")

  def readCSV(filePath: String, header: Boolean): (List[List[String]], Option[List[String]]) = {
    val reader = CSVReader.open(filePath)
    if (header) {
      val headerLine = reader.readNext()
      val lines = reader.all()
      (lines, headerLine)
    } else {
    (reader.all(), None)
    }
  }

  def getDatapointsFromLines(lines: List[List[String]]): Try[List[Array[Double]]] = Try{
    val dataPoints = lines.map{
      case line: List[String] => line.iterator.toArray.map{
        case value: String => value.toDouble
      }
    }
    dataPoints
  }

  def readFormat(format: String): Option[(String, Boolean) => (List[List[String]], Option[List[String]])] = {
      val formatMap = Map("csv" -> readCSV _)
      formatMap.get(format)
    }

  def dataPointFromArrayHeader(arr: Array[Double], head: Array[String]): DataPoint = DataPoint(arr, 0, head)

  def getDefaultHeaders(lines: List[List[String]]): Array[String] = {
    lines(0).zipWithIndex.map{ valInd => s"field${valInd._2}" }.toArray
  }

  def read(format: String, filepath: String, hasHeader: Boolean): Option[Array[DataPoint]] = {
    val dataPoints = readFormat(format) match {
      case Some(readFunc) => {
        val linesHeader = readFunc(filepath, hasHeader)
        val lines = linesHeader._1
        val header = linesHeader._2 match {
          case Some(hdr: List[String])  => hdr.toArray
          case _ => getDefaultHeaders(lines)
        }
        getDatapointsFromLines(lines) match {
          case Success(dataPointsValues: List[Array[Double]]) => {
            Some(dataPointsValues.map{ case dpv: Array[Double] =>
              dataPointFromArrayHeader(dpv, header) }.toArray) }
          case Failure(e) => {
            logger.warn(s"Not all datapoints read correctly, ${e}")
            None }
        }
      }
      case None => { logger.warn(s"Failed to return valid reading method")
        None }
      }
      dataPoints
    }

}
