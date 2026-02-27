package com.redes.redes.controllers;

import com.redes.redes.dto.MonitorizacionConfigDTO;
import com.redes.redes.models.MonitorizacionConfig;
import com.redes.redes.services.MonitorizacionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorizacionConfigController {

    @Autowired
    private MonitorizacionConfigService service;

    @GetMapping("/monitorizacion-config")
    public ResponseEntity<MonitorizacionConfigDTO> getConfig() {
        MonitorizacionConfig config = service.getOrCreate();
        return ResponseEntity.ok(new MonitorizacionConfigDTO(config.getRefreshMinutes()));
    }

    @PutMapping("/monitorizacion-config")
    public ResponseEntity<MonitorizacionConfigDTO> updateConfig(@RequestBody MonitorizacionConfigDTO dto) {
        MonitorizacionConfig config = service.setRefreshMinutes(dto != null ? dto.getRefreshMinutes() : null);
        return ResponseEntity.ok(new MonitorizacionConfigDTO(config.getRefreshMinutes()));
    }
}
