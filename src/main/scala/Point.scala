package com.petergrimshaw.Kmeans

abstract class Point {
  val position: Array[Double]
  val clusterIndex: Int

  override def toString(): String = {
    val posStr = position.map{ case pos: Double => "%.3f".format(pos) }.mkString(",")
    s"Position: ${posStr}, Cluster Index: ${clusterIndex}"
  }

}
