package antifraud.model.entity;

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
public class IPEntity {
    @Id @GeneratedValue
    Long id;
    @IpAddress
    String ip;
}
