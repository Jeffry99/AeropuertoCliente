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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.TransaccionDTO;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class TransaccionesInformacionController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private Label txtNombre;
    @FXML
    private Label txtDescripcion;
    @FXML
    private Label lblFechaRegistro;
    @FXML
    private Button btnVolver;
    @FXML
    private Label txtFechaRegistro;
    @FXML
    private Label txtId;
    @FXML
    private Label txtLugar;
    @FXML
    private Label txtRol;
    @FXML
    private Label txtCedula;
    @FXML
    private Label txtTelefono;
    @FXML
    private Label txtDireccion;
    @FXML
    private Label txtEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String modalidad = (String) AppContext.getInstance().get("ModalidadTransaccion");
        if(modalidad.equals("Ver")){
            
            TransaccionDTO transaccion = (TransaccionDTO) AppContext.getInstance().get("TransaccionEnCuestion");
            GenerarTransacciones.crearTransaccion("Se observa transaccion con id "+transaccion.getId(), "EmpleadosInformacion");
            txtCedula.setText(transaccion.getUsuario().getEmpleado().getCedula());
            txtDescripcion.setText(transaccion.getDescripcion());
            txtDireccion.setText(transaccion.getUsuario().getEmpleado().getDireccion());
            SimpleDateFormat formatterF = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatterH = new SimpleDateFormat("HH:mm:ss");
            txtFechaRegistro.setText(formatterF.format(transaccion.getFechaRegistro())+"  Hora: "+formatterH.format(transaccion.getFechaRegistro()));
            txtId.setText(String.valueOf(transaccion.getId()));
            txtLugar.setText(transaccion.getLugar());
            txtNombre.setText(transaccion.getUsuario().getEmpleado().getNombre());
            txtRol.setText(transaccion.getRol());
            txtTelefono.setText(transaccion.getUsuario().getEmpleado().getTelefono());
            if(transaccion.getEstado()==true){
                txtEstado.setText("Activo");
            }else{
                txtEstado.setText("Inactivo");
            }
        }  
    }
            

    @FXML
    private void actVolver(ActionEvent event) {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Transacciones" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    
}
