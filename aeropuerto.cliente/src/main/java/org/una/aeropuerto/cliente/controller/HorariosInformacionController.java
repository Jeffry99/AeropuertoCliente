/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;


import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.json.bind.annotation.JsonbDateFormat;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.HorarioDTO;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.HorarioService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
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
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date horaInicioGuardar;
    
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date horaFinalGuardar;
    
    /**
     * Initializes the controller class.
     */
    private String modalidad="";
    private HorarioDTO horarioEnCuestion = new HorarioDTO();
    
    
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    @FXML
    private Label horaInicio;
    @FXML
    private Label horaDeFin;
    @FXML
    private ImageView imgHoraInicio;
    @FXML
    private ImageView imgHoraFinal;
    
    
    int diaInicio=1;
    int diaFinal=1;
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
        initDias();
        cbxDiaInicio.setValue("Lunes");
        cbxDiaFinal.setValue("Lunes");
        
        initEmpleados();
        modalidad= (String) AppContext.getInstance().get("ModalidadHorario");
        
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            horarioEnCuestion=(HorarioDTO) AppContext.getInstance().get("HorarioEnCuestion");
            
            diaInicio=horarioEnCuestion.getDiaInicio();
            diaFinal=horarioEnCuestion.getDiaFinal();
                    
            cbxDiaInicio.setValue(getDiaString(diaInicio));
            cbxDiaFinal.setValue(getDiaString(diaFinal));
            String horaDeInicio = formatter.format(horarioEnCuestion.getHoraInicio());
            horaInicio.setText(horaDeInicio+getTipo(horaDeInicio));
            String horafin = formatter.format(horarioEnCuestion.getHoraFinal());
            horaDeFin.setText(horafin+getTipo(horafin));
            
            horaInicioGuardar=horarioEnCuestion.getHoraInicio();
            horaFinalGuardar=horarioEnCuestion.getHoraFinal();
            
            cbxEmpleado.setValue(horarioEnCuestion.getEmpleado());
            
            if(horarioEnCuestion.getEstado()==true){
                estado = true;
                txtEstado.setText("Activo");
                btnCambiarEstado.setText("Anular");
            }else{
                estado = false;
                txtEstado.setText("Inactivo");
                btnCambiarEstado.setText("Activar");
            }
            cbxEmpleado.setDisable(true);
            
            if(modalidad.equals("Ver")){
                cbxDiaInicio.setDisable(true);
                cbxDiaFinal.setDisable(true);
                btnGuardar.setVisible(false);
                btnGuardar.setDisable(true);
                imgHoraInicio.setVisible(false);
                imgHoraInicio.setDisable(true);
                imgHoraFinal.setVisible(false);
                imgHoraFinal.setDisable(true);
                btnCambiarEstado.setDisable(true);
                btnCambiarEstado.setVisible(false);
            }
            
        }else{
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
        }
        
    }    
    
    public String getTipo(String h){
        String ch[] = h.split(":");
        int hora = Integer.valueOf(ch[0]);
        if(hora>11){
            return " PM";
        }else{
            return " AM";
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
    

    public boolean validar(){
        if(horaInicio.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la hora de inicio de su jornada");
            return false;
        }
        if(horaDeFin.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la hora de inicio de su jornada");
            return false;
        }
        if(cbxEmpleado.getValue()==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el empleado al que le desea registrar un horario");
            return false;
        }
        
        return true;
    }
    
    private HorarioService horarioService = new HorarioService();
    
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            horarioEnCuestion.setEmpleado(cbxEmpleado.getValue());
            horarioEnCuestion.setDiaInicio(diaInicio);
            horarioEnCuestion.setDiaFinal(diaFinal);
            horarioEnCuestion.setHoraInicio(horaInicioGuardar);
            horarioEnCuestion.setHoraFinal(horaFinalGuardar);
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=horarioService.modificar(horarioEnCuestion.getId(), horarioEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica horario con id "+horarioEnCuestion.getId(), "HorariosInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de horario", "Se ha modificado el horario correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de horario", respuesta.getMensaje());
                }
            }else{
                if(modalidad.equals("Agregar")){
                    horarioEnCuestion.setEstado(true);
                    Respuesta respuesta=horarioService.crear(horarioEnCuestion);
                    if(respuesta.getEstado()){
                        horarioEnCuestion=(HorarioDTO) respuesta.getResultado("Horario");
                        GenerarTransacciones.crearTransaccion("Se crea horario con id "+horarioEnCuestion.getId(), "HorariosInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de horario", "Se ha registrado el horario correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de horario", respuesta.getMensaje());
                    }
                }
            }
            
        }
    }
    
    public void volver(){
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Horarios" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
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

    @FXML
    private void actSelHoraInicio(MouseEvent event) {
        try{
            Stage stage = new Stage();
            AppContext.getInstance().set("ModalidadSeleccionDeHora", "Horarios");
            AppContext.getInstance().set("ModalidadSeleccionDeHoraHorarios", "Inicio");
            AppContext.getInstance().set("ControllerHorarios", this);
            Parent root = FXMLLoader.load(App.class.getResource("SeleccionDeHora" + ".fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Selección de hora de inicio");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actSelHoraFinal(MouseEvent event) {
        try{
            Stage stage = new Stage();
            AppContext.getInstance().set("ModalidadSeleccionDeHora", "Horarios");
            AppContext.getInstance().set("ModalidadSeleccionDeHoraHorarios", "Final");
            AppContext.getInstance().set("ControllerHorarios", this);
            Parent root = FXMLLoader.load(App.class.getResource("SeleccionDeHora" + ".fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Selección de hora de fin");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void setHora(Date date, String hora, String mod){
        if(mod.equals("Inicio")){
            horaInicio.setText(hora);
            horaInicioGuardar=date;
        }else{
            horaFinalGuardar=date;
            horaDeFin.setText(hora);
        }
        
    }

    @FXML
    private void actSelDiaInicio(ActionEvent event) {
        diaInicio=getDiaInt(cbxDiaInicio.getValue());
    }

    @FXML
    private void actSelDiaFinal(ActionEvent event) {
        diaFinal=getDiaInt(cbxDiaFinal.getValue());
    }

    @FXML
    private void actCambiarEstado(ActionEvent event) {
        String mensaje="";
        if(estado){
            horarioEnCuestion.setEstado(false);
            mensaje="Se anula el horario con id "+horarioEnCuestion.getId();
        }else{
            horarioEnCuestion.setEstado(true);
            mensaje="Se activa el horario con id "+horarioEnCuestion.getId();
        }
        Respuesta respuesta=horarioService.modificar(horarioEnCuestion.getId(), horarioEnCuestion);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "HorariosInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado del horario", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del horario", respuesta.getMensaje());
        }
    }
}
