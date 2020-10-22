/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aerolinea.cliente.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class MenuPrincipalController implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane apContenedor;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void actEmpleados(ActionEvent event) {
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
    
}
