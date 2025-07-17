package com.perf.demo.api.simulation;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.stream.Collectors;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class CombinedFeederSimulation2 extends Simulation {

    private final FeederBuilder<String> csvFeeder = csv("testDataFeeders/user.csv").circular();
    private final String jsonTemplate;

    public CombinedFeederSimulation2() throws IOException {
        this.jsonTemplate = loadJsonTemplate("src/test/resources/testDataFeeders/user.json");
        ScenarioBuilder scenario = createScenario();
        HttpProtocolBuilder protocol = createHttpProtocol();
        setupSimulation(scenario, protocol);
    }

    private String loadJsonTemplate(String path) throws IOException {
        return Files.lines(Paths.get(path)).collect(Collectors.joining("\n"));
    }

    private ScenarioBuilder createScenario() {
        return scenario("Combined Feeder Simulation")
                .feed(csvFeeder)
                .exec(session -> {
                    String name = session.getString("name");
                    String job = session.getString("job");
                    String requestBody = jsonTemplate
                            .replace("${name}", name)
                            .replace("${job}", job);
                    return session.set("requestBody", requestBody);
                })
                .exec(http("Login Request")
                        .post("/users")
                        .body(StringBody("${requestBody}")).asJson()
                        .check(status().is(201))
                );
    }

    private HttpProtocolBuilder createHttpProtocol() {
        return http.baseUrl("https://reqres.in/api")
                .acceptHeader("application/json")
                .contentTypeHeader("application/json");
    }

    private void setupSimulation(ScenarioBuilder scenario, HttpProtocolBuilder protocol) {
        Integer duringSeconds = Integer.getInteger("duringSeconds", 10);
        Integer constantUsers = Integer.getInteger("constantUsers", 10);

        setUp(
                scenario.injectClosed(
                        constantConcurrentUsers(constantUsers).during(duringSeconds)
                )
        )
                .protocols(protocol)
                .maxDuration(1800)
                .assertions(
                        global().responseTime().max().lt(20000),
                        global().successfulRequests().percent().gt(95.0)
                );
    }
}