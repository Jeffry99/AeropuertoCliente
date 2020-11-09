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
    private TextField txtNombre;
    
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
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
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
                txtEstado.setText("Activo");
                btnCambiarEstado.setText("Anular");
            }else{
                estado=false;
                txtEstado.setText("Inactivo");
                btnCambiarEstado.setText("Activar");
            }
            
            if(modalidad.equals("Ver")){
                btnCambiarEstado.setDisable(true);
                btnCambiarEstado.setVisible(false);
                GenerarTransacciones.crearTransaccion("Se observa aerolinea con id "+aerolineaEnCuestion.getId(), "AerolineasInformacion");
                txtResponsable.setDisable(true);
                txtNombre.setDisable(true);
            }
        }else{
            txtEstado.setText("Activo");
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
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
                    aerolineaEnCuestion.setEstado(true);
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
    private void actCambiarEstado(ActionEvent event) {
        String mensaje="";
        if(estado){
            aerolineaEnCuestion.setEstado(false);
            mensaje="Se anula la aerolinea con id "+aerolineaEnCuestion.getId();
        }else{
            aerolineaEnCuestion.setEstado(true);
            mensaje="Se activa la aerolinea con id "+aerolineaEnCuestion.getId();
        }
        Respuesta respuesta=aerolineaService.modificar(aerolineaEnCuestion.getId(), aerolineaEnCuestion);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "AerolineasInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado de la aerolinea", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado de la aerolinea", respuesta.getMensaje());
        }
    }

    
    
}

