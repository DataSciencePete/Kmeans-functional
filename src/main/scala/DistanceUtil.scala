package com.petergrimshaw.Kmeans

object DistanceUtil{

  def getDistance[T <: Point, U <: Point](pnt1: T, pnt2: U): Double = {

  val squaredAbsDist = pnt1.position.zip(pnt2.position).map{ case (p1: Double ,p2: Double) =>
    Math.pow(Math.abs(p1-p2),2.0) }
  val sumSquaredAbsDist = squaredAbsDist.foldLeft(0.0)( (x,y) => x + y)

  Math.sqrt(sumSquaredAbsDist)
  }

  //Implement method for checking centroid is close to a points
  def isCloseTo[T <: Point, U <: Point](pnt1: T, pnt2: U, tolerance: Double = 1e-3): Boolean = {
    DistanceUtil.getDistance(pnt1 ,pnt2) <= tolerance
  }

}
