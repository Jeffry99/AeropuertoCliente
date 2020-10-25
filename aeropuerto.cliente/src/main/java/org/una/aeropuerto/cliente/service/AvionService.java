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
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class AvionService {
        public Respuesta crear(AvionDTO avion){
        try{
            ConexionService conexion = new ConexionService("aviones/crear");
            conexion.post(avion);
            if(conexion.isError()){
                System.out.println("Error creacion de avion: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el avion");
            }
            AvionDTO result = (AvionDTO) conexion.readEntity(AvionDTO.class);
            return new Respuesta(true, "Avion", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de avion: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    public Respuesta modificar(Long id, AvionDTO avion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("aviones/modificar", "/{id}", parametros);
            conexion.put(avion);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el avion");
            }
            AvionDTO result = (AvionDTO) conexion.readEntity(AvionDTO.class);
            return new Respuesta(true, "Avion", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de avion: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("aviones", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el avion por su id");
            }
            AvionDTO result = (AvionDTO) conexion.readEntity(AvionDTO.class);
            return new Respuesta(true, "Avion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("aviones/");
            conexion.get();
            
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los aviones");  
            }
            List<AvionDTO> result = (List<AvionDTO>) conexion.readEntity(new GenericType<List<AvionDTO>>(){});
            return new Respuesta(true, "Aviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("aviones/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el avion por su id");
            }
            List<AvionDTO> result = (List<AvionDTO>) conexion.readEntity(new GenericType<List<AvionDTO>>(){});            
            return new Respuesta(true, "Aviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByMatriculaAproximate(String matricula){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", matricula);
            ConexionService conexion = new ConexionService("aviones/list/matricula", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el avion por su matricula");
            }
            List<AvionDTO> result = (List<AvionDTO>) conexion.readEntity(new GenericType<List<AvionDTO>>(){});            
            return new Respuesta(true, "Aviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByAerolinea(Long aerolinea){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", aerolinea);
            ConexionService conexion = new ConexionService("aviones/list/aerolinea", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar aviones por aerolinea");
            }
            List<AvionDTO> result = (List<AvionDTO>) conexion.readEntity(new GenericType<List<AvionDTO>>(){});            
            return new Respuesta(true, "Aviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByTipoAvion(Long tipoAvion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", tipoAvion);
            ConexionService conexion = new ConexionService("aviones/list/tipoAvion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar aviones por tipo de avion");
            }
            List<AvionDTO> result = (List<AvionDTO>) conexion.readEntity(new GenericType<List<AvionDTO>>(){});            
            return new Respuesta(true, "Aviones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
