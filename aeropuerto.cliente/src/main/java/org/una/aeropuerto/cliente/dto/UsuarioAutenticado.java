/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.dto;

import org.una.aeropuerto.cliente.service.ParametroAplicacionService;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Pablo-VE
 */
public class UsuarioAutenticado {
    private static UsuarioAutenticado instance = null;
    
    private UsuarioDTO usuarioLogeado;
    private String tokenJwt;
    
    private UsuarioAutenticado() {
        this.usuarioLogeado = new UsuarioDTO();
        tokenJwt="";
    } 
    
    public static UsuarioAutenticado getInstance(){
        if(instance == null){
            instance = new UsuarioAutenticado();
        }
        return instance;
    }

    public UsuarioDTO getUsuarioLogeado() {
        return usuarioLogeado;
    }

    public void setUsuarioLogeado(UsuarioDTO usuarioLogeado) {
        this.usuarioLogeado = usuarioLogeado;
    }

    public String getTokenJwt() {
        return tokenJwt;
    }

    public void setTokenJwt(String tokenJwt) {
        this.tokenJwt = tokenJwt;
    }
    
    public boolean solicitarPermisoGerente(){
        
        return false;
    }
    
    public String getRol(){
        return usuarioLogeado.getRol().getNombre();
    }
    
    
    
    
}
