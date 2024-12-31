package com.perf.demo.api.simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import java.nio.file.{Files, Paths}
import scala.io.Source

class CombinedFeederSimulation extends Simulation {


  def readFile(path: String): String = {
    Source.fromFile(path).getLines().mkString("\n")
  }
  // Define the CSV feeder
  val csvFeeder = csv("testDataFeeders/user.csv").circular

  // Load the JSON template
  val jsonTemplate = readFile("src/test/resources/testDataFeeders/user.json")

  // Define the HTTP protocol
  val httpProtocol = http.baseUrl("https://reqres.in/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  // Define the scenario
  val scn = scenario("Combined Feeder Simulation")
    .feed(csvFeeder)
    .exec(session => {
      // Replace the placeholders in the JSON template with session values
      val name = session("name").as[String]
      val job = session("job").as[String]
      val requestBody = jsonTemplate
        .replace("${name}", name)
        .replace("${job}", job)
      session.set("requestBody", requestBody)
    })
    .exec(http("Login Request")
      .post("/users")
      .body(StringBody("${requestBody}")).asJson
      .check(status.is(201)))

  // Set up the simulation
  setUp(
    scn.inject(atOnceUsers(10))
  ).protocols(httpProtocol)
}
