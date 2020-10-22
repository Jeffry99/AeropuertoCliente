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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class TransaccionesController implements Initializable {

    @FXML
    private Button btnVolver;
    @FXML
    private TableView<?> tvTransacciones;
    @FXML
    private ComboBox<?> cbTipoBusqueda;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ComboBox<?> cbBuscar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actBorrar(ActionEvent event) {
    }

    @FXML
    private void actBuscar(ActionEvent event) {
    }
    
}
