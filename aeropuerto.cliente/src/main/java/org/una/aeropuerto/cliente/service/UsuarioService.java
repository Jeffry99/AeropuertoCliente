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
import org.una.aeropuerto.cliente.dto.UsuarioDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Pablo-VE
 */
public class UsuarioService {
    public Respuesta crear(UsuarioDTO usuario){
        try{
            ConexionService conexion = new ConexionService("usuarios/crear");
            conexion.post(usuario);
            if(conexion.isError()){
                System.out.println("Error creacion de usuario: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el usuario");
            }
            UsuarioDTO result = (UsuarioDTO) conexion.readEntity(UsuarioDTO.class);
            return new Respuesta(true, "Usuario", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de usuario: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, UsuarioDTO usuario){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("usuarios/modificar", "/{id}", parametros);
            conexion.put(usuario);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el usuario");
            }
            UsuarioDTO result = (UsuarioDTO) conexion.readEntity(UsuarioDTO.class);
            return new Respuesta(true, "Usuario", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de usuario: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByRol(Long rol){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", rol);
            ConexionService conexion = new ConexionService("usuarios/list/rol", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los empleados por el rol");
            }
            List<UsuarioDTO> result = (List<UsuarioDTO>) conexion.readEntity(new GenericType<List<UsuarioDTO>>(){});
            return new Respuesta(true, "Usuarios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("usuarios/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los usuarios por el estado");
            }
            List<UsuarioDTO> result = (List<UsuarioDTO>) conexion.readEntity(new GenericType<List<UsuarioDTO>>(){});
            return new Respuesta(true, "Usuarios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("usuarios", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el usuario por su id");
            }
            UsuarioDTO result = (UsuarioDTO) conexion.readEntity(UsuarioDTO.class);
            return new Respuesta(true, "Usuario",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("usuarios/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los usuarios");
            }
            List<UsuarioDTO> result = (List<UsuarioDTO>) conexion.readEntity(new GenericType<List<UsuarioDTO>>(){});
            return new Respuesta(true, "Usuarios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
}
