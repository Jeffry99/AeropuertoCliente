/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.service.AutenticacionService;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class LoginController {

    @FXML
    private Button btnIngresar;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    
    private AutenticacionService autenticacionService = new AutenticacionService();
    
    @FXML
    private void actIngresar(ActionEvent event) {
        txtUsuario.setText("lujepa2");
        txtContrasena.setText("Una2020");
        if(validar()){
            Respuesta respuesta = autenticacionService.Login(txtUsuario.getText(), txtContrasena.getText());
            if(respuesta.getEstado().equals(true)){
                Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Inicio de sesion", respuesta.getMensaje());
                try{
                    Parent root = FXMLLoader.load(App.class.getResource("MenuPrincipal" + ".fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    Stage stage2 = (Stage) btnIngresar.getScene().getWindow();
                    stage2.close(); 
                }catch(IOException ex){
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
                }
                
            }else{
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Inicio de sesión", respuesta.getMensaje());
            }
        }
    
    }
    
    public boolean validar(){
        if(txtUsuario.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite su cedula");
            return false;
        }
        if(txtContrasena.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite su contraseña");
            return false;
        }
        return true;
    }
    
}
