/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AreaTrabajoDTO;
import org.una.aeropuerto.cliente.dto.ServicioTipoDTO;
import org.una.aeropuerto.cliente.service.AreaTrabajoService;
import org.una.aeropuerto.cliente.service.ServicioTipoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class ServiciosTiposInformacionController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private Label labelID;
    @FXML
    private TextField txtNombre;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private Label labelFechaRegistro;
    @FXML
    private Label labelFechaModificacion;
    @FXML
    private JFXComboBox<AreaTrabajoDTO> cbxAreaTrabajo;
    @FXML
    private Label fechaMod;
    private String modalidad="";
    
    private ServicioTipoDTO ServicioTipoEnCuestion = new ServicioTipoDTO();
   
    ServicioTipoService servicioTipoService = new ServicioTipoService();
    AreaTrabajoService areaTrabajoService = new AreaTrabajoService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniAreasTrabajos();
        
        modalidad = (String) AppContext.getInstance().get("ModalidadServicioTipo");
        btnGuardar.setVisible(false);
        btnGuardar.setDisable(true);  
        labelID.setText(String.valueOf(ServicioTipoEnCuestion.getId()));
        txtNombre.setText(ServicioTipoEnCuestion.getNombre());
        txtDescripcion.setText(ServicioTipoEnCuestion.getDescripcion());
        cbxAreaTrabajo.setValue(ServicioTipoEnCuestion.getAreaTrabajo());
//        labelFechaRegistro.setText(ServicioTipoEnCuestion.getFechaRegistro().toString());
//        labelFechaModificacion.setText(ServicioTipoEnCuestion.getFechaModificacion().toString());
        
        if(!modalidad.equals("Ver")){
            btnGuardar.setVisible(true);
            btnGuardar.setDisable(false);
        }
        
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            ServicioTipoEnCuestion = (ServicioTipoDTO) AppContext.getInstance().get("ServicioTipoEnCuestion");     
            cbxAreaTrabajo.setDisable(true);
            if(ServicioTipoEnCuestion.getEstado()){
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
                GenerarTransacciones.crearTransaccion("Se observa un tipo de servicio con id "+ServicioTipoEnCuestion.getId(), "ServicioTiposInformacion");
                cbxAreaTrabajo.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
            }
         
    }  
    
    public boolean validar(){
        if(cbxAreaTrabajo.getItems().isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el aerea de trabajo");
            return false;
        }
        if(txtNombre.getText() == ""){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del tipo de servicio");
            return false;
        }
        if(txtDescripcion.getText() == ""){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la descripción swl tipo de servicio");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del tipo de servicio");
            return false;
        }
        return true;
    }
    
    private Boolean estado;
    @FXML
    private void actEstadoActivo(ActionEvent event) {
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
        estado=true;
    }

    @FXML
    private void actEstadoInactivo(ActionEvent event) {
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
        estado=false;
    }

    @FXML
    private void actCancelar(ActionEvent event) {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("ServiciosTipos" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actGuardar(ActionEvent event) {
         if(validar()){
            ServicioTipoEnCuestion.setNombre(txtNombre.getText());
            ServicioTipoEnCuestion.setDescripcion(txtDescripcion.getText());
            ServicioTipoEnCuestion.setAreaTrabajo(cbxAreaTrabajo.getValue());
            ServicioTipoEnCuestion.setEstado(estado);
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=servicioTipoService.modificar(ServicioTipoEnCuestion.getId(), ServicioTipoEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica el tipo de servicio con id "+ServicioTipoEnCuestion.getId(), "ServicioTipoInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación del tipo de servicio", "Se ha modificado el tipo de servicio correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación tipo de servicio", respuesta.getMensaje());
                } 
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=servicioTipoService.crear(ServicioTipoEnCuestion);
                    if(respuesta.getEstado()){
                        ServicioTipoEnCuestion=(ServicioTipoDTO) respuesta.getResultado("ServicioTipo");
                        GenerarTransacciones.crearTransaccion("Se asigna un nuevo tipo servicio del id "+ServicioTipoEnCuestion.getId(), "ServicioTipoInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro una area de trabajo a un empleado", "Se ha registrado un nuevo tipo de servicio correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro un nuevo tipo de servicio", respuesta.getMensaje());
                    }
                }
            }
        }
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
    
     public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("ServiciosTipos" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
}
