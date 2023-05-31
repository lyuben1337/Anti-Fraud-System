package antifraud.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class TransactionLimits {
    @Id
    @GeneratedValue
    Long id;

    private Long maxAllowed;

    private Long maxManual;
}
