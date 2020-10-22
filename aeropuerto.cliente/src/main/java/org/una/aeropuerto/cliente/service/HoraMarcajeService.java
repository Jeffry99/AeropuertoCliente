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
import org.una.aeropuerto.cliente.dto.HoraMarcajeDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Pablo-VE
 */
public class HoraMarcajeService {
    public Respuesta crear(HoraMarcajeDTO horaMarcaje){
        try{
            ConexionService conexion = new ConexionService("horas_marcajes/crear");
            conexion.post(horaMarcaje);
            if(conexion.isError()){
                System.out.println("Error creacion de hora marcaje: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear la hora de marcaje");
            }
            HoraMarcajeDTO result = (HoraMarcajeDTO) conexion.readEntity(HoraMarcajeDTO.class);
            return new Respuesta(true, "HoraMarcaje", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de hora de marcaje: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta modificar(Long id, HoraMarcajeDTO horaMarcaje){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("horas_marcajes/modificar", "/{id}", parametros);
            conexion.put(horaMarcaje);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar la hora de marcaje");
            }
            HoraMarcajeDTO result = (HoraMarcajeDTO) conexion.readEntity(HoraMarcajeDTO.class);
            return new Respuesta(true, "HoraMarcaje", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de hora de marcaje: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByEmpleado(Long empleadoID){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", empleadoID);
            ConexionService conexion = new ConexionService("horas_marcajes/list/empleado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las horas de marcaje del empleado");
            }
            List<HoraMarcajeDTO> result = (List<HoraMarcajeDTO>) conexion.readEntity(new GenericType<List<HoraMarcajeDTO>>(){});
            return new Respuesta(true, "HorasMarcajes",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("horas_marcajes/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las horas de marcaje por estado");
            }
            List<HoraMarcajeDTO> result = (List<HoraMarcajeDTO>) conexion.readEntity(new GenericType<List<HoraMarcajeDTO>>(){});
            return new Respuesta(true, "HorasMarcajes",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByTipo(String tipo){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", tipo);
            ConexionService conexion = new ConexionService("horas_marcajes/list/tipo", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar las horas de marcaje por tipo");
            }
            List<HoraMarcajeDTO> result = (List<HoraMarcajeDTO>) conexion.readEntity(new GenericType<List<HoraMarcajeDTO>>(){});
            return new Respuesta(true, "HorasMarcajes",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("horas_marcajes", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al busca la hora de marcaje por su id");
            }
            HoraMarcajeDTO result = (HoraMarcajeDTO) conexion.readEntity(HoraMarcajeDTO.class);
            return new Respuesta(true, "HoraMarcaje",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("horas_marcajes/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todas las horas de marcaje");
            }
            List<HoraMarcajeDTO> result = (List<HoraMarcajeDTO>) conexion.readEntity(new GenericType<List<HoraMarcajeDTO>>(){});
            return new Respuesta(true, "HorasMarcajes",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
}
