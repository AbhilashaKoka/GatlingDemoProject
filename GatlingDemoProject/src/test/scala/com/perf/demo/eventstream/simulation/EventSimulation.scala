//package com.perf.demo.eventstream.simulation
//
//import io.gatling.core.Predef.*
//import io.github.amerousful.kafka.Predef.*
//import io.github.amerousful.kafka.protocol.KafkaBroker
//
//
//
//class EventSimulation extends Simulation {
//  
//  val kafkaProtocol = kafka
//    .broker(KafkaBroker("localhost", 9092)) // Replace with your Kafka broker details
//    .acks("1") // Acknowledgment level
//    .producerIdenticalSerializer("org.apache.kafka.common.serialization.StringSerializer")
//    .consumerIdenticalDeserializer("org.apache.kafka.common.serialization.StringDeserializer")
//
//  val kafkaProducerScenario = scenario("Kafka Producer Scenario")
//    .exec(
//      kafka("Kafka: fire and forget")
//        .send
//        .topic("input_topic") // Replace with your topic name
//        .payload(StringBody("#{payload}")) // Replace with your payload
//        .key("#{key}") // Optional: Add a key if needed
//    )
//
//  val kafkaConsumerScenario = scenario("Kafka Consumer Scenario")
//    .exec(
//      kafka("Kafka: Only consume")
//        .onlyConsume
//        .readTopic("#{output_topic}") // Replace with your topic name
//        .payloadForTracking(session=>"payload") // Optional: Track payload
//        .keyForTracking("key") // Optional: Track key
//    )
//
//  setUp(
//    kafkaProducerScenario.inject(atOnceUsers(10)), // Adjust user load as needed
//    kafkaConsumerScenario.inject(atOnceUsers(10))
//  ).protocols(kafkaProtocol)
//  
//}