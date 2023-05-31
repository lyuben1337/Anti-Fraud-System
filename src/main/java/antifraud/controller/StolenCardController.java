package antifraud.controller;

import antifraud.model.entity.StolenCardEntity;
import antifraud.model.request.NewStolenCard;
import antifraud.model.response.CardDeleteStatus;
import antifraud.service.StolenCardService;
import antifraud.validation.CardNumber;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/antifraud")
public class StolenCardController {
    StolenCardService service;

    @PostMapping("/stolencard")
    @ResponseStatus(HttpStatus.OK)
    StolenCardEntity saveStolenCard(@RequestBody NewStolenCard stolenCard) {
        return service.saveStolenCard(stolenCard.number());
    }

    @DeleteMapping("/stolencard/{number}")
    @ResponseStatus(HttpStatus.OK)
    CardDeleteStatus deleteStolenCard(@PathVariable @CardNumber String number) {
        return service.deleteStolenCard(number);
    }

    @GetMapping("/stolencard")
    @ResponseStatus(HttpStatus.OK)
    List<StolenCardEntity> getStolenCardList() {
        return service.getStolenCardList();
    }
}
