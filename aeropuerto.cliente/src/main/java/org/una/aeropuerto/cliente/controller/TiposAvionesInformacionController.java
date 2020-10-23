/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

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
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.service.TipoAvionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class TiposAvionesInformacionController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private Label lblIdNumero;
    @FXML
    private TextField txtNombre;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txtDistancia;

    private TipoAvionService tipoAvionService = new TipoAvionService();
    private TipoAvionDTO tipoAvionEnCuestion = new TipoAvionDTO();
    private String modalidad="";
    private Boolean estado;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    modalidad = (String) AppContext.getInstance().get("ModalidadTipoAvion");
    btnGuardar.setVisible(false);
    btnGuardar.setDisable(true);  
        
    if(!modalidad.equals("Ver")){
        btnGuardar.setVisible(true);
        btnGuardar.setDisable(false);
    }   
      
     if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            tipoAvionEnCuestion = (TipoAvionDTO) AppContext.getInstance().get("TipoAvionEnCuestion");
            txtNombre.setText(tipoAvionEnCuestion.getNombre());
            txtDistancia.setText(String.valueOf(tipoAvionEnCuestion.getDistanciaRecomendada()));
            if(tipoAvionEnCuestion.getEstado()){
                estado=true;
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa tipoAvion con id "+tipoAvionEnCuestion.getId(), "TiposAvionesInformacion");
                txtDistancia.setDisable(true);
                txtNombre.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
            }
        }
    } 
    public boolean validar(){
        if(txtDistancia.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la distancia recomendada del tipo de avion");
            return false;
        }
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del tipo de avion");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del tipo de avion");
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
            
            tipoAvionEnCuestion.setNombre(txtNombre.getText());
            tipoAvionEnCuestion.setDistanciaRecomendada(Float.parseFloat(txtDistancia.getText()));
            tipoAvionEnCuestion.setEstado(estado);
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=tipoAvionService.modificar(tipoAvionEnCuestion.getId(), tipoAvionEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica el tipo de avion con id "+tipoAvionEnCuestion.getId(), "TiposAvionesInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación el tipo de avion", "Se ha modificado el tipo de avion correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación el tipo de estado", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=tipoAvionService.crear(tipoAvionEnCuestion);
                    if(respuesta.getEstado()){
                        tipoAvionEnCuestion = (TipoAvionDTO) respuesta.getResultado("TipoAvion");
                        GenerarTransacciones.crearTransaccion("Se crea un tipo de avion con id "+tipoAvionEnCuestion.getId(), "TiposAvionesInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro un tipo de avion", "Se ha registrado un tipo de avion correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de un tipo de avion", respuesta.getMensaje());
                    }
                }
            }
            
            
        }
    }
   
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("TiposAvion" + ".fxml"));
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
