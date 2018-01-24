package flip.sim

import flip.pdf.{Dist, SamplingDist}

/**
  * Squared Eucledian Distance, or Minkowski L2 Distance between two pdfs.
  * https://en.wikipedia.org/wiki/Euclidean_distance#Squared_Euclidean_distance
  * https://en.wikipedia.org/wiki/Hilbert_space
  * */
trait L2Sq extends DensitySim {

  val norm1: Double

  val norm2: Double

  def point(value1: Double, value2: Double): Double = {
    math.pow((value1 / norm1) - (value2 / norm2), 2)
  }

}

object L2Sq {

  private case class L2SqImpl(norm1: Double, norm2: Double) extends L2Sq

  def apply[A](d1: SamplingDist[A],
               d2: Dist[A]): L2Sq = {
    val norm1 = Hilbert.normForSamplingDist(d1)
    val norm2 = d2.sampling(d1)
      .map(plottedD2 => Hilbert.normForSamplingDist(plottedD2))
      .getOrElse(Double.PositiveInfinity)

    L2SqImpl(norm1, norm2)
  }

}