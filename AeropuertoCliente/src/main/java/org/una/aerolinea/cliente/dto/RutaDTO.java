/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aerolinea.cliente.dto;

/**
 *
 * @author Pablo-VE
 */
public class RutaDTO {
    private Long id;
    private float distancia;
    private String origen;
    private String destino;
    private boolean estado;

    public RutaDTO() {
    }

    public RutaDTO(Long id, float distancia, String origen, String destino, boolean estado) {
        this.id = id;
        this.distancia = distancia;
        this.origen = origen;
        this.destino = destino;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
}
