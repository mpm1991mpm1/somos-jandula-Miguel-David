package com.redes.redes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redes.redes.dto.Estados;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "red_id", foreignKey = @ForeignKey(name = "fk_historial_estado_red"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Red red;

    public HistorialEstado() {}

    public HistorialEstado(Red red, Estados estado) {
        this.red = red;
        this.ssid = red.getSsid();
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

    public Red getRed() {
        return red;
    }

    public void setRed(Red red) {
        this.red = red;
    }
}
