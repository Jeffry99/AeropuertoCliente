/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aerolinea.cliente.dto;

/**
 *
 * @author Pablo-VE
 */
public class AuthenticationResponse {
    private String jwt;
    private UsuarioDTO usuario;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String jwt, UsuarioDTO usuario) {
        this.jwt = jwt;
        this.usuario = usuario;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
    
    
}
