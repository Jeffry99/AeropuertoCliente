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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.una.aeropuerto.cliente.service.ParametroAplicacionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class SolicitudPermisoController implements Initializable {

    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Label lbTitulo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnAceptar;

    /**
     * Initializes the controller class.
     */
    String permiso="";
    private ParametroAplicacionService parametroService = new ParametroAplicacionService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        permiso=(String) AppContext.getInstance().get("ModalidadSolicitudPermiso");
        if(permiso.equals("ContraseñaAdministrador")){
            lbTitulo.setText("Digite la contraseña secreta de administración");
        }else if(permiso.equals("ContraseñaGerente")){
            lbTitulo.setText("Digite la contraseña secreta de gerencia");
        }
        
        // TODO
    }    

    @FXML
    private void actCancelar(ActionEvent event) {
        salir();
    }

    @FXML
    private void actAceptar(ActionEvent event) {
        if(txtContrasena.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Solicitud de permiso", lbTitulo.getText()+" por favor.");
        }else{
            Respuesta res = parametroService.getByNombreAndValor(permiso, txtContrasena.getText());
            AppContext.getInstance().set("Permiso", res.getEstado());
            if(res.getEstado()){
                Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Solicitud de permiso", "Autorización concedida");
                salir();
            }else{
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Solicitud de permiso", "Autorización no concedida, contraseña incorrecta");
            }
            
        }
    }
    
    public void salir(){
        Stage stage = (Stage) lbTitulo.getScene().getWindow();
        stage.close();
    }
}
