/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aerolinea.cliente.service;

import org.una.aerolinea.cliente.conexion.ConexionService;
import org.una.aerolinea.cliente.dto.AuthenticationRequest;
import org.una.aerolinea.cliente.dto.AuthenticationResponse;
import org.una.aerolinea.cliente.dto.UsuarioAutenticado;
import org.una.aerolinea.cliente.util.Respuesta;

/**
 *
 * @author Pablo-VE
 */
public class AutenticacionService {
    public Respuesta Login(String cedula, String password){
        try{
            AuthenticationRequest authetication = new AuthenticationRequest(cedula, password);
            ConexionService conexion = new ConexionService("autenticacion/login");
            conexion.post(authetication);
            if(conexion.isError()){
                System.out.println(conexion.getError());
                return new Respuesta(false, conexion.getError(), "Error al iniciar sesion");
            }
            AuthenticationResponse usuario = (AuthenticationResponse) conexion.readEntity(AuthenticationResponse.class);
            UsuarioAutenticado.getInstance().setUsuarioLogeado(usuario.getUsuario());
            UsuarioAutenticado.getInstance().setTokenJwt(usuario.getJwt());
            return new Respuesta(true, "Inicio de sesión exitoso");
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return new Respuesta(false, ex.toString(), "No puedo establecerce conexion con el servidor");
        }
    }
}
