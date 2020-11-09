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
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.service.RolService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class RolesInformacionController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private Button btnVolver;

    /**
     * Initializes the controller class.
     */
    private String modalidad;
    private RolService rolService = new RolService();
    private RolDTO rol = new RolDTO();
    @FXML
    private Button btnCambiarEstado;
    @FXML
    private Label txtEstado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
        modalidad = (String) AppContext.getInstance().get("ModalidadRol");
        
        btnGuardar.setVisible(false);
        btnGuardar.setDisable(true);
        
        if(!modalidad.equals("Ver")){
            btnGuardar.setVisible(true);
            btnGuardar.setDisable(false);
        }
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            rol = (RolDTO) AppContext.getInstance().get("RolEnCuestion");
            txtDescripcion.setText(rol.getDescripcion());
            txtNombre.setText(rol.getNombre());
            if(rol.getEstado()==true){
                estado=true;
                txtEstado.setText("Activo");
                btnCambiarEstado.setText("Anular");
            }else{
                estado=false;
                txtEstado.setText("Inactivo");
                btnCambiarEstado.setText("Activar");
            }
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa rol con id "+rol.getId(), "RolesInformacion");
                txtDescripcion.setDisable(true);
                txtNombre.setDisable(true);
                btnCambiarEstado.setDisable(true);
                btnCambiarEstado.setVisible(false);
            }      
        }else{
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
        }
    }    

    
    
    

    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            rol.setNombre(txtNombre.getText());
            rol.setDescripcion(txtDescripcion.getText());
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=rolService.modificar(rol.getId(), rol);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica rol con id "+rol.getId(), "RolesInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de rol", "Se ha modificado el rol correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de rol", respuesta.getMensaje());
                }
            }else{
                if(modalidad.equals("Agregar")){
                    rol.setEstado(true);
                    Respuesta respuesta=rolService.crear(rol);
                    if(respuesta.getEstado()){
                        rol=(RolDTO) respuesta.getResultado("Rol");
                        GenerarTransacciones.crearTransaccion("Se crea rol con id "+rol.getId(), "RolesInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de rol", "Se ha registrado el rol correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de rol", respuesta.getMensaje());
                    }
                }
            }
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }
    
    public boolean validar(){
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del rol");
            return false;
        }
        if(txtDescripcion.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la descripcion del rol");
            return false;
        }
        
        return true;
    }

    private Boolean estado;
    
    

    
    public void volver(){
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Roles" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actCambiarEstado(ActionEvent event) {
        String mensaje="";
        if(estado){
            rol.setEstado(false);
            mensaje="Se anula el rol con id "+rol.getId();
        }else{
            rol.setEstado(true);
            mensaje="Se activa el rol con id "+rol.getId();
        }
        Respuesta respuesta=rolService.modificar(rol.getId(), rol);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "RolesInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado del rol", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del rol", respuesta.getMensaje());
        }
    }
    
}
