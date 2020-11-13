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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AreaTrabajoDTO;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.TrabajoEmpleadoDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.AreaTrabajoService;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.TrabajoEmpleadoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class TrabajosEmpleadosInformacionController implements Initializable {


    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private ComboBox<AreaTrabajoDTO> cbxAreaTrabajo;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelCedula;

    
    private String modalidad="";
       
    private TrabajoEmpleadoService trabajoEmpleadoService = new TrabajoEmpleadoService();
    
    private TrabajoEmpleadoDTO trabajoEmpleadoEnCuestion = new TrabajoEmpleadoDTO();
    private EmpleadoDTO empleadoEnCuestion = new EmpleadoDTO();
    
    AreaTrabajoService areaTrabajoService = new AreaTrabajoService();
    EmpleadoService empleadosService = new EmpleadoService();
    
 
    private EmpleadoDTO empleado = new EmpleadoDTO();
    
    
    @FXML
    private ComboBox<EmpleadoDTO> cbxEmpleado;
    @FXML
    private Label labelNombreAreaTrabajo;
    @FXML
    private Label labelDescripcionAreaTrabajo;
    @FXML
    private Line barra1;
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
        iniAreasTrabajos();
        iniEmpleados();

        modalidad = (String) AppContext.getInstance().get("ModalidadTrabajoEmpleado");
        btnGuardar.setVisible(false);
        btnGuardar.setDisable(true);  
        cbxAreaTrabajo.setValue(trabajoEmpleadoEnCuestion.getAreaTrabajo());
        cbxEmpleado.setValue(trabajoEmpleadoEnCuestion.getEmpleado());


        if(!modalidad.equals("Ver")){
            btnGuardar.setVisible(true);
            btnGuardar.setDisable(false);
        }   

        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){

            trabajoEmpleadoEnCuestion = (TrabajoEmpleadoDTO) AppContext.getInstance().get("TrabajoEmpleadoEnCuestion");

            labelNombre.setText(trabajoEmpleadoEnCuestion.getEmpleado().getNombre());
            labelCedula.setText(trabajoEmpleadoEnCuestion.getEmpleado().getCedula());
            cbxAreaTrabajo.setValue(trabajoEmpleadoEnCuestion.getAreaTrabajo());
            cbxEmpleado.setValue(trabajoEmpleadoEnCuestion.getEmpleado());
            labelNombreAreaTrabajo.setText(trabajoEmpleadoEnCuestion.getAreaTrabajo().getNombre());
            labelDescripcionAreaTrabajo.setText(trabajoEmpleadoEnCuestion.getAreaTrabajo().getDescripcion());

            if(trabajoEmpleadoEnCuestion.getEstado()){
                estado=true;
                txtEstado.setText("Activo");
                btnCambiarEstado.setText("Anular");
            }else{
                estado=false;
                txtEstado.setText("Inactivo");
                btnCambiarEstado.setText("Activar");
            }
        }

        if(modalidad.equals("Ver")){
            GenerarTransacciones.crearTransaccion("Se observa trabajo empleado con id "+trabajoEmpleadoEnCuestion.getId(), "TrabajosEmpleadosInformacion");
            cbxAreaTrabajo.setDisable(true);
            cbxEmpleado.setDisable(true);
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);

        }
        if(modalidad.equals("Modificar")){
            cbxEmpleado.setDisable(true);
        }
        if(modalidad.equals("Agregar")){
            cbxEmpleado.setDisable(false);
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
        }
}

   
    public boolean validar(){
        if(cbxAreaTrabajo.getItems().isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el área de trabajo");
            return false;
        }
        if(cbxEmpleado.getItems().isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor selecione el empleado");
            return false;
        }
        return true;
    }
    @FXML
    private void actCancelar(ActionEvent event) {
        volver();
    }

    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("TrabajosEmpleados" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actGuardar(ActionEvent event) {
       if(validar()){
            trabajoEmpleadoEnCuestion.setAreaTrabajo(cbxAreaTrabajo.getValue());
            
            trabajoEmpleadoEnCuestion.setEmpleado(cbxEmpleado.getValue());
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=trabajoEmpleadoService.modificar(trabajoEmpleadoEnCuestion.getId(), trabajoEmpleadoEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica la a con id "+trabajoEmpleadoEnCuestion.getId(), "TrabajoEmpleadoInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación del area de trabajo de un empleado", "Se ha modificado el area de trabajo correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación del area de trabajo", respuesta.getMensaje());
                } 
            }else{
                if(modalidad.equals("Agregar")){
                    trabajoEmpleadoEnCuestion.setEstado(true);
                    Respuesta respuesta=trabajoEmpleadoService.crear(trabajoEmpleadoEnCuestion);
                    if(respuesta.getEstado()){
                        trabajoEmpleadoEnCuestion=(TrabajoEmpleadoDTO) respuesta.getResultado("TrabajoEmpleado");
                        GenerarTransacciones.crearTransaccion("Se asigna una area de trabajo a un empleado del id "+trabajoEmpleadoEnCuestion.getId(), "TrabajoEmpleadoInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro una area de trabajo a un empleado", "Se ha registrado una area de trabajo a un empleado correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro una area de trabajo a un empleado", respuesta.getMensaje());
                    }
                }
            }
        }
    }
    
   
    private Boolean estado=null;
    
    
    private AreaTrabajoDTO areaTra;

    public void iniAreasTrabajos(){
        ArrayList<AreaTrabajoDTO> areasTrabajos = new ArrayList<AreaTrabajoDTO>();
        Respuesta respuesta1 = areaTrabajoService.getAll();
        if(respuesta1.getEstado()==true){
            areasTrabajos = (ArrayList<AreaTrabajoDTO>) respuesta1.getResultado("AreasTrabajos");
        }
        ObservableList items3 = FXCollections.observableArrayList(areasTrabajos);   
        cbxAreaTrabajo.setItems(items3); 
    }
    
    public void iniEmpleados(){
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta1 = empleadosService.getAll();
        if(respuesta1.getEstado()==true){
            empleados = (ArrayList<EmpleadoDTO>) respuesta1.getResultado("Empleados");
        }
        ObservableList items3 = FXCollections.observableArrayList(empleados);   
        cbxEmpleado.setItems(items3); 
    }

    @FXML
    private void actAreaTrabajo(ActionEvent event) {
        labelDescripcionAreaTrabajo.setText(cbxAreaTrabajo.getValue().getDescripcion());
        labelNombreAreaTrabajo.setText(cbxAreaTrabajo.getValue().getNombre());
    }

    @FXML
    private void actEmpleado(ActionEvent event) {
        labelNombre.setText(cbxEmpleado.getValue().getNombre());
        labelCedula.setText(cbxEmpleado.getValue().getCedula());
    }

    @FXML
    private void actCambiarEstado(ActionEvent event) {
        try{
            if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gerente")){
                CambiarEstado();
            }else if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gestor")){
                Stage stage = new Stage();
                AppContext.getInstance().set("ModalidadSolicitudPermiso", "ContraseñaGerente,TrabajoEmpleado");
                AppContext.getInstance().set("ControllerPermiso", this);
                Parent root = FXMLLoader.load(App.class.getResource("SolicitudPermiso" + ".fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Trabajo de empleado "+trabajoEmpleadoEnCuestion.getEmpleado().getNombre());
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
            trabajoEmpleadoEnCuestion.setEstado(false);
            mensaje="Se anula el trabajo empleado con id "+trabajoEmpleadoEnCuestion.getId();
        }else{
            trabajoEmpleadoEnCuestion.setEstado(true);
            mensaje="Se activa el trabajo empleado con id "+trabajoEmpleadoEnCuestion.getId();
        }
        Respuesta respuesta=trabajoEmpleadoService.modificar(trabajoEmpleadoEnCuestion.getId(), trabajoEmpleadoEnCuestion);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "TrabajosEmpleadosInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado del trabajo de empleado", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del trabajo de empleado", respuesta.getMensaje());
        } 
    }

}
