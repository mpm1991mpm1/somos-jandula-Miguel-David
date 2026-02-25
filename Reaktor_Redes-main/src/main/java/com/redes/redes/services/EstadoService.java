package com.redes.redes.services;

import com.redes.redes.dto.EstadoEntradaDTO;
import com.redes.redes.dto.EstadoSalidaDTO;
import com.redes.redes.models.HistorialEstado;
import com.redes.redes.repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    @Autowired
    EstadoRepository repository;

    public void guardarEstado(EstadoEntradaDTO estadoRecibido) {
        if (estadoRecibido == null || estadoRecibido.getNombreRed() == null || estadoRecibido.getNombreRed().isBlank()) {
            throw new IllegalArgumentException("El nombre de red es obligatorio");
        }

        if (estadoRecibido.getEstado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        HistorialEstado estado = new HistorialEstado(estadoRecibido.getNombreRed().trim(), estadoRecibido.getEstado());

        repository.save(estado);

    }

    public List<EstadoSalidaDTO> mostrarUltimoEstado() {
        Map<String, HistorialEstado> ultimosPorRed = new LinkedHashMap<>();

        repository.findAllByOrderByFechaReporteDescIdDesc()
            .forEach(estado -> ultimosPorRed.putIfAbsent(estado.getSsid(), estado));

        return ultimosPorRed.values().stream()
            .sorted(Comparator.comparing(HistorialEstado::getSsid))
                .map(estado -> new EstadoSalidaDTO(
                        estado.getSsid(),
                        estado.getEstado(),
                        estado.getFechaReporte()
                ))
                .collect(Collectors.toList());
    }

}
