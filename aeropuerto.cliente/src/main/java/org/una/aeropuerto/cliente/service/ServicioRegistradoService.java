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
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class ServicioRegistradoService {
    public Respuesta crear(ServicioRegistradoDTO servicio){
        try{
            ConexionService conexion = new ConexionService("servicios_registrados/crear");
            conexion.post(servicio);
            if(conexion.isError()){
                System.out.println("Error creacion de servicio aeropuerto: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el servicio aeropuerto");
            }
            ServicioRegistradoDTO result = (ServicioRegistradoDTO) conexion.readEntity(ServicioRegistradoDTO.class);
            return new Respuesta(true, "ServicioAeropuerto", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de servicio aeropuerto: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    public Respuesta modificar(Long id, ServicioRegistradoDTO servicio){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("servicios_registrados/modificar", "/{id}", parametros);
            conexion.put(servicio);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el servicio aeropuerto");
            }
            ServicioRegistradoDTO result = (ServicioRegistradoDTO) conexion.readEntity(ServicioRegistradoDTO.class);
            return new Respuesta(true, "ServicioAeropuerto", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de servicio aeropuerto: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("servicios_registrados", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el servicio aeropuerto por su id");
            }
            ServicioRegistradoDTO result = (ServicioRegistradoDTO) conexion.readEntity(ServicioRegistradoDTO.class);
            return new Respuesta(true, "ServicioAeropuerto",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("servicios_registrados/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los servicios del aeropuerto");
            }
            List<ServicioRegistradoDTO> result = (List<ServicioRegistradoDTO>) conexion.readEntity(new GenericType<List<ServicioRegistradoDTO>>(){});
            return new Respuesta(true, "ServiciosAeropuerto",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("servicios_registrados/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los servicios aeropuerto por estado");
            }
            List<ServicioRegistradoDTO> result = (List<ServicioRegistradoDTO>) conexion.readEntity(new GenericType<List<ServicioRegistradoDTO>>(){});
            return new Respuesta(true, "ServiciosAeropuerto",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByAvion(Long avion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", avion);
            ConexionService conexion = new ConexionService("servicios_registrados/list/avion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los servicios registrados por avión");
            }
            List<ServicioRegistradoDTO> result = (List<ServicioRegistradoDTO>) conexion.readEntity(new GenericType<List<ServicioRegistradoDTO>>(){});
            return new Respuesta(true, "ServiciosAeropuerto",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstadoCobro(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("servicios_registrados/list/estadoCobro", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los servicios aeropuerto por estado");
            }
            List<ServicioRegistradoDTO> result = (List<ServicioRegistradoDTO>) conexion.readEntity(new GenericType<List<ServicioRegistradoDTO>>(){});
            return new Respuesta(true, "ServiciosAeropuerto",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByTipoAproximate(String tipo){//FALTA TERMINAR
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", tipo);
            ConexionService conexion = new ConexionService("servicios_registrados/list/tipo", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los servicios aeropuerto por tipo");
            }
            List<ServicioRegistradoDTO> result = (List<ServicioRegistradoDTO>) conexion.readEntity(new GenericType<List<ServicioRegistradoDTO>>(){});
            return new Respuesta(true, "ServiciosAeropuerto",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByCobroRango(Float mas, Float menos){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("cobroMas", mas);
            parametros.put("cobroMenos", menos);
            ConexionService conexion = new ConexionService("servicios_registrados/list/cobroRango", "/{cobroMas}/{cobroMenos}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los servicios aeropuerto por cobro");
            }
            List<ServicioRegistradoDTO> result = (List<ServicioRegistradoDTO>) conexion.readEntity(new GenericType<List<ServicioRegistradoDTO>>(){});
            return new Respuesta(true, "ServiciosAeropuerto",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
