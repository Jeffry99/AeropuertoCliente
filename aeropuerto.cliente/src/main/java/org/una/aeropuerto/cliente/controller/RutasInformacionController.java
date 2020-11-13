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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.RutaDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
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
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
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
                txtEstado.setText("Activo");
                btnCambiarEstado.setText("Anular");
            }else{
                estado=false;
                txtEstado.setText("Inactivo");
                btnCambiarEstado.setText("Activar");
            }

            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa la ruta con id "+rutaEnCuestion.getId(), "RutasInformacion");
                txtDestino.setDisable(true);
                txtOrigen.setDisable(true);
                txtDistancia.setDisable(true);
                btnCambiarEstado.setDisable(true);
                btnCambiarEstado.setVisible(false);
            }
        }else{
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
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
                    rutaEnCuestion.setEstado(true);
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
    private void actCambiarEstado(ActionEvent event) {
        try{
            if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gerente")){
                CambiarEstado();
            }else if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gestor")){
                Stage stage = new Stage();
                AppContext.getInstance().set("ModalidadSolicitudPermiso", "ContraseñaGerente,Ruta");
                AppContext.getInstance().set("ControllerPermiso", this);
                Parent root = FXMLLoader.load(App.class.getResource("SolicitudPermiso" + ".fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Ruta "+rutaEnCuestion.getOrigen()+"-"+rutaEnCuestion.getDestino());
                stage.setResizable(Boolean.FALSE);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
                stage.show();
            }
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }; 
    }
    
    public void CambiarEstado(){
        String mensaje="";
        if(estado){
            rutaEnCuestion.setEstado(false);
            mensaje="Se anula la ruta con id "+rutaEnCuestion.getId();
        }else{
            rutaEnCuestion.setEstado(true);
            mensaje="Se activa la ruta con id "+rutaEnCuestion.getId();
        }
        Respuesta respuesta=rutaService.modificar(rutaEnCuestion.getId(), rutaEnCuestion);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "RutasInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado de la ruta", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado de la ruta", respuesta.getMensaje());
        }
    }

    
}
