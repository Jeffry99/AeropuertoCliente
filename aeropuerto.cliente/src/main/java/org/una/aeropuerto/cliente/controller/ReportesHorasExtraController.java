/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.una.aeropuerto.cliente.dto.AreaTrabajoDTO;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.service.AreaTrabajoService;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.ReporteServicioRegistradoDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class ReportesHorasExtraController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label lblTipoBusqueda;
    @FXML
    private Button btnGenerar;
    @FXML
    private Button btnVolver;
    @FXML
    private Label horaInicio;
    @FXML
    private Label horaDeFin;
    @FXML
    private Label txtEstado;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFinal;
    @FXML
    private ComboBox<Object> combobox;
    @FXML
    private CheckBox cbxEmpleado;
    @FXML
    private CheckBox cbxArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        combobox.setVisible(false);
    }    

    @FXML
    private void actGenerar(ActionEvent event) {
        if(verificarCampos()){
            //generarReporte();
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actEmpleado(ActionEvent event) {
        cbxArea.setSelected(false);
        combobox.getItems().clear();
        combobox.setVisible(true);
        lblTipoBusqueda.setText("Empleado:");
        combobox.setPromptText("Seleccionar Empleado");
        initEmpleados();
    }

    @FXML
    private void actArea(ActionEvent event) {
        cbxEmpleado.setSelected(false);
        combobox.getItems().clear();
        combobox.setVisible(true);
        lblTipoBusqueda.setText("Área:");
        combobox.setPromptText("Seleccionar Área");
        initAreasTrabajo();
    }
    
    private void initEmpleados(){
        EmpleadoService empleadoService = new EmpleadoService();
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta = empleadoService.getByEstado(true);
        if(respuesta.getEstado()){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
            ObservableList items = FXCollections.observableArrayList(empleados);
            combobox.setItems(items);
        }
    }
    
    private void initAreasTrabajo(){
        AreaTrabajoService areaService = new AreaTrabajoService();
        ArrayList<AreaTrabajoDTO> areas = new ArrayList<AreaTrabajoDTO>();
        Respuesta respuesta = areaService.getByEstado(true);
        if(respuesta.getEstado()){
            areas = (ArrayList<AreaTrabajoDTO>) respuesta.getResultado("AreasTrabajos");
            ObservableList items = FXCollections.observableArrayList(areas);
            combobox.setItems(items);
        }
    }
    
    private boolean verificarCampos(){
        if(combobox.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el área de trabajo o empleado");
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
        return true;
    }
    
   
    
}
