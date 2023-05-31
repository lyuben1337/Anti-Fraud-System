package antifraud.model.request;

import antifraud.validation.CardNumber;
import antifraud.validation.IpAddress;
import antifraud.validation.RegionCode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record TransactionRequest (
        @NotNull
        @Positive
        Integer amount,

        @NotNull
        @IpAddress
        String ip,

        @NotNull
        @CardNumber
        String number,

        @NotNull
        @RegionCode
        String region,

        @DateTimeFormat
        LocalDateTime date
)
{}
