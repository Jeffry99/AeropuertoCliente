/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.dto.TransaccionDTO;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.RolService;
import org.una.aeropuerto.cliente.service.TransaccionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Formato;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class TransaccionesController implements Initializable {

    @FXML
    private Button btnVolver;
    @FXML
    private TableView<TransaccionDTO> tvTransacciones;
    @FXML
    private Button btnBuscarRol;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnBuscarId;
    @FXML
    private Button btnBuscarLugar;
    @FXML
    private Button btnBuscarNombreUsuario;
    @FXML
    private Button btnBuscarDescripcion;
    @FXML
    private JFXTextField txtLugar;
    @FXML
    private JFXTextField txtNombreUsuario;
    @FXML
    private JFXTextField txtDescripcion;
    @FXML
    private JFXComboBox<RolDTO> cbxRol;

    /**
     * Initializes the controller class.
     */
    private TransaccionService transaccionService = new TransaccionService();
    private RolService rolService = new RolService();
    private EmpleadoService empleadoService = new EmpleadoService();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formato();
        ArrayList<RolDTO> roles = new ArrayList<RolDTO>();
        Respuesta respuesta = rolService.getAll();
        if(respuesta.getEstado()==true){
            roles = (ArrayList<RolDTO>) respuesta.getResultado("Roles");
            
        }
        
        ObservableList items = FXCollections.observableArrayList(roles);   
        cbxRol.setItems(items);
        
        cargarTodos();
        // TODO
    }    

    private void formato(){
        txtId.setTextFormatter(Formato.getInstance().integerFormat());
    }
    @FXML
    private void actVolver(ActionEvent event) {
    }

    
    
    @FXML
    private void actSelRol(ActionEvent event) {
    }

    @FXML
    private void actBuscarRol(ActionEvent event) {
        if(cbxRol.getValue()!=null){
            ArrayList<TransaccionDTO>  transacciones= new ArrayList<TransaccionDTO>();
            Respuesta respuesta = transaccionService.getByRolAproximate(cbxRol.getValue().getNombre());
            if(respuesta.getEstado().equals(true)){
                transacciones = (ArrayList<TransaccionDTO>) respuesta.getResultado("Transacciones");
            }
            cargarTabla(transacciones);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el rol con el que se ejecutó la transacción que desea buscar");
        }
    }

    @FXML
    private void actBuscarId(ActionEvent event) {
        if(!txtId.getText().isBlank()){
            ArrayList<TransaccionDTO> transacciones = new ArrayList<TransaccionDTO>();
            Respuesta respuesta = transaccionService.getById(Long.valueOf(txtId.getText()));
            if(respuesta.getEstado().equals(true)){
                TransaccionDTO tran = (TransaccionDTO) respuesta.getResultado("Transaccion");
                if(tran != null){
                    transacciones.add(tran);
                }
            }
            cargarTabla(transacciones);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id de la transacción que desea buscar");
        }
    }

    @FXML
    private void actBuscarLugar(ActionEvent event) {
        if(!txtLugar.getText().isBlank()){
            ArrayList<TransaccionDTO>  transacciones= new ArrayList<TransaccionDTO>();
            Respuesta respuesta = transaccionService.getByLugarAproximate(txtLugar.getText());
            if(respuesta.getEstado().equals(true)){
                transacciones = (ArrayList<TransaccionDTO>) respuesta.getResultado("Transacciones");
            }
            cargarTabla(transacciones);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el lugar donde se ejecutó la transacción que desea buscar");
        }
    }

    @FXML
    private void actBuscarNombreUsuario(ActionEvent event) {
        ArrayList<TransaccionDTO> transaccionesFiltradas = new ArrayList<TransaccionDTO>();
        if(!txtNombreUsuario.getText().isBlank()){
            ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
            Respuesta respuesta = empleadoService.getByNombreAproximate(txtNombreUsuario.getText());
            if(respuesta.getEstado()==true){
                empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
                ArrayList<TransaccionDTO> transacciones = new ArrayList<TransaccionDTO>();
                respuesta = transaccionService.getAll();
                if(respuesta.getEstado()==true){
                    transacciones = (ArrayList<TransaccionDTO>) respuesta.getResultado("Transacciones");
                    for(int i=0; i<transacciones.size(); i++){  
                        for(int j=0; j<empleados.size(); j++){
                            if(transacciones.get(i).getUsuario().getEmpleado().getId()==empleados.get(j).getId()){
                                transaccionesFiltradas.add(transacciones.get(i));
                            }
                        }
                    }
                }
                    
            }
             
            cargarTabla(transaccionesFiltradas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la nombre del usuario que realizó la transacción que desea buscar");
        }
        
        
        
    }

    @FXML
    private void actBuscarDescripcion(ActionEvent event) {
        if(!txtDescripcion.getText().isBlank()){
            ArrayList<TransaccionDTO>  transacciones= new ArrayList<TransaccionDTO>();
            Respuesta respuesta = transaccionService.getByDescripcionAproximate(txtDescripcion.getText());
            if(respuesta.getEstado().equals(true)){
                transacciones = (ArrayList<TransaccionDTO>) respuesta.getResultado("Transacciones");
            }
            cargarTabla(transacciones);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la descripción de la transacción que desea buscar");
        }
    }
    
    public void cargarTabla(ArrayList<TransaccionDTO> transacciones){
        tvTransacciones.getColumns().clear();
        if(!transacciones.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(transacciones);   
            
            TableColumn <TransaccionDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            /*TableColumn <TransaccionDTO, String>colDescripcion = new TableColumn("Descripcion");
            colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));*/
            TableColumn<TransaccionDTO, String> colUsuario = new TableColumn("Usuario");
            colUsuario.setCellValueFactory(tran -> {
                String nombreUsuario;
                nombreUsuario = tran.getValue().getUsuario().getEmpleado().getNombre();
                return new ReadOnlyStringWrapper(nombreUsuario);
            });
            TableColumn <TransaccionDTO, String>colRol = new TableColumn("Rol");
            colRol.setCellValueFactory(new PropertyValueFactory("rol"));
            TableColumn<TransaccionDTO, String> colFechaRegistro = new TableColumn("Fecha");
            colFechaRegistro.setCellValueFactory(hora -> {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String fecha = formatter.format(hora.getValue().getFechaRegistro());
                return new ReadOnlyStringWrapper(fecha);
            });
            TableColumn<TransaccionDTO, String> colHora = new TableColumn("Hora");
            colHora.setCellValueFactory(hora -> {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String fecha = formatter.format(hora.getValue().getFechaRegistro());
                return new ReadOnlyStringWrapper(fecha);
            });
            
            
            tvTransacciones.getColumns().addAll(colId);
            //tvTransacciones.getColumns().addAll(colDescripcion);
            tvTransacciones.getColumns().addAll(colUsuario);
            tvTransacciones.getColumns().addAll(colRol);
            tvTransacciones.getColumns().addAll(colFechaRegistro);
            tvTransacciones.getColumns().addAll(colHora);
            addButtonToTable();
            tvTransacciones.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<TransaccionDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<TransaccionDTO, Void>, TableCell<TransaccionDTO, Void>> cellFactory = new Callback<TableColumn<TransaccionDTO, Void>, TableCell<TransaccionDTO, Void>>() {
            @Override
            public TableCell<TransaccionDTO, Void> call(final TableColumn<TransaccionDTO, Void> param) {
                final TableCell<TransaccionDTO, Void> cell = new TableCell<TransaccionDTO, Void>() {

                    
                    private final Button btn = new Button("Ver");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            TransaccionDTO data = getTableView().getItems().get(getIndex());
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

        tvTransacciones.getColumns().add(colBtn);

    }
    
    public void cargarTodos(){
        ArrayList<TransaccionDTO> transacciones = new ArrayList<TransaccionDTO>();
        Respuesta respuesta = transaccionService.getAll();
        if(respuesta.getEstado().equals(true)){
            transacciones = (ArrayList<TransaccionDTO>) respuesta.getResultado("Transacciones");
        }
        cargarTabla(transacciones);
    }
    
    public void ver(TransaccionDTO transaccion){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadTransaccion", "Ver");
        AppContext.getInstance().set("TransaccionEnCuestion", transaccion);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TransaccionesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
}
