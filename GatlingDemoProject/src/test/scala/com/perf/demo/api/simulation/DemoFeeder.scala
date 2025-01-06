package com.perf.demo.api.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DemoFeeder extends Simulation{

  //protocol
  val httpProtocol = http.baseUrl("https://reqres.in/api")

  val feeder=csv("testDataFeeders/user.csv")

  val scn=scenario("Feeder Demo")
    .repeat(3) {
      feed(feeder)
        .exec { session =>
          println("name: " + session("name").as[String])
          println("job: " + session("job").as[String])
          session
        }

        .pause(1)
    }


//  val scn = scenario("Create User")
//    .feed(feeder)
//    .exec(
//      http("Create user req")
//        .post("/users")
//        .header("content-type", "application/json")
//        .asJson
//        .body(StringBody(
//          """
//            |{
//            |  "name": "${name}",
//            |  "job": "${job}"
//            |}
//            |""".stripMargin)).asJson
////.body(RawFileBody("testDataFeeders/user.json")).asJson
//        .check(
//          status is 201,
//          jsonPath("$.name") is "morpheus"))

  //setup
  setUp(
    scn.inject(rampUsers(3).during(2)).protocols(httpProtocol))

}




