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
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.dto.UsuarioDTO;
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
public class UsuariosInformacionController implements Initializable {

    @FXML
    private Label txtCedula;
    @FXML
    private ComboBox<RolDTO> cbRol;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAdministrarContrasena;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private Label txtNombre;
    @FXML
    private Button btnSeleccionadorEmpleador;
    @FXML
    private Rectangle rectanguloContenedor;
    @FXML
    private StackPane ContenedorUsuarios;

    /**
     * Initializes the controller class.
     */
    private String modalidad;
    
    private UsuarioService usuarioService = new UsuarioService();
    private UsuarioDTO usuarioEnCuestion = new UsuarioDTO();
    private RolService rolService = new RolService();
    private EmpleadoDTO empleado = new EmpleadoDTO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modalidad = (String) AppContext.getInstance().get("ModalidadUsuario");
        
        
        ArrayList<RolDTO> roles = new ArrayList();
        Respuesta respuesta = rolService.getByEstado(true);
        if(respuesta.getEstado()==true){
            roles = (ArrayList<RolDTO>) respuesta.getResultado("Roles");
        }
        ObservableList items2 = FXCollections.observableArrayList(roles);   
        cbRol.setItems(items2);
        
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            usuarioEnCuestion = (UsuarioDTO) AppContext.getInstance().get("UsuarioEnCuestion");
            txtNombre.setText(usuarioEnCuestion.getEmpleado().getNombre());
            txtCedula.setText(usuarioEnCuestion.getEmpleado().getCedula());
            cbRol.setValue(usuarioEnCuestion.getRol());
            empleado=usuarioEnCuestion.getEmpleado();
            if(usuarioEnCuestion.getEstado()==true){
                estado=true;
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa empleado con id "+usuarioEnCuestion.getId(), "UsuariosInformacion");
                btnGuardar.setVisible(false);
                btnGuardar.setDisable(true);
                rbActivo.setDisable(true);
                rbInactivo.setDisable(true);
                cbRol.setDisable(true);
                btnAdministrarContrasena.setDisable(true);
                btnAdministrarContrasena.setVisible(false);
                btnSeleccionadorEmpleador.setDisable(true);
                btnSeleccionadorEmpleador.setVisible(false);
                rectanguloContenedor.setVisible(false);
            }
            
        }
        
    }    

    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            usuarioEnCuestion.setEmpleado(empleado);
            usuarioEnCuestion.setEstado(estado);
            usuarioEnCuestion.setRol(cbRol.getValue());
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=usuarioService.modificar(usuarioEnCuestion.getId(), usuarioEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica usuario con id "+usuarioEnCuestion.getId(), "UsuariosInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de usuario", "Se ha modificado el usuario correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de usuario", respuesta.getMensaje());
                }
            }else{
                if(modalidad.equals("Agregar")){
                    usuarioEnCuestion.setPasswordEncriptado(contrasena);
                    Respuesta respuesta=usuarioService.crear(usuarioEnCuestion);
                    if(respuesta.getEstado()){
                        usuarioEnCuestion=(UsuarioDTO) respuesta.getResultado("Usuario");
                        GenerarTransacciones.crearTransaccion("Se crea usuario con id "+usuarioEnCuestion.getId(), "UsuariosInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de usuario", "Se ha registrado el usuario correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de usuario", respuesta.getMensaje());
                    }
                }
            }
        }
        
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
        
    }

    @FXML
    private void actAdministrarContrasena(ActionEvent event) {
        try{
            AppContext.getInstance().set("controllerUsuarios", this);
            AppContext.getInstance().set("ModalidadContrasena", modalidad);
            Parent root = FXMLLoader.load(App.class.getResource("UsuariosContrasena" + ".fxml"));
            ContenedorUsuarios.getChildren().clear();
            ContenedorUsuarios.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
        
    }

    @FXML
    private void actRol(ActionEvent event) {
        
    }

    private Boolean estado;
    
    @FXML
    private void actActivo(ActionEvent event) {
        rbActivo.setSelected(true);
        rbInactivo.setSelected(false);
        estado=true;
        
    }

    @FXML
    private void actInactivo(ActionEvent event) {
        rbActivo.setSelected(false);
        rbInactivo.setSelected(true);
        estado=false;
        
    }

    @FXML
    private void actSeleccionadorEmpleador(ActionEvent event) {
        try{
            AppContext.getInstance().set("controllerUsuarios", this);
            Parent root = FXMLLoader.load(App.class.getResource("UsuariosSeleccionEmpleados" + ".fxml"));
            ContenedorUsuarios.getChildren().clear();
            ContenedorUsuarios.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
        
    }
    
    public void setEmpleado(EmpleadoDTO emp){
        empleado=emp;
        txtNombre.setText(empleado.getNombre());
        txtCedula.setText(empleado.getCedula());
    }
    
    private String contrasena;

    public void setContrasena(String contra) {
        this.contrasena = contra;
    }
    
    public void setUsuario(UsuarioDTO usu){
        usuarioEnCuestion = usu;
    }
    
    
    
    public boolean validar(){
        if(empleado.getCedula().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el empleado al que quiere vincular el usuario");
            return false;
        }
        if(cbRol.getValue()==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el rol que va tener el usuario");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del usuario");
            return false;
        }
        if(modalidad.equals("Agregar")){
            if(contrasena.isBlank()){
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor configure la contraseña del usuario");
                return false;
            }
        }
        
        return true;
    }
    
    public void volver(){
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Usuarios" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
}
