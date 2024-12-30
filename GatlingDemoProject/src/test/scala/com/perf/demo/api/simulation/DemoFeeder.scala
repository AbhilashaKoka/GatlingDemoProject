package com.perf.demo.api.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DemoFeeder extends Simulation{

  val feeder=csv("testDataFeeders/user.csv")

  val scn=scenario("Feeder Demo")
    .repeat(3) {
    feed(feeder)
        .exec { session =>
          println("name:" + session("name").as[String])
          println("job:" + session("job").as[String])
          session
        }
        .pause(1)
    }

  setUp(scn.inject(atOnceUsers(1)))

}
