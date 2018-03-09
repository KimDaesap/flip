package flip.pdf

import flip.conf.SamplingDistConf
import flip.measure.Measure
import flip.plot.DensityPlot
import flip.range.RangeM
import flip.rand.IRng

import scala.language.higherKinds

/**
  * SamplingDist, or Sampling distribution provides the sampling points and its
  * probability density.
  * */
trait SamplingDist[A] extends Dist[A] {

  def conf: SamplingDistConf

}

trait SamplingDistPropOps[D[_] <: SamplingDist[_]] extends DistPropOps[D] with SamplingDistPropLaws[D] {

  def sampling[A](dist: D[A]): DensityPlot

}

trait SamplingDistPropLaws[D[_] <: SamplingDist[_]] { self: SamplingDistPropOps[D] =>

  def interpolationPdf[A](dist: D[A], a: A): Double = {
    val plot = sampling(dist)

    DensityPlot.interpolation(plot, dist.measure.asInstanceOf[Measure[A]].to(a))
  }

}

object SamplingDist extends SamplingDistPropOps[SamplingDist] {

  def forSmoothDist[A](dist: SmoothDist[A], domains: List[RangeM[A]]): SamplingDist[A] =
    SmoothDist.samplingDist(dist, domains)

  def modifyRng[A](dist: SamplingDist[A], f: IRng => IRng): SamplingDist[A] = dist match {
    case (sketch: Sketch[A]) => Sketch.modifyRng(sketch, f)
    case (plotted: PlottedDist[A]) => PlottedDist.modifyRng(plotted, f)
    case _ => ???
  }

  def probability[A](dist: SamplingDist[A], start: A, end: A): Double = dist match {
    case (sketch: Sketch[A]) => Sketch.probability(sketch, start, end)
    case (plotted: PlottedDist[A]) => PlottedDist.probability(plotted, start, end)
    case _ => ???
  }

  def sampling[A](dist: SamplingDist[A]): DensityPlot = dist match {
    case (sketch: Sketch[A]) => Sketch.sampling(sketch)
    case (plotted: PlottedDist[A]) => PlottedDist.sampling(plotted)
    case _ => ???
  }

  def sample[A](dist: SamplingDist[A]): (SamplingDist[A], A) = dist match {
    case sketch: Sketch[_] => Sketch.sample(sketch)
    case plotted: PlottedDist[_] => PlottedDist.sample(plotted)
  }

  override def pdf[A](dist: SamplingDist[A], a: A): Prim = dist match {
    case (sketch: Sketch[_]) => Sketch.fastPdf(sketch, a)
    case _ => super.interpolationPdf(dist, a)
  }

}
