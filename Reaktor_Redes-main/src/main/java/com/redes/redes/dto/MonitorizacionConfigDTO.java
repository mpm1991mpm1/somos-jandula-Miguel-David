package com.redes.redes.dto;

public class MonitorizacionConfigDTO {

    private Integer refreshMinutes;

    public MonitorizacionConfigDTO() {
    }

    public MonitorizacionConfigDTO(Integer refreshMinutes) {
        this.refreshMinutes = refreshMinutes;
    }

    public Integer getRefreshMinutes() {
        return refreshMinutes;
    }

    public void setRefreshMinutes(Integer refreshMinutes) {
        this.refreshMinutes = refreshMinutes;
    }
}
