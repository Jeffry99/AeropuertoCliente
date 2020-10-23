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
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Pablo-VE
 */
public class EmpleadoService {
    public Respuesta crear(EmpleadoDTO empleado){
        try{
            ConexionService conexion = new ConexionService("empleados/crear");
            conexion.post(empleado);
            if(conexion.isError()){
                System.out.println("Error creacion de empleado: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el empleado");
            }
            EmpleadoDTO result = (EmpleadoDTO) conexion.readEntity(EmpleadoDTO.class);
            return new Respuesta(true, "Empleado", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de empleado: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, EmpleadoDTO empleado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("empleados/modificar", "/{id}", parametros);
            conexion.put(empleado);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el empleado");
            }
            EmpleadoDTO result = (EmpleadoDTO) conexion.readEntity(EmpleadoDTO.class);
            return new Respuesta(true, "Empleado", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de empleado: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("empleados/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los empleados por el estado");
            }
            List<EmpleadoDTO> result = (List<EmpleadoDTO>) conexion.readEntity(new GenericType<List<EmpleadoDTO>>(){});
            return new Respuesta(true, "Empleados",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByNombreAproximate(String nombre){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", nombre);
            ConexionService conexion = new ConexionService("empleados/list/nombre", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los empleados por su nombre");
            }
            List<EmpleadoDTO> result = (List<EmpleadoDTO>) conexion.readEntity(new GenericType<List<EmpleadoDTO>>(){});
            return new Respuesta(true, "Empleados",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByCedula(String cedula){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", cedula);
            ConexionService conexion = new ConexionService("empleados/cedula", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el empleado por su id");
            }
            EmpleadoDTO result = (EmpleadoDTO) conexion.readEntity(EmpleadoDTO.class);
            return new Respuesta(true, "Empleado",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByCedulaAproximate(String cedula){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", cedula);
            ConexionService conexion = new ConexionService("empleados/list/cedula", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los empleados por su cedula");
            }
            List<EmpleadoDTO> result = (List<EmpleadoDTO>) conexion.readEntity(new GenericType<List<EmpleadoDTO>>(){});
            return new Respuesta(true, "Empleados",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("empleados", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el empleado por su id");
            }
            EmpleadoDTO result = (EmpleadoDTO) conexion.readEntity(EmpleadoDTO.class);
            return new Respuesta(true, "Empleado",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("empleados/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los empleados");
            }
            List<EmpleadoDTO> result = (List<EmpleadoDTO>) conexion.readEntity(new GenericType<List<EmpleadoDTO>>(){});
            return new Respuesta(true, "Empleados",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
