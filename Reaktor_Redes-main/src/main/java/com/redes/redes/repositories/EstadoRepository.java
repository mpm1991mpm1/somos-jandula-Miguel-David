package com.redes.redes.repositories;

import com.redes.redes.models.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoRepository extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findAllByOrderByFechaReporteDescIdDesc();

}
