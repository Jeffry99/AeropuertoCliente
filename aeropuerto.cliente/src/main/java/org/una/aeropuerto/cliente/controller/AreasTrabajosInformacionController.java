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
import org.una.aeropuerto.cliente.dto.AreaTrabajoDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.AreaTrabajoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class AreasTrabajosInformacionController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txtDescripcion;

    private AreaTrabajoService areaTrabajoService = new AreaTrabajoService();
    private AreaTrabajoDTO areaTrabajoEnCuestion = new AreaTrabajoDTO();
    private String modalidad="";
    private Boolean estado;
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
    modalidad = (String) AppContext.getInstance().get("ModalidadAreaTrabajo");
    btnGuardar.setVisible(false);
    btnGuardar.setDisable(true);  
        
    if(!modalidad.equals("Ver")){
        btnGuardar.setVisible(true);
        btnGuardar.setDisable(false);
    }   
      
     if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            areaTrabajoEnCuestion = (AreaTrabajoDTO) AppContext.getInstance().get("AreaTrabajoEnCuestion");
            txtNombre.setText(areaTrabajoEnCuestion.getNombre());
            txtDescripcion.setText(areaTrabajoEnCuestion.getDescripcion());
            if(areaTrabajoEnCuestion.getEstado()){
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
                GenerarTransacciones.crearTransaccion("Se observa area de trabajo con id "+areaTrabajoEnCuestion.getId(), "AreasTrabajosInformacion");
                txtDescripcion.setDisable(true);
                txtNombre.setDisable(true);
            }
        }else{
            txtEstado.setText("Activo");
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
        }
    }
    
     public boolean validar(){
        if(txtDescripcion.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la descripcion del area de trabajo");
            return false;
        }
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del area de trabajo");
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
            areaTrabajoEnCuestion.setNombre(txtNombre.getText());
            areaTrabajoEnCuestion.setDescripcion(txtDescripcion.getText());
            
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=areaTrabajoService.modificar(areaTrabajoEnCuestion.getId(), areaTrabajoEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica el aerea de trabajo con id "+areaTrabajoEnCuestion.getId(), "AreasTrabajosInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación del area de trabajo", "Se ha modificado el aerea de trabajo correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de empleado", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    areaTrabajoEnCuestion.setEstado(true);
                    Respuesta respuesta=areaTrabajoService.crear(areaTrabajoEnCuestion);
                    if(respuesta.getEstado()){
                        areaTrabajoEnCuestion = (AreaTrabajoDTO) respuesta.getResultado("AreaTrabajo");
                        GenerarTransacciones.crearTransaccion("Se crea una area de trabajo con id "+areaTrabajoEnCuestion.getId(), "AreasTrabajosInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de una area de trabajo", "Se ha registrado el una area de trabajo correctamente");
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
            Parent root = FXMLLoader.load(App.class.getResource("AreasTrabajo" + ".fxml"));
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
                AppContext.getInstance().set("ModalidadSolicitudPermiso", "ContraseñaGerente,AreaTrabajo");
                AppContext.getInstance().set("ControllerPermiso", this);
                Parent root = FXMLLoader.load(App.class.getResource("SolicitudPermiso" + ".fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Area de trabajo "+areaTrabajoEnCuestion.getNombre());
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
            areaTrabajoEnCuestion.setEstado(false);
            mensaje="Se anula el area de trabajo con id "+areaTrabajoEnCuestion.getId();
        }else{
            areaTrabajoEnCuestion.setEstado(true);
            mensaje="Se activa el area de trabajo con id "+areaTrabajoEnCuestion.getId();
        }
        Respuesta respuesta=areaTrabajoService.modificar(areaTrabajoEnCuestion.getId(), areaTrabajoEnCuestion);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "AreaTrabajosInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado del area de trabajo", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del area de trabajo", respuesta.getMensaje());
        }
    }

    
}
