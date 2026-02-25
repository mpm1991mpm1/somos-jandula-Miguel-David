package com.redes.redes.models;

import com.redes.redes.dto.Estados;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "historial_estados")
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ssid", nullable = false)
    private String ssid;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estados estado;

    @Column(name = "fecha_reporte", nullable = false)
    private Timestamp fechaReporte;

    public HistorialEstado() {}

    public HistorialEstado(String ssid, Estados estado) {
        this.ssid = ssid;
        this.estado = estado;
        this.fechaReporte = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Timestamp getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(Timestamp fechaReporte) {
        this.fechaReporte = fechaReporte;
    }
}
