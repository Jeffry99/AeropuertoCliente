/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.HoraMarcajeDTO;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.HoraMarcajeService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class HoraMarcajeInformacionController implements Initializable {

    @FXML
    private Label txtNombre;
    @FXML
    private Label txtCedula;
    @FXML
    private RadioButton rbActivo;
    @FXML
    private RadioButton rbInactivo;
    @FXML
    private ComboBox<String> cbxTipo;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAgregar;
    @FXML
    private TableView<EmpleadoDTO> tvEmpleados;

    /**
     * Initializes the controller class.
     */
    private String modalidad;
    private EmpleadoDTO empleadoSeleccionado = new EmpleadoDTO();
    private HoraMarcajeDTO horaMarcajeEnCuestion = new HoraMarcajeDTO();
    private HoraMarcajeService horaMarcajeService = new HoraMarcajeService();
    @FXML
    private Label txtFechaRegistro;
    @FXML
    private Label lbSeleccionarEmpleado;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modalidad = (String) AppContext.getInstance().get("ModalidadHoraMarcaje");
        ArrayList tipos = new ArrayList();
        tipos.add("Entrada");
        tipos.add("Salida");
        ObservableList items3 = FXCollections.observableArrayList(tipos);   
        cbxTipo.setItems(items3);
        cargarEmpleados();
        
        
        if(modalidad.equals("Ver")||modalidad.equals("Modificar")){
            horaMarcajeEnCuestion = (HoraMarcajeDTO) AppContext.getInstance().get("HoraMarcajeEnCuestion");
            
            txtCedula.setText(horaMarcajeEnCuestion.getEmpleado().getCedula());
            txtNombre.setText(horaMarcajeEnCuestion.getEmpleado().getNombre());
            if(horaMarcajeEnCuestion.getTipo()==1){
                tipo=1;
                cbxTipo.setValue("Entrada");
            }else{
                tipo=2;
                cbxTipo.setValue("Salida");
            }
            if(horaMarcajeEnCuestion.getEstado()==true){
                estado=true;
                rbActivo.setSelected(true);
                rbInactivo.setSelected(false);
            }else{
                estado=false;
                rbActivo.setSelected(false);
                rbInactivo.setSelected(true);
            }
            txtFechaRegistro.setText(horaMarcajeEnCuestion.getFechaRegistro().toString());
            if(modalidad.equals("Ver")){
                GenerarTransacciones.crearTransaccion("Se observa hora de marcaje con id "+horaMarcajeEnCuestion.getId(), "HoraMarcajeInformacion");
                lbSeleccionarEmpleado.setVisible(false);
                tvEmpleados.setVisible(false);
                tvEmpleados.setDisable(true);
            }
        }
        
        
        
    }    

    private EmpleadoService empleadoService = new EmpleadoService();
    public void cargarEmpleados(){
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta = empleadoService.getByEstado(true);
        if(respuesta.getEstado().equals(true)){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
        }
        cargarTabla(empleados);
        
    }
    public void cargarTabla(ArrayList<EmpleadoDTO> empleados){
        tvEmpleados.getColumns().clear();
        if(!empleados.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(empleados);   
            
            TableColumn <EmpleadoDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <EmpleadoDTO, String>colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn <EmpleadoDTO, String>colCedula = new TableColumn("Cédula");
            colCedula.setCellValueFactory(new PropertyValueFactory("cedula"));
            tvEmpleados.getColumns().addAll(colId);
            tvEmpleados.getColumns().addAll(colNombre);
            tvEmpleados.getColumns().addAll(colCedula);
            addButtonToTable();
            tvEmpleados.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<EmpleadoDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<EmpleadoDTO, Void>, TableCell<EmpleadoDTO, Void>> cellFactory = new Callback<TableColumn<EmpleadoDTO, Void>, TableCell<EmpleadoDTO, Void>>() {
            @Override
            public TableCell<EmpleadoDTO, Void> call(final TableColumn<EmpleadoDTO, Void> param) {
                final TableCell<EmpleadoDTO, Void> cell = new TableCell<EmpleadoDTO, Void>() {

                    private final Button btn = new Button("Seleccionar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            EmpleadoDTO data = getTableView().getItems().get(getIndex());
                            setEmpleado(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    
                    HBox pane = new HBox(btn);

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

        tvEmpleados.getColumns().add(colBtn);

    }
    
    public void setEmpleado(EmpleadoDTO empleado){
        empleadoSeleccionado=empleado;
        txtNombre.setText(empleadoSeleccionado.getNombre());
        txtCedula.setText(empleadoSeleccionado.getCedula());
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

    
    Integer tipo;
    @FXML
    private void actSelTipo(ActionEvent event) {
        if(cbxTipo.getValue().equals("Entrada")){
            tipo=1;
        }else{
            tipo=2;
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }

    @FXML
    private void actGuardar(ActionEvent event) {
        if(validar()){
            horaMarcajeEnCuestion.setEmpleado(empleadoSeleccionado);
            horaMarcajeEnCuestion.setEstado(estado);
            horaMarcajeEnCuestion.setTipo(tipo);
            if(modalidad.equals("Modificar")){
                Respuesta respuesta=horaMarcajeService.modificar(horaMarcajeEnCuestion.getId(), horaMarcajeEnCuestion);
                if(respuesta.getEstado()){
                    GenerarTransacciones.crearTransaccion("Se modifica la hora de marcaje con id "+horaMarcajeEnCuestion.getId(), "HoraMarcajeInformacion");
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Modificación de hora de marcaje", "Se ha modificado la hora de marcaje correctamente");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Modificación de hora de marcaje", respuesta.getMensaje());
                } 
            }else{
                if(modalidad.equals("Agregar")){
                    Respuesta respuesta=horaMarcajeService.crear(horaMarcajeEnCuestion);
                    if(respuesta.getEstado()){
                        horaMarcajeEnCuestion=(HoraMarcajeDTO) respuesta.getResultado("HoraMarcaje");
                        GenerarTransacciones.crearTransaccion("Se crea la hora de marcaje con id "+horaMarcajeEnCuestion.getId(), "HoraMarcajeInformacion");
                        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de hora de marcaje", "Se ha registrado la hora de marcaje correctamente");
                        volver();
                    }else{
                        Mensaje.showAndWait(Alert.AlertType.ERROR, "Registro de hora de marcaje", respuesta.getMensaje());
                    }
                }
            }
        }
    }
    
    public boolean validar(){
        if(empleadoSeleccionado.getCedula().isBlank()){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el empleado");
            return false;
        }
        if(tipo==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el tipo de hora de marcaje");
            return false;
        }
        if(estado==null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de la hora de marcaje");
            return false;
        }
        return true;
    }
    
    public void volver(){
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("HoraMarcaje" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
}
