/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.util;

import java.text.SimpleDateFormat;
import org.una.aeropuerto.cliente.dto.VueloDTO;

/**
 *
 * @author Jeffry
 */
public class ReporteVueloDTO {
    private String ruta;
    private String estadoVuelo;
    private String fecha;
    private String avion;
    
    public ReporteVueloDTO(){}
    
    public void cast(VueloDTO vuelo){
        ruta = vuelo.getRuta().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        fecha = formatter.format(vuelo.getFecha());
        avion = vuelo.getAvion().toString();
        int estado = vuelo.getEstado();
        if(estado == 1){
            estadoVuelo = "En revisión";
        }else{
            if(estado == 2){
                estadoVuelo = "Autorizado";
            }else{
                if(estado == 3){
                    estadoVuelo = "No autorizado";
                }else{
                    if(estado == 4){
                        estadoVuelo = "Cancelado";
                    }
                }
            }
        }
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getEstadoVuelo() {
        return estadoVuelo;
    }

    public void setEstadoVuelo(String estadoVuelo) {
        this.estadoVuelo = estadoVuelo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAvion() {
        return avion;
    }

    public void setAvion(String avion) {
        this.avion = avion;
    }
    
    
}
