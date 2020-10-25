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
import org.una.aeropuerto.cliente.dto.HorarioDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Pablo-VE
 */
public class HorarioService {
    public Respuesta crear(HorarioDTO horario){
        try{
            ConexionService conexion = new ConexionService("horarios/crear");
            conexion.post(horario);
            if(conexion.isError()){
                System.out.println("Error creacion de hora marcaje: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el horario");
            }
            HorarioDTO result = (HorarioDTO) conexion.readEntity(HorarioDTO.class);
            return new Respuesta(true, "Horario", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de hora de marcaje: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, HorarioDTO horario){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("horarios/modificar", "/{id}", parametros);
            conexion.put(horario);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el horario");
            }
            HorarioDTO result = (HorarioDTO) conexion.readEntity(HorarioDTO.class);
            return new Respuesta(true, "Horario", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion horario: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEmpleadoAndEstado(Long empleado, boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term1", empleado);
            parametros.put("term2", estado);
            ConexionService conexion = new ConexionService("horarios/list/empleadohorario", "/{term1}/{term2}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los horarios activos de un empleado");
            }
            List<HorarioDTO> result = (List<HorarioDTO>) conexion.readEntity(new GenericType<List<HorarioDTO>>(){});
            return new Respuesta(true, "Horarios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEmpleado(Long empleado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", empleado);
            ConexionService conexion = new ConexionService("horarios/list/empleado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los horarios de un empleado");
            }
            List<HorarioDTO> result = (List<HorarioDTO>) conexion.readEntity(new GenericType<List<HorarioDTO>>(){});
            return new Respuesta(true, "Horarios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("horarios/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los horarios por estado");
            }
            List<HorarioDTO> result = (List<HorarioDTO>) conexion.readEntity(new GenericType<List<HorarioDTO>>(){});
            return new Respuesta(true, "Horarios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("horarios", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar un horario por su id");
            }
            HorarioDTO result = (HorarioDTO) conexion.readEntity(HorarioDTO.class);
            return new Respuesta(true, "Horario",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("horarios/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los horarios");
            }
            List<HorarioDTO> result = (List<HorarioDTO>) conexion.readEntity(new GenericType<List<HorarioDTO>>(){});
            return new Respuesta(true, "Horarios",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No pudo establecerse conexion con el servidor");
        }
    }
}
