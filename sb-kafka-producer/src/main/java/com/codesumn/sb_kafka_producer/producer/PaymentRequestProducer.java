package com.codesumn.sb_kafka_producer.producer;

import com.codesumn.sb_kafka_producer.dto.request.PaymentRecordRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestProducer {

    @Value("${topics.payment.request.topic}")
    private String paymentRequestTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentRequestProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendMessage(PaymentRecordRequestDTO payment) throws Exception {
        String paymentJson = objectMapper.writeValueAsString(payment);
        kafkaTemplate.send(paymentRequestTopic, paymentJson);
        return "Pagamento enviado para processamento";
    }
}
