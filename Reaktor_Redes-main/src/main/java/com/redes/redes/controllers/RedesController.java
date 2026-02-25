package com.redes.redes.controllers;

import com.redes.redes.dto.RedesEntradaDTO;
import com.redes.redes.models.Red;
import com.redes.redes.services.RedesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RedesController {

    @Autowired
    RedesService service;

    @PostMapping("/configuracion-redes")
    public ResponseEntity<String> registroRed(@RequestBody RedesEntradaDTO redesDto) {
            service.registrarRed(redesDto);
            return ResponseEntity.ok().body("Red registrada");
    }

    @GetMapping("/configuracion-redes")
    public ResponseEntity<List<Red>> verRedes() {
        return ResponseEntity.ok().body(service.verRedes());
    }

    @DeleteMapping("/configuracion-redes/{id}")
    public ResponseEntity<String> eliminarRed(@PathVariable Long id) {
            service.eliminarRed(id);
            return ResponseEntity.ok().body("Red eliminada");
    }
}
