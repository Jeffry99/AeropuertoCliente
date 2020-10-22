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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class MenuPrincipalController implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem btnEmpleados;
    @FXML
    private MenuItem btnUsuarios;
    @FXML
    private MenuItem btnRoles;
    @FXML
    private MenuItem btnHorarios;
    @FXML
    private MenuItem btnHoraMarcaje;
    @FXML
    private MenuItem btnTransacciones;
    @FXML
    private StackPane Contenedor;
    @FXML
    private Menu TituloUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppContext.getInstance().set("Contenedor", this.Contenedor);
        
        
        
            
            TituloUsuario.setText(UsuarioAutenticado.getInstance().getUsuarioLogeado().getEmpleado().getNombre());
        
    }    

    @FXML
    private void actEmpleados(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getClassLoader().getResource("Empleados" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
        
    }

    @FXML
    private void actUsuarios(ActionEvent event) {
    }

    @FXML
    private void actRoles(ActionEvent event) {
    }

    @FXML
    private void actHorarios(ActionEvent event) {
    }

    @FXML
    private void actHoraMarcaje(ActionEvent event) {
    }

    @FXML
    private void actTransacciones(ActionEvent event) {
    }

    @FXML
    private void actVerInformacion(ActionEvent event) {
    }

    @FXML
    private void actCerrarSesion(ActionEvent event) {
    }
    
}
