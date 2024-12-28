package com.perf.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class ApiTestForGetMethod extends Simulation{
  //Gatling script we need 3 thing
  //protocol
  val httpProtocol=http.baseUrl("https://reqres.in/api/users")
  //scenario
  val scn=scenario("Get API for single user")
    .exec(http("Single user").get("/2")
      .check(
       status.is(200),
      jsonPath("$.data.first_name").is("Janet"))
            )
      .pause(1)

  //setup
  setUp(
  scn.inject(rampUsers(10).during(5))
    .protocols(httpProtocol)
  )
}