/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;
import org.una.aeropuerto.cliente.conexion.ConexionService;
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class TipoAvionService {
    public Respuesta crear(TipoAvionDTO tipoAvion){
        try{
            ConexionService conexion = new ConexionService("tipos_aviones/crear");
            conexion.post(tipoAvion);
            if(conexion.isError()){
                System.out.println("Error creacion de avion: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el tipo de avion");
            }
            TipoAvionDTO result = (TipoAvionDTO) conexion.readEntity(TipoAvionDTO.class);
            return new Respuesta(true, "TipoAvion", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion del tipo de avion: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, TipoAvionDTO tipoAvion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("tipos_aviones/modificar", "/{id}", parametros);
            conexion.put(tipoAvion);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el tipo de avion");
            }
            TipoAvionDTO result = (TipoAvionDTO) conexion.readEntity(TipoAvionDTO.class);
            return new Respuesta(true, "TipoAvion", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de tipo de avion: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("tipos_aviones", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de aviones por su id");
            }
            TipoAvionDTO result = (TipoAvionDTO) conexion.readEntity(TipoAvionDTO.class);
            return new Respuesta(true, "TipoAvion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("tipos_aviones/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los tipos de aviones");
            }
            List<TipoAvionDTO> result = (List<TipoAvionDTO>) conexion.readEntity(new GenericType<List<TipoAvionDTO>>(){});
            return new Respuesta(true, "TiposAviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("tipos_aviones/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de aviones por el estado");
            }
            List<TipoAvionDTO> result = (List<TipoAvionDTO>) conexion.readEntity(new GenericType<List<TipoAvionDTO>>(){});
            return new Respuesta(true, "TiposAviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByDistanciaRango(float distancia){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", distancia);
            ConexionService conexion = new ConexionService("tipos_aviones/list/destino", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de aviones por distancia");
            }
            List<TipoAvionDTO> result = (List<TipoAvionDTO>) conexion.readEntity(new GenericType<List<TipoAvionDTO>>(){});
            return new Respuesta(true, "TiposAviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByNombreAproximate(String nombre){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", nombre);
            ConexionService conexion = new ConexionService("tipos_aviones/list/nombre", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de aviones por el nombre");
            }
            List<TipoAvionDTO> result = (List<TipoAvionDTO>) conexion.readEntity(new GenericType<List<TipoAvionDTO>>(){});
            return new Respuesta(true, "TiposAviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
