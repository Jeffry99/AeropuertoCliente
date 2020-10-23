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
public class ServicioRegistradoDTO {
    private Long id;
    private ServicioTipoDTO servicioTipo;
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private java.util.Date fechaRegistro;
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private java.util.Date fechaModificacion;
    private float cobro;
    private boolean estadoCobro;
    private float duracion;
    private String observaciones;
    private boolean estado;
    private EmpleadoDTO responsable;
    private AvionDTO avion;

    public ServicioRegistradoDTO() {
    }

    public ServicioRegistradoDTO(Long id, ServicioTipoDTO servicioTipo, Date fechaRegistro, Date fechaModificacion, float cobro, boolean estadoCobro, float duracion, String observaciones, boolean estado, EmpleadoDTO responsable, AvionDTO avion) {
        this.id = id;
        this.servicioTipo = servicioTipo;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.cobro = cobro;
        this.estadoCobro = estadoCobro;
        this.duracion = duracion;
        this.observaciones = observaciones;
        this.estado = estado;
        this.responsable = responsable;
        this.avion = avion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicioTipoDTO getServicioTipo() {
        return servicioTipo;
    }

    public void setServicioTipo(ServicioTipoDTO servicioTipo) {
        this.servicioTipo = servicioTipo;
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

    public float getCobro() {
        return cobro;
    }

    public void setCobro(float cobro) {
        this.cobro = cobro;
    }

    public boolean getEstadoCobro() {
        return estadoCobro;
    }

    public void setEstadoCobro(boolean estadoCobro) {
        this.estadoCobro = estadoCobro;
    }

    public float getDuracion() {
        return duracion;
    }

    public void setDuracion(float duracion) {
        this.duracion = duracion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public EmpleadoDTO getResponsable() {
        return responsable;
    }

    public void setResponsable(EmpleadoDTO responsable) {
        this.responsable = responsable;
    }

    public AvionDTO getAvion() {
        return avion;
    }

    public void setAvion(AvionDTO avion) {
        this.avion = avion;
    }
    
    
}
