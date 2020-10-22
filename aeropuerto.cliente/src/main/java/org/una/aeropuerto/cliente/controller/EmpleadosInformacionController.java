/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.util.AppContext;

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
    
    private String modalidad="";
    
    private EmpleadoDTO empleadoEnCuestion = new EmpleadoDTO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modalidad = (String) AppContext.getInstance().get("ModalidadEmpleado");
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
            empleadoEnCuestion = (EmpleadoDTO) AppContext.getInstance().get("EmpleadoEnCuestion");
            txtNombre.setText(empleadoEnCuestion.getNombre());
            txtCedula.setText(empleadoEnCuestion.getCedula());
            txtTelefono.setText(empleadoEnCuestion.getTelefono());
            txtDireccion.setText(empleadoEnCuestion.getDireccion());  
            if(empleadoEnCuestion.getJefe()!= null){
                rbSi.setSelected(true);
                rbNo.setSelected(false);
            }else{
                rbSi.setSelected(false);
                rbNo.setSelected(true);
                cbxJefeDirecto.setValue(empleadoEnCuestion.getJefe());
                cbxJefeDirecto.setVisible(true);
                cbxJefeDirecto.setDisable(false);
            }
            lblFechaCreacion1.setText("Creado el "+empleadoEnCuestion.getFechaRegistro());
            lblFechaModificacion1.setText("Modificado el "+empleadoEnCuestion.getFechaModificacion());
        }
        
        
        
        

    }
    
    @FXML
    private void actGuardar(ActionEvent event) {
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actEsJefe(ActionEvent event) {
        
    }

    @FXML
    private void actNoEsJefe(ActionEvent event) {
        
        
       
    }

    @FXML
    private void actEstadoActivo(ActionEvent event) {
    }

    @FXML
    private void actEstadoInactivo(ActionEvent event) {
    }
    
}
