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
import org.una.aeropuerto.cliente.dto.RutaDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class RutaService {
    public Respuesta crear(RutaDTO ruta){
        try{
            ConexionService conexion = new ConexionService("rutas/crear");
            conexion.post(ruta);
            if(conexion.isError()){
                System.out.println("Error creacion de ruta: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear la ruta");
            }
            RutaDTO result = (RutaDTO) conexion.readEntity(RutaDTO.class);
            return new Respuesta(true, "Ruta", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de ruta: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    public Respuesta modificar(Long id, RutaDTO ruta){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("rutas/modificar", "/{id}", parametros);
            conexion.put(ruta);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar la ruta");
            }
            RutaDTO result = (RutaDTO) conexion.readEntity(RutaDTO.class);
            return new Respuesta(true, "Ruta", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de ruta: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("rutas", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar la ruta por su id");
            }
            RutaDTO result = (RutaDTO) conexion.readEntity(RutaDTO.class);
            return new Respuesta(true, "Ruta",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("rutas/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todas las rutas");
            }
            List<RutaDTO> result = (List<RutaDTO>) conexion.readEntity(new GenericType<List<RutaDTO>>(){});
            return new Respuesta(true, "Rutas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("rutas/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las rutas por estado");
            }
            List<RutaDTO> result = (List<RutaDTO>) conexion.readEntity(new GenericType<List<RutaDTO>>(){});
            return new Respuesta(true, "Rutas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByOrigenAproximate(String origen){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", origen);
            ConexionService conexion = new ConexionService("rutas/list/origen", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las rutas por origen");
            }
            List<RutaDTO> result = (List<RutaDTO>) conexion.readEntity(new GenericType<List<RutaDTO>>(){});
            return new Respuesta(true, "Rutas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByDestinoAproximate(String destino){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", destino);
            ConexionService conexion = new ConexionService("rutas/list/destino", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las rutas por destino");
            }
            List<RutaDTO> result = (List<RutaDTO>) conexion.readEntity(new GenericType<List<RutaDTO>>(){});
            return new Respuesta(true, "Rutas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByDistanciaRango(float distancia){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", distancia);
            ConexionService conexion = new ConexionService("rutas/list/destino", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las rutas por distancia");
            }
            List<RutaDTO> result = (List<RutaDTO>) conexion.readEntity(new GenericType<List<RutaDTO>>(){});
            return new Respuesta(true, "Rutas",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
