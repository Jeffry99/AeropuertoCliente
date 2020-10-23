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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.util.Respuesta;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class AerolineasInformacionController implements Initializable {
    
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
    private TextField txtResponsable;
        
    private AerolineaService aerolineaService = new AerolineaService();
    private AerolineaDTO aerolineaEnCuestion = new AerolineaDTO();
    private String modalidad="";
    private Boolean estado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
    modalidad = (String) AppContext.getInstance().get("ModalidadAerolinea");
    btnGuardar.setVisible(false);
    btnGuardar.setDisable(true);  
        
    if(!modalidad.equals("Ver")){
        btnGuardar.setVisible(true);
        btnGuardar.setDisable(false);
    }   
      
     if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            aerolineaEnCuestion = (AerolineaDTO) AppContext.getInstance().get("AerolineaEnCuestion");
            txtNombre.setText(aerolineaEnCuestion.getNombre());
            txtResponsable.setText(aerolineaEnCuestion.getResponsable());
            if(aerolineaEnCuestion.getEstado()){
                estado=true;
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa aerolinea con id "+aerolineaEnCuestion.getId(), "AerolineasInformacion");
                txtResponsable.setDisable(true);
                txtNombre.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
            }
        }
    }
    
    public boolean validar(){
        if(txtResponsable.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el responsable de la aerolonea");
            return false;
        }
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre de la aerolinea");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de la aerolinea");
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
            
            aerolineaEnCuestion.setNombre(txtNombre.getText());
            aerolineaEnCuestion.setResponsable(txtResponsable.getText());
            aerolineaEnCuestion.setEstado(estado);
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=aerolineaService.modificar(aerolineaEnCuestion.getId(), aerolineaEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica la aerolinea con id "+aerolineaEnCuestion.getId(), "AerolineasInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de la aerolinea", "Se ha modificado la aerolinea correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de empleado", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=aerolineaService.crear(aerolineaEnCuestion);
                    if(respuesta.getEstado()){
                        aerolineaEnCuestion = (AerolineaDTO) respuesta.getResultado("Aerolinea");
                        GenerarTransacciones.crearTransaccion("Se crea la aerolinea con id "+aerolineaEnCuestion.getId(), "AerolineasInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de la aerolinea", "Se ha registrado el una aerolina correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de una aerolinea", respuesta.getMensaje());
                    }
                }
            }
        }
    }
    
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Aerolineas" + ".fxml"));
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
    private void actEstadoIctivo(ActionEvent event) {
        estado = false;
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
    }
    
}

