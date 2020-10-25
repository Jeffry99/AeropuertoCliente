/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.BitacoraAvionDTO;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.BitacoraAvionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class BitacoraInformacionController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Label lblIdNumero;
    @FXML
    private ComboBox<AvionDTO> cbAvion;
    @FXML
    private Label lblFechaModificacion;
    @FXML
    private Label lblFechaCreacion;
    @FXML
    private Spinner<Double> spDistancia;
    @FXML
    private Spinner<Integer> spCombustible;
    @FXML
    private Spinner<Integer> spTiempo;
    @FXML
    private TextField txtUbicacion;
    private String modalidad = "";
    BitacoraAvionService bitacoraService = new BitacoraAvionService();
    BitacoraAvionDTO bitacora = new BitacoraAvionDTO();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        modalidad = (String) AppContext.getInstance().get("ModalidadBitacora");
        SpinnerValueFactory<Double> value = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999999, 0);
        SpinnerValueFactory<Integer> value2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        SpinnerValueFactory<Integer> value3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999999, 0);
        spDistancia.setValueFactory(value);
        spCombustible.setValueFactory(value2);
        spTiempo.setValueFactory(value3);
        initAviones();
        if(modalidad.equals("Ver")){
            llenarDatos();
            btnGuardar.setVisible(false);          
            spTiempo.setDisable(true);
            spDistancia.setDisable(true);
            cbAvion.setDisable(true);
            spCombustible.setDisable(true);
            rbActivo.setDisable(true);
            rbInactivo.setDisable(true);
            txtUbicacion.setDisable(true);
        }
        if(modalidad.equals("Modificar")){
            llenarDatos();
        }
        if(modalidad.equals("Agregar")){
            lblFechaCreacion.setText("");
            lblFechaModificacion.setText("");
            lblIdNumero.setVisible(false);
        }
    }
    
    public void llenarDatos(){
        bitacora = (BitacoraAvionDTO)AppContext.getInstance().get("BitacoraEnCuestion");
        lblIdNumero.setText(bitacora.getId().toString());
        spTiempo.getValueFactory().setValue(bitacora.getTiempoTierra());
        spCombustible.getValueFactory().setValue(bitacora.getCombustible());
        spDistancia.getValueFactory().setValue(Double.valueOf(bitacora.getDistanciaRecorrida()));
        cbAvion.setValue(bitacora.getAvion());
        lblFechaCreacion.setText(bitacora.getFechaRegistro().toString());
        txtUbicacion.setText(bitacora.getUbicacion());
        
        if(bitacora.getFechaModificacion() != null){
            lblFechaModificacion.setText(bitacora.getFechaModificacion().toString());
        }else{
            lblFechaModificacion.setText("");
        }
        if(bitacora.getEstado()){
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
            bitacora.setAvion(cbAvion.getValue());
            bitacora.setCombustible(spCombustible.getValue());
            bitacora.setDistanciaRecorrida(spDistancia.getValue().floatValue());
            bitacora.setEstado(estado);
            bitacora.setTiempoTierra(spTiempo.getValue());
            bitacora.setUbicacion(txtUbicacion.getText());
            
            
            
            if(modalidad.equals("Modificar")){
                
                Respuesta respuesta=bitacoraService.modificar(bitacora.getId(), bitacora);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica bitácora con id "+bitacora.getId(), "BitacoraInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de bitácora", "Se ha modificado la bitácora correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de bitácora", respuesta.getMensaje());
                }
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=bitacoraService.crear(bitacora);
                    if(respuesta.getEstado()){
                        bitacora = (BitacoraAvionDTO) respuesta.getResultado("BitacoraAvion");
                        GenerarTransacciones.crearTransaccion("Se crea bitácora con id "+bitacora.getId(), "BitacoraInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de bitácora", "Se ha registrado la bitácora correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de bitácora", respuesta.getMensaje());
                    }
                }
            }
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Bitacora" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
    private Boolean estado;
    @FXML
    private void actActivo(ActionEvent event) {
        estado = true;
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
    }

    @FXML
    private void actInactivo(ActionEvent event) {
        estado = false;
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
    }
    
    public boolean validar(){
        if(spTiempo.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite tiempo en tierra");
            return false;
        }
        if(spDistancia.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la distancia recorrida");
            return false;
        }
        if(spCombustible.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el porcentaje de combustible");
            return false;
        }
        if(cbAvion.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el avión");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del servicio registrado");
            return false;
        }
        if(txtUbicacion.getText().isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la ubicación del avión");
            return false;
        }
        return true;
    }
    
    public void initAviones(){
        AvionService avionService = new AvionService();
        ArrayList<AvionDTO> aviones;
        Respuesta respuesta = avionService.getByEstado(true);
        if(respuesta.getEstado()){
            aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            ObservableList items = FXCollections.observableArrayList(aviones);
            cbAvion.setItems(items);
        }
    }
    
}