package com.codesumn.sb_kafka_producer.api;

import com.codesumn.sb_kafka_producer.dto.request.PaymentRecordRequestDTO;
import com.codesumn.sb_kafka_producer.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public String integratePayment(@RequestBody @Valid PaymentRecordRequestDTO payment) {
        return this.paymentService.integratePayment(payment);
    }
}
