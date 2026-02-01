package com.redes.redes.dto;

public class EstadoEntradaDTO {

    private String nombreRed;
    private Estados estado;

    public EstadoEntradaDTO(String nombreRed, Estados estado) {
        this.nombreRed = nombreRed;
        this.estado = estado;
    }

    public EstadoEntradaDTO() {
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
}
