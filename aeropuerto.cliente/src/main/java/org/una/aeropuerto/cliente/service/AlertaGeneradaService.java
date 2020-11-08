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
import org.una.aeropuerto.cliente.dto.AlertaGeneradaDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class AlertaGeneradaService {
    public Respuesta crear(AlertaGeneradaDTO alerta){
        try{
            ConexionService conexion = new ConexionService("alertas_generadas/crear");
            conexion.post(alerta);
            if(conexion.isError()){
                System.out.println("Error creacion de alerta generada: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear la alerta generada");
            }
            AlertaGeneradaDTO result = (AlertaGeneradaDTO) conexion.readEntity(AlertaGeneradaDTO.class);
            return new Respuesta(true, "AlertaGenerada", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de alerta generada: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, AlertaGeneradaDTO alerta){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("alertas_generadas/modificar", "/{id}", parametros);
            conexion.put(alerta);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar la alerta generada");
            }
            AlertaGeneradaDTO result = (AlertaGeneradaDTO) conexion.readEntity(AlertaGeneradaDTO.class);
            return new Respuesta(true, "AlertaGenerada", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de alerta generada: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("alertas_generadas", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las alertas generadas por su id");
            }
            AlertaGeneradaDTO result = (AlertaGeneradaDTO) conexion.readEntity(AlertaGeneradaDTO.class);
            return new Respuesta(true, "AlertaGenerada",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("alertas_generadas/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todas las alertas generadas");
            }
            List<AlertaGeneradaDTO> result = (List<AlertaGeneradaDTO>) conexion.readEntity(new GenericType<List<AlertaGeneradaDTO>>(){});
            return new Respuesta(true, "AlertasGeneradas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByAutorizacion(Long autorizacion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", autorizacion);
            ConexionService conexion = new ConexionService("alertas_generadas/list/autorizacion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las alertas generadas por su autorizacion");
            }
            List<AlertaGeneradaDTO> result = (List<AlertaGeneradaDTO>) conexion.readEntity(new GenericType<List<AlertaGeneradaDTO>>(){});
            return new Respuesta(true, "AlertasGeneradas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByTipoAlerta(Long tipoAlerta){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", tipoAlerta);
            ConexionService conexion = new ConexionService("alertas_generadas/list/tipoAlerta", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las alertas generadas por su tipo de alerta");
            }
            List<AlertaGeneradaDTO> result = (List<AlertaGeneradaDTO>) conexion.readEntity(new GenericType<List<AlertaGeneradaDTO>>(){});
            return new Respuesta(true, "AlertasGeneradas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByVuelo(Long vuelo){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", vuelo);
            ConexionService conexion = new ConexionService("alertas_generadas/list/vuelo", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las alertas generadas por su vuelo");
            }
            List<AlertaGeneradaDTO> result = (List<AlertaGeneradaDTO>) conexion.readEntity(new GenericType<List<AlertaGeneradaDTO>>(){});
            return new Respuesta(true, "AlertasGeneradas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("alertas_generadas/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las alertas generadas por su estado");
            }
            List<AlertaGeneradaDTO> result = (List<AlertaGeneradaDTO>) conexion.readEntity(new GenericType<List<AlertaGeneradaDTO>>(){});
            return new Respuesta(true, "AlertasGeneradas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
