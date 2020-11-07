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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.service.RolService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class RolesInformacionController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private Button btnVolver;

    /**
     * Initializes the controller class.
     */
    private String modalidad;
    private RolService rolService = new RolService();
    private RolDTO rol = new RolDTO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modalidad = (String) AppContext.getInstance().get("ModalidadRol");
        
        btnGuardar.setVisible(false);
        btnGuardar.setDisable(true);
        
        if(!modalidad.equals("Ver")){
            btnGuardar.setVisible(true);
            btnGuardar.setDisable(false);
        }
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            rol = (RolDTO) AppContext.getInstance().get("RolEnCuestion");
            txtDescripcion.setText(rol.getDescripcion());
            txtNombre.setText(rol.getNombre());
            if(rol.getEstado()==true){
                estado=true;
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa rol con id "+rol.getId(), "RolesInformacion");
                txtDescripcion.setDisable(true);
                txtNombre.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
            }      
        }
    }    

    
    
    

    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            rol.setNombre(txtNombre.getText());
            rol.setDescripcion(txtDescripcion.getText());
            rol.setEstado(estado);
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=rolService.modificar(rol.getId(), rol);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica rol con id "+rol.getId(), "RolesInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de rol", "Se ha modificado el rol correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de rol", respuesta.getMensaje());
                }
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=rolService.crear(rol);
                    if(respuesta.getEstado()){
                        rol=(RolDTO) respuesta.getResultado("Rol");
                        GenerarTransacciones.crearTransaccion("Se crea rol con id "+rol.getId(), "RolesInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de rol", "Se ha registrado el rol correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de rol", respuesta.getMensaje());
                    }
                }
            }
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }
    
    public boolean validar(){
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del rol");
            return false;
        }
        if(txtDescripcion.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la descripcion del rol");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del rol");
            return false;
        }
        
        return true;
    }

    private Boolean estado;
    
    @FXML
    private void actActivo(ActionEvent event) {
        estado = true;
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
    }

    @FXML
    private void actInactivo(ActionEvent event) {
        estado = false;
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
    }

    
    public void volver(){
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Roles" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
}
