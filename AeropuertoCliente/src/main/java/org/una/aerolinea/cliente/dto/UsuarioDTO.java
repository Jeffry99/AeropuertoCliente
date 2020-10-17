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
public class UsuarioDTO {
    private Long id;
    private String passwordEncriptado;
    private EmpleadoDTO empleado;
    private RolDTO rol;
    private boolean estado;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String passwordEncriptado, EmpleadoDTO empleado, RolDTO rol, boolean estado) {
        this.id = id;
        this.passwordEncriptado = passwordEncriptado;
        this.empleado = empleado;
        this.rol = rol;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPasswordEncriptado() {
        return passwordEncriptado;
    }

    public void setPasswordEncriptado(String passwordEncriptado) {
        this.passwordEncriptado = passwordEncriptado;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    public RolDTO getRol() {
        return rol;
    }

    public void setRol(RolDTO rol) {
        this.rol = rol;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
}
