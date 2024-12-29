package com.perf.demo.api.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PutMethodSimulation extends Simulation{

  //protocol
  val httpProtocol=http
    .baseUrl("https://reqres.in/api")

  //scenario
  val updateuserscn1=scenario("Update User")
    .exec(
      http("Update user req")
        .put("/users/2")
        .header("content-type","application/json")
        .asJson
        .body(RawFileBody("testData/user.json")).asJson
        //        .body(StringBody(
        //          """
        //            |{
        //            |    "name": "morpheus",
        //            |    "job": "leader"
        //            |}
        //            |""".stripMargin)).asJson
        .check(
          status is 200,
          jsonPath("$.name") is "morpheus")
    )

  //setup
  setUp(
    updateuserscn1.inject(rampUsers(5).during(5)).protocols(httpProtocol)
    
  )

}

