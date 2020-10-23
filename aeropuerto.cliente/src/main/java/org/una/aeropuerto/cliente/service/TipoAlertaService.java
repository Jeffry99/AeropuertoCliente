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
import org.una.aeropuerto.cliente.dto.TipoAlertaDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class TipoAlertaService {
    public Respuesta crear(TipoAlertaDTO alerta){
        try{
            ConexionService conexion = new ConexionService("alertas/crear");
            conexion.post(alerta);
            if(conexion.isError()){
                System.out.println("Error creacion de rol: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el tipo de alerta");
            }
            TipoAlertaDTO result = (TipoAlertaDTO) conexion.readEntity(TipoAlertaDTO.class);
            return new Respuesta(true, "TipoAlerta", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion del tipo de alerta: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, TipoAlertaDTO alerta){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("alertas/modificar", "/{id}", parametros);
            conexion.put(alerta);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el tipo de alerta");
            }
            TipoAlertaDTO result = (TipoAlertaDTO) conexion.readEntity(TipoAlertaDTO.class);
            return new Respuesta(true, "TipoAlerta", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de tipo de alerta: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("alertas", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de alertas por su id");
            }
            TipoAlertaDTO result = (TipoAlertaDTO) conexion.readEntity(TipoAlertaDTO.class);
            return new Respuesta(true, "TiposAlertas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("alertas/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los tipos de alertas");
            }
            List<TipoAlertaDTO> result = (List<TipoAlertaDTO>) conexion.readEntity(new GenericType<List<TipoAlertaDTO>>(){});
            return new Respuesta(true, "TiposAlertas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("alertas/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de alertas por el estado");
            }
            List<TipoAlertaDTO> result = (List<TipoAlertaDTO>) conexion.readEntity(new GenericType<List<TipoAlertaDTO>>(){});
            return new Respuesta(true, "TiposAlertas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByDescripcionAproximate(String descripcion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", descripcion);
            ConexionService conexion = new ConexionService("alertas/list/descripcion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los tipos de alertas por la descripcion");
            }
            List<TipoAlertaDTO> result = (List<TipoAlertaDTO>) conexion.readEntity(new GenericType<List<TipoAlertaDTO>>(){});
            return new Respuesta(true, "TiposAlertas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
