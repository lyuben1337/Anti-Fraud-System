package antifraud.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardNumberValidator implements ConstraintValidator<CardNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)  {
        if (value == null || value.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d{16}$");
        Matcher matcher = pattern.matcher(value);

        if(!(matcher.matches() && isCreditCard(value))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Card number has wrong format");
        }

        return true;
    }

    private boolean isCreditCard(String cardNumber) {
        int[] cardIntArray= Arrays.stream(cardNumber.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();

        for(int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;
            if(num > 9) {
                num = num % 10 + num / 10;
            }
            cardIntArray[i] = num;
        }

        return Arrays.stream(cardIntArray).sum() % 10 == 0;
    }
}
