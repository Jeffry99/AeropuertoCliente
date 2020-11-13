/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class ReportesRecorridosController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ComboBox<ObservableList> cbox;
    @FXML
    private Label labelTipoBusqueda;
    @FXML
    private Button btnGenerar;
    @FXML
    private Button btnVolver;
    @FXML
    private Label txtEstado;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFinal;
    @FXML
    private RadioButton btnAerolineas;
    @FXML
    private RadioButton btnZonas;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
    }    
    
    private void cargarAerolineasCbx(){
        AerolineaService AerolineaService = new AerolineaService();
        ArrayList<AerolineaDTO> aerolineas = new ArrayList<AerolineaDTO>();
        Respuesta respuesta = AerolineaService.getByEstado(true);
        if(respuesta.getEstado()){
            aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
            ObservableList items = FXCollections.observableArrayList(aerolineas);
            cbox.setItems(items);
            System.out.println("Combo cargado con aerolineas");
        }
    }
    
    private void cargarZonasCbx(){
        
    }

    @FXML
    private void actGenerar(ActionEvent event) {
        verificarCampos();
        
        
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actBtnAerolineas(ActionEvent event) {
       if(btnZonas.isSelected()){
           btnZonas.setSelected(false);
       }
       cargarAerolineasCbx();
    }

    @FXML
    private void actBtnZonas(ActionEvent event) {
        if(btnAerolineas.isSelected()){
            btnAerolineas.setSelected(false);
        }
        cargarZonasCbx();
    }
    
    private boolean verificarCampos(){
        if(cbox.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione "
                    + "el tipo de busqueda que desea realizar");
            return false;
        }
        
        return true;
    }
    
}
