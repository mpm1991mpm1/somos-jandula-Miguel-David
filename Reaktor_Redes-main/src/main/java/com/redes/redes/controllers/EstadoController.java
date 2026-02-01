package com.redes.redes.controllers;

import com.redes.redes.dto.EstadoEntradaDTO;
import com.redes.redes.models.HistorialEstado;
import com.redes.redes.models.Red;
import com.redes.redes.repositories.HistorialRepository;
import com.redes.redes.repositories.RedRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EstadoController {

    private final HistorialRepository historialRepository;
    private final RedRepository redRepository;

    public EstadoController(HistorialRepository historialRepository, RedRepository redRepository) {
        this.historialRepository = historialRepository;
        this.redRepository = redRepository;
    }

    /**
     * Registrar prueba
     * Endpoint: POST /registros-redes
     */
    @PostMapping("/registros-redes")
    public ResponseEntity<String> registroEstado(@RequestBody EstadoEntradaDTO datosRegistro) {

        String textoEstado = datosRegistro.getEstado().getDescripcion();

        HistorialEstado nuevoEstado = new HistorialEstado(
                datosRegistro.getNombreRed(),
                textoEstado
        );

        historialRepository.save(nuevoEstado);
        return ResponseEntity.ok().body("Estado registrado");
    }

    /**
     * Consultar historial
     * Endpoint: GET /registros-redes
     */
    @GetMapping("/registros-redes")
    public ResponseEntity<List<HistorialEstado>> verEstado() {
        return ResponseEntity.ok().body(historialRepository.findAll());
    }

    /**
     * Consultar último estado por red existente
     * Endpoint: GET /estado-actual
     */
    @GetMapping("/estado-actual")
    public ResponseEntity<List<HistorialEstado>> verUltimoEstadoPorRed() {
        List<String> ssids = redRepository.findAll()
                .stream()
                .map(Red::getSsid)
                .collect(Collectors.toList());

        if (ssids.isEmpty()) {
            return ResponseEntity.ok().body(List.of());
        }

        return ResponseEntity.ok().body(historialRepository.findUltimoEstadoPorSsid(ssids));
    }
}
