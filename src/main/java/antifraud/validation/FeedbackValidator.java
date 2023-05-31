package antifraud.validation;

import antifraud.model.consts.Region;
import antifraud.model.consts.TransactionResult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FeedbackValidator implements ConstraintValidator<Feedback, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            // Attempt to convert the string value to a Region enum value
            TransactionResult transactionResult = TransactionResult.valueOf(value);

            // If the conversion is successful, the value is a valid region code
            return true;
        } catch (IllegalArgumentException e) {
            // If an IllegalArgumentException is thrown, the value is not a valid region code
            return false;
        } catch (NullPointerException e) {
            // Handle the case when the value is null and a NullPointerException is thrown
            return false;
        }
    }
}
