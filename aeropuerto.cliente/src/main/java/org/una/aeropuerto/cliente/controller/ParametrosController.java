/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.dto.ParametroAplicacionDTO;
import org.una.aeropuerto.cliente.service.ParametroAplicacionService;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class ParametrosController implements Initializable {

    @FXML
    private Button btnBuscarNombre;
    @FXML
    private JFXTextField txtBuscarNombre;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private JFXComboBox<String> cbxBuscarEstado;
    @FXML
    private TableView<ParametroAplicacionDTO> tvParametros;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextField txtValor;
    @FXML
    private JFXTextArea txtDescripcion;
    @FXML
    private JFXComboBox<String> cbxEstado;
    @FXML
    private Label lbFechaRegistro;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lbFechaModificacion;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Label lbTituloDetalle;
    /**
     * Initializes the controller class.
     */
    
    private ParametroAplicacionService parametroService = new ParametroAplicacionService();
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private String modalidad="";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnGuardar.setText("Agregar");
        modalidad="Agregar";
        initEstado();
        cargarTodos();
        lbFechaRegistro.setText("");
        lbFechaModificacion.setText("");
        lbTituloDetalle.setText("Agregar parámetro");
    }    

    @FXML
    private void actBuscarNombre(ActionEvent event) {
        if(!txtBuscarNombre.getText().isBlank()){
            ArrayList<ParametroAplicacionDTO> parametros = new ArrayList<ParametroAplicacionDTO>();
            Respuesta respuesta = parametroService.getByNombreAproximate(txtBuscarNombre.getText());
            if(respuesta.getEstado().equals(true)){
                parametros = (ArrayList<ParametroAplicacionDTO>) respuesta.getResultado("ParametrosAplicacion");
            }
            cargarTabla(parametros);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del parámetro que desea buscar");
        }
    }

    private Boolean estadoBuscar = null;
    @FXML
    private void actBuscarEstado(ActionEvent event) {
        if(estadoBuscar!=null){
            ArrayList<ParametroAplicacionDTO> parametros = new ArrayList<ParametroAplicacionDTO>();
            Respuesta respuesta = parametroService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                parametros = (ArrayList<ParametroAplicacionDTO>) respuesta.getResultado("ParametrosAplicacion");
            }
            cargarTabla(parametros);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del parámetro que desea buscar");
        }
    }

    @FXML
    private void actSelEstado(ActionEvent event) {
        if(cbxBuscarEstado.getValue().equals("Activo")){
            estadoBuscar=true;
        }else{
            estadoBuscar=false;
        }
    }

    private Boolean estadoGuardar = null;
    @FXML
    private void actEstado(ActionEvent event) {
        if(cbxEstado.getValue().equals("Activo")){
            estadoGuardar=true;
        }else{
            estadoGuardar=false;
        }
    }
    
    private void initEstado(){
        ArrayList estados = new ArrayList();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);   
        
        cbxEstado.setItems(items);
        cbxBuscarEstado.setItems(items);
    }
    
    public void cargarTodos(){
        ArrayList<ParametroAplicacionDTO> parametros = new ArrayList<ParametroAplicacionDTO>();
        Respuesta respuesta = parametroService.getAll();
        if(respuesta.getEstado().equals(true)){
            parametros = (ArrayList<ParametroAplicacionDTO>) respuesta.getResultado("ParametrosAplicacion");
        }
        cargarTabla(parametros);
    }
    
    public void cargarTabla(ArrayList<ParametroAplicacionDTO> parametros){
        tvParametros.getColumns().clear();
        if(!parametros.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(parametros);   
            TableColumn <ParametroAplicacionDTO, String>colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn <ParametroAplicacionDTO, String>colValor = new TableColumn("Valor");
            colValor.setCellValueFactory(new PropertyValueFactory("valor"));
            TableColumn<ParametroAplicacionDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(par -> {
                String estadoString;
                if(par.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
 
            tvParametros.getColumns().addAll(colNombre);
            tvParametros.getColumns().addAll(colValor);
            tvParametros.getColumns().addAll(colEstado);
            addButtonToTable();
            tvParametros.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<ParametroAplicacionDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<ParametroAplicacionDTO, Void>, TableCell<ParametroAplicacionDTO, Void>> cellFactory = new Callback<TableColumn<ParametroAplicacionDTO, Void>, TableCell<ParametroAplicacionDTO, Void>>() {
            @Override
            public TableCell<ParametroAplicacionDTO, Void> call(final TableColumn<ParametroAplicacionDTO, Void> param) {
                final TableCell<ParametroAplicacionDTO, Void> cell = new TableCell<ParametroAplicacionDTO, Void>() {
                   
                    
                    private final Button btn = new Button("Editar");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            ParametroAplicacionDTO data = getTableView().getItems().get(getIndex());
                            ver(data);
                            }catch(Exception ex){}
                        });
                    }                 
                    HBox pane = new HBox(btn);
                    {
                        pane.setAlignment(Pos.CENTER);
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);       
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        tvParametros.getColumns().add(colBtn);
    }
    
    private ParametroAplicacionDTO parametroEnCuestion = new ParametroAplicacionDTO();
    
    public void ver(ParametroAplicacionDTO parametro){
        lbTituloDetalle.setText("Modificar parámetro");
        btnGuardar.setText("Modificar");
        modalidad="Modificar";
        
        parametroEnCuestion=parametro;
        txtNombre.setText(parametroEnCuestion.getNombre());
        if(parametroEnCuestion.getDescripcion()!=null){
            txtDescripcion.setText(parametroEnCuestion.getDescripcion());
        }
        txtValor.setText(parametroEnCuestion.getValor());
        estadoGuardar=parametroEnCuestion.getEstado();
        if(parametroEnCuestion.getEstado()){
            cbxEstado.setValue("Activo");
        }else{
            cbxEstado.setValue("Inactivo");
        }
        lbFechaRegistro.setText("Fecha de registro: "+formatter.format(parametroEnCuestion.getFechaRegistro()));
        lbFechaModificacion.setText("Fecha de modificación: "+formatter.format(parametroEnCuestion.getFechaModificacion()));
    }
    
    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            parametroEnCuestion.setNombre(txtNombre.getText());
            parametroEnCuestion.setValor(txtValor.getText());
            parametroEnCuestion.setEstado(estadoGuardar);
            if(txtDescripcion.getText().isBlank()){
                if(modalidad.equals("Modificar")){
                    parametroEnCuestion.setDescripcion("");
                }
            }else{
                parametroEnCuestion.setDescripcion(txtDescripcion.getText());
            }
            
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=parametroService.modificar(parametroEnCuestion.getId(), parametroEnCuestion);
                if(respuesta.getEstado()){
                    cargarTodos();
                    GenerarTransacciones.crearTransaccion("Se modifica parámetro con id "+parametroEnCuestion.getId(), "Parámetros");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de parámetro", "Se ha modificado el parámetro correctamente");
                    limpiar();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de parámetro", respuesta.getMensaje());
                }
            }else{
                Respuesta respuesta=parametroService.crear(parametroEnCuestion);
                if(respuesta.getEstado()){
                    cargarTodos();
                    parametroEnCuestion=(ParametroAplicacionDTO) respuesta.getResultado("ParametroAplicacion");
                    GenerarTransacciones.crearTransaccion("Se crea parámetro con id "+parametroEnCuestion.getId(), "Parámetros");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de parámetro", "Se ha registrado el parámetro correctamente");
                    limpiar();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de parámetro", respuesta.getMensaje());
                }
            }
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actLimpiar(ActionEvent event) {
        limpiar();
    }
    public void limpiar(){
        btnGuardar.setText("Agregar");
        lbTituloDetalle.setText("Agregar parámetro");
        modalidad="Agregar";
        txtNombre.setText("");
        txtValor.setText("");
        estadoGuardar = null;
        txtDescripcion.setText("");
        lbFechaRegistro.setText("");
        lbFechaModificacion.setText("");
        parametroEnCuestion =  new ParametroAplicacionDTO();
    }
    
    public boolean validar(){
        if(txtNombre.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del parámetro");
            return false;
        }
        if(txtValor.getText().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el valor del parámetro");
            return false;
        }
        if(estadoGuardar==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del parámetro");
            return false;
        }
        
        return true;
    }
}
