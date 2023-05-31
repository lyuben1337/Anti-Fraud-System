package antifraud.model.entity;

import antifraud.validation.CardNumber;
import antifraud.validation.IpAddress;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StolenCardEntity {
    @Id
    @GeneratedValue
    Long id;
    @CardNumber
    String number;
}
