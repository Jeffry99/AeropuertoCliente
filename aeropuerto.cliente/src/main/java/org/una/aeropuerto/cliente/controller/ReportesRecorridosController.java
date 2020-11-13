/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.dto.RutaDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.service.RutaService;
import org.una.aeropuerto.cliente.util.GeneradorReportes;
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
    private ComboBox<Object> cbox;
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
    private RadioButton btnAerolineas;
    private RadioButton btnZonas;
    @FXML
    private CheckBox cbxAerolinea;
    @FXML
    private CheckBox cbxZona;
    
    private GeneradorReportes generadorReportes;
    private String tipoFiltro;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        cbox.setVisible(false);
    }    
    
    private void cargarAerolineasCbx(){
        AerolineaService AerolineaService = new AerolineaService();
        ArrayList<AerolineaDTO> aerolineas = new ArrayList<AerolineaDTO>();
        Respuesta respuesta = AerolineaService.getByEstado(true);
        if(respuesta.getEstado()){
            aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
            ObservableList items = FXCollections.observableArrayList(aerolineas);
            cbox.setItems(items);
        }
    }
    
    private void cargarZonasCbx(){
        RutaService rutaService = new RutaService();
        ArrayList<RutaDTO> rutas = new ArrayList<RutaDTO>();
        Respuesta respuesta = rutaService.getByEstado(true);
        if(respuesta.getEstado()){
            rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Rutas");
            ObservableList items = FXCollections.observableArrayList(rutas);
            cbox.setItems(items);
        }
    }

    @FXML
    private void actGenerar(ActionEvent event) throws JRException, IOException {
        if(verificarCampos()){
            generadorReportes.generarReporte();
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }

    private void actBtnAerolineas(ActionEvent event) {
       if(btnZonas.isSelected()){
           btnZonas.setSelected(false);
       }
       cargarAerolineasCbx();
    }

    private void actBtnZonas(ActionEvent event) {
        if(btnAerolineas.isSelected()){
            btnAerolineas.setSelected(false);
        }
        cargarZonasCbx();
    }
    
    private boolean verificarCampos(){
        if(cbox.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la aerolínea o zona");
            return false;
        }
        if(dpInicio.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la fecha de inicio");
            return false;
        }
        if(dpFinal.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la fecha de finalización");
            return false;
        }
        generadorReportes = new GeneradorReportes("Recorrido", tipoFiltro, cbox.getValue(), Date.valueOf(dpInicio.getValue()), Date.valueOf(dpFinal.getValue()));
        return true;
    }
    @FXML
    private void actAerolinea(ActionEvent event) {
        cbxZona.setSelected(false);
        cbox.getItems().clear();
        cbox.setVisible(true);
        labelTipoBusqueda.setText("Aerolínea:");
        cbox.setPromptText("Seleccionar Aerolínea");
        tipoFiltro = "Aerolinea";
        cargarAerolineasCbx();
    }

    @FXML
    private void actZona(ActionEvent event) {
        cbxAerolinea.setSelected(false);
        cbox.getItems().clear();
        cbox.setVisible(true);
        labelTipoBusqueda.setText("Zona:");
        cbox.setPromptText("Seleccionar Zona");
        tipoFiltro = "Zona";
        cargarZonasCbx();
    }
    
}
