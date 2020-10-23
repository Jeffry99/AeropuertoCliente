/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class UsuariosContrasenaController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private PasswordField txtContrasenaActual;
    @FXML
    private PasswordField txtContrasenaNueva;
    @FXML
    private PasswordField txtContrasenaConfirmar;

    /**
     * Initializes the controller class.
     */
    
    private String modalidad;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modalidad=(String) AppContext.getInstance().get("ModalidadContrasena");
        if(modalidad.equals("Agregar")){
            txtContrasenaActual.setDisable(true);
            txtContrasenaActual.setVisible(false);
        }else{
            txtContrasenaActual.setDisable(false);
            txtContrasenaActual.setVisible(true);
        }
        // TODO
    }    

    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            if(txtContrasenaNueva.getText().equals(txtContrasenaConfirmar.getText())){
                UsuariosInformacionController usuController = (UsuariosInformacionController) AppContext.getInstance().get("controllerUsuarios");
                usuController.setContrasena(txtContrasenaNueva.getText());
            }else{
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Guardar contraseña", "Las contraseñas no coinciden");  
            }
        }
    }
    
    public boolean validar(){
        if(modalidad.equals("Modificar")){
            if(txtContrasenaActual.getText().isBlank()){
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la contraseña actual del usuario");
                return false;
            }
        }
        if(txtContrasenaNueva.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la contraseña nueva del usuario");
            return false;
        }
        if(txtContrasenaConfirmar.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor confirme la contraseña actual del usuario");
            return false;
        }
        
        return true;
    }

    
}
