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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AreaTrabajoDTO;
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
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
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
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa aerolinea con id "+areaTrabajoEnCuestion.getId(), "AreasTrabajosInformacion");
                txtDescripcion.setDisable(true);
                txtNombre.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
            }
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
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del area de trabajo");
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
            areaTrabajoEnCuestion.setEstado(estado);
            
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
    private void actEstadoInactivo(ActionEvent event) {
        estado = false;
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
    }

    @FXML
    private void actEstadoActivo(ActionEvent event) {
        estado = true;
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
    }
}
