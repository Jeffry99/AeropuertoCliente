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
import org.una.aeropuerto.cliente.dto.TransaccionDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Pablo-VE
 */
public class TransaccionService {
    public Respuesta crear(TransaccionDTO transaccion){
        try{
            ConexionService conexion = new ConexionService("transacciones/crear");
            conexion.post(transaccion);
            if(conexion.isError()){
                System.out.println("Error creacion de transaccion: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear la transaccion");
            }
            TransaccionDTO result = (TransaccionDTO) conexion.readEntity(TransaccionDTO.class);
            return new Respuesta(true, "Transaccion", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de transaccion: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, TransaccionDTO transaccion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("transacciones/modificar", "/{id}", parametros);
            conexion.put(transaccion);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar la transaccion");
            }
            TransaccionDTO result = (TransaccionDTO) conexion.readEntity(TransaccionDTO.class);
            return new Respuesta(true, "Transaccion", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de usuario: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByRolAproximate(String rol){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", rol);
            ConexionService conexion = new ConexionService("transacciones/list/rol", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las transacciones por su rol");
            }
            List<TransaccionDTO> result = (List<TransaccionDTO>) conexion.readEntity(new GenericType<List<TransaccionDTO>>(){});
            return new Respuesta(true, "Transacciones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("transacciones/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las transacciones por el estado");
            }
            List<TransaccionDTO> result = (List<TransaccionDTO>) conexion.readEntity(new GenericType<List<TransaccionDTO>>(){});
            return new Respuesta(true, "Transaccion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByLugarAproximate(String lugar){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", lugar);
            ConexionService conexion = new ConexionService("transacciones/list/lugar", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las transacciones por el lugar donde se hicieron");
            }
            List<TransaccionDTO> result = (List<TransaccionDTO>) conexion.readEntity(new GenericType<List<TransaccionDTO>>(){});
            return new Respuesta(true, "Transacciones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByDescripcionAproximate(String descripcion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", descripcion);
            ConexionService conexion = new ConexionService("transacciones/list/descripcion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las transacciones por su descripcion");
            }
            List<TransaccionDTO> result = (List<TransaccionDTO>) conexion.readEntity(new GenericType<List<TransaccionDTO>>(){});
            return new Respuesta(true, "Transacciones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("transacciones", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar la transaccion por su id");
            }
            TransaccionDTO result = (TransaccionDTO) conexion.readEntity(TransaccionDTO.class);
            return new Respuesta(true, "Transaccion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("transacciones/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todas las transacciones");
            }
            List<TransaccionDTO> result = (List<TransaccionDTO>) conexion.readEntity(new GenericType<List<TransaccionDTO>>(){});
            return new Respuesta(true, "Transacciones",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
}
