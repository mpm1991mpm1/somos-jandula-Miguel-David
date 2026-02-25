package com.redes.redes.controllers;

import com.redes.redes.dto.EstadoEntradaDTO;
import com.redes.redes.dto.EstadoSalidaDTO;
import com.redes.redes.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EstadoController {

    @Autowired
    EstadoService service;

    @PostMapping("/registros-redes")
    public ResponseEntity<String> registroEstado(@RequestBody EstadoEntradaDTO datosRegistro) {
            service.guardarEstado(datosRegistro);
            return ResponseEntity.ok().body("Estado guardado");
    }

    @GetMapping("/registros-redes")
    public ResponseEntity<List<EstadoSalidaDTO>> verEstado() {
        return ResponseEntity.ok().body(service.mostrarUltimoEstado());
    }

}
