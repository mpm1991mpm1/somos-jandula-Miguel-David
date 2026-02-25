package com.redes.redes.repositories;

import com.redes.redes.models.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialRepository extends JpaRepository<HistorialEstado, Long> {

    @Query("select h from HistorialEstado h " +
            "where h.ssid in :ssids " +
            "and h.fechaReporte = (select max(h2.fechaReporte) from HistorialEstado h2 where h2.ssid = h.ssid)")
    List<HistorialEstado> findUltimoEstadoPorSsid(@Param("ssids") List<String> ssids);
}
