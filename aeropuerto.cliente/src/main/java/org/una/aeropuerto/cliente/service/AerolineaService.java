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
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class AerolineaService {
    public Respuesta crear(AerolineaDTO aerolinea){
        try{
            ConexionService conexion = new ConexionService("aerolineas/crear");
            conexion.post(aerolinea);
            if(conexion.isError()){
                System.out.println("Error creacion de aerolineas: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear la aerolinea");
            }
            AerolineaDTO result = (AerolineaDTO) conexion.readEntity(AerolineaDTO.class);
            return new Respuesta(true, "Aerolinea", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de aerolinea: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, AerolineaDTO aerolinea){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("aerolineas/modificar", "/{id}", parametros);
            conexion.put(aerolinea);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar la aerolinea");
            }
            AerolineaDTO result = (AerolineaDTO) conexion.readEntity(AerolineaDTO.class);
            return new Respuesta(true, "Aerolinea", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de aerolinea: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("aerolineas", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar laa aerolineas por su id");
            }
            AerolineaDTO result = (AerolineaDTO) conexion.readEntity(AerolineaDTO.class);
            return new Respuesta(true, "Aerolineas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("aerolineas/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todas las aerolineas");
            }
            List<AerolineaDTO> result = (List<AerolineaDTO>) conexion.readEntity(new GenericType<List<AerolineaDTO>>(){});
            return new Respuesta(true, "Aerolineas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("aerolineas/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las aerolineas por el estado");
            }
            List<AerolineaDTO> result = (List<AerolineaDTO>) conexion.readEntity(new GenericType<List<AerolineaDTO>>(){});
            return new Respuesta(true, "Aerolineas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByNombreAproximate(String nombre){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", nombre);
            ConexionService conexion = new ConexionService("aerolineas/list/nombre", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las aerolineas por nombre");
            }
            List<AerolineaDTO> result = (List<AerolineaDTO>) conexion.readEntity(new GenericType<List<AerolineaDTO>>(){});
            return new Respuesta(true, "Aerolineas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByResponsableAproximate(String responsable){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", responsable);
            ConexionService conexion = new ConexionService("aerolineas/list/responsable", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las aerolineas por responsable");
            }
            List<AerolineaDTO> result = (List<AerolineaDTO>) conexion.readEntity(new GenericType<List<AerolineaDTO>>(){});
            return new Respuesta(true, "Aerolineas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
