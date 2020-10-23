package org.una.aeropuerto.cliente.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.RutaDTO;
import org.una.aeropuerto.cliente.service.RutaService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class RutasInformacionController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private Label lblIdNumero;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txtDestino;
    @FXML
    private TextField txtDistancia;
    @FXML
    private TextField txtOrigen;

    private RutaService rutaService = new RutaService();
    private RutaDTO rutaEnCuestion = new RutaDTO();
    private String modalidad="";
    private Boolean estado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {    

        modalidad = (String) AppContext.getInstance().get("ModalidadRuta");
        btnGuardar.setVisible(false);
        btnGuardar.setDisable(true);  

        if(!modalidad.equals("Ver")){
            btnGuardar.setVisible(true);
            btnGuardar.setDisable(false);
        }   

        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            rutaEnCuestion = (RutaDTO) AppContext.getInstance().get("RutaEnCuestion");
            txtDistancia.setText(String.valueOf(rutaEnCuestion.getDistancia()));
            txtOrigen.setText(rutaEnCuestion.getOrigen());
            txtDestino.setText(rutaEnCuestion.getDestino());
            if(rutaEnCuestion.getEstado()){
                estado=true;
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }

            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa la ruta con id "+rutaEnCuestion.getId(), "RutasInformacion");
                txtDestino.setDisable(true);
                txtOrigen.setDisable(true);
                txtDistancia.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
            }
        }
    }    

     public boolean validar(){
        if(txtOrigen.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el origen de la ruta");
            return false;
        }
        if(txtDestino.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el destino de la ruta");
            return false;
        }
        if(txtDistancia.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la distancia de la ruta");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de la ruta");
            return false;
        }
        return true;
    }
     
    @FXML
    private void actCancelar(ActionEvent event) {
        volver();
    }

    @FXML
    private void actGuardar(ActionEvent event) {
    if(validar()){
            rutaEnCuestion.setDestino(txtDestino.getText());
            rutaEnCuestion.setOrigen(txtOrigen.getText());
            rutaEnCuestion.setDistancia(Float.parseFloat(txtDistancia.getText()));
            rutaEnCuestion.setEstado(estado);
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=rutaService.modificar(rutaEnCuestion.getId(), rutaEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica la ruta con id "+rutaEnCuestion.getId(), "RutasInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de la ruta", "Se ha modificado la ruta correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de la rura", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=rutaService.crear(rutaEnCuestion);
                    if(respuesta.getEstado()){
                        rutaEnCuestion = (RutaDTO) respuesta.getResultado("Ruta");
                        GenerarTransacciones.crearTransaccion("Se crea la aerolinea con id "+rutaEnCuestion.getId(), "RutasInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de la ruta", "Se ha registrado el una ruta correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de una ruta", respuesta.getMensaje());
                    }
                }
            }
        }
    
    }
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Rutas" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actEstadoActivo(ActionEvent event) {
        estado = true;
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
    }

    @FXML
    private void actEstadoInactivo(ActionEvent event) {
        estado = false;
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
    }
    
}
