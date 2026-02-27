package com.redes.redes.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "monitorizacion_config")
public class MonitorizacionConfig {

    public static final long SINGLETON_ID = 1L;

    @Id
    private Long id;

    @Column(name = "refresh_minutes")
    private Integer refreshMinutes;

    public MonitorizacionConfig() {
    }

    public MonitorizacionConfig(Long id, Integer refreshMinutes) {
        this.id = id;
        this.refreshMinutes = refreshMinutes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRefreshMinutes() {
        return refreshMinutes;
    }

    public void setRefreshMinutes(Integer refreshMinutes) {
        this.refreshMinutes = refreshMinutes;
    }
}
