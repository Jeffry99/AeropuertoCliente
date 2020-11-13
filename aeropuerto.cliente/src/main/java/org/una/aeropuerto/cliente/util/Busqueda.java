/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.util;

import java.util.ArrayList;
import java.util.Date;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;

/**
 *
 * @author Luis
 */

public class Busqueda {
 
    private ServicioRegistradoService servicioRegistradoService = new ServicioRegistradoService();
    ArrayList<ServicioRegistradoDTO> servicios;
    ArrayList<ServicioRegistradoDTO> serviciosFechas = new ArrayList();
     

    public ArrayList<ServicioRegistradoDTO> busquedaRangoFechas(Date menor, Date mayor, ArrayList<ServicioRegistradoDTO> listaFechas){
        if(!listaFechas.isEmpty()){
            for(int i=0; i<listaFechas.size(); i++){
                if(menor.before(listaFechas.get(i).getFechaRegistro())){
                    if(mayor.after(listaFechas.get(i).getFechaRegistro())){
                        serviciosFechas.add(listaFechas.get(i));
                    }
                }
            }  
        }else{
            System.out.println("No hay servicios registrados");
        }
        return serviciosFechas;
    }
}
