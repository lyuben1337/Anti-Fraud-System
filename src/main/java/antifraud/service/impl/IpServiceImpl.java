package antifraud.service.impl;

import antifraud.model.entity.IPEntity;
import antifraud.model.response.IpDeleteStatus;
import antifraud.repository.IpRepository;
import antifraud.service.IpService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class IpServiceImpl implements IpService {
    IpRepository repository;

    @Override
    public IPEntity register(String ip) {
        if(repository.existsByIp(ip)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    ip + " is already in the database");
        }
        return repository.save(
                IPEntity.builder()
                        .ip(ip)
                        .build()
        );
    }

    @Override
    public IpDeleteStatus delete(String ip) {
        if(!repository.existsByIp(ip)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    ip + " is not found in Database");
        }
        repository.deleteByIp(ip);
        return new IpDeleteStatus("IP " + ip + " successfully removed!");
    }

    @Transactional(readOnly = true)
    @Override
    public List<IPEntity> getIpList() {
        return repository.findAll();
    }
}
