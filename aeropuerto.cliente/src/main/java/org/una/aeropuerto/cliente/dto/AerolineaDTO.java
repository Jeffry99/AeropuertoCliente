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
public class AerolineaDTO {
    private Long id;
    private String nombre;
    private String responsable; 
    private boolean estado;

    public AerolineaDTO() {
    }

    public AerolineaDTO(Long id, String nombre, String responsable, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.responsable = responsable;
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

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public String toString(){
        return nombre;
    }
}
