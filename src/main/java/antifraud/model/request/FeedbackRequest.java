package antifraud.model.request;

import antifraud.validation.Feedback;

public record FeedbackRequest(
        Long transactionId,

        @Feedback
        String feedback
) {
}
