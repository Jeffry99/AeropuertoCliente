/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.BitacoraAvionDTO;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.dto.ServicioTipoDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.BitacoraAvionService;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;
import org.una.aeropuerto.cliente.service.ServicioTipoService;
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
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    @FXML
    private ComboBox<ServicioTipoDTO> cbTipoServicio;
    @FXML
    private Spinner<Double> txtCobro;
    @FXML
    private Label lblFechaRegistro;
    @FXML
    private Label lblFechaModificacion;
    @FXML
    private ComboBox<EmpleadoDTO> cbResponsable;
    
    private String modalidad="";
    private ServicioRegistradoDTO servicio = new ServicioRegistradoDTO();
    @FXML
    private Spinner<Double> txtDuracion;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private RadioButton rbActivoCobro;
    @FXML
    private RadioButton rbInactivoCobro;
    
    private Boolean estado;
    private Boolean estadoCobro;
    private ServicioRegistradoService servicioService;
    @FXML
    private ComboBox<AvionDTO> cbAviones;
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
        servicioService = new ServicioRegistradoService();
        servicio = new ServicioRegistradoDTO();
        modalidad = (String) AppContext.getInstance().get("ModalidadServicioRegistrado");
        SpinnerValueFactory<Double> value = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999999, 0);
        SpinnerValueFactory<Double> value2 = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999999, 0);
        txtCobro.setValueFactory(value);
        txtDuracion.setValueFactory(value2);
        initTiposServicio();
        initResponsables();
        initAviones();
        lblFechaModificacion.setText("");
        if(modalidad.equals("Ver")){
            llenarDatos();
            btnGuardar.setVisible(false);          
            txtCobro.setDisable(true);
            cbTipoServicio.setDisable(true);
            cbAviones.setDisable(true);
            rbActivoCobro.setDisable(true);
            rbInactivoCobro.setDisable(true);
            cbResponsable.setDisable(true);
            txtObservaciones.setDisable(true);
            txtDuracion.setDisable(true);
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
        }
        if(modalidad.equals("Modificar")){
            llenarDatos();
        }
        if(modalidad.equals("Agregar")){
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
            lblFechaRegistro.setText("");
            
        }
    }    
    
    public void llenarDatos(){
        servicio = (ServicioRegistradoDTO)AppContext.getInstance().get("ServicioRegistradoEnCuestion");
        txtCobro.getValueFactory().setValue(Double.valueOf(servicio.getCobro()));
        cbTipoServicio.setValue(servicio.getServicioTipo());
        cbResponsable.setValue(servicio.getResponsable());
        cbAviones.setValue(servicio.getAvion());
        lblFechaRegistro.setText(servicio.getFechaRegistro().toString());
        txtDuracion.getValueFactory().setValue(Double.valueOf(servicio.getDuracion()));
        txtObservaciones.setText(servicio.getObservaciones());
        
        if(servicio.getFechaModificacion() != null){
            lblFechaModificacion.setText(servicio.getFechaModificacion().toString());
        }else{
            lblFechaModificacion.setText("");
        }
        if(servicio.getEstado()){
            estado = true;
            txtEstado.setText("Activo");
            btnCambiarEstado.setText("Anular");
        }else{
            estado = false;
            txtEstado.setText("Inactivo");
            btnCambiarEstado.setText("Activar");
        }
        if(servicio.getEstadoCobro()){
            rbActivoCobro.setSelected(true);
            rbInactivoCobro.setSelected(false);
            estado = true;
        }else{
            rbActivoCobro.setSelected(false);
            rbInactivoCobro.setSelected(true);
            estado = false;
        }
        
    }
    
    public boolean validar(){
        if(txtCobro.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite monto del cobro");
            return false;
        }
        if(txtDuracion.getValue() == null){
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
        if(estadoCobro==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de cobro");
            return false;
        }
        return true;
    }
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            servicio.setCobro(txtCobro.getValue().floatValue());
            servicio.setResponsable(cbResponsable.getValue());
            servicio.setAvion(cbAviones.getValue());
            servicio.setDuracion(txtDuracion.getValue().floatValue());
            servicio.setEstadoCobro(estadoCobro);
            servicio.setServicioTipo(cbTipoServicio.getValue());
            
            if(generarAlertaHora()){
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
                        if(generarAlertaZonaDescarga()){
                            servicio.setEstado(true);
                            Respuesta respuesta=servicioService.crear(servicio);
                            if(respuesta.getEstado()){
                                servicio = (ServicioRegistradoDTO) respuesta.getResultado("ServicioAeropuerto");

                                GenerarTransacciones.crearTransaccion("Se crea servicio registrado con id "+servicio.getId(), "AvionesServiciosInformacion");
                                Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de servicio registrado", "Se ha registrado el servicio registrado correctamente");
                                volver();
                                crearBitacora();
                            }else{
                                Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de servicio registrado", respuesta.getMensaje());
                            }
                        }else{
                            Mensaje.showAndWait(Alert.AlertType.WARNING, "Registro de servicio", "El avion debe ir a zona de descarga antes de ir a "+servicio.getServicioTipo().getAreaTrabajo().getNombre());
                        }   
                    }
                }
            }else{
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Registro de servicio", "El avion se encuentra actualmente en otro servicio");
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
        ServicioTipoService serviciosTiposService = new ServicioTipoService();
        ArrayList<ServicioTipoDTO> tiposServicio;
        Respuesta respuesta = serviciosTiposService.getByEstado(true);
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
            cbResponsable.setItems(items);
        }
    }
    
    public void initAviones(){
        AvionService avionService = new AvionService();
        ArrayList<AvionDTO> aviones;
        Respuesta respuesta = avionService.getByEstado(true);
        if(respuesta.getEstado()){
            aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            ObservableList items = FXCollections.observableArrayList(aviones);
            cbAviones.setItems(items);
        }
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

    @FXML
    private void actCambiarEstado(ActionEvent event) {
        try{
            if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gerente")){
                CambiarEstado();
            }else if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gestor")){
                Stage stage = new Stage();
                AppContext.getInstance().set("ModalidadSolicitudPermiso", "ContraseñaGerente,ServicioRegistrado");
                AppContext.getInstance().set("ControllerPermiso", this);
                Parent root = FXMLLoader.load(App.class.getResource("SolicitudPermiso" + ".fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Servicio registrado "+servicio.getServicioTipo()+"-"+servicio.getId());
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
            servicio.setEstado(false);
            mensaje="Se anula el servicio registrado con id "+servicio.getId();
        }else{
            servicio.setEstado(true);
            mensaje="Se activa el servicio registrado con id "+servicio.getId();
        }
        Respuesta respuesta=servicioService.modificar(servicio.getId(), servicio);
        if(respuesta.getEstado()){
            GenerarTransacciones.crearTransaccion(mensaje, "serviciosInformacion");
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado del servicio registrado", mensaje+" correctamente");
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del servicio registrado", respuesta.getMensaje());
        }
    }
    
    private boolean generarAlertaZonaDescarga(){
        BitacoraAvionDTO bitacora = getBitacoraMayor();
        if(bitacora!=null){
            String area= servicio.getServicioTipo().getAreaTrabajo().getNombre();
            if(area.equals("Mantenimiento")||area.equals("Carga de combustible")||area.equals("Hangar")){
                if(bitacora.getTiempoTierra()==0 && bitacora.getUbicacion().equals("Pista")){
                    return false;
                }
            } 
        }
        
        return true;
    }
    
    private boolean generarAlertaHora(){
        
        ServicioRegistradoDTO servicioM = getMayor();
        if(servicioM!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(servicioM.getFechaRegistro());
            calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)+(int)servicioM.getDuracion());
            System.out.println(calendar.getTime());
            Date fechaFinal = calendar.getTime();
            Date fechaYa = new Date();
            System.out.println("fecha final: "+fechaFinal);
            System.out.println("fecha ya: "+fechaYa);
            if(fechaYa.before(fechaFinal)){
                return false;
            }
        }
        return true;
    }
    
    private ServicioRegistradoDTO getMayor(){
        ServicioRegistradoService servicioRegistradoService = new ServicioRegistradoService();
        ServicioRegistradoDTO servicioMayor= new ServicioRegistradoDTO();
        ArrayList<ServicioRegistradoDTO> servicios = new ArrayList<ServicioRegistradoDTO>();
        Respuesta respuesta = servicioRegistradoService.getByAvion(servicio.getAvion().getId());
        if(respuesta.getEstado().equals(true)){
            servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
            
            if(servicios.size()>0){
                servicioMayor=servicios.get(0);
            }
            if(servicios.size()>1){
                for(int i=1; i<servicios.size(); i++){
                    if(servicios.get(i).getId()>servicioMayor.getId()){
                        servicioMayor=servicios.get(i);
                    }
                }
            }
            return servicioMayor;
        } 
        return null;
    }
    
    public void crearBitacora(){
        BitacoraAvionDTO bitacora = new BitacoraAvionDTO();
        BitacoraAvionDTO bitacoraM = getBitacoraMayor();
        bitacora.setAvion(servicio.getAvion());
        if(bitacoraM != null){
            bitacora.setDistanciaRecorrida(bitacoraM.getDistanciaRecorrida());
            bitacora.setTiempoTierra(bitacoraM.getTiempoTierra()+(int)servicio.getDuracion());
            if(servicio.getServicioTipo().getAreaTrabajo().getNombre().equals("Carga de combustible")){
                bitacora.setCombustible(100);
            }else{
                bitacora.setCombustible(bitacoraM.getCombustible());
            }
            
        }else{
            bitacora.setDistanciaRecorrida(0);
            bitacora.setTiempoTierra((int)servicio.getDuracion());
            if(servicio.getServicioTipo().getAreaTrabajo().getNombre().equals("Carga de combustible")){
                bitacora.setCombustible(100);
            }else{
                bitacora.setCombustible(0);
            }
        }
        bitacora.setUbicacion(servicio.getServicioTipo().getAreaTrabajo().getNombre());
        bitacora.setEstado(true);
        BitacoraAvionService bitacoraService = new BitacoraAvionService();
        bitacoraM.setEstado(false);
        Respuesta res = bitacoraService.crear(bitacora);
        if(res.getEstado()){
            res = bitacoraService.modificar(bitacoraM.getId(), bitacoraM);
        }
        
        
        
        
    }
    
    public BitacoraAvionDTO getBitacoraMayor(){
        BitacoraAvionService bitacoraService = new BitacoraAvionService();
        BitacoraAvionDTO bitacoraMayor= new BitacoraAvionDTO();
        ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList<BitacoraAvionDTO>();
        Respuesta respuesta = bitacoraService.getByAvion(servicio.getAvion().getId());
        if(respuesta.getEstado().equals(true)){
            bitacoras = (ArrayList<BitacoraAvionDTO>) respuesta.getResultado("BitacorasAvion");
            
            if(bitacoras.size()>0){
                bitacoraMayor=bitacoras.get(0);
            }
            if(bitacoras.size()>1){
                for(int i=1; i<bitacoras.size(); i++){
                    if(bitacoras.get(i).getId()>bitacoraMayor.getId()){
                        bitacoraMayor=bitacoras.get(i);
                    }
                }
            }
            return bitacoraMayor;
        } 
        return null;
    }
}
