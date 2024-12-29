package com.perf.demo.api.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DeleteMethodSimulation extends Simulation{

  //protocol
  val httpProtocol=http
    .baseUrl("https://reqres.in/api")

  //scenario
  val deleteuserscn1=scenario("delete User")
    .exec(
      http("delete user req")
        .delete("/users/2")
        .check(status is 204)
    )
        .pause(1)

  //setup
  setUp(
    deleteuserscn1.inject(rampUsers(3).during(2)).protocols(httpProtocol)

  )

}

