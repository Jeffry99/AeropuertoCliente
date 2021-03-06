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
public class VueloDTO {
    private Long id;
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date fecha;
    private AvionDTO avion;
    private int estado;
    private RutaDTO ruta;

    public VueloDTO() {
    }

    public VueloDTO(Long id, Date fecha, AvionDTO avion, int estado, RutaDTO ruta) {
        this.id = id;
        this.fecha = fecha;
        this.avion = avion;
        this.estado = estado;
        this.ruta = ruta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public AvionDTO getAvion() {
        return avion;
    }

    public void setAvion(AvionDTO avion) {
        this.avion = avion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public RutaDTO getRuta() {
        return ruta;
    }

    public void setRuta(RutaDTO ruta) {
        this.ruta = ruta;
    }
    
    @Override
    public String toString(){
        return id.toString();
    }
    
}
