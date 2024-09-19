package com.codesumn.sb_kafka_consumer.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestConsumer {

    @KafkaListener(topics = "${topics.payment.request.topic}")
    public void consume(String message) {
        System.out.println("Mensagem recebida: " + message);
    }
}
