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
import org.una.aeropuerto.cliente.dto.AreaTrabajoDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class AreaTrabajoService {
    public Respuesta crear(AreaTrabajoDTO areaTrabajo){
        try{
            ConexionService conexion = new ConexionService("areas_trabajos/crear");
            conexion.post(areaTrabajo);
            if(conexion.isError()){
                System.out.println("Error creacion de area de trabajo: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el area de trabajo");
            }
            AreaTrabajoDTO result = (AreaTrabajoDTO) conexion.readEntity(AreaTrabajoDTO.class);
            return new Respuesta(true, "AreaTrabajo", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de area de trabajo: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, AreaTrabajoDTO areaTrabajo){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("areas_trabajos/modificar", "/{id}", parametros);
            conexion.put(areaTrabajo);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el area de trabajo");
            }
            AreaTrabajoDTO result = (AreaTrabajoDTO) conexion.readEntity(AreaTrabajoDTO.class);
            return new Respuesta(true, "AreaTrabajo", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion area de trabajo: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("areas_trabajos", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar un area de trabajo por su id");
            }
            AreaTrabajoDTO result = (AreaTrabajoDTO) conexion.readEntity(AreaTrabajoDTO.class);
            return new Respuesta(true, "AreaTrabajo",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("areas_trabajos/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos las areas de trabajo");
            }
            List<AreaTrabajoDTO> result = (List<AreaTrabajoDTO>) conexion.readEntity(new GenericType<List<AreaTrabajoDTO>>(){});
            return new Respuesta(true, "AreasTrabajos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("areas_trabajos/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las areas de trabajo por estado");
            }
            List<AreaTrabajoDTO> result = (List<AreaTrabajoDTO>) conexion.readEntity(new GenericType<List<AreaTrabajoDTO>>(){});
            return new Respuesta(true, "AreasTrabajos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByNombreAproximate(String nombre){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", nombre);
            ConexionService conexion = new ConexionService("areas_trabajos/list/nombre", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las areas de trabajo por su nombre");
            }
            List<AreaTrabajoDTO> result = (List<AreaTrabajoDTO>) conexion.readEntity(new GenericType<List<AreaTrabajoDTO>>(){});
            return new Respuesta(true, "AreasTrabajos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByDescripcionAproximate(String descripcion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", descripcion);
            ConexionService conexion = new ConexionService("areas_trabajo/list/descripcion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las areas de trabajo por la descripcion");
            }
            List<AreaTrabajoDTO> result = (List<AreaTrabajoDTO>) conexion.readEntity(new GenericType<List<AreaTrabajoDTO>>(){});
            return new Respuesta(true, "AreasTrabajos",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
