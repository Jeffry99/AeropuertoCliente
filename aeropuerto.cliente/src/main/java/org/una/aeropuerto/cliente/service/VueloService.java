/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;
import org.una.aeropuerto.cliente.conexion.ConexionService;
import org.una.aeropuerto.cliente.dto.VueloDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class VueloService {
    public Respuesta crear(VueloDTO vuelo){
        try{
            ConexionService conexion = new ConexionService("vuelos/crear");
            conexion.post(vuelo);
            if(conexion.isError()){
                System.out.println("Error creacion de vuelo: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el vuelo");
            }
            VueloDTO result = (VueloDTO) conexion.readEntity(VueloDTO.class);
            return new Respuesta(true, "Vuelo", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion del vuelo: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, VueloDTO vuelo){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("vuelos/modificar", "/{id}", parametros);
            conexion.put(vuelo);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el vuelo");
            }
            VueloDTO result = (VueloDTO) conexion.readEntity(VueloDTO.class);
            return new Respuesta(true, "Vuelo", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de vuelo: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("vuelos", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los vuelos por su id");
            }
            VueloDTO result = (VueloDTO) conexion.readEntity(VueloDTO.class);
            return new Respuesta(true, "Vuelo",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("vuelos/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los vuelos");
            }
            List<VueloDTO> result = (List<VueloDTO>) conexion.readEntity(new GenericType<List<VueloDTO>>(){});
            return new Respuesta(true, "Vuelos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(int estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("vuelos/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los vuelos por su estado");
            }
            List<VueloDTO> result = (List<VueloDTO>) conexion.readEntity(new GenericType<List<VueloDTO>>(){});
            return new Respuesta(true, "Vuelos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByAvion(Long avion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", avion);
            ConexionService conexion = new ConexionService("vuelos/list/avion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los vuelos por avion");
            }
            List<VueloDTO> result = (List<VueloDTO>) conexion.readEntity(new GenericType<List<VueloDTO>>(){});
            return new Respuesta(true, "Vuelos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByRuta(Long ruta){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", ruta);
            ConexionService conexion = new ConexionService("vuelos/list/ruta", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los vuelos por ruta");
            }
            List<VueloDTO> result = (List<VueloDTO>) conexion.readEntity(new GenericType<List<VueloDTO>>(){});
            return new Respuesta(true, "Vuelos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByFecha(Date ruta){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", ruta);
            ConexionService conexion = new ConexionService("vuelos/list/fecha", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los vuelos por fecha");
            }
            List<VueloDTO> result = (List<VueloDTO>) conexion.readEntity(new GenericType<List<VueloDTO>>(){});
            return new Respuesta(true, "Vuelos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
}
