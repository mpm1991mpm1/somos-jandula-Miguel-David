package com.redes.redes.controllers;

import com.redes.redes.dto.RedesEntradaDTO;
import com.redes.redes.models.Red;
import com.redes.redes.repositories.RedRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RedesController {

    private final RedRepository redRepository;

    public RedesController(RedRepository redRepository) {
        this.redRepository = redRepository;
    }

    /**
     * Alta de red
     * Endpoint: POST /configuracion-redes
     */
    @PostMapping("/configuracion-redes")
    public ResponseEntity<String> registroRed(@RequestBody RedesEntradaDTO redesDto) {
        Optional<Red> existente = redRepository.findBySsid(redesDto.getNombre());
        if (existente.isPresent()) {
            return ResponseEntity.badRequest().body("La red ya existe");
        }

        Red nuevaRed = new Red(
                redesDto.getNombre(),
                redesDto.getUsuario(),
                redesDto.getContrasena(),
                redesDto.getSeguridad()
        );

        redRepository.save(nuevaRed);
        return ResponseEntity.ok().body("Red registrada");
    }

    /**
     * Listar objetivos
     * Endpoint: GET /configuracion-redes
     */
    @GetMapping("/configuracion-redes")
    public ResponseEntity<List<Red>> verRedes() {
        return ResponseEntity.ok().body(redRepository.findAll());
    }

    /**
     * Eliminar red
     * Endpoint: DELETE /configuracion-redes/{id}
     */
    @DeleteMapping("/configuracion-redes/{id}")
    public ResponseEntity<String> eliminarRed(@PathVariable Long id) {
        if (!redRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        redRepository.deleteById(id);
        return ResponseEntity.ok().body("Red eliminada");
    }
}
