/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.TrabajoEmpleadoDTO;
import org.una.aeropuerto.cliente.service.TrabajoEmpleadoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class TrabajosEmpleadosInformacionController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private Label lblIdNumero;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;

    private TrabajoEmpleadoService trabajoEmpleadoService = new TrabajoEmpleadoService();
    private TrabajoEmpleadoDTO trabajoEmpleadoEnCuestion = new TrabajoEmpleadoDTO();
    private String modalidad="";
    private Boolean estado;
    @FXML
    private ComboBox<TrabajoEmpleadoDTO> cbxAreaTrabajo;
    @FXML
    private ComboBox<TrabajoEmpleadoDTO> cbxEmpleado;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    modalidad = (String) AppContext.getInstance().get("ModalidadTrabajoEmpleado");
    btnGuardar.setVisible(false);
    btnGuardar.setDisable(true);  
        
    if(!modalidad.equals("Ver")){
        btnGuardar.setVisible(true);
        btnGuardar.setDisable(false);
    }   
      
     if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            trabajoEmpleadoEnCuestion = (TrabajoEmpleadoDTO) AppContext.getInstance().get("AerolineaEnCuestion");
           // cbxAreaTrabajo.setItems(trabajoEmpleadoEnCuestion.getAreaTrabajo());
            //cbxEmpleado.setItems(trabajoEmpleadoEnCuestion.getEmpleado());
            if(trabajoEmpleadoEnCuestion.getEstado()){
                estado=true;
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa trabajo empleado con id "+trabajoEmpleadoEnCuestion.getId(), "TrabajosEmpleadosInformacion");
                cbxAreaTrabajo.setDisable(true);
                cbxEmpleado.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
            }
        }
    }  
 
    public boolean validar(){
        if(cbxAreaTrabajo.getItems().isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el aerea de trabajo");
            return false;
        }
        if(cbxEmpleado.getItems().isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el empleado");
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
   
    
}
