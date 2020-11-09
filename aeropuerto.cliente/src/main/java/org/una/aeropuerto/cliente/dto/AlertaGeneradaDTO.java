/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.dto;

import java.util.Date;
import javax.json.bind.annotation.JsonbDateFormat;

/**
 *
 * @author Pablo-VE
 */
public class AlertaGeneradaDTO {
    private Long id;
    private int estado;
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private java.util.Date fechaRegistro;
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private java.util.Date fechaModificacion;
    private String autorizacion;
    private VueloDTO vuelo;
    private TipoAlertaDTO tipoAlerta;

    public AlertaGeneradaDTO() {
    }

    public AlertaGeneradaDTO(Long id, int estado, Date fechaRegistro, Date fechaModificacion, String autorizacion, VueloDTO vuelo, TipoAlertaDTO tipoAlerta) {
        this.id = id;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.autorizacion = autorizacion;
        this.vuelo = vuelo;
        this.tipoAlerta = tipoAlerta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public VueloDTO getVuelo() {
        return vuelo;
    }

    public void setVuelo(VueloDTO vuelo) {
        this.vuelo = vuelo;
    }

    public TipoAlertaDTO getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(TipoAlertaDTO tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }
    
    
}
