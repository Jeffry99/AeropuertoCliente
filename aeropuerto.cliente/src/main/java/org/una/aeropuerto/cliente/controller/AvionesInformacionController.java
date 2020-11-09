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
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.BitacoraAvionDTO;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.dto.VueloDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.BitacoraAvionService;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;
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
public class AvionesInformacionController implements Initializable {

    @FXML
    private Label lblCedula;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    private Label lblNombre;
    @FXML
    private Label lblTelefono;
    @FXML
    private Label lblDireccion;
    private Label lblIdNumero;
    
    private String modalidad="";
    AerolineaService aerolineaService = new AerolineaService();
    private AvionService avionService = new AvionService();
    @FXML
    private TextField txtMatricula;
    @FXML
    private ComboBox<AerolineaDTO> cbAerolinea;
    @FXML
    private ComboBox<TipoAvionDTO> cbTipoAvion;
    private AvionDTO avion = new AvionDTO();
    @FXML
    private Button btnServicios;
    @FXML
    private Button btnVuelos;
    @FXML
    private Button btnBitacora;
    @FXML
    private TableView<Object> tvTabla;
    @FXML
    private Button btnOcultar;
    @FXML
    private Label lblInformacion;
    @FXML
    private Pane pane;
    @FXML
    private Label lblCedula1;
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
        tvTabla.setVisible(false);
        btnOcultar.setVisible(false);
        modalidad = (String) AppContext.getInstance().get("ModalidadAvion");
        initAerolineas();
        initTiposAvion();
        if(modalidad.equals("Ver")){
            llenarDatos();
            btnGuardar.setVisible(false);
            btnGuardar.setDisable(true);
            
            txtMatricula.setDisable(true);
            cbAerolinea.setDisable(true);
            cbTipoAvion.setDisable(true);
            
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
        }
        if(modalidad.equals("Modificar")){
            llenarDatos();
        }
        if(modalidad.equals("Agregar")){
            lblIdNumero.setVisible(false);
            lblNombre.setVisible(false);
            btnServicios.setVisible(false);
            btnVuelos.setVisible(false);
            btnBitacora.setVisible(false);
            pane.setVisible(false);
            lblInformacion.setVisible(false);
            txtEstado.setText("Activo");
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
        }
    }    
    public void llenarDatos(){
        avion = (AvionDTO)AppContext.getInstance().get("AvionEnCuestion");
        lblIdNumero.setText(avion.getId().toString());
        txtMatricula.setText(avion.getMatricula());
        cbAerolinea.setValue(avion.getAerolinea());
        cbTipoAvion.setValue(avion.getTipoAvion());
        
        if(avion.getEstado()){
            estado = true;
            txtEstado.setText("Activo");
            btnCambiarEstado.setText("Anular");
        }else{
            estado = false;
            txtEstado.setText("Inactivo");
            btnCambiarEstado.setText("Activar");
        }
    }
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            avion.setMatricula(txtMatricula.getText());
            
            avion.setAerolinea(cbAerolinea.getValue());
            avion.setTipoAvion(cbTipoAvion.getValue());
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=avionService.modificar(avion.getId(), avion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica avion con id "+avion.getId(), "AvionesInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de avión", "Se ha modificado el avión correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de avión", respuesta.getMensaje());
                }
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    avion.setEstado(true);
                    Respuesta respuesta=avionService.crear(avion);
                    if(respuesta.getEstado()){
                        avion = (AvionDTO) respuesta.getResultado("Avion");
                        BitacoraAvionService bitacoraService = new BitacoraAvionService();
                        BitacoraAvionDTO bitacora = new BitacoraAvionDTO();
                        bitacora.setAvion(avion);
                        bitacora.setCombustible(100);
                        bitacora.setDistanciaRecorrida(0);
                        bitacora.setEstado(true);
                        bitacora.setTiempoTierra(0);
                        bitacora.setUbicacion("Hangar");
                        respuesta = bitacoraService.crear(bitacora);
                        GenerarTransacciones.crearTransaccion("Se crea empleado con id "+avion.getId(), "AvionesInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de avión", "Se ha registrado el avión correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de avión", respuesta.getMensaje());
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
    
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Aviones" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
    public boolean validar(){
        if(txtMatricula.getText().isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la cedula del empleado");
            return false;
        }
        if(cbAerolinea.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del cliente");
            return false;
        }
        if(cbTipoAvion.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del cliente");
            return false;
        }
        return true;
    }
    
    public void initAerolineas(){
        
        ArrayList<AerolineaDTO> aerolineas;
        Respuesta respuesta = aerolineaService.getByEstado(true);
        if(respuesta.getEstado()){
            aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
            ObservableList items = FXCollections.observableArrayList(aerolineas);
            cbAerolinea.setItems(items);
        }
    }
    
    public void initTiposAvion(){
        TipoAvionService tipoAvionService = new TipoAvionService();
        ArrayList<TipoAvionDTO> tiposAvion;
        Respuesta respuesta = tipoAvionService.getByEstado(true);
        if(respuesta.getEstado()){
            tiposAvion = (ArrayList<TipoAvionDTO>) respuesta.getResultado("TiposAviones");
            ObservableList items = FXCollections.observableArrayList(tiposAvion);
            cbTipoAvion.setItems(items);
        }
    }

    @FXML
    private void actServicios(ActionEvent event) {
        tvTabla.setVisible(true);
        btnOcultar.setVisible(true);
        cargarTablaServicios();
    }

    @FXML
    private void actVuelos(ActionEvent event) {
        tvTabla.setVisible(true);
        btnOcultar.setVisible(true);
        cargarTablaVuelos();
    }

    @FXML
    private void actBitacora(ActionEvent event) {
        tvTabla.setVisible(true);
        btnOcultar.setVisible(true);
        cargarTablaBitacora();
    }

    @FXML
    private void actOcultarTable(ActionEvent event) {
        tvTabla.setVisible(false);
        btnOcultar.setVisible(false);
        lblInformacion.setText("Seleccione la información que quiere observar sobre este avión");
    }
    
    
    public void cargarTablaServicios(){
        ServicioRegistradoService servicioService = new ServicioRegistradoService();
        ArrayList<Object> lista = new ArrayList<Object>();
        Respuesta respuesta = servicioService.getByAvion(avion.getId());
        if(respuesta.getEstado().equals(true)){
            lista = (ArrayList<Object>) respuesta.getResultado("ServiciosAeropuerto");
        }
        tvTabla.getColumns().clear();
        if(!lista.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(lista);
            
            TableColumn <Object, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            
            TableColumn <Object, String>colTipoServicio = new TableColumn("Tipo de Servicio");
            colTipoServicio.setCellValueFactory(new PropertyValueFactory("servicioTipo"));
            
            TableColumn <Object, String>colCobro = new TableColumn("Cobro");
            colCobro.setCellValueFactory(new PropertyValueFactory("cobro"));
            
            TableColumn<Object, String> colEstadoCobro = new TableColumn("Estado de Cobro");
            colEstadoCobro.setCellValueFactory(av -> {
                String estadoString;
                ServicioRegistradoDTO servicio = (ServicioRegistradoDTO)av.getValue();
                if(servicio.getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            
            TableColumn<Object, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(av -> {
                String estadoString;
                ServicioRegistradoDTO servicio = (ServicioRegistradoDTO)av.getValue();
                if(servicio.getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            
            TableColumn<Object, String> colResponsable = new TableColumn("Responsable");
            colResponsable.setCellValueFactory(new PropertyValueFactory("responsable"));
            
            TableColumn<Object, String> colAvion = new TableColumn("Avión");
            colAvion.setCellValueFactory(new PropertyValueFactory("avion"));
            
            tvTabla.getColumns().addAll(colId);
            tvTabla.getColumns().addAll(colTipoServicio);
            tvTabla.getColumns().addAll(colCobro);
            tvTabla.getColumns().addAll(colEstado);
            tvTabla.getColumns().addAll(colEstadoCobro);
            tvTabla.getColumns().addAll(colResponsable);
            tvTabla.getColumns().addAll(colAvion);
            tvTabla.setItems(items);
        }else{
            tvTabla.setVisible(false);
            btnOcultar.setVisible(false);
            lblInformacion.setText("No hay elementos para mostrar");
        }
    }
    
    public void cargarTablaVuelos(){
        VueloService vueloService = new VueloService();
        ArrayList<Object> lista = new ArrayList<Object>();
        Respuesta respuesta = vueloService.getByAvion(avion.getId());
        if(respuesta.getEstado().equals(true)){
            lista = (ArrayList<Object>) respuesta.getResultado("Vuelos");
        }
        tvTabla.getColumns().clear();
        if(!lista.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(lista);   
            
            TableColumn <Object, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            
            TableColumn <Object, String>colAvion = new TableColumn("Avión");
            
            
            TableColumn <Object, String>colOrigen = new TableColumn("Origen");
                colOrigen.setCellValueFactory(av -> {
                VueloDTO vuelo = (VueloDTO)av.getValue();
                String origen = vuelo.getRuta().getOrigen();
                return new ReadOnlyStringWrapper(origen);
            });
            
            TableColumn <Object, String>colDestino = new TableColumn("Destino");
                colDestino.setCellValueFactory(av -> {
                VueloDTO vuelo = (VueloDTO)av.getValue();
                String destino = vuelo.getRuta().getDestino();
                return new ReadOnlyStringWrapper(destino);
            });
            
            TableColumn <Object, String>colFecha = new TableColumn("Fecha");
            colFecha.setCellValueFactory(new PropertyValueFactory("fecha"));
            
            TableColumn<Object, String> colEstado = new TableColumn("Estado");
                colEstado.setCellValueFactory(av -> {
                String estadoString;
                VueloDTO vuelo = (VueloDTO)av.getValue();
                if(vuelo.getEstado()==1)
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvTabla.getColumns().addAll(colId);
            tvTabla.getColumns().addAll(colOrigen);
            tvTabla.getColumns().addAll(colDestino);
            tvTabla.getColumns().addAll(colAvion);
            tvTabla.getColumns().addAll(colFecha);
            tvTabla.getColumns().addAll(colEstado);
            tvTabla.setItems(items);
        }else{
            tvTabla.setVisible(false);
            btnOcultar.setVisible(false);
            lblInformacion.setText("No hay elementos para mostrar");
        }
    }
    
    public void cargarTablaBitacora(){
        BitacoraAvionService bitacoraService = new BitacoraAvionService();
        ArrayList<Object> lista = new ArrayList<Object>();
        Respuesta respuesta = bitacoraService.getByAvion(avion.getId());
        if(respuesta.getEstado().equals(true)){
            lista = (ArrayList<Object>) respuesta.getResultado("BitacorasAvion");
        }
        
        tvTabla.getColumns().clear();
        if(!lista.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(lista);   
            
            TableColumn <Object, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
          
            TableColumn <Object, String>colCombustible = new TableColumn("Combustible");
            colCombustible.setCellValueFactory(new PropertyValueFactory("combustible"));
          
            TableColumn <Object, String>colDistancia = new TableColumn("Distancia Recorrida");
            colDistancia.setCellValueFactory(new PropertyValueFactory("distanciaRecorrida"));
         
            TableColumn <Object, String>colTiempo = new TableColumn("Tiempo en Tierra");
            colTiempo.setCellValueFactory(new PropertyValueFactory("tiempoTierra"));
            
            TableColumn <Object, String>colUbicacion = new TableColumn("Ubicación");
            colUbicacion.setCellValueFactory(new PropertyValueFactory("ubicacion"));
           
            TableColumn <Object, String>colAvion = new TableColumn("Avión");
            colAvion.setCellValueFactory(new PropertyValueFactory("avion"));
            
            TableColumn<Object, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(av -> {
                BitacoraAvionDTO bitacora = (BitacoraAvionDTO)av.getValue();
                String estadoString;
                if(bitacora.getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvTabla.getColumns().addAll(colId);
            tvTabla.getColumns().addAll(colCombustible);
            tvTabla.getColumns().addAll(colDistancia);
            tvTabla.getColumns().addAll(colTiempo);
            tvTabla.getColumns().addAll(colUbicacion);
            tvTabla.getColumns().addAll(colAvion);
            tvTabla.setItems(items);
        }else{
            tvTabla.setVisible(false);
            btnOcultar.setVisible(false);
            lblInformacion.setText("No hay elementos para mostrar");
        }
    }

    @FXML
    private void actCambiarEstado(ActionEvent event) {
        String mensaje="";
        if(estado){
            avion.setEstado(false);
            mensaje="Se anula el avion con id "+avion.getId();
        }else{
            avion.setEstado(true);
            mensaje="Se activa el avion con id "+avion.getId();
        }
        Respuesta respuesta=avionService.modificar(avion.getId(), avion);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "AvionesInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado del avion", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del avion", respuesta.getMensaje());
        }
    }
}
