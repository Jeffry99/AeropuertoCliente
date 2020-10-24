/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.dto;

/**
 *
 * @author Pablo-VE
 */
public class TrabajoEmpleadoDTO {
    private Long id;
    EmpleadoDTO empleado;
    private AreaTrabajoDTO areaTrabajo;
    private boolean estado;

    public TrabajoEmpleadoDTO() {
    }

    public TrabajoEmpleadoDTO(Long id, EmpleadoDTO empleado, AreaTrabajoDTO areaTrabajo, boolean estado) {
        this.id = id;
        this.empleado = empleado;
        this.areaTrabajo = areaTrabajo;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    public AreaTrabajoDTO getAreaTrabajo() {
        return areaTrabajo;
    }

    public void setAreaTrabajo(AreaTrabajoDTO areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    
}
