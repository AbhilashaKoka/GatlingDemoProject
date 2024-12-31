package com.perf.demo.api.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PostMethodSimulation extends Simulation{

  //protocol
  val httpProtocol=http
    .baseUrl("https://reqres.in/api")

  //scenario
  val createuserscn1=scenario("Create User")
    .exec(
      http("Create user req")
        .post("/users")
        .header("content-type","application/json")
        .asJson
       .body(RawFileBody("testDataFeeders/user1.json")).asJson
//        .body(StringBody(
//          """
//            |{
//            |    "name": "morpheus",
//            |    "job": "leader"
//            |}
//            |""".stripMargin)).asJson
        .check(
          status is 201,
          jsonPath("$.name") is "morpheus")
         )

  //setup
  setUp(
    createuserscn1.inject(rampUsers(5).during(5)).protocols(httpProtocol)
  )

}
