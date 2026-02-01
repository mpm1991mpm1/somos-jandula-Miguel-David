package com.redes.redes.models;

import jakarta.persistence.*;

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
}
