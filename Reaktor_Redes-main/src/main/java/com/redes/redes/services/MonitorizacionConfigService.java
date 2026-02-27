package com.redes.redes.services;

import com.redes.redes.models.MonitorizacionConfig;
import com.redes.redes.repositories.MonitorizacionConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonitorizacionConfigService {

    @Autowired
    private MonitorizacionConfigRepository repository;

    @Transactional
    public MonitorizacionConfig getOrCreate() {
        return repository.findById(MonitorizacionConfig.SINGLETON_ID)
                .orElseGet(() -> repository.save(new MonitorizacionConfig(MonitorizacionConfig.SINGLETON_ID, null)));
    }

    @Transactional
    public MonitorizacionConfig setRefreshMinutes(Integer refreshMinutes) {
        if (refreshMinutes != null && refreshMinutes < 1) {
            throw new IllegalArgumentException("refreshMinutes debe ser >= 1 o null");
        }

        MonitorizacionConfig config = getOrCreate();
        config.setRefreshMinutes(refreshMinutes);
        return repository.save(config);
    }
}
