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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.dto.ServicioTipoDTO;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;
import org.una.aeropuerto.cliente.service.ServicioTipoService;
import org.una.aeropuerto.cliente.util.Busqueda;
import org.una.aeropuerto.cliente.util.GeneradorReportes;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.ReporteServicioRegistradoDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class ReportesRecaudacionController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnVolver;
    @FXML
    private Label horaInicio;
    @FXML
    private Label horaDeFin;
    @FXML
    private Label txtEstado;
    @FXML
    private ComboBox<ServicioTipoDTO> cbServicio;
    @FXML
    private Button btnGenerar;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFinal;
    
    private GeneradorReportes generadorReportes;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initServicios();
    }    


    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }

    @FXML
    private void actGenerar(ActionEvent event) throws JRException, IOException {
        if(verificarCampos()){
            generadorReportes.generarReporte();
        }
    }
    
    private void initServicios(){
        ServicioTipoService servicioService = new ServicioTipoService();
        ArrayList<ServicioTipoDTO> servicios = new ArrayList<ServicioTipoDTO>();
        Respuesta respuesta = servicioService.getByEstado(true);
        if(respuesta.getEstado()){
            servicios = (ArrayList<ServicioTipoDTO>) respuesta.getResultado("TiposServicios");
            ObservableList items = FXCollections.observableArrayList(servicios);
            cbServicio.setItems(items);
        }
    }
    
    private boolean verificarCampos(){
        if(cbServicio.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el servicio");
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
        
        generadorReportes = new GeneradorReportes("Recaudacion", "", cbServicio.getValue().toString(), Date.valueOf(dpInicio.getValue()), Date.valueOf(dpFinal.getValue()));
        return true;
    }
    
    private void volver(){
//        try{
//            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
//            Parent root = FXMLLoader.load(App.class.getResource("MenuPrincipal" + ".fxml"));
//            Contenedor.getChildren().clear();
//            Contenedor.getChildren().add(root);
//        }catch(IOException ex){}
    }  
}
