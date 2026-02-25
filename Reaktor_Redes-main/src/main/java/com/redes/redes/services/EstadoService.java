package com.redes.redes.services;

import com.redes.redes.dto.EstadoEntradaDTO;
import com.redes.redes.dto.EstadoSalidaDTO;
import com.redes.redes.models.HistorialEstado;
import com.redes.redes.models.Red;
import com.redes.redes.repositories.EstadoRepository;
import com.redes.redes.repositories.RedRepository;
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

    @Autowired
    RedRepository redRepository;

    public void guardarEstado(EstadoEntradaDTO estadoRecibido) {
        if (estadoRecibido == null || estadoRecibido.getNombreRed() == null || estadoRecibido.getNombreRed().isBlank()) {
            throw new IllegalArgumentException("El nombre de red es obligatorio");
        }

        if (estadoRecibido.getEstado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        String ssid = estadoRecibido.getNombreRed().trim();

        Red red = redRepository.findBySsidIgnoreCase(ssid)
            .orElseThrow(() -> new IllegalArgumentException("No existe una red registrada con ese nombre"));

        HistorialEstado estado = new HistorialEstado(red, estadoRecibido.getEstado());

        repository.save(estado);

    }

    public List<EstadoSalidaDTO> mostrarUltimoEstado() {
        Map<String, HistorialEstado> ultimosPorRed = new LinkedHashMap<>();

        repository.findAllByOrderByFechaReporteDescIdDesc()
            .forEach(estado -> {
                String ssid = estado.getRed() != null ? estado.getRed().getSsid() : estado.getSsid();
                ultimosPorRed.putIfAbsent(ssid, estado);
            });

        return ultimosPorRed.values().stream()
            .sorted(Comparator.comparing(estado -> estado.getRed() != null ? estado.getRed().getSsid() : estado.getSsid()))
                .map(estado -> new EstadoSalidaDTO(
                        estado.getRed() != null ? estado.getRed().getSsid() : estado.getSsid(),
                        estado.getEstado(),
                        estado.getFechaReporte()
                ))
                .collect(Collectors.toList());
    }

}
