package flip.hcounter

import org.scalacheck.{Arbitrary, Gen}
import org.specs2.mutable._
import org.specs2.ScalaCheck
import flip.counter.CounterGen
import flip.hcounter.HCounter

class HCounterSpec extends Specification with ScalaCheck {

  "HCounter" should {

    "ops" in {

      "update" in {

        "basic" in {
          todo
        }

        "irregular index" in todo

      }

      "get" in {

        "basic" in {
          val hcounter = HCounter.empty(1, 100000, 0)
          hcounter.get(1) must beAnInstanceOf[Some[HCounter]]
        }

        "irregular index: overflow" in {
//          val hcounter = HCounter.empty(1, 100000, 0)
//          hcounter.get(100000).fold(ok)(hc => ko("Returns something with irregular index."))

          todo
        }

        "irregular index: underflow" in {
//          val hcounter = HCounter.empty(1, 100000, 0)
//          hcounter.get(-1).fold(ok)(hc => ko("Returns something with irregular index."))

          todo
        }

      }

    }

  }

}

object HCounterGen {

  def hcounterGen: Gen[HCounter] = for {
    depth <- Gen.choose(1, 10)
    width <- Gen.choose(100, 10000)
    seed = 0
  } yield HCounter.empty(depth, width, seed)

  def hcounterA: Arbitrary[HCounter] = Arbitrary(hcounterGen)

}