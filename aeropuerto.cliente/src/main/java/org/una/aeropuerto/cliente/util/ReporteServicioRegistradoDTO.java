/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.util;

import java.util.Date;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
/**
 *
 * @author Jeffry
 */
public class ReporteServicioRegistradoDTO{
    private String cobro;
    private String estadoCobro;
    private String fechaRegistro;
    private String avion;
    public ReporteServicioRegistradoDTO(){
        
    }
    
    public void casteo(ServicioRegistradoDTO servicio){
        cobro = String.valueOf(servicio.getCobro());
        fechaRegistro = servicio.getFechaRegistro().toString();
        avion = servicio.getAvion().toString();
        if(servicio.getEstadoCobro()){
            estadoCobro = "Activo";
        }else{
            estadoCobro = "Inactivo";
        }
    }
    
    public void setCobro(String cobro){
        this.cobro = cobro;
    }

    public String getCobro(){
        return "₡" + cobro;
    }
    public String getEstadoCobro() {
        return estadoCobro;
    }

    public void setEstadoCobro(String estadoCobro) {
        this.estadoCobro = estadoCobro;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getAvion() {
        return avion;
    }

    public void setAvion(String avion) {
        this.avion = avion;
    }
   
 
    
}
