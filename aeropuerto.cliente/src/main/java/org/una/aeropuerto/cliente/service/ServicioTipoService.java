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
import org.una.aeropuerto.cliente.dto.ServicioTipoDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class ServicioTipoService {
    public Respuesta crear(ServicioTipoDTO servicioTipo){
        try{
            ConexionService conexion = new ConexionService("servicios_tipos/crear");
            conexion.post(servicioTipo);
            if(conexion.isError()){
                System.out.println("Error creacion de tipo de servicio: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el tipo de servicio");
            }
            ServicioTipoDTO result = (ServicioTipoDTO) conexion.readEntity(ServicioTipoDTO.class);
            return new Respuesta(true, "TipoServicio", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de tipo de servicio: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, ServicioTipoDTO servicioTipo){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("servicios_tipos/modificar", "/{id}", parametros);
            conexion.put(servicioTipo);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el tipo de servicio");
            }
            ServicioTipoDTO result = (ServicioTipoDTO) conexion.readEntity(ServicioTipoDTO.class);
            return new Respuesta(true, "TipoServicio", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion tipo de servicio: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("servicios_tipos", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar un tipo de servicio por su id");
            }
            ServicioTipoDTO result = (ServicioTipoDTO) conexion.readEntity(ServicioTipoDTO.class);
            return new Respuesta(true, "TipoServicio",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("servicios_tipos/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los tipos de servicios");
            }
            List<ServicioTipoDTO> result = (List<ServicioTipoDTO>) conexion.readEntity(new GenericType<List<ServicioTipoDTO>>(){});
            return new Respuesta(true, "TiposServicios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("servicios_tipos/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de servicios por estado");
            }
            List<ServicioTipoDTO> result = (List<ServicioTipoDTO>) conexion.readEntity(new GenericType<List<ServicioTipoDTO>>(){});
            return new Respuesta(true, "TiposServicios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByNombreAproximate(String nombre){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", nombre);
            ConexionService conexion = new ConexionService("servicios_tipos/list/nombre", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de servicios por su nombre");
            }
            List<ServicioTipoDTO> result = (List<ServicioTipoDTO>) conexion.readEntity(new GenericType<List<ServicioTipoDTO>>(){});
            return new Respuesta(true, "TiposServicios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByDescripcionAproximate(String descripcion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", descripcion);
            ConexionService conexion = new ConexionService("servicios_tipos/list/descripcion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de servicios por la descripcion");
            }
            List<ServicioTipoDTO> result = (List<ServicioTipoDTO>) conexion.readEntity(new GenericType<List<ServicioTipoDTO>>(){});
            return new Respuesta(true, "TiposServicios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByAreaTrabajo(Long areaTrabajo){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", areaTrabajo);
            ConexionService conexion = new ConexionService("servicios_tipos/list/areaTrabajo", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de servicio por areas de trabajos");
            }
            List<ServicioTipoDTO> result = (List<ServicioTipoDTO>) conexion.readEntity(new GenericType<List<ServicioTipoDTO>>(){});
            return new Respuesta(true, "TiposServicios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
