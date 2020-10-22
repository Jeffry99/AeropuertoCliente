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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class EmpleadosInformacionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    final ToggleGroup groupEstado = new ToggleGroup();
    final ToggleGroup groupJefe = new ToggleGroup();
    
    private TableView<?> tvSubempleados;
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
    private Label lblSubempleados;
    @FXML
    private RadioButton rbNo;
    @FXML
    private Label lblFechaCreacion1;
    private Label lblFechaCreacion2;
    @FXML
    private Label lblFechaModificacion1;
    private Label lblFechaModificacion2;
    @FXML
    private ComboBox<?> cbxJefeDirecto;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        rbActivo.setToggleGroup(groupEstado);
        rbInactivo.setToggleGroup(groupEstado);
        rbSi.setToggleGroup(groupEstado);
        rbNo.setToggleGroup(groupEstado);
        
    }    
    public void vistaJefe(int posicion){
        
        //Falta acomodar bien las posiciones
        
        tvSubempleados.setVisible(true);
        lblSubempleados.setVisible(true);
        
        lblCedula.setLayoutX(posicion);
        lblDireccion.setLayoutX(posicion);
        lblEstado.setLayoutX(posicion);
        lblJefe.setLayoutX(posicion);
        lblNombre.setLayoutX(posicion);
        lblTelefono.setLayoutX(posicion);
        lblFechaCreacion1.setLayoutX(posicion);
        lblFechaModificacion1.setLayoutX(posicion);
        
        lblFechaCreacion2.setLayoutX(posicion+100);
        lblFechaModificacion2.setLayoutX(posicion+100);
        txtCedula.setLayoutX(posicion+150);
        txtDireccion.setLayoutX(posicion+150);
        rbActivo.setLayoutX(posicion+150);
        rbInactivo.setLayoutX(posicion+300);
        txtNombre.setLayoutX(posicion+150);
        txtTelefono.setLayoutX(posicion+150);

    }
    @FXML
    private void actGuardar(ActionEvent event) {
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actEsJefe(ActionEvent event) {
        vistaJefe(100);
    }

    @FXML
    private void actNoEsJefe(ActionEvent event) {
        vistaJefe(270);
        
       
    }

    @FXML
    private void actEstadoActivo(ActionEvent event) {
    }

    @FXML
    private void actEstadoInactivo(ActionEvent event) {
    }
    
}
