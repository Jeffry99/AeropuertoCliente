/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.dto;

import java.util.Date;

/**
 *
 * @author Pablo-VE
 */
public class HorarioDTO {
    private Long id;
    private int diaInicio;
    private Date horaInicio;
    private int diaFinal;
    private Date horaFinal;
    private EmpleadoDTO empleado;
    private boolean estado;

    public HorarioDTO() {
    }

    public HorarioDTO(Long id, int diaInicio, Date horaInicio, int diaFinal, Date horaFinal, EmpleadoDTO empleado, boolean estado) {
        this.id = id;
        this.diaInicio = diaInicio;
        this.horaInicio = horaInicio;
        this.diaFinal = diaFinal;
        this.horaFinal = horaFinal;
        this.empleado = empleado;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(int diaInicio) {
        this.diaInicio = diaInicio;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getDiaFinal() {
        return diaFinal;
    }

    public void setDiaFinal(int diaFinal) {
        this.diaFinal = diaFinal;
    }

    public Date getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
}
