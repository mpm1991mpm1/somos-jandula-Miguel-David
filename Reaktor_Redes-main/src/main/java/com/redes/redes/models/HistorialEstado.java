package com.redes.redes.models;

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
    private String estado;

    @Column(name = "fecha_reporte", nullable = false)
    private Timestamp fechaReporte;

    public HistorialEstado() {}

    public HistorialEstado(String ssid, String estado) {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(Timestamp fechaReporte) {
        this.fechaReporte = fechaReporte;
    }
}
