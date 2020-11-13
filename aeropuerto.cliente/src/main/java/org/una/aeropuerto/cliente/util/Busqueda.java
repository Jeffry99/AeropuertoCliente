/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.util;

import java.util.ArrayList;
import java.util.Date;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.dto.VueloDTO;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;

/**
 *
 * @author Luis
 */
public class Busqueda {
 
    private final ServicioRegistradoService servicioRegistradoService = new ServicioRegistradoService();
    ArrayList<ServicioRegistradoDTO> servicios;
    ArrayList<ServicioRegistradoDTO> serviciosFechas = new ArrayList();
    ArrayList<VueloDTO> vuelosFechas = new ArrayList();
     
    public ArrayList<ServicioRegistradoDTO> busquedaRangoRecaudacion(Date menor, Date mayor, ArrayList<ServicioRegistradoDTO> lista){
        if(!lista.isEmpty()){
            for(int i=0; i<lista.size(); i++){
                if(menor.before(lista.get(i).getFechaRegistro())){
                    if(mayor.after(lista.get(i).getFechaRegistro())){
                        serviciosFechas.add(lista.get(i));
                    }
                }
            }  
        }else{
            System.out.println("No hay servicios registrados");
        }
        return serviciosFechas;
    }
    public ArrayList<VueloDTO> busquedaRangoRecorridos(Date menor, Date mayor, ArrayList<VueloDTO> lista){
        if(!lista.isEmpty()){
            for(int i=0; i<lista.size(); i++){
                if(menor.before(lista.get(i).getFecha())){
                    if(mayor.after(lista.get(i).getFecha())){
                        vuelosFechas.add(lista.get(i));
                    }
                }
            }  
        }else{
            System.out.println("No hay vuelos registrados");
        }
        return vuelosFechas;
    }
}

