package com.redes.redes.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Estados {

    Conectado("Conectado"),
    FalloAuth("Fallo de Auth"),
    SinSeñal("Sin señal");

    private final String descripcion;

    Estados(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonValue
    public String getDescripcion() {
        return descripcion;
    }
}
