package antifraud.controller;

import antifraud.model.entity.IPEntity;
import antifraud.model.request.NewIP;
import antifraud.model.response.IpDeleteStatus;
import antifraud.service.IpService;
import antifraud.validation.IpAddress;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/antifraud")
public class IpController {
    IpService service;

    @PostMapping("/suspicious-ip")
    @ResponseStatus(HttpStatus.OK)
    IPEntity registerIp(@RequestBody NewIP newIP) {
        return service.register(newIP.ip());
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    @ResponseStatus(HttpStatus.OK)
    IpDeleteStatus deleteIp(@Valid @PathVariable @IpAddress String ip) {
        return service.delete(ip);
    }

    @GetMapping("/suspicious-ip")
    @ResponseStatus(HttpStatus.OK)
    List<IPEntity> getIpList() {
        return service.getIpList();
    }
}
