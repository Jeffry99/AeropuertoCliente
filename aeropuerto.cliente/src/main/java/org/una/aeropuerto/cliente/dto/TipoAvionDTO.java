/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.dto;

/**
 *
 * @author Pablo-VE
 */
public class TipoAvionDTO {
    private Long id;
    private String nombre;
    private float distanciaRecomendada;
    private boolean estado;

    public TipoAvionDTO() {
    }

    public TipoAvionDTO(Long id, String nombre, float distanciaRecomendada, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.distanciaRecomendada = distanciaRecomendada;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getDistanciaRecomendada() {
        return distanciaRecomendada;
    }

    public void setDistanciaRecomendada(float distanciaRecomendada) {
        this.distanciaRecomendada = distanciaRecomendada;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
}
