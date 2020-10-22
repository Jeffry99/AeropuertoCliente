/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.dto;

import java.util.Date;
import javax.json.bind.annotation.JsonbDateFormat;

/**
 *
 * @author Pablo-VE
 */
public class TransaccionDTO {
    private Long id;
    private String descripcion;
    private String lugar;
    private String rol;
    private UsuarioDTO usuario;
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date fechaRegistro;
    private boolean estado;

    public TransaccionDTO() {
    }

    public TransaccionDTO(Long id, String descripcion, String lugar, String rol, UsuarioDTO usuario, Date fechaRegistro, boolean estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.rol = rol;
        this.usuario = usuario;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public String toString(){
        return descripcion+" por "+usuario.getEmpleado().getNombre()+" el "+fechaRegistro+" en "+lugar;
    }
    
}
