package sketch.scope.conf

/**
  * Licensed by Probe Technology, Inc.
  */
trait DefaultSketchConf extends PeriodicSketchConf {

  val mixingRatio: Double = 1

  val dataKernelWindow: Double = 1e-10

  val startThreshold: Double = 100

  val thresholdPeriod: Double = 100

  val cmap: UniformCmapConf = CmapConf.uniform(1000, 10, Some(-10000d), Some(10000d))

  val counter: CounterConf = CounterConf.apply(200, 2)

}

object DefaultSketchConf extends DefaultSketchConf
