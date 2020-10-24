/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AreaTrabajoDTO;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.TrabajoEmpleadoDTO;
import org.una.aeropuerto.cliente.service.AreaTrabajoService;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.TrabajoEmpleadoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class TrabajosEmpleadosInformacionController implements Initializable {

    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private ComboBox<AreaTrabajoDTO> cbxAreaTrabajo;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelCedula;

    
    private String modalidad="";
       
    private TrabajoEmpleadoService trabajoEmpleadoService = new TrabajoEmpleadoService();
    
    private TrabajoEmpleadoDTO trabajoEmpleadoEnCuestion = new TrabajoEmpleadoDTO();
    private EmpleadoDTO empleadoEnCuestion = new EmpleadoDTO();
    
    AreaTrabajoService areaTrabajoService = new AreaTrabajoService();
    EmpleadoService empleadosService = new EmpleadoService();
    
    private TrabajoEmpleadoDTO trabajoEmpleado = new TrabajoEmpleadoDTO();
    private EmpleadoDTO empleado = new EmpleadoDTO();
    
    
    @FXML
    private ComboBox<EmpleadoDTO> cbxEmpleado;
    @FXML
    private Rectangle cuadro;
    @FXML
    private Label labelNom;
    @FXML
    private Line barra;
    @FXML
    private Label labelCed;
    @FXML
    private Label labelEmp;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    iniAreasTrabajos();
    iniEmpleados();
    
    modalidad = (String) AppContext.getInstance().get("ModalidadTrabajoEmpleado");
    btnGuardar.setVisible(false);
    btnGuardar.setDisable(true);  
    cbxAreaTrabajo.setValue(trabajoEmpleadoEnCuestion.getAreaTrabajo());
    cbxEmpleado.setValue(trabajoEmpleadoEnCuestion.getEmpleado());
    
    
    if(!modalidad.equals("Ver")){
        btnGuardar.setVisible(true);
        btnGuardar.setDisable(false);
    }   
      
    if(modalidad.equals("Ver")||modalidad.equals("Modificar")){

        trabajoEmpleadoEnCuestion = (TrabajoEmpleadoDTO) AppContext.getInstance().get("TrabajoEmpleadoEnCuestion");
       
        labelNombre.setText(trabajoEmpleadoEnCuestion.getEmpleado().getNombre());
        labelCedula.setText(trabajoEmpleadoEnCuestion.getEmpleado().getCedula());
        cbxAreaTrabajo.setValue(trabajoEmpleadoEnCuestion.getAreaTrabajo());
        cbxEmpleado.setValue(trabajoEmpleadoEnCuestion.getEmpleado());

        if(trabajoEmpleadoEnCuestion.getEstado()){
            estado=true;
            rbActivo.setSelected(true);
            rbInactivo.setSelected(false);
        }else{
            estado=false;
            rbActivo.setSelected(false);
            rbInactivo.setSelected(true);
        }
    }
            
    if(modalidad.equals("Ver")){
        GenerarTransacciones.crearTransaccion("Se observa trabajo empleado con id "+trabajoEmpleadoEnCuestion.getId(), "TrabajosEmpleadosInformacion");
        cbxAreaTrabajo.setDisable(true);
        rbActivo.setDisable(true);
        rbInactivo.setDisable(true);
        cbxEmpleado.setDisable(true);
        
    }
    if(modalidad.equals("Modificar")){
          cbxEmpleado.setDisable(true);
    }
    if(modalidad.equals("Agregar")){
          cbxEmpleado.setDisable(false);
    }
}

   
    public boolean validar(){
        if(cbxAreaTrabajo.getItems().isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el aerea de trabajo");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del area de trabajo");
            return false;
        }
        return true;
    }
    @FXML
    private void actCancelar(ActionEvent event) {
        volver();
    }

    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("TrabajosEmpleados" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actGuardar(ActionEvent event) {
       if(validar()){
            trabajoEmpleadoEnCuestion.setAreaTrabajo(cbxAreaTrabajo.getValue());
            trabajoEmpleadoEnCuestion.setEstado(estado);
            trabajoEmpleadoEnCuestion.setEmpleado(cbxEmpleado.getValue());
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=trabajoEmpleadoService.modificar(trabajoEmpleadoEnCuestion.getId(), trabajoEmpleadoEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica la a con id "+trabajoEmpleadoEnCuestion.getId(), "TrabajoEmpleadoInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación del area de trabajo de un empleado", "Se ha modificado el area de trabajo correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación del area de trabajo", respuesta.getMensaje());
                } 
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=trabajoEmpleadoService.crear(trabajoEmpleadoEnCuestion);
                    if(respuesta.getEstado()){
                        trabajoEmpleadoEnCuestion=(TrabajoEmpleadoDTO) respuesta.getResultado("TrabajoEmpleado");
                        GenerarTransacciones.crearTransaccion("Se asigna una area de trabajo a un empleado del id "+trabajoEmpleadoEnCuestion.getId(), "TrabajoEmpleadoInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro una area de trabajo a un empleado", "Se ha registrado una area de trabajo a un empleado correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro una area de trabajo a un empleado", respuesta.getMensaje());
                    }
                }
            }
        }
    }
    
   
    private Boolean estado;
    @FXML
    private void actSelActivo(ActionEvent event) {
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
        estado=true;
    }

    @FXML
    private void actSecInactivo(ActionEvent event) {
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
        estado=false;
    }
    
    private AreaTrabajoDTO areaTra;
    @FXML
    private void cbxActAreaTrabajo(ActionEvent event) {

    }

    public void iniAreasTrabajos(){
        ArrayList<AreaTrabajoDTO> areasTrabajos = new ArrayList<AreaTrabajoDTO>();
        Respuesta respuesta1 = areaTrabajoService.getAll();
        if(respuesta1.getEstado()==true){
            areasTrabajos = (ArrayList<AreaTrabajoDTO>) respuesta1.getResultado("AreasTrabajos");
        }
        ObservableList items3 = FXCollections.observableArrayList(areasTrabajos);   
        cbxAreaTrabajo.setItems(items3); 
    }
    
    public void iniEmpleados(){
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta1 = empleadosService.getAll();
        if(respuesta1.getEstado()==true){
            empleados = (ArrayList<EmpleadoDTO>) respuesta1.getResultado("Empleados");
        }
        ObservableList items3 = FXCollections.observableArrayList(empleados);   
        cbxEmpleado.setItems(items3); 
    }

    @FXML
    private void cbxActEmpleado(ActionEvent event) {
    }

}
