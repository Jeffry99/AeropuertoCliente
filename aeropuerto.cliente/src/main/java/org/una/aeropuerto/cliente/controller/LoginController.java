/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.service.AutenticacionService;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 *
 * @author Jeffry
 */
public class LoginController implements Initializable{

    @FXML
    private Button btnIngresar;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    
    private AutenticacionService autenticacionService = new AutenticacionService();
    @FXML
    private TextField txtVerContrasena;
    @FXML
    private JFXCheckBox cbContrasena;
    
    private String contrasena = null;
    /**
     * Initializes the controller class.
     */
    boolean contrasenaVisible = false;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUsuario.requestFocus();
        txtUsuario.setText("lujepa2");
        txtContrasena.setText("Una2020");
        
        cbContrasena.selectedProperty().addListener( t -> {
            if(cbContrasena.isSelected()){
                txtVerContrasena.setVisible(true);
                txtContrasena.setVisible(false);
                txtVerContrasena.setText(txtContrasena.getText());
                contrasenaVisible = true;
            }else{
                txtVerContrasena.setVisible(false);
                txtContrasena.setText(txtVerContrasena.getText());
                txtContrasena.setVisible(true);
                txtVerContrasena.setText("");
                contrasenaVisible = false;
            }
        });
    }   
    @FXML
    private void actIngresar(ActionEvent event) {
        ingresar();
    }
    
    public boolean validar(){
        if(contrasenaVisible){
            contrasena = txtVerContrasena.getText();
        }else{
            contrasena = txtContrasena.getText();
        }
        if(txtUsuario.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite su cedula");
            return false;
        }
        if(contrasena==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite su contraseña");
            return false;
        }
        return true;
    }

    @FXML
    private void actKey(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            txtContrasena.requestFocus();
        }
    }

    @FXML
    private void actKey2(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            ingresar();
        }
    }
    
    public void ingresar(){
        
        if(validar()){
            
            
            Respuesta respuesta = autenticacionService.Login(txtUsuario.getText(), contrasena);
            if(respuesta.getEstado().equals(true)){
                Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Inicio de sesion", respuesta.getMensaje());
                try{
                    Parent root = FXMLLoader.load(App.class.getResource("MenuPrincipal" + ".fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(Boolean.FALSE);
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
    
}
