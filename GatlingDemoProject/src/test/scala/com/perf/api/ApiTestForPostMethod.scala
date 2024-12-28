package com.perf.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ApiTestForPostMethod extends Simulation{

  //protocol
  val httpProtocol=http
    .baseUrl("https://reqres.in/api")

  //scenario
  val scn1=scenario("Create User")
    .exec(
      http("Create user req")
        .post("/users")
        .header("content-type","application/json")
        .asJson
        .body(StringBody(
          """
            |{
            |    "name": "morpheus",
            |    "job": "leader"
            |}
            |""".stripMargin)).asJson
        .check(
          status is 201,
        jsonPath("$.name") is "morpheus")
         )

  //setup
  setUp(
    scn1.inject(rampUsers(5).during(5)).protocols(httpProtocol)
  )

}
