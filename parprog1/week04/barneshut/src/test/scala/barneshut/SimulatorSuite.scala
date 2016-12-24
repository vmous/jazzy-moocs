package barneshut

import barneshut.conctrees._

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SimulatorSuite extends FunSuite {

  test("testUpdateBoundaries") {
    val model = new SimulationModel
    val simulator = new Simulator(model.taskSupport, model.timeStats)
    val body = new Body(5, 25, 47, 0.1f, 0.1f)
    val boundaries = new Boundaries()
    boundaries.minX = 1
    boundaries.minY = 1
    boundaries.maxX = 10
    boundaries.maxY = 17
    simulator.updateBoundaries(boundaries, body)
    assert(boundaries.minX === 1 && boundaries.minY === 1 && boundaries.maxX === 25 && boundaries.maxY === 47)
  }

  test("testMergeBoundaries") {
    val model = new SimulationModel
    val simulator = new Simulator(model.taskSupport, model.timeStats)
    val a = new Boundaries()
    val b = new Boundaries()
    a.minX = 1
    a.minY = 1
    a.maxX = 30
    a.maxY = 27
    b.minX = -1
    b.minY = -1
    b.maxX = 10
    b.maxY = 17
    val r = simulator.mergeBoundaries(a, b)
    assert(a.minX === 1 && a.minY === 1 && a.maxX === 30 && a.maxY === 27)
    assert(b.minX === -1 && b.minY === -1 && b.maxX === 10 && b.maxY === 17)
    assert(r.minX === -1 && r.minY === -1 && r.maxX === 30 && r.maxY === 27)
  }

  test("computeBoundaries") {
    val model = new SimulationModel
    val simulator = new Simulator(model.taskSupport, model.timeStats)

    val bodies = Seq(
      new Body(1, 0f, 1f, 0f, 0f),
      new Body(1, 10f, 11f, 0f, 0f)
    )

    val b = simulator.computeBoundaries(bodies)
    assert(b.minX === 0f, "minX")
    assert(b.minY === 1f, "minY")
    assert(b.maxX === 10f, "maxX")
    assert(b.maxY === 11f, "maxY")
  }

  test("computeSectorMatrix 2 points") {
    val model = new SimulationModel
    val simulator = new Simulator(model.taskSupport, model.timeStats)

    val bodies = Seq(
      new Body(1, 1, 1, 0f, 0f),
      new Body(1, 97, 97, 0f, 0f)
    )

    val b = simulator.computeBoundaries(bodies)
    val sm = simulator.computeSectorMatrix(bodies, b)
    assert(sm.matrix.size === SECTOR_PRECISION * SECTOR_PRECISION, "matrix.size")

    val max: Int = SECTOR_PRECISION * SECTOR_PRECISION - 2
    assert(sm.matrix.slice(1, max).forall(_.size == 0), "size 0 for all but first and last")

    assert(sm.matrix(SECTOR_PRECISION * SECTOR_PRECISION - 1).size == 1, "matrix(SECTOR_PRECISION * SECTOR_PRECISION " +
      "- 1).size")

    assert(sm.matrix(0).size == 1, "matrix(0).size")
  }

  test("computeSectorMatrix 4 points") {
    val model = new SimulationModel
    val simulator = new Simulator(model.taskSupport, model.timeStats)

    val bodies = Seq(
      new Body(1, 1, 1, 0f, 0f),
      new Body(1, 13, 1, 0f, 0f),
      new Body(1, 25, 1, 0f, 0f),
      new Body(1, 97, 97, 0f, 0f)
    )

    val b = simulator.computeBoundaries(bodies)
    val sm = simulator.computeSectorMatrix(bodies, b)
    assert(sm.matrix.size === SECTOR_PRECISION * SECTOR_PRECISION, "matrix.size")

    val max: Int = SECTOR_PRECISION * SECTOR_PRECISION - 2
    assert(sm.matrix.slice(3, max).forall(_.size == 0), "size 0 for all but first and last")

    assert(sm(0,0).size == 1, "sm(0,0).size")
    assert(sm(1,0).size == 1, "sm(1,0).size")
    assert(sm(2,0).size == 1, "sm(2,0).size")


    assert(sm.matrix(SECTOR_PRECISION * SECTOR_PRECISION - 1).size == 1, "matrix(SECTOR_PRECISION * SECTOR_PRECISION " +
      "- 1).size")
  }

  test("Unexpected size example (_no_ result invoked)") {
    var a = new ConcBuffer[Body]
    var b = new ConcBuffer[Body]

    a += new Body(1, 1, 1, 0f, 0f)
    val oneBody = a combine b
    assert(oneBody.size === 1)

    b += new Body(1, 12, 12, 0f, 0f)
    val twoBodies = (a combine b)

    assert(twoBodies.size != 2)
    // The previous assertion must hold, because there was a prior call to size. See also following tests
  }

  test("Unexpected size example (result invoked)") {
    var a = new ConcBuffer[Body]
    var b = new ConcBuffer[Body]

    a += new Body(1, 1, 1, 0f, 0f)
    val oneBody = a combine b
    assert(oneBody.result.size === 1)

    b += new Body(1, 12, 12, 0f, 0f)
    val twoBodies = (a combine b)

    assert(twoBodies.result.size != 2)
    // The previous assertion must hold, because there was a prior call to size. See also following tests
  }

  test("Unexpected size example (no prior result)") {
    var a = new ConcBuffer[Body]
    var b = new ConcBuffer[Body]

    a += new Body(1, 1, 1, 0f, 0f)
    b += new Body(1, 12, 12, 0f, 0f)
    val twoBodies = (a combine b)

    assert(twoBodies.result.size === 2)
  }

}
