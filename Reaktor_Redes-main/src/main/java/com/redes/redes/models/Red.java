package com.redes.redes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "redes")
public class Red {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ssid;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String password;

    @Column
    private String seguridad;

    @JsonIgnore
    @OneToMany(mappedBy = "red", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstado> historialEstados = new ArrayList<>();

    public Red() {
    }

    public Red(String ssid, String usuario, String password, String seguridad) {
        this.ssid = ssid;
        this.usuario = usuario;
        this.password = password;
        this.seguridad = seguridad;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(String seguridad) {
        this.seguridad = seguridad;
    }

    public List<HistorialEstado> getHistorialEstados() {
        return historialEstados;
    }

    public void setHistorialEstados(List<HistorialEstado> historialEstados) {
        this.historialEstados = historialEstados;
    }
}
