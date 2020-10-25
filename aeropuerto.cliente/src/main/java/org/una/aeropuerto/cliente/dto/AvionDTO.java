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
public class AvionDTO {
    private Long id;
    private String matricula;
    private AerolineaDTO aerolinea;
    private TipoAvionDTO tipoAvion;
    private boolean estado;

    public AvionDTO() {
    }

    public AvionDTO(Long id, String matricula, AerolineaDTO aerolinea, TipoAvionDTO tipoAvion, boolean estado) {
        this.id = id;
        this.matricula = matricula;
        this.aerolinea = aerolinea;
        this.tipoAvion = tipoAvion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public AerolineaDTO getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(AerolineaDTO aerolinea) {
        this.aerolinea = aerolinea;
    }

    public TipoAvionDTO getTipoAvion() {
        return tipoAvion;
    }

    public void setTipoAvion(TipoAvionDTO tipoAvion) {
        this.tipoAvion = tipoAvion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public String toString(){
        return aerolinea+" - "+matricula;
    }
}
