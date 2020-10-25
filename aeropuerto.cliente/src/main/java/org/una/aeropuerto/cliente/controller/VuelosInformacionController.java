/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.json.bind.annotation.JsonbDateFormat;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.RutaDTO;
import org.una.aeropuerto.cliente.dto.VueloDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.RutaService;
import org.una.aeropuerto.cliente.service.TipoAvionService;
import org.una.aeropuerto.cliente.service.VueloService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class VuelosInformacionController implements Initializable {

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
    @FXML
    private ComboBox<RutaDTO> cbRuta;
    @FXML
    private ComboBox<AvionDTO> cbAvion;
    @FXML
    private DatePicker dpFecha;
    private VueloDTO vuelo = new VueloDTO();
    private String modalidad = "";
    private VueloService vueloService = new VueloService();
    @FXML
    private ImageView imgHora;
    @FXML
    public Label lbHora;
    /**
     * 
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRutas();
        initAviones();
        // TODO
        
        modalidad = AppContext.getInstance().get("ModalidadVuelo").toString();
        
        if(modalidad.equals("Ver")){
            llenarDatos();
            btnGuardar.setVisible(false);
            btnGuardar.setDisable(true);
            
            cbAvion.setDisable(true);
            cbRuta.setDisable(true);
            dpFecha.setDisable(true);
            rbActivo.setDisable(true);
            rbInactivo.setDisable(true);
        }
        if(modalidad.equals("Modificar")){
            llenarDatos();
        }
        if(modalidad.equals("Agregar")){
            vuelo = new VueloDTO();
            lblIdNumero.setVisible(false);
        }
            
    }    
    public void llenarDatos(){
        vuelo = (VueloDTO) AppContext.getInstance().get("VueloEnCuestion");
        lblIdNumero.setText(vuelo.getId().toString());
        cbAvion.setValue(vuelo.getAvion());
        cbRuta.setValue(vuelo.getRuta());
        
        fechaGuardar=vuelo.getFecha();
        
        
        
        
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        lbHora.setText(formatter.format(fechaGuardar));
        
        
        ZoneId defaultZoneId = ZoneId.systemDefault();
	Instant instant = fechaGuardar.toInstant();
        dpFecha.setValue(instant.atZone(defaultZoneId).toLocalDate());
        
        getFecha();
        setHora();

        
        if(vuelo.isEstado()){
            rbActivo.setSelected(true);
            rbInactivo.setSelected(false);
            estado = true;
        }else{
            rbActivo.setSelected(false);
            rbInactivo.setSelected(true);
            estado = false;
        }
    }
    
    
    
    
    
    
    
    
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            
            fechaGuardar=dateCalendar.getTime();
            vuelo.setAvion(cbAvion.getValue());
            vuelo.setEstado(estado);
            vuelo.setFecha(fechaGuardar);
            vuelo.setRuta(cbRuta.getValue());

            
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=vueloService.modificar(vuelo.getId(), vuelo);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica vuelo con id "+vuelo.getId(), "VuelosInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de vuelo", "Se ha modificado el vuelo correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de vuelo", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=vueloService.crear(vuelo);
                    if(respuesta.getEstado()){
                        vuelo = (VueloDTO) respuesta.getResultado("Vuelo");
                        GenerarTransacciones.crearTransaccion("Se crea vuelo con id "+vuelo.getId(), "VuelosInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de vuelo", "Se ha registrado el vuelo correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de vuelo", respuesta.getMensaje());
                    }
                }
            }
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }
    private Boolean estado;
    @FXML
    private void actActivo(ActionEvent event) {
        estado = true;
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
    }

    @FXML
    private void actInactivo(ActionEvent event) {
        estado = false;
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
    }
    public void initAviones(){
        AvionService avionService = new AvionService();
        ArrayList<AvionDTO> aviones;
        Respuesta respuesta = avionService.getByEstado(true);
        if(respuesta.getEstado()){
            aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            ObservableList items = FXCollections.observableArrayList(aviones);
            cbAvion.setItems(items);
        }
    }
    
    public void initRutas(){
        RutaService rutaService = new RutaService();
        ArrayList<RutaDTO> rutas;
        Respuesta respuesta = rutaService.getByEstado(true);
        if(respuesta.getEstado()){
            rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Rutas");
            ObservableList items = FXCollections.observableArrayList(rutas);
            cbRuta.setItems(items);
        }
    }
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Vuelos" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
    public Boolean EstadoHora = false;
    
    public boolean validar(){
        if(cbAvion.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el avión");
            return false;
        }
        if(cbRuta.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la ruta");
            return false;
        }
        if(dpFecha.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la fecha");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado");
            return false;
        }
        if(modalidad.equals("Agregar")){
            if(EstadoHora==false){
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la hora del vuelo");
                return false;
            }
        }
        return true;
    }

    @FXML
    private void actRutas(MouseEvent event) {
        
    }

    @FXML
    private void actAviones(MouseEvent event) {
        
    }

    
    Calendar dateCalendar = Calendar.getInstance();
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date fechaGuardar;
    
    
    public void getFecha(){
        String fecha[]=dpFecha.getValue().toString().split("-");
        dateCalendar.set(Calendar.YEAR,Integer.valueOf(fecha[0]));
        dateCalendar.set(Calendar.MONTH, Integer.valueOf(fecha[1])-1);
        dateCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(fecha[2]));
        fechaGuardar=dateCalendar.getTime();
    }
    
    public void setHora(){
        String hora[]=lbHora.getText().split(":");
        dateCalendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(hora[0]));
        dateCalendar.set(Calendar.MINUTE,Integer.valueOf(hora[1]));
        fechaGuardar=dateCalendar.getTime();
    }
    
    
    @FXML
    private void actSelFecha(ActionEvent event) {
        getFecha();
        
        System.out.println("--------------------------------");
        System.out.println("Fecha date picker: "+dpFecha.getValue().toString());
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");
        System.out.println("Fecha date: "+fechaGuardar);
        System.out.println("--------------------------------");
    }

    @FXML
    private void actSelHora(MouseEvent event) {
        try{
            Stage stage = new Stage();
            AppContext.getInstance().set("ModalidadSeleccionDeHora", "Vuelos");
            AppContext.getInstance().set("ControllerVuelos", this);
            Parent root = FXMLLoader.load(App.class.getResource("SeleccionDeHora" + ".fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Selección de hora de vuelo");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
}
