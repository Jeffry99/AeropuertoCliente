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
import org.una.aeropuerto.cliente.dto.BitacoraAvionDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class BitacoraAvionService {
    public Respuesta crear(BitacoraAvionDTO bitacora){
        try{
            ConexionService conexion = new ConexionService("bitacoras_aviones/crear");
            conexion.post(bitacora);
            if(conexion.isError()){
                System.out.println("Error creacion de rol: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el tipo de alerta");
            }
            BitacoraAvionDTO result = (BitacoraAvionDTO) conexion.readEntity(BitacoraAvionDTO.class);
            return new Respuesta(true, "BitacoraAvion", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion del tipo de alerta: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, BitacoraAvionDTO bitacora){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("bitacoras_aviones/modificar", "/{id}", parametros);
            conexion.put(bitacora);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar la bitacora del avion");
            }
            BitacoraAvionDTO result = (BitacoraAvionDTO) conexion.readEntity(BitacoraAvionDTO.class);
            return new Respuesta(true, "BitacoraAvion", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de la bitacora del avion: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("bitacoras_aviones", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las bitacoras de aviones por su id");
            }
            BitacoraAvionDTO result = (BitacoraAvionDTO) conexion.readEntity(BitacoraAvionDTO.class);
            return new Respuesta(true, "BitacoraAvion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("bitacoras_aviones/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todas las bitacoras de aviones");
            }
            List<BitacoraAvionDTO> result = (List<BitacoraAvionDTO>) conexion.readEntity(new GenericType<List<BitacoraAvionDTO>>(){});
            return new Respuesta(true, "BitacorasAvion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("bitacoras_aviones/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las bitacoras de aviones por el estado");
            }
            List<BitacoraAvionDTO> result = (List<BitacoraAvionDTO>) conexion.readEntity(new GenericType<List<BitacoraAvionDTO>>(){});
            return new Respuesta(true, "BitacorasAvion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByCombustible(int combustible){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", combustible);
            ConexionService conexion = new ConexionService("bitacoras_aviones/list/combustible", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las bitacoras de aviones por combustible");
            }
            List<BitacoraAvionDTO> result = (List<BitacoraAvionDTO>) conexion.readEntity(new GenericType<List<BitacoraAvionDTO>>(){});
            return new Respuesta(true, "BitacorasAvion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByDistanciaRecorrida(int distancia){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", distancia);
            ConexionService conexion = new ConexionService("bitacoras_aviones/list/distancia", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las bitacoras de aviones por distancia");
            }
            List<BitacoraAvionDTO> result = (List<BitacoraAvionDTO>) conexion.readEntity(new GenericType<List<BitacoraAvionDTO>>(){});
            return new Respuesta(true, "BitacorasAvion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByTiempoTierra(int tiempo){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", tiempo);
            ConexionService conexion = new ConexionService("bitacoras_aviones/list/tiempo", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las bitacoras de aviones por tiempo en tierra");
            }
            List<BitacoraAvionDTO> result = (List<BitacoraAvionDTO>) conexion.readEntity(new GenericType<List<BitacoraAvionDTO>>(){});
            return new Respuesta(true, "BitacorasAvion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByUbicacion(String ubicacion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", ubicacion);
            ConexionService conexion = new ConexionService("bitacoras_aviones/list/ubicacion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las bitacoras de aviones por ubicacion");
            }
            List<BitacoraAvionDTO> result = (List<BitacoraAvionDTO>) conexion.readEntity(new GenericType<List<BitacoraAvionDTO>>(){});
            return new Respuesta(true, "BitacorasAvion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
}
