/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import org.una.aeropuerto.cliente.dto.HoraMarcajeDTO;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.HoraMarcajeService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class HoraMarcajeController implements Initializable {

    @FXML
    private TableView<HoraMarcajeDTO> tvHorasMarcaje;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnBuscarId;
    @FXML
    private ComboBox<String> cbxEstado;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private ComboBox<EmpleadoDTO> cbxEmpleado;
    @FXML
    private Button btnBuscarEmpleado;
    @FXML
    private ComboBox<String> cbxTipo;
    @FXML
    private Button btnBuscarTipo;

    /**
     * Initializes the controller class.
     */
    private EmpleadoService empleadoService = new EmpleadoService();
    private HoraMarcajeService horaService = new HoraMarcajeService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList estados = new ArrayList();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);   
        cbxEstado.setItems(items);
        
        
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta = empleadoService.getAll();
        if(respuesta.getEstado()==true){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
        }
        ObservableList items2 = FXCollections.observableArrayList(empleados);   
        cbxEmpleado.setItems(items2);
        
        
        ArrayList tipos = new ArrayList();
        tipos.add("Entrada");
        tipos.add("Salida");
        ObservableList items3 = FXCollections.observableArrayList(tipos);   
        cbxTipo.setItems(items3);
        
        cargarTodos();
    }    

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadHoraMarcaje", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("HoraMarcajeInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actBuscarId(ActionEvent event) {
        if(!txtId.getText().isBlank()){
            ArrayList<HoraMarcajeDTO> horasMarcaje = new ArrayList<HoraMarcajeDTO>();
            Respuesta respuesta = horaService.getById(Long.valueOf(txtId.getText()));
            if(respuesta.getEstado().equals(true)){
                HoraMarcajeDTO hora = (HoraMarcajeDTO) respuesta.getResultado("HoraMarcaje");
                horasMarcaje.add(hora);
            }
            cargarTabla(horasMarcaje);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el id de la hora de marcaje que desea buscar");
        }
    }

    
    private Boolean estadoBuscar;
    @FXML
    private void actSelEstado(ActionEvent event) {
        if(cbxEstado.getValue().equals("Activo")){
            estadoBuscar=true;
        }else{
            estadoBuscar=false;
        }
    }

    @FXML
    private void actBuscarEstado(ActionEvent event) {
        if(estadoBuscar!=null){
            ArrayList<HoraMarcajeDTO> horasMarcaje = new ArrayList<HoraMarcajeDTO>();
            Respuesta respuesta = horaService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                horasMarcaje = (ArrayList<HoraMarcajeDTO>) respuesta.getResultado("HorasMarcajes");
            }
            cargarTabla(horasMarcaje);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de la hora de marcaje que desea buscar");
        }
    }

    @FXML
    private void actSelEmpleado(ActionEvent event) {
    }

    @FXML
    private void actBuscarEmpleado(ActionEvent event) {
        if(cbxEmpleado.getValue()!=null){
            ArrayList<HoraMarcajeDTO> horasMarcaje = new ArrayList<HoraMarcajeDTO>();
            Respuesta respuesta = horaService.getByEmpleado(cbxEmpleado.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                horasMarcaje = (ArrayList<HoraMarcajeDTO>) respuesta.getResultado("HorasMarcajes");
            }
            cargarTabla(horasMarcaje);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el empleado de la hora de marcaje que desea buscar");
        }
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
    private void actBuscarTipo(ActionEvent event) {
        if(tipo!=null){
            ArrayList<HoraMarcajeDTO> horasMarcaje = new ArrayList<HoraMarcajeDTO>();
            Respuesta respuesta = horaService.getByTipo(tipo);
            if(respuesta.getEstado().equals(true)){
                horasMarcaje = (ArrayList<HoraMarcajeDTO>) respuesta.getResultado("HorasMarcajes");
            }
            cargarTabla(horasMarcaje);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el tipo de la hora de marcaje que desea buscar");
        }
    }
    
    public void cargarTodos(){
        ArrayList<HoraMarcajeDTO> horasMarcaje = new ArrayList<HoraMarcajeDTO>();
        Respuesta respuesta = horaService.getAll();
        if(respuesta.getEstado().equals(true)){
            horasMarcaje = (ArrayList<HoraMarcajeDTO>) respuesta.getResultado("HorasMarcajes");
        }
        cargarTabla(horasMarcaje);
    }
    
    public void cargarTabla(ArrayList<HoraMarcajeDTO> horasMarcajes){
        tvHorasMarcaje.getColumns().clear();
        if(!horasMarcajes.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(horasMarcajes);   
            
            TableColumn <HoraMarcajeDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn<HoraMarcajeDTO, String> colEmpleado = new TableColumn("Empleado");
            colEmpleado.setCellValueFactory(emp -> {
                String string;
                string = emp.getValue().getEmpleado().getNombre();
                return new ReadOnlyStringWrapper(string);
            });
            TableColumn<HoraMarcajeDTO, String> colTipo = new TableColumn("Tipo");
            colTipo.setCellValueFactory(emp -> {
                String string;
                if(emp.getValue().getTipo()==1)
                    string = "Entrada";
                else
                    string = "Salida";
                return new ReadOnlyStringWrapper(string);
            });
            TableColumn<HoraMarcajeDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(emp -> {
                String estadoString;
                if(emp.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            TableColumn <HoraMarcajeDTO, Date>colFecha = new TableColumn("Fecha");
            colFecha.setCellValueFactory(new PropertyValueFactory("fechaRegistro"));
            tvHorasMarcaje.getColumns().addAll(colId);
            tvHorasMarcaje.getColumns().addAll(colEmpleado);
            tvHorasMarcaje.getColumns().addAll(colTipo);
            tvHorasMarcaje.getColumns().addAll(colEstado);
            tvHorasMarcaje.getColumns().addAll(colFecha);
            addButtonToTable();
            tvHorasMarcaje.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<HoraMarcajeDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<HoraMarcajeDTO, Void>, TableCell<HoraMarcajeDTO, Void>> cellFactory = new Callback<TableColumn<HoraMarcajeDTO, Void>, TableCell<HoraMarcajeDTO, Void>>() {
            @Override
            public TableCell<HoraMarcajeDTO, Void> call(final TableColumn<HoraMarcajeDTO, Void> param) {
                final TableCell<HoraMarcajeDTO, Void> cell = new TableCell<HoraMarcajeDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            HoraMarcajeDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            HoraMarcajeDTO data = getTableView().getItems().get(getIndex());
                            ver(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    HBox pane = new HBox(btn, btn2);

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

        tvHorasMarcaje.getColumns().add(colBtn);

    }
    
    public void ver(HoraMarcajeDTO hora){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadHoraMarcaje", "Ver");
        AppContext.getInstance().set("HoraMarcajeEnCuestion", hora);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("HoraMarcajeInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void modificar(HoraMarcajeDTO hora){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadHoraMarcaje", "Modificar");
        AppContext.getInstance().set("HoraMarcajeEnCuestion", hora);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("HoraMarcajeInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
}
