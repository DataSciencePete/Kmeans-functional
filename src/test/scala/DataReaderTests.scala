package com.petergrimshaw.Kmeans

import org.scalatest.{FlatSpec, Matchers}

class DataReaderTests extends FlatSpec with Matchers {

  "Data reader given a list of lists of string representing only numeric values" should
  "return a list of arrays of doubles" in {

    val numericStringLines = List(List("1.0", "2.0", "3.0"), List("4.2", "5.9", "6.1"))
    val dataPoints = DataReader.getDatapointsFromLines(numericStringLines).get
    val numDataPoints = List(Array(1.0, 2.0, 3.0), Array(4.2, 5.9, 6.1))
    assert(dataPoints.zip(numDataPoints).map{ case (arr1: Array[Double], arr2: Array[Double]) => arr1.sameElements(arr2) } == List(true, true) )
  }

  "Data reader given a list of lists of string inlcuding non-numeric values" should
  "throw an exception" in {

    val nonNumericStringLines = List(List("1.0", "2.0", "3.0"), List("4.2", "foo", "bar"))
    val dataPoints = DataReader.getDatapointsFromLines(nonNumericStringLines)
    a [java.lang.NumberFormatException] should be thrownBy {
      dataPoints.get }
  }


}
