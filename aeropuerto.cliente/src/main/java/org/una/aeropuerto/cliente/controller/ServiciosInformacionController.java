/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.dto.ServicioTipoDTO;
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;
import org.una.aeropuerto.cliente.service.ServicioTipoService;
import org.una.aeropuerto.cliente.service.TipoAvionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class ServiciosInformacionController implements Initializable {

    @FXML
    private Label lblCedula;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblTelefono;
    @FXML
    private Label lblDireccion;
    @FXML
    private Label lblEstado;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Label lblIdNumero;
    private ComboBox<String> cbEstadoCobro;
    @FXML
    private Label lblTelefono1;
    @FXML
    private ComboBox<ServicioTipoDTO> cbTipoServicio;
    @FXML
    private TextField txtCobro;
    @FXML
    private Label lblTelefono11;
    @FXML
    private Label lblFechaRegistro;
    @FXML
    private Label lblFechaModificacion;
    @FXML
    private Label lblTelefono1121;
    @FXML
    private ComboBox<EmpleadoDTO> cbResponsable;
    
    private String modalidad="";
    private ServicioRegistradoDTO servicio;
    @FXML
    private Label lblNombre1;
    @FXML
    private TextField txtDuracion;
    @FXML
    private Label lblNombre11;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private RadioButton rbActivoCobro;
    @FXML
    private RadioButton rbInactivoCobro;
    
    private Boolean estado;
    private Boolean estadoCobro;
    private ServicioRegistradoService servicioService = new ServicioRegistradoService();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        modalidad = (String) AppContext.getInstance().get("ModalidadServicioRegistrado");

        initTiposServicio();
        initResponsables();
        
        if(modalidad.equals("Ver")){
            llenarDatos();
            btnGuardar.setVisible(false);
            btnGuardar.setDisable(true);
            
            txtCobro.setDisable(true);
            cbTipoServicio.setDisable(true);
            cbEstadoCobro.setDisable(true);
            rbActivo.setDisable(true);
            rbInactivo.setDisable(true);
            cbResponsable.setDisable(true);
            txtObservaciones.setDisable(true);
            txtDuracion.setDisable(true);
        }
        if(modalidad.equals("Modificar")){
            llenarDatos();
        }
        if(modalidad.equals("Agregar")){
            lblFechaRegistro.setText("");
            
        }
    }    
    
    public void llenarDatos(){
        servicio = (ServicioRegistradoDTO)AppContext.getInstance().get("ServicioRegistradoEnCuestion");
        lblIdNumero.setText(servicio.getId().toString());
        txtCobro.setText(String.valueOf(servicio.getCobro()));
        cbTipoServicio.setValue(servicio.getServicioTipo());
        cbResponsable.setValue(servicio.getResponsable());
        lblFechaRegistro.setText(servicio.getFechaRegistro().toString());
        txtDuracion.setText(String.valueOf(servicio.getDuracion()));
        txtObservaciones.setText(servicio.getObservaciones());
        
        if(servicio.getFechaModificacion() != null){
            lblFechaModificacion.setText(servicio.getFechaModificacion().toString());
        }else{
            lblFechaModificacion.setText("");
        }
        if(servicio.getEstado()){
            rbActivo.setSelected(true);
            rbInactivo.setSelected(false);
            estado = true;
        }else{
            rbActivo.setSelected(false);
            rbInactivo.setSelected(true);
            estado = false;
        }
        if(servicio.getEstadoCobro()){
            rbActivoCobro.setSelected(true);
            rbInactivoCobro.setSelected(false);
            estado = true;
        }else{
            rbActivo.setSelected(false);
            rbInactivo.setSelected(true);
            estado = false;
        }
        
    }
    
    public boolean validar(){
        if(txtCobro.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite monto del cobro");
            return false;
        }
        if(txtDuracion.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la duracion del servicio");
            return false;
        }
        if(cbTipoServicio.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el tipo de servicio");
            return false;
        }
        if(cbResponsable.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el empleado responsable");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del servicio registrado");
            return false;
        }
        if(estadoCobro==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de cobro");
            return false;
        }
        return true;
    }
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            servicio.setCobro(Float.valueOf(txtCobro.getText()));
            servicio.setEstado(estado);
            servicio.setResponsable(cbResponsable.getValue());
            servicio.setAvion((AvionDTO)AppContext.getInstance().get("AvionEnCuestion"));
            servicio.setDuracion(Float.valueOf(txtDuracion.getText()));
            servicio.setEstadoCobro(estadoCobro);
            servicio.setServicioTipo(cbTipoServicio.getValue());
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=servicioService.modificar(servicio.getId(), servicio);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica servicio registrado con id "+servicio.getId(), "AvionesServiciosInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de servicio registrado", "Se ha modificado el servicio registrado correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de servicio registrado", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=servicioService.crear(servicio);
                    if(respuesta.getEstado()){
                        servicio = (ServicioRegistradoDTO) respuesta.getResultado("ServicioAeropuerto");
                        GenerarTransacciones.crearTransaccion("Se crea servicio registrado con id "+servicio.getId(), "AvionesServiciosInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de servicio registrado", "Se ha registrado el servicio registrado correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de servicio registrado", respuesta.getMensaje());
                    }
                }
            }
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Servicios" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
    public void initTiposServicio(){
        ServicioTipoService serviciosService = new ServicioTipoService();
        ArrayList<ServicioTipoDTO> tiposServicio;
        Respuesta respuesta = serviciosService.getByEstado(true);
        if(respuesta.getEstado()){
            tiposServicio = (ArrayList<ServicioTipoDTO>) respuesta.getResultado("TiposServicios");
            ObservableList items = FXCollections.observableArrayList(tiposServicio);
            cbTipoServicio.setItems(items);
        }
    }
    
    public void initResponsables(){
        EmpleadoService empleadoService = new EmpleadoService();
        ArrayList<EmpleadoDTO> empleados;
        Respuesta respuesta = empleadoService.getByEstado(true);
        if(respuesta.getEstado()){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
            ObservableList items = FXCollections.observableArrayList(empleados);
            cbTipoServicio.setItems(items);
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

    @FXML
    private void actEstadoCobroActivo(ActionEvent event) {
        estadoCobro = true;
        rbActivoCobro.setSelected(true);
        rbInactivoCobro.setSelected(false);
    }

    @FXML
    private void actEstadoCobroInactivo(ActionEvent event) {
        estadoCobro = false;
        rbActivoCobro.setSelected(false);
        rbInactivoCobro.setSelected(true);
    }
    
}
