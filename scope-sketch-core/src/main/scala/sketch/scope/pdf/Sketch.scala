package sketch.scope.pdf

import cats.implicits._
import sketch.scope.cmap.Cmap
import sketch.scope.hcounter.HCounter
import sketch.scope.hmap.HDim
import sketch.scope.measure.Measure
import sketch.scope.plot.{DensityPlot, Plot}

import scala.language.higherKinds

/**
  * Licensed by Probe Technology, Inc.
  *
  * Sketch Data Structure Interface.
  */
trait Sketch[A] extends SampleDist[A] {

  def structures: Structures

}

trait SketchPropOps[S[_]<:Sketch[_]] extends SketchPropLaws[S] with SampleDistPropOps[S] {

  // Read ops

  def count[A](sketch: S[A], from: A, to: A): Option[Count]

  def sum(sketch: S[_]): Count

  // Update ops

  def modifyStructure[A](sketch: S[A], f: Structures => Option[Structures]): Option[S[A]]

  def narrowUpdate[A](sketch: S[A], as: List[(A, Count)]): Option[S[A]]

  def deepUpdate[A](sketch: S[A], as: List[(A, Count)]): Option[(S[A], Structure)]

  //  def clear(sketch: S): S

}

trait SketchPropLaws[S[_]<:Sketch[_]] { self: SketchPropOps[S] =>

  def rearrange[A](sketch: S[A]): Option[S[A]] = deepUpdate(sketch, Nil).map(_._1)

}

object Sketch extends SketchPrimPropOps[Sketch] {

  def apply[A](measure: Measure[A], structure: Structures): Sketch[A] = SimpleSketch(measure, structure)

  /**
    * @param measure  measure of Sketch
    * @param caDepth  size of Sketch structure
    * @param caSize   Cmap size.
    * @param coDepth  HCounter depth
    * @param coSize   HCounter size
    * */
  def empty[A](measure: Measure[A], caDepth: Int, caSize: Int, coDepth: Int, coSize: Int): Sketch[A] =
    PeriodicSketch.empty(measure, caDepth, caSize, coDepth, coSize)

  // mapping ops

  def modifyStructure[A](sketch: Sketch[A], f: Structures => Option[Structures]): Option[Sketch[A]] = sketch match {
    case sketch: PeriodicSketch[_] => PeriodicSketch.modifyStructure(sketch, f)
    case _ => SimpleSketch.modifyStructure(sketch, f)
  }

  // syntatic sugars

  def update[A](sketch: Sketch[A], as: List[(A, Count)]): Option[Sketch[A]] = sketch match {
    case sketch: PeriodicSketch[A] => PeriodicSketch.update(sketch, as)
    case _ => narrowUpdate(sketch, as)
  }

}