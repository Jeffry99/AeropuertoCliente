/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aerolinea.cliente.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;
import org.una.aerolinea.cliente.conexion.ConexionService;
import org.una.aerolinea.cliente.dto.TrabajoEmpleadoDTO;
import org.una.aerolinea.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class TrabajoEmpleadoService {
    public Respuesta crear(TrabajoEmpleadoDTO trabajoEmpleado){
        try{
            ConexionService conexion = new ConexionService("trabajos_empleados/crear");
            conexion.post(trabajoEmpleado);
            if(conexion.isError()){
                System.out.println("Error creacion de trabajo empleado: "+conexion.getError());
                return new Respuesta(false, conexion.getError(), "No se pudo crear el trabajo empleado");
            }
            TrabajoEmpleadoDTO result = (TrabajoEmpleadoDTO) conexion.readEntity(TrabajoEmpleadoDTO.class);
            return new Respuesta(true, "TrabajoEmpleado", result);
        }catch(Exception ex){
            System.out.println("Excepcion creacion de trabajo empleado: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    public Respuesta modificar(Long id, TrabajoEmpleadoDTO trabajoEmpleado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("trabajos_empleados/modificar", "/{id}", parametros);
            conexion.put(trabajoEmpleado);
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "No se pudo modificar el trabajo empleado");
            }
            TrabajoEmpleadoDTO result = (TrabajoEmpleadoDTO) conexion.readEntity(TrabajoEmpleadoDTO.class);
            return new Respuesta(true, "TrabajoEmpleado", result);
        }catch(Exception ex){
            System.out.println("Excepcion modificacion de trabajo empleado: "+ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getById(Long id){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            ConexionService conexion = new ConexionService("trabajos_empleados", "/{id}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar el trabajo empleado por su id");
            }
            TrabajoEmpleadoDTO result = (TrabajoEmpleadoDTO) conexion.readEntity(TrabajoEmpleadoDTO.class);
            return new Respuesta(true, "TrabajoEmpleado",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getAll(){
        try{
            ConexionService conexion = new ConexionService("trabajos_empleados/");
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar todos los trabajos empleados");
            }
            List<TrabajoEmpleadoDTO> result = (List<TrabajoEmpleadoDTO>) conexion.readEntity(new GenericType<List<TrabajoEmpleadoDTO>>(){});
            return new Respuesta(true, "TrabajosEmpleados",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByEstado(boolean estado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", estado);
            ConexionService conexion = new ConexionService("trabajos_empleados/list/estado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los trabajos empleados por el estado");
            }
            List<TrabajoEmpleadoDTO> result = (List<TrabajoEmpleadoDTO>) conexion.readEntity(new GenericType<List<TrabajoEmpleadoDTO>>(){});
            return new Respuesta(true, "TrabajosEmpleados",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByEmpleado(Long empleado){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", empleado);
            ConexionService conexion = new ConexionService("trabajos_empleados/list/empleado", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los empleados de un trabajo empleado");
            }
            List<TrabajoEmpleadoDTO> result = (List<TrabajoEmpleadoDTO>) conexion.readEntity(new GenericType<List<TrabajoEmpleadoDTO>>(){});
            return new Respuesta(true, "TrabajosEmpleados",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
    
    public Respuesta getByAreaTrabajo(Long areaTrabajo){
        try{
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("term", areaTrabajo);
            ConexionService conexion = new ConexionService("trabajos_empleados/list/area_trabajo", "/{term}", parametros);
            conexion.get();
            if(conexion.isError()){
                return new Respuesta(false, conexion.getError(), "Error al buscar los empleados de un trabajo empleado");
            }
            List<TrabajoEmpleadoDTO> result = (List<TrabajoEmpleadoDTO>) conexion.readEntity(new GenericType<List<TrabajoEmpleadoDTO>>(){});
            return new Respuesta(true, "TrabajosEmpleados",result);
        }catch(Exception ex){
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
}
