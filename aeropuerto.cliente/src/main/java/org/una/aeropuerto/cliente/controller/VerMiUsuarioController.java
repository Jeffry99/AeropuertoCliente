/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.dto.UsuarioDTO;
import org.una.aeropuerto.cliente.util.AppContext;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class VerMiUsuarioController implements Initializable {

    @FXML
    private Label lbNombre;
    @FXML
    private Label lnDireccion;
    @FXML
    private Label lbEstado;
    @FXML
    private Label lbCedula;
    @FXML
    private Label lbFechaCreacion;
    @FXML
    private Label lbFechaModificacion;
    @FXML
    private Label lbJefeDirecto;
    @FXML
    private Label lbRol;
    @FXML
    private Label lbDescripcionRol;
    @FXML
    private Label lbEstado1;
    @FXML
    private Label lbTelefono1;
    @FXML
    private Button btnVolver;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnVolver.setVisible(false);
        btnVolver.setDisable(true);
        
        UsuarioDTO usuario = UsuarioAutenticado.getInstance().getUsuarioLogeado();
        lbNombre.setText(usuario.getEmpleado().getNombre());
        lbCedula.setText(usuario.getEmpleado().getCedula());
        if(usuario.getEmpleado().getDireccion()!=null){
            lnDireccion.setText(usuario.getEmpleado().getDireccion());
        }else{
            lnDireccion.setText("No tiene dirección registrada");
        }
        String tel=usuario.getEmpleado().getTelefono();
        if(usuario.getEmpleado().getTelefono()!=null){
            lbTelefono1.setText(usuario.getEmpleado().getTelefono());
        }else{
            lbTelefono1.setText("No tiene teléfono registrado");
        }
        if(usuario.getEmpleado().getEstado()){
            lbEstado.setText("Activo");
        }else{
            lbEstado.setText("Inactivo");
        }
        if(usuario.getEmpleado().getJefe()!=null){
            lbJefeDirecto.setText(usuario.getEmpleado().getJefe().getNombre());
        }else{
            lbJefeDirecto.setText("No tiene");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lbFechaCreacion.setText(formatter.format(usuario.getEmpleado().getFechaRegistro()));
        lbFechaModificacion.setText(formatter.format(usuario.getEmpleado().getFechaModificacion()));
        lbRol.setText(usuario.getRol().getNombre());
        if(usuario.getRol().getDescripcion()!=null){
            lbDescripcionRol.setText(usuario.getEmpleado().getTelefono());
        }else{
            lbDescripcionRol.setText("");
        }
        if(usuario.getEstado()){
            lbEstado1.setText("Activo");
        }else{
            lbEstado1.setText("Inactivo");
        }
        
        if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("Marcaje")){
            btnVolver.setVisible(true);
            btnVolver.setDisable(false);
        }
    }   

    @FXML
    private void actVolver(ActionEvent event) {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Contenedor.getChildren().clear();
            Parent root = FXMLLoader.load(App.class.getResource("HoraMarcajeInformacion" + ".fxml"));
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
    
    
}
