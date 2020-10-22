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
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Pablo-VE
 */
public class RolService {
    public Respuesta crear(RolDTO rol){
        try{
            ConexionService conexion = new ConexionService("roles/crear");
            conexion.post(rol);
            if(conexion.isError()){
                System.out.println("Error creacion de rol: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el rol");
            }
            RolDTO result = (RolDTO) conexion.readEntity(RolDTO.class);
            return new Respuesta(true, "Rol", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de rol: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, RolDTO rol){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("roles/modificar", "/{id}", parametros);
            conexion.put(rol);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el rol");
            }
            RolDTO result = (RolDTO) conexion.readEntity(RolDTO.class);
            return new Respuesta(true, "Rol", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de rol: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("roles/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los roles por el estado");
            }
            List<RolDTO> result = (List<RolDTO>) conexion.readEntity(new GenericType<List<RolDTO>>(){});
            return new Respuesta(true, "Roles",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByDescripcionAproximate(String descripcion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", descripcion);
            ConexionService conexion = new ConexionService("roles/list/descripcion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los roles por la descripcion");
            }
            List<RolDTO> result = (List<RolDTO>) conexion.readEntity(new GenericType<List<RolDTO>>(){});
            return new Respuesta(true, "Roles",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByNombreAproximate(String nombre){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", nombre);
            ConexionService conexion = new ConexionService("roles/list/nombre", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los roles por su nombre");
            }
            List<RolDTO> result = (List<RolDTO>) conexion.readEntity(new GenericType<List<RolDTO>>(){});
            return new Respuesta(true, "Roles",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("roles", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los roles por su id");
            }
            RolDTO result = (RolDTO) conexion.readEntity(RolDTO.class);
            return new Respuesta(true, "Rol",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("roles/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los roles");
            }
            List<RolDTO> result = (List<RolDTO>) conexion.readEntity(new GenericType<List<RolDTO>>(){});
            return new Respuesta(true, "Roles",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
}
