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
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.dto.UsuarioDTO;
import org.una.aeropuerto.cliente.service.AutenticacionService;
import org.una.aeropuerto.cliente.service.EmpleadoService;
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
    private Label lblEstado;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
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
    private RadioButton rbActivoUsuario;
    @FXML
    private RadioButton rbInactivoUsuario;
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
    private Label lblEstado2;
    @FXML
    private Label lbContrasenaNueva;
    @FXML
    private Label lbContrasenaConfirmar;
    @FXML
    private Label lbContrasenaActual;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initEmpleadosJefe();
        initRoles();
          
        
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
            
            //////////////////////////////////////////////////////////////////////
            //sets empleado
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
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            lblFechaCreacion1.setText("Creado el "+empleadoEnCuestion.getFechaRegistro());
            lblFechaModificacion1.setText("Modificado el "+empleadoEnCuestion.getFechaModificacion());
            lblFechaCreacion1.setVisible(true);
            lblFechaModificacion1.setVisible(true);
            //////////////////////////////////////////////////////////////////////
            
            //////////////////////////////////////////////////////////////////////
            //sets usuario
            cbRol.setValue(usuarioEnCuestion.getRol());
            if(usuarioEnCuestion.getEstado()==true){
                estadoUsuario=true;
                rbActivoUsuario.setSelected(true);
                rbInactivoUsuario.setSelected(false);
            }else{
                estadoUsuario=false;
                rbActivoUsuario.setSelected(false);
                rbInactivoUsuario.setSelected(true);
            }
            //////////////////////////////////////////////////////////////////////
            
            
            
            
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa empleado con id "+empleadoEnCuestion.getId(), "EmpleadosInformacion");
                txtCedula.setDisable(true);
                txtNombre.setDisable(true);
                txtTelefono.setDisable(true);
                txtDireccion.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
                rbSi.setDisable(true);
                rbNo.setDisable(true);
                cbxJefeDirecto.setDisable(true);
                
                
                rbActivoUsuario.setDisable(true);
                rbInactivoUsuario.setDisable(true);
                cbRol.setDisable(true);
                txtContrasenaActual.setDisable(true);
                txtContrasenaActual.setVisible(false);
                txtContrasenaConfirmar.setDisable(true);
                txtContrasenaConfirmar.setVisible(false);
                txtContrasenaNueva.setDisable(true);
                txtContrasenaNueva.setVisible(false);
                lbContrasenaConfirmar.setVisible(false);
                lbContrasenaNueva.setVisible(false);
                lbContrasenaActual.setVisible(false);
                
                
                
            }
        }else{
            lbContrasenaActual.setVisible(false);
            txtContrasenaActual.setVisible(false);
            txtContrasenaActual.setDisable(true);
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
    public boolean validar(){
        if(txtCedula.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la cedula del empleado");
            return false;
        }
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del cliente");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del empleado");
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
        if(estadoUsuario==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del usuario");
            return false;
        }
        if(!txtContrasenaNueva.getText().isBlank()){
            if(txtContrasenaConfirmar.getText().isBlank()){
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor confirme la contraseña");
                return false;
            }else{
                if(!txtContrasenaNueva.getText().equals(txtContrasenaConfirmar.getText())){
                    Mensaje.showAndWait(Alert.AlertType.WARNING, "Contraseña", "La contraseña a confirmar no coincide con la contraseña nueva");  
                    return false;
                }else{
                    //evaluar contrasena actual si va editar el usuario, hacer login con la cedula del empleado y contrasena actual si me devulve false no funciono
                    if(modalidad.equals("Modificar")){
                        if(txtContrasenaActual.getText().isBlank()){
                            Mensaje.showAndWait(Alert.AlertType.WARNING, "Contraseña", "Digite la contraseña actual");  
                            return false;
                        }else{
                            AutenticacionService auService = new AutenticacionService();
                            Respuesta res = auService.Login(empleadoEnCuestion.getCedula(), txtContrasenaActual.getText());
                            if(res.getEstado()){
                                cambioContrasena=true;
                            }else{
                                Mensaje.showAndWait(Alert.AlertType.WARNING, "Contraseña", "La contraseña actual no coincide");  
                                return false;
                            }
                        }
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
            empleadoEnCuestion.setEstado(estado);

            if(!esJefe){
                empleadoEnCuestion.setJefe(cbxJefeDirecto.getValue());
            }else{
                empleadoEnCuestion.setJefe(null);
            }
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=empleadoService.modificar(empleadoEnCuestion.getId(), empleadoEnCuestion);
                if(respuesta.getEstado()){
                    empleadoEnCuestion = (EmpleadoDTO) respuesta.getResultado("Empleado");
                    
                    usuarioEnCuestion.setEmpleado(empleadoEnCuestion);
                    usuarioEnCuestion.setEstado(estadoUsuario);
                    usuarioEnCuestion.setRol(cbRol.getValue());
                    
                    if(cambioContrasena==true){
                        usuarioEnCuestion.setPasswordEncriptado(txtContrasenaNueva.getText());
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
                
                
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=empleadoService.crear(empleadoEnCuestion);
                    if(respuesta.getEstado()){
                        empleadoEnCuestion = (EmpleadoDTO) respuesta.getResultado("Empleado");
                        
                        usuarioEnCuestion.setEmpleado(empleadoEnCuestion);
                        usuarioEnCuestion.setEstado(estadoUsuario);
                        usuarioEnCuestion.setRol(cbRol.getValue());
                        usuarioEnCuestion.setPasswordEncriptado(txtContrasenaNueva.getText());
                        
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
            }
        }
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
    
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Usuarios" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }


    
    private Boolean estadoUsuario;
    @FXML
    private void actEstadoActivoUsuario(ActionEvent event) {
        rbActivoUsuario.setSelected(true);
        rbInactivoUsuario.setSelected(false);
        estadoUsuario=true;
    }

    @FXML
    private void actEstadoInactivoUsuario(ActionEvent event) {
        rbActivoUsuario.setSelected(false);
        rbInactivoUsuario.setSelected(true);
        estadoUsuario=false;
    }
}
