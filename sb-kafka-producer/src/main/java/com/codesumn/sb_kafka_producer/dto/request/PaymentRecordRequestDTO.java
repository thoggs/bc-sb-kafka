package com.codesumn.sb_kafka_producer.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PaymentRecordRequestDTO(
        @NotBlank String number,
        @NotBlank String value,
        @NotBlank String description
) {
}
