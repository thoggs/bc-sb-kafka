package com.codesumn.sb_kafka_producer.service;

import com.codesumn.sb_kafka_producer.dto.request.PaymentRecordRequestDTO;
import com.codesumn.sb_kafka_producer.producer.PaymentRequestProducer;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PaymentService {
    private final PaymentRequestProducer paymentRequestProducer;

    public PaymentService(PaymentRequestProducer paymentRequestProducer) {
        this.paymentRequestProducer = paymentRequestProducer;
    }

    public String integratePayment(PaymentRecordRequestDTO payment) {
        try {
            return paymentRequestProducer.sendMessage(payment);
        } catch (Exception e) {
            Logger.getLogger(PaymentService.class.getName()).severe(e.getMessage());
            return "Erro ao enviar pagamento para processamento";
        }
    }
}
