/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;
import org.una.aeropuerto.cliente.conexion.ConexionService;
import org.una.aeropuerto.cliente.dto.ParametroAplicacionDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class ParametroAplicacionService {
    public Respuesta crear(ParametroAplicacionDTO trabajoEmpleado){
        try{
            ConexionService conexion = new ConexionService("parametros_aplicacion/crear");
            conexion.post(trabajoEmpleado);
            if(conexion.isError()){
                System.out.println("Error creacion del parametro de aplicacion: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el parametro de aplicacion");
            }
            ParametroAplicacionDTO result = (ParametroAplicacionDTO) conexion.readEntity(ParametroAplicacionDTO.class);
            return new Respuesta(true, "ParametroAplicacion", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion del parametro de aplicacion: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    public Respuesta modificar(Long id, ParametroAplicacionDTO trabajoEmpleado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("parametros_aplicacion/modificar", "/{id}", parametros);
            conexion.put(trabajoEmpleado);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el parametro de aplicacion");
            }
            ParametroAplicacionDTO result = (ParametroAplicacionDTO) conexion.readEntity(ParametroAplicacionDTO.class);
            return new Respuesta(true, "ParametroAplicacion", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion del parametro de aplicacion: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("parametros_aplicacion", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el parametro de aplicacion por su id");
            }
            ParametroAplicacionDTO result = (ParametroAplicacionDTO) conexion.readEntity(ParametroAplicacionDTO.class);
            return new Respuesta(true, "ParametroAplicacion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByNombreAndValor(String nombre, String valor){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("nombre", nombre);
            parametros.put("valor", valor);
            ConexionService conexion = new ConexionService("parametros_aplicacion/nombreValor", "/{nombre}/{valor}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al solicitar permiso");
            }
            ParametroAplicacionDTO result = (ParametroAplicacionDTO) conexion.readEntity(ParametroAplicacionDTO.class);
            return new Respuesta(true, "ParametroAplicacion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("parametros_aplicacion/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los parametros de aplicacion");
            }
            List<ParametroAplicacionDTO> result = resultado((List<ParametroAplicacionDTO>) conexion.readEntity(new GenericType<List<ParametroAplicacionDTO>>(){}));
            return new Respuesta(true, "ParametrosAplicacion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("parametros_aplicacion/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los parametros de aplicacion por el estado");
            }
            List<ParametroAplicacionDTO> result = resultado((List<ParametroAplicacionDTO>) conexion.readEntity(new GenericType<List<ParametroAplicacionDTO>>(){}));
            return new Respuesta(true, "ParametrosAplicacion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByNombreAproximate(String nombre){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", nombre);
            ConexionService conexion = new ConexionService("parametros_aplicacion/list/nombre", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los parametros de aplicacion por su nombre");
            }
            List<ParametroAplicacionDTO> result = resultado((List<ParametroAplicacionDTO>) conexion.readEntity(new GenericType<List<ParametroAplicacionDTO>>(){}));
            return new Respuesta(true, "ParametrosAplicacion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByDescripcionAproximate(String descripcion){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", descripcion);
            ConexionService conexion = new ConexionService("parametros_aplicacion/list/descripcion", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los parametros de aplicacion por la descripcion");
            }
            List<ParametroAplicacionDTO> result = resultado((List<ParametroAplicacionDTO>) conexion.readEntity(new GenericType<List<ParametroAplicacionDTO>>(){}));
            return new Respuesta(true, "ParametrosAplicacion",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    
    private List<ParametroAplicacionDTO> resultado(List<ParametroAplicacionDTO> result){
        List<ParametroAplicacionDTO> result1 = new ArrayList<ParametroAplicacionDTO>();
        if(result.size()>0){
            for(int i=0; i<result.size(); i++){
                if(!result.get(i).getNombre().equals("ContraseñaAdministrador")&&!result.get(i).getNombre().equals("ContraseñaGerente")){
                    result1.add(result.get(i));
                }
            }
        }
        return result1;
        
    }
}
