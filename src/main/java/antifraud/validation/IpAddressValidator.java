package antifraud.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpAddressValidator implements ConstraintValidator<IpAddress, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.equals("")) {
            return false;
        }
        Pattern pattern =
                Pattern.compile("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$");
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "IP address has the wrong format");
        } else {
            for (int i = 1; i <= 4; i++) {
                int octet = Integer.valueOf(matcher.group(i));
                if (octet > 255) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "IP address has the wrong format");
                }
            }
            return true;
        }
    }
}
