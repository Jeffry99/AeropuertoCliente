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
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class EmpleadosInformacionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    private Label lblCedula;
    @FXML
    private Label lblJefe;
    @FXML
    private TextField txtCedula;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    @FXML
    private RadioButton rbSi;
    @FXML
    private Label lblNombre;
    @FXML
    private TextField txtNombre;
    @FXML
    private Label lblTelefono;
    @FXML
    private TextField txtTelefono;
    @FXML
    private Label lblDireccion;
    @FXML
    private TextField txtDireccion;
    @FXML
    private Label lblEstado;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private RadioButton rbNo;
    @FXML
    private Label lblFechaCreacion1;
    @FXML
    private Label lblFechaModificacion1;
    @FXML
    private ComboBox<EmpleadoDTO> cbxJefeDirecto;
    
    private String modalidad="";
    
    private EmpleadoDTO empleadoEnCuestion = new EmpleadoDTO();
    
    private EmpleadoService empleadoService = new EmpleadoService();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta = empleadoService.getByEstado(true);
        if(respuesta.getEstado()){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
            ObservableList items = FXCollections.observableArrayList(empleados);
            cbxJefeDirecto.setItems(items);
        }
          
        
        modalidad = (String) AppContext.getInstance().get("ModalidadEmpleado");
        btnGuardar.setVisible(false);
        btnGuardar.setDisable(true);
        
        lblFechaCreacion1.setVisible(false);
        lblFechaModificacion1.setVisible(false);
        
        cbxJefeDirecto.setVisible(false);
        cbxJefeDirecto.setDisable(true);
        
        if(!modalidad.equals("Ver")){
            btnGuardar.setVisible(true);
            btnGuardar.setDisable(false);
        }
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            empleadoEnCuestion = (EmpleadoDTO) AppContext.getInstance().get("EmpleadoEnCuestion");
            txtNombre.setText(empleadoEnCuestion.getNombre());
            txtCedula.setText(empleadoEnCuestion.getCedula());
            txtTelefono.setText(empleadoEnCuestion.getTelefono());
            txtDireccion.setText(empleadoEnCuestion.getDireccion());  
            if(empleadoEnCuestion.getJefe()!= null){
                rbSi.setSelected(false);
                rbNo.setSelected(true);
                esJefe=false;
                cbxJefeDirecto.setValue(empleadoEnCuestion.getJefe());
                cbxJefeDirecto.setVisible(true);
                cbxJefeDirecto.setDisable(false);
            }else{
                esJefe=true;
                rbSi.setSelected(true);
                rbNo.setSelected(false);
                
            }
            if(empleadoEnCuestion.getEstado()){
                estado=true;
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            lblFechaCreacion1.setText("Creado el "+empleadoEnCuestion.getFechaRegistro());
            lblFechaModificacion1.setText("Modificado el "+empleadoEnCuestion.getFechaModificacion());
            lblFechaCreacion1.setVisible(true);
            lblFechaModificacion1.setVisible(true);
            
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa empleado con id "+empleadoEnCuestion.getId(), "EmpleadosInformacion");
                txtCedula.setDisable(true);
                txtNombre.setDisable(true);
                txtTelefono.setDisable(true);
                txtDireccion.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
                rbSi.setDisable(true);
                rbNo.setDisable(true);
                cbxJefeDirecto.setDisable(true);
            }
        }
        
        
        
        

    }
    
    public boolean validar(){
        if(txtCedula.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la cedula del empleado");
            return false;
        }
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del cliente");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del empleado");
            return false;
        }
        if(esJefe==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione si el empleado es jefe o no");
            return false;
        }else{
            if(!esJefe){
                if(cbxJefeDirecto.getValue()==null){
                    Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el jefe directo del empleado");
                    return false; 
                }
            } 
        }
        return true;
    }
    
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            
            empleadoEnCuestion.setNombre(txtNombre.getText());
            empleadoEnCuestion.setCedula(txtCedula.getText());
            empleadoEnCuestion.setDireccion(txtDireccion.getText());
            empleadoEnCuestion.setTelefono(txtTelefono.getText());
            empleadoEnCuestion.setEstado(estado);
            if(!esJefe){
                empleadoEnCuestion.setJefe(cbxJefeDirecto.getValue());
            }else{
                empleadoEnCuestion.setJefe(null);
            }
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=empleadoService.modificar(empleadoEnCuestion.getId(), empleadoEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica empleado con id "+empleadoEnCuestion.getId(), "EmpleadosInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de empleado", "Se ha modificado el empleado correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de empleado", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=empleadoService.crear(empleadoEnCuestion);
                    if(respuesta.getEstado()){
                        empleadoEnCuestion = (EmpleadoDTO) respuesta.getResultado("Empleado");
                        GenerarTransacciones.crearTransaccion("Se crea empleado con id "+empleadoEnCuestion.getId(), "EmpleadosInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de empleado", "Se ha registrado el empleado correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de empleado", respuesta.getMensaje());
                    }
                }
            }
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }

    private Boolean esJefe;
    
    @FXML
    private void actEsJefe(ActionEvent event) {
        esJefe = true;
        rbSi.setSelected(true);
        rbNo.setSelected(false);
        
        cbxJefeDirecto.setVisible(false);
        cbxJefeDirecto.setDisable(true);
    }

    @FXML
    private void actNoEsJefe(ActionEvent event) {
        esJefe = false;
        rbSi.setSelected(false);
        rbNo.setSelected(true);
        cbxJefeDirecto.setVisible(true);
        cbxJefeDirecto.setDisable(false);
    }

    private Boolean estado;
    
    @FXML
    private void actEstadoActivo(ActionEvent event) {
        estado = true;
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
    }

    @FXML
    private void actEstadoInactivo(ActionEvent event) {
        estado = false;
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
    }
    
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Empleados" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
}
