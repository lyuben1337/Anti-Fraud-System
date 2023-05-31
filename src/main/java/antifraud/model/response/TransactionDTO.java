package antifraud.model.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionDTO(
        Long transactionId,
        Long amount,
        String ip,
        String number,
        String region,
        LocalDateTime date,
        String result,
        String feedback
) {
}
