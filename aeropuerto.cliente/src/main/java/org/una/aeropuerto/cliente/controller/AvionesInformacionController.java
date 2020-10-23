/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.TipoAvionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class AvionesInformacionController implements Initializable {

    @FXML
    private Label lblCedula;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblTelefono;
    @FXML
    private Label lblDireccion;
    @FXML
    private Label lblEstado;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Label lblIdNumero;
    private Button btnVuelos;
    private Button btnServicios;
    private Button btnBitacora;
    
    private String modalidad="";
    
    private AvionService avionService = new AvionService();
    private TextField txtMatricula;
    private ComboBox<AerolineaDTO> cbAerolinea;
    private ComboBox<TipoAvionDTO> cbTipoAvion;
    private AvionDTO avion;
    @FXML
    private TextField txtCedula;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        modalidad = (String) AppContext.getInstance().get("ModalidadAvion");
        initAerolineas();
        initTiposAvion();
        if(modalidad.equals("Ver")){
            llenarDatos();
            btnGuardar.setVisible(false);
            btnGuardar.setDisable(true);
            
            txtMatricula.setDisable(true);
            cbAerolinea.setDisable(true);
            cbTipoAvion.setDisable(true);
            rbActivo.setDisable(true);
            rbInactivo.setDisable(true);
        }
        if(modalidad.equals("Modificar")){
            llenarDatos();
        }
        if(modalidad.equals("Agregar")){
            btnServicios.setVisible(false);
            btnVuelos.setVisible(false);
            btnBitacora.setVisible(false);
            
        }
    }    
    public void llenarDatos(){
        avion = (AvionDTO)AppContext.getInstance().get("AvionEnCuestion");
        lblIdNumero.setText(avion.getId().toString());
        txtMatricula.setText(avion.getMatricula());
        cbAerolinea.setValue(avion.getAerolinea());
        cbTipoAvion.setValue(avion.getTipoAvion());
        
        if(avion.getEstado()){
            rbActivo.setSelected(true);
            rbInactivo.setSelected(false);
            estado = true;
        }else{
            rbActivo.setSelected(false);
            rbInactivo.setSelected(true);
            estado = false;
        }
    }
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            avion.setMatricula(txtMatricula.getText());
            avion.setEstado(estado);
            avion.setAerolinea(cbAerolinea.getValue());
            avion.setTipoAvion(cbTipoAvion.getValue());
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=avionService.modificar(avion.getId(), avion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica avion con id "+avion.getId(), "AvionesInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de avión", "Se ha modificado el avión correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de avión", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=avionService.crear(avion);
                    if(respuesta.getEstado()){
                        avion = (AvionDTO) respuesta.getResultado("Avion");
                        GenerarTransacciones.crearTransaccion("Se crea empleado con id "+avion.getId(), "AvionesInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de avión", "Se ha registrado el avión correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de avión", respuesta.getMensaje());
                    }
                }
            }
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }
    
    private Boolean estado;
    private void actActivo(ActionEvent event) {
        estado = true;
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
    }

    private void actInactivo(ActionEvent event) {
        estado = false;
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
    }
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Aviones" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
    public boolean validar(){
        if(txtMatricula.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la cedula del empleado");
            return false;
        }
        if(cbAerolinea.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del cliente");
            return false;
        }
        if(cbTipoAvion.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del cliente");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del empleado");
            return false;
        }
        return true;
    }
    
    public void initAerolineas(){
        AerolineaService aerolineaService = new AerolineaService();
        ArrayList<AerolineaDTO> aerolineas;
        Respuesta respuesta = aerolineaService.getByEstado(true);
        if(respuesta.getEstado()){
            aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
            ObservableList items = FXCollections.observableArrayList(aerolineas);
            cbAerolinea.setItems(items);
        }
    }
    
    public void initTiposAvion(){
        TipoAvionService tipoAvionService = new TipoAvionService();
        ArrayList<TipoAvionDTO> tiposAvion;
        Respuesta respuesta = tipoAvionService.getByEstado(true);
        if(respuesta.getEstado()){
            tiposAvion = (ArrayList<TipoAvionDTO>) respuesta.getResultado("TiposAviones");
            ObservableList items = FXCollections.observableArrayList(tiposAvion);
            cbTipoAvion.setItems(items);
        }
    }
    
}
