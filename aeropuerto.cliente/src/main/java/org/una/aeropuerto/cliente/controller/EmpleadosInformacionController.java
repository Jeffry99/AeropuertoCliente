/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.ParametroAplicacionDTO;
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.dto.UsuarioDTO;
import org.una.aeropuerto.cliente.service.AutenticacionService;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.ParametroAplicacionService;
import org.una.aeropuerto.cliente.service.RolService;
import org.una.aeropuerto.cliente.service.UsuarioService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class EmpleadosInformacionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    private Label lblCedula;
    @FXML
    private Label lblJefe;
    @FXML
    private TextField txtCedula;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    @FXML
    private RadioButton rbSi;
    @FXML
    private Label lblNombre;
    @FXML
    private TextField txtNombre;
    @FXML
    private Label lblTelefono;
    @FXML
    private TextField txtTelefono;
    @FXML
    private Label lblDireccion;
    @FXML
    private TextField txtDireccion;
    @FXML
    private RadioButton rbNo;
    @FXML
    private Label lblFechaCreacion1;
    @FXML
    private Label lblFechaModificacion1;
    @FXML
    private ComboBox<EmpleadoDTO> cbxJefeDirecto;
    @FXML
    private ComboBox<RolDTO> cbRol;
    @FXML
    private TextField txtContrasenaActual;
    @FXML
    private TextField txtContrasenaNueva;
    @FXML
    private TextField txtContrasenaConfirmar;
    
    private UsuarioDTO usuarioEnCuestion = new UsuarioDTO();
    private UsuarioService usuarioService = new UsuarioService();
    private String modalidad=""; 
    private EmpleadoDTO empleadoEnCuestion = new EmpleadoDTO();
    private EmpleadoService empleadoService = new EmpleadoService();
    @FXML
    private Label lblNombre1;
    @FXML
    private Label lblNombre11;
    @FXML
    private Label lblEstado1;
    @FXML
    private Label lbContrasenaNueva;
    @FXML
    private Label lbContrasenaConfirmar;
    @FXML
    private Label lbContrasenaActual;
    @FXML
    private Label txtEstado;
    @FXML
    private Button btnCambiarEstado;
    @FXML
    private Rectangle rectangulo;
    @FXML
    private TextField txtVerContrasenaNueva;
    @FXML
    private TextField txtVerContrasenaConfirmar;
    @FXML
    private TextField txtVerContrasenaActual;
    @FXML
    private JFXCheckBox cbContrasenaNueva;
    @FXML
    private JFXCheckBox cbContrasenaConfirmar;
    @FXML
    private JFXCheckBox cbContrasenaActual;
    
    private String cedulaIni ="";
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initPasswords();
        initEmpleadosJefe();
        initRoles();
        btnCambiarEstado.setStyle("-fx-text-fill: #000000; -fx-background-color:  #aaf2db;");
        modalidad = (String) AppContext.getInstance().get("ModalidadEmpleadoUsuario");
        btnGuardar.setVisible(false);
        btnGuardar.setDisable(true);
        lblFechaCreacion1.setVisible(false);
        lblFechaModificacion1.setVisible(false);
        cbxJefeDirecto.setVisible(false);
        cbxJefeDirecto.setDisable(true);
        if(!modalidad.equals("Ver")){
            btnGuardar.setVisible(true);
            btnGuardar.setDisable(false);
        }
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            usuarioEnCuestion = (UsuarioDTO) AppContext.getInstance().get("UsuarioEnCuestion");
            empleadoEnCuestion = usuarioEnCuestion.getEmpleado();
            cedulaIni = empleadoEnCuestion.getCedula();
            txtNombre.setText(empleadoEnCuestion.getNombre());
            txtCedula.setText(empleadoEnCuestion.getCedula());
            txtTelefono.setText(empleadoEnCuestion.getTelefono());
            txtDireccion.setText(empleadoEnCuestion.getDireccion());  
            if(empleadoEnCuestion.getJefe()!= null){
                rbSi.setSelected(false);
                rbNo.setSelected(true);
                esJefe=false;
                cbxJefeDirecto.setValue(empleadoEnCuestion.getJefe());
                cbxJefeDirecto.setVisible(true);
                cbxJefeDirecto.setDisable(false);
            }else{
                esJefe=true;
                rbSi.setSelected(true);
                rbNo.setSelected(false);
            }
            if(empleadoEnCuestion.getEstado()){
                estado=true;
                txtEstado.setText("Activo");
                btnCambiarEstado.setText("Anular");
            }else{
                estado=false;
                txtEstado.setText("Inactivo");
                btnCambiarEstado.setText("Activar");
            }
            lblFechaCreacion1.setText("Creado el "+empleadoEnCuestion.getFechaRegistro());
            lblFechaModificacion1.setText("Modificado el "+empleadoEnCuestion.getFechaModificacion());
            lblFechaCreacion1.setVisible(true);
            lblFechaModificacion1.setVisible(true);
            cbRol.setValue(usuarioEnCuestion.getRol());
            if(modalidad.equals("Ver")){
                btnCambiarEstado.setDisable(true);
                btnCambiarEstado.setVisible(false);
                GenerarTransacciones.crearTransaccion("Se observa empleado con id "+empleadoEnCuestion.getId(), "EmpleadosInformacion");
                txtCedula.setDisable(true);
                txtNombre.setDisable(true);
                txtTelefono.setDisable(true);
                txtDireccion.setDisable(true);
                rbSi.setDisable(true);
                rbNo.setDisable(true);
                cbxJefeDirecto.setDisable(true);
                cbRol.setDisable(true);
                txtContrasenaActual.setDisable(true);
                txtContrasenaActual.setVisible(false);
                txtContrasenaConfirmar.setDisable(true);
                txtContrasenaConfirmar.setVisible(false);
                txtContrasenaNueva.setDisable(true);
                txtContrasenaNueva.setVisible(false);
                txtVerContrasenaActual.setDisable(true);
                txtVerContrasenaActual.setVisible(false);
                txtVerContrasenaConfirmar.setDisable(true);
                txtVerContrasenaConfirmar.setVisible(false);
                txtVerContrasenaNueva.setDisable(true);
                txtVerContrasenaNueva.setVisible(false);
                cbContrasenaActual.setVisible(false);
                cbContrasenaActual.setDisable(true);
                cbContrasenaNueva.setVisible(false);
                cbContrasenaNueva.setDisable(true);
                cbContrasenaConfirmar.setVisible(false);
                cbContrasenaConfirmar.setDisable(true);
                lbContrasenaConfirmar.setVisible(false);
                lbContrasenaNueva.setVisible(false);
                lbContrasenaActual.setVisible(false);
                rectangulo.setVisible(false); 
            }
        }else{
            lbContrasenaActual.setVisible(false);
            txtContrasenaActual.setVisible(false);
            txtContrasenaActual.setDisable(true);
            cbContrasenaActual.setVisible(false);
            cbContrasenaActual.setDisable(true);
            txtVerContrasenaActual.setDisable(true);
            txtVerContrasenaActual.setVisible(false);
            txtEstado.setText("Activo");
            btnCambiarEstado.setDisable(true);
            btnCambiarEstado.setVisible(false);
            rectangulo.setVisible(false);
        }
    }
    
    public void initEmpleadosJefe(){
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta = empleadoService.getByEstado(true);
        if(respuesta.getEstado()){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
            ObservableList items = FXCollections.observableArrayList(empleados);
            cbxJefeDirecto.setItems(items);
        }
    }
    

    public void initRoles(){
        RolService rolService = new RolService();
        ArrayList<RolDTO> roles = new ArrayList();
        Respuesta respuesta = rolService.getByEstado(true);
        if(respuesta.getEstado()==true){
            roles = (ArrayList<RolDTO>) respuesta.getResultado("Roles");
        }
        ObservableList items2 = FXCollections.observableArrayList(roles);   
        cbRol.setItems(items2);
    }
    
    
    boolean cambioContrasena = true;
    String contrasenaActual = null;
    String contrasenaNueva = null;
    String contrasenaConfirmar = null;
    public boolean validar(){
        if(visibilidadContrasenaActual){
            contrasenaActual = txtVerContrasenaActual.getText();
        }else{
            contrasenaActual = txtContrasenaActual.getText();
        }
        
        if(visibilidadContrasenaNueva){
            contrasenaNueva = txtVerContrasenaNueva.getText();
        }else{
            contrasenaNueva = txtContrasenaNueva.getText();
        }
        
        if(visibilidadContrasenaConfirmar){
            contrasenaConfirmar = txtVerContrasenaConfirmar.getText();
        }else{
            contrasenaConfirmar = txtContrasenaConfirmar.getText();
        }
        
        
        
        if(txtCedula.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la cedula del empleado");
            return false;
        }
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del cliente");
            return false;
        }
        if(esJefe==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione si el empleado es jefe o no");
            return false;
        }else{
            if(!esJefe){
                if(cbxJefeDirecto.getValue()==null){
                    Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el jefe directo del empleado");
                    return false; 
                }
            } 
        }
        
        if(cbRol.getValue()==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el rol que va tener el usuario");
            return false;
        }
        if(!contrasenaNueva.isBlank()){
            if(contrasenaConfirmar.isBlank()){
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor confirme la contraseña");
                return false;
            }else{
                if(!contrasenaNueva.equals(contrasenaConfirmar)){
                    Mensaje.showAndWait(Alert.AlertType.WARNING, "Contraseña", "La contraseña a confirmar no coincide con la contraseña nueva");  
                    return false;
                }else{
                    if(modalidad.equals("Modificar")){
                        if(contrasenaActual.isBlank()){
                            Mensaje.showAndWait(Alert.AlertType.WARNING, "Contraseña", "Digite la contraseña actual");  
                            return false;
                        }else{
                            AutenticacionService auService = new AutenticacionService();
                            Respuesta res = auService.Login(cedulaIni, contrasenaActual);
                            if(res.getEstado()){
                                if(validarParametroContrasena()){
                                    cambioContrasena=true;
                                }else{
                                    cambioContrasena=false;
                                }
                                
                            }else{
                                cambioContrasena=false;
                                Mensaje.showAndWait(Alert.AlertType.WARNING, "Contraseña", "La contraseña actual no coincide");  
                                return false;
                            }
                        }
                    }else{
                        return validarParametroContrasena();
                    }
                }
            } 
        }else{
            cambioContrasena=false;
        }
        return true;
    }
    
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            empleadoEnCuestion.setNombre(txtNombre.getText());
            empleadoEnCuestion.setCedula(txtCedula.getText());
            empleadoEnCuestion.setDireccion(txtDireccion.getText());
            empleadoEnCuestion.setTelefono(txtTelefono.getText());
            if(!esJefe){
                empleadoEnCuestion.setJefe(cbxJefeDirecto.getValue());
            }else{
                empleadoEnCuestion.setJefe(null);
            }
            try{
                if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("administrador")){
                    ejecutarAccion();
                }else if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("gestor")){
                    Stage stage = new Stage();
                    AppContext.getInstance().set("ModalidadSolicitudPermiso", "ContraseñaAdministrador,Usuario");
                    AppContext.getInstance().set("ControllerPermiso", this);
                    Parent root = FXMLLoader.load(App.class.getResource("SolicitudPermiso" + ".fxml"));
                    stage.setScene(new Scene(root));
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
    }
    
    boolean visibilidadContrasenaActual = false;
    boolean visibilidadContrasenaNueva = false;
    boolean visibilidadContrasenaConfirmar = false;

    public void initPasswords(){
        cbContrasenaActual.selectedProperty().addListener( t -> {
            if(cbContrasenaActual.isSelected()){
                txtVerContrasenaActual.setVisible(true);
                txtContrasenaActual.setVisible(false);
                txtVerContrasenaActual.setText(txtContrasenaActual.getText());
                visibilidadContrasenaActual = true;
            }else{
                txtVerContrasenaActual.setVisible(false);
                txtContrasenaActual.setText(txtVerContrasenaActual.getText());
                txtContrasenaActual.setVisible(true);
                txtVerContrasenaActual.setText("");
                visibilidadContrasenaActual = false;
            }
        });
        
        cbContrasenaNueva.selectedProperty().addListener( t -> {
            if(cbContrasenaNueva.isSelected()){
                txtVerContrasenaNueva.setVisible(true);
                txtContrasenaNueva.setVisible(false);
                txtVerContrasenaNueva.setText(txtContrasenaNueva.getText());
                visibilidadContrasenaNueva = true;
            }else{
                txtVerContrasenaNueva.setVisible(false);
                txtContrasenaNueva.setText(txtVerContrasenaNueva.getText());
                txtContrasenaNueva.setVisible(true);
                txtVerContrasenaNueva.setText("");
                visibilidadContrasenaNueva = false;
            }
        });
        
        cbContrasenaConfirmar.selectedProperty().addListener( t -> {
            if(cbContrasenaConfirmar.isSelected()){
                txtVerContrasenaConfirmar.setVisible(true);
                txtContrasenaConfirmar.setVisible(false);
                txtVerContrasenaConfirmar.setText(txtContrasenaConfirmar.getText());
                visibilidadContrasenaConfirmar = true;
            }else{
                txtVerContrasenaConfirmar.setVisible(false);
                txtContrasenaConfirmar.setText(txtVerContrasenaConfirmar.getText());
                txtContrasenaConfirmar.setVisible(true);
                txtVerContrasenaConfirmar.setText("");
                visibilidadContrasenaConfirmar = false;
            }
        });
    }
    
    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }

    private Boolean esJefe;
    
    @FXML
    private void actEsJefe(ActionEvent event) {
        esJefe = true;
        rbSi.setSelected(true);
        rbNo.setSelected(false);
        
        cbxJefeDirecto.setVisible(false);
        cbxJefeDirecto.setDisable(true);
    }

    @FXML
    private void actNoEsJefe(ActionEvent event) {
        esJefe = false;
        rbSi.setSelected(false);
        rbNo.setSelected(true);
        cbxJefeDirecto.setVisible(true);
        cbxJefeDirecto.setDisable(false);
    }

    private Boolean estado;
    
    
    
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Usuarios" + ".fxml"));
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
                AppContext.getInstance().set("ModalidadSolicitudPermiso", "ContraseñaGerente,Empleado");
                AppContext.getInstance().set("ControllerPermiso", this);
                Parent root = FXMLLoader.load(App.class.getResource("SolicitudPermiso" + ".fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Empleado "+empleadoEnCuestion.getNombre());
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
            empleadoEnCuestion.setEstado(false);
            usuarioEnCuestion.setEstado(false);
            mensaje="Se anula el empleado con id "+empleadoEnCuestion.getId();
        }else{
            empleadoEnCuestion.setEstado(true);
            usuarioEnCuestion.setEstado(true);
            mensaje="Se activa el empleado con id "+empleadoEnCuestion.getId();
        }
        Respuesta respuesta=empleadoService.modificar(empleadoEnCuestion.getId(), empleadoEnCuestion);
        if(respuesta.getEstado()){
            respuesta=usuarioService.modificar(usuarioEnCuestion.getId(), usuarioEnCuestion);
            if(respuesta.getEstado()){
                GenerarTransacciones.crearTransaccion(mensaje, "EmpleadosInformacion");
                Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Estado del empleado", mensaje+" correctamente");
                volver();
            }else{
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del empleado", respuesta.getMensaje());
            }
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Estado del empleado", respuesta.getMensaje());
        }
    }

    
    private void Agregar(){
        empleadoEnCuestion.setEstado(true);
        Respuesta respuesta=empleadoService.crear(empleadoEnCuestion);
        if(respuesta.getEstado()){
            empleadoEnCuestion = (EmpleadoDTO) respuesta.getResultado("Empleado");

            usuarioEnCuestion.setEmpleado(empleadoEnCuestion);
            usuarioEnCuestion.setEstado(true);
            usuarioEnCuestion.setRol(cbRol.getValue());
            usuarioEnCuestion.setPasswordEncriptado(contrasenaNueva);

            Respuesta respuestaUsuario=usuarioService.crear(usuarioEnCuestion);
            if(respuestaUsuario.getEstado()){
                usuarioEnCuestion=(UsuarioDTO) respuestaUsuario.getResultado("Usuario");
                GenerarTransacciones.crearTransaccion("Se crea empleado y usuario con id "+empleadoEnCuestion.getId(), "EmpleadosInformacion");
                Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de empleado", "Se ha registrado el empleado correctamente");
                volver();
            }
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de empleado", respuesta.getMensaje());
        }
    }
    
    private void Modificar(){
        Respuesta respuesta=empleadoService.modificar(empleadoEnCuestion.getId(), empleadoEnCuestion);
        if(respuesta.getEstado()){
            empleadoEnCuestion = (EmpleadoDTO) respuesta.getResultado("Empleado");usuarioEnCuestion.setEmpleado(empleadoEnCuestion);
            usuarioEnCuestion.setRol(cbRol.getValue());

            if(cambioContrasena==true){
                usuarioEnCuestion.setPasswordEncriptado(contrasenaNueva);
            }
            Respuesta respuestaUsuario=usuarioService.modificar(usuarioEnCuestion.getId(), usuarioEnCuestion);
            if(respuestaUsuario.getEstado()){
                usuarioEnCuestion=(UsuarioDTO) respuestaUsuario.getResultado("Usuario");
                GenerarTransacciones.crearTransaccion("Se modifica empleado y usuario con id "+usuarioEnCuestion.getId(), "EmpleadosInformacion");
                Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de empleado", "Se ha modificado el empleado correctamente");
                volver();
            }
        }else{
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de empleado", respuesta.getMensaje());
        }
    }
    
    public void ejecutarAccion(){
        if(modalidad.equals("Modificar")){
            Modificar();  
        }else{
            if(modalidad.equals("Agregar")){
                Agregar();
            }
        }
    }
    
    private boolean validarParametroContrasena(){
        ParametroAplicacionService parametroService = new ParametroAplicacionService();
        Respuesta res = parametroService.getByNombreAproximate("Tamaño Password");
        try{
            if(res.getEstado()){
                List<ParametroAplicacionDTO> parametros  = (List<ParametroAplicacionDTO>) res.getResultado("ParametrosAplicacion");
                if(parametros.size()>0){
                    for(int i=0; i<parametros.size(); i++){
                        if(parametros.get(i).getNombre().equals("Tamaño Password")){
                            if(contrasenaNueva.length()<Integer.valueOf(parametros.get(i).getValor())){
                                Mensaje.showAndWait(Alert.AlertType.ERROR, "Contraseña", "La contraseña debe tener una extensión mínima de "+parametros.get(i).getValor());
                                return false;
                            }
                        }
                    }
                }
            }
        }catch(Exception ex){
            return true;
        }
        
        return true;
    }

    
    
}
