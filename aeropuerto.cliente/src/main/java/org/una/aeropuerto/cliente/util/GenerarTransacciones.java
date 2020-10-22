/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.util;

import org.una.aeropuerto.cliente.dto.TransaccionDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.TransaccionService;

/**
 *
 * @author Pablo-VE
 */
public class GenerarTransacciones {
    
    
    public static void crearTransaccion(String descripcion, String lugar){
        TransaccionService transaccionService = new TransaccionService();
        TransaccionDTO transaccion = new TransaccionDTO();
        transaccion.setEstado(true);
        transaccion.setDescripcion(descripcion);
        transaccion.setLugar(lugar);
        transaccion.setRol(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre());
        transaccion.setUsuario(UsuarioAutenticado.getInstance().getUsuarioLogeado());
        
        Respuesta respuesta = transaccionService.crear(transaccion);
        if(respuesta.getEstado()==true){
            transaccion=(TransaccionDTO) respuesta.getResultado("Transaccion");
            System.out.println("Transaccion creada: "+transaccion.toString());
        }else{
            System.out.println("Error al crear transaccion: "+respuesta.getMensajeInterno());
        }
    }
}
