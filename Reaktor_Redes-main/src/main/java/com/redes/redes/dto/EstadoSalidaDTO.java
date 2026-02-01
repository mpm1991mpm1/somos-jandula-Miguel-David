package com.redes.redes.dto;

import java.sql.Timestamp;

public class EstadoSalidaDTO {

    private String nombreRed;
    private Estados estado;
    private Timestamp fechaHora;

    public EstadoSalidaDTO(String nombreRed, Estados estado) {
        this.nombreRed = nombreRed;
        this.estado = estado;
        this.fechaHora = new Timestamp(System.currentTimeMillis());
    }

    public EstadoSalidaDTO() {
    }

    public String getNombreRed() {
        return nombreRed;
    }

    public void setNombreRed(String nombreRed) {
        this.nombreRed = nombreRed;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }
}
