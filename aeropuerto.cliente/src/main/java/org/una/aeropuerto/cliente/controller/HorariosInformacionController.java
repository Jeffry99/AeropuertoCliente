/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;


import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.HorarioDTO;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class HorariosInformacionController implements Initializable {

    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ComboBox<String> cbxDiaInicio;
    @FXML
    private ComboBox<String> cbxDiaFinal;
    @FXML
    private ComboBox<EmpleadoDTO> cbxEmpleado;
    @FXML
    private Label lblEstado2;
    @FXML
    private RadioButton rbActivoUsuario;
    @FXML
    private RadioButton rbInactivoUsuario;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    
    
    /**
     * Initializes the controller class.
     */
    private String modalidad="";
    private HorarioDTO horarioEnCuestion = new HorarioDTO();
    
    
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    @FXML
    private JFXTimePicker horaInicio;
    @FXML
    private JFXTimePicker horaFinal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDias();
        initEmpleados();
        modalidad= (String) AppContext.getInstance().get("ModalidadHorario");
        
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            horarioEnCuestion=(HorarioDTO) AppContext.getInstance().get("HorarioEnCuestion");
            
            cbxDiaInicio.setValue(getDiaString(horarioEnCuestion.getDiaInicio()));
            cbxDiaFinal.setValue(getDiaString(horarioEnCuestion.getDiaFinal()));
            //setHoraInicio
            String horaDeInicio = formatter.format(horarioEnCuestion.getHoraInicio());
            horaInicio.setValue(LocalTime.parse(horaDeInicio));
            
            //setHoraFinal
            String horaDeFin = formatter.format(horarioEnCuestion.getHoraInicio());
            horaFinal.setValue(LocalTime.parse(horaDeFin));
            
            cbxEmpleado.setValue(horarioEnCuestion.getEmpleado());
            
            if(horarioEnCuestion.getEstado()==true){
                estado = true;
                rbActivoUsuario.setSelected(true);
                rbInactivoUsuario.setSelected(false);
            }else{
                estado = false;
                rbActivoUsuario.setSelected(false);
                rbInactivoUsuario.setSelected(true);
            }
            
            cbxEmpleado.setDisable(true);
            if(modalidad.equals("Ver")){
                cbxDiaInicio.setDisable(true);
                cbxDiaFinal.setDisable(true);
                btnGuardar.setVisible(false);
                btnGuardar.setDisable(true);
                //inhabilirar hora inicio
                //inhabilirar hora final
            }
            
        }
        
    }    
    
    public void initDias(){
        ArrayList dias = new ArrayList();
        dias.add("Lunes");
        dias.add("Martes");
        dias.add("Miércoles");
        dias.add("Jueves");
        dias.add("Viernes");
        dias.add("Sábado");
        dias.add("Domingo");
        ObservableList items3 = FXCollections.observableArrayList(dias);   
        cbxDiaInicio.setItems(items3);
        cbxDiaFinal.setItems(items3);
    }
    
    public void initEmpleados(){
        EmpleadoService empleadoService= new EmpleadoService();
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta = empleadoService.getAll();
        if(respuesta.getEstado()==true){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
        }
        ObservableList items2 = FXCollections.observableArrayList(empleados);   
        cbxEmpleado.setItems(items2);
    }

    
    
    private Boolean estado;
    @FXML
    private void actEstadoActivoUsuario(ActionEvent event) {
        estado = true;
        rbActivoUsuario.setSelected(true);
        rbInactivoUsuario.setSelected(false);
    }

    @FXML
    private void actEstadoInactivoUsuario(ActionEvent event) {
        estado = false;
        rbActivoUsuario.setSelected(false);
        rbInactivoUsuario.setSelected(true);
    }

    @FXML
    private void actGuardar(ActionEvent event) {
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }
    
    public String getDiaString(int d){
        if(d==1){
            return "Lunes";
        }else{
            if(d==2){
                return "Martes";
            }else{
                if(d==3){
                    return "Miércoles";
                }else{
                    if(d==4){
                        return "Jueves";
                    }else{
                        if(d==5){
                            return "Viernes";
                        }else{
                            if(d==6){
                                return "Sábado";
                            }else{
                                if(d==7){
                                    return "Domingo";
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public int getDiaInt(String d){
        if(d.equals("Lunes")){
            return 1;
        }else{
            if(d.equals("Martes")){
                return 2;
            }else{
                if(d.equals("Miércoles")){
                    return 3;
                }else{
                    if(d.equals("Jueves")){
                        return 4;
                    }else{
                        if(d.equals("Viernes")){
                            return 5;
                        }else{
                            if(d.equals("Sábado")){
                                return 6;
                            }else{
                                if(d.equals("Domingo")){
                                    return 7;
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }
}
