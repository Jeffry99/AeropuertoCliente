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
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.TipoAvionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Formato;
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
    private TextField txtNombre;
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
    @FXML
    private TextField txtDistanciaMaxima;
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formato();
        btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
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
            txtDistanciaMaxima.setText(String.valueOf(tipoAvionEnCuestion.getDistanciaMaxima()));
            if(tipoAvionEnCuestion.getEstado()){
                estado=true;
                txtEstado.setText("Activo");
                btnCambiarEstado.setText("Anular");
            }else{
                estado=false;
                txtEstado.setText("Inactivo");
                btnCambiarEstado.setText("Activar");
            }
            
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa tipoAvion con id "+tipoAvionEnCuestion.getId(), "TiposAvionesInformacion");
                txtDistancia.setDisable(true);
                txtNombre.setDisable(true);
                txtDistanciaMaxima.setDisable(true);
                btnCambiarEstado.setDisable(true);
                btnCambiarEstado.setVisible(false);
            }
        }else{
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
        }
    } 
    
    public void formato(){
        txtNombre.setTextFormatter(Formato.getInstance().maxLengthFormat(50));
        txtDistanciaMaxima.setTextFormatter(Formato.getInstance().twoDecimalFormat());
        txtDistancia.setTextFormatter(Formato.getInstance().twoDecimalFormat());
    }
    public boolean validar(){
        if(txtDistancia.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la distancia recomendada del tipo de avion");
            return false;
        }
        if(txtDistanciaMaxima.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la distancia máxima del tipo de avion");
            return false;
        }
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del tipo de avion");
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
            
            tipoAvionEnCuestion.setDistanciaMaxima(Float.valueOf(txtDistanciaMaxima.getText()));
            
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
                    tipoAvionEnCuestion.setEstado(true);
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
    private void actCambiarEstado(ActionEvent event) {
        try{
            if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gerente")){
                CambiarEstado();
            }else if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gestor")){
                Stage stage = new Stage();
                AppContext.getInstance().set("ModalidadSolicitudPermiso", "ContraseñaGerente,TipoAvion");
                AppContext.getInstance().set("ControllerPermiso", this);
                Parent root = FXMLLoader.load(App.class.getResource("SolicitudPermiso" + ".fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Tipo de avion "+tipoAvionEnCuestion.getNombre());
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
            tipoAvionEnCuestion.setEstado(false);
            mensaje="Se anula el tipo de avion con id "+tipoAvionEnCuestion.getId();
        }else{
            tipoAvionEnCuestion.setEstado(true);
            mensaje="Se activa el tipo de avion con id "+tipoAvionEnCuestion.getId();
        }
        Respuesta respuesta=tipoAvionService.modificar(tipoAvionEnCuestion.getId(), tipoAvionEnCuestion);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "TiposAvionesInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado del tipo de avion", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del tipo de avion", respuesta.getMensaje());
        }
    }

    
    
}
