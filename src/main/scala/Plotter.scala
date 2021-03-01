package com.petergrimshaw.Kmeans

import vegas._
import vegas.render.WindowRenderer._
import java.io._
import com.typesafe.config.ConfigFactory

object Plotter {

  def plot(dataPoints: Array[DataPoint]): Unit = {

    val conf = ConfigFactory.load("kmeans.conf")
    val outFile = conf.getString("plotHTMLOutputFile")

    val vegaData = dataPoints.map{ case dp: DataPoint =>
      dp.position.zip(dp.fieldNames).map{ case valFn: (Double, String) => valFn._2 -> valFn._1 }.toMap + ("clusterIndex" -> dp.clusterIndex)
    }

    val fn1 = dataPoints(0).fieldNames(0)
    val fn2 = dataPoints(0).fieldNames(1)

    val plot = Vegas("Clustering Scatter", width=800, height=800)
    .withData(vegaData)
    .mark(Point)
    .encodeX(fn1, Quantitative)
    .encodeY(fn2, Quantitative)
    .encodeColor(field = "clusterIndex", dataType = Nominal, scale=Scale(rangeNominals=List("#bd0b23", "#0b66ba")))
    .configMark(size = 50, filled = true)

    //Write the plot to the project directory
    val ioFile = new File(outFile)
    val bw = new BufferedWriter(new FileWriter(ioFile))
    bw.write(plot.html.pageHTML("KMeans"))
    bw.close()

  }
}
