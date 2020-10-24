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
import org.una.aeropuerto.cliente.dto.AreaTrabajoDTO;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.TrabajoEmpleadoDTO;
import org.una.aeropuerto.cliente.service.AreaTrabajoService;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.TrabajoEmpleadoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class TrabajosEmpleadosController implements Initializable {
 
    private TrabajoEmpleadoService trabajoEmpleadoService = new TrabajoEmpleadoService();
    
    @FXML
    private TableView<TrabajoEmpleadoDTO> tvAreaTrabajo;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnBuscarId;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtbuscarId;
    @FXML
    private ComboBox<String> cbxEstado;
    @FXML
    private Button btnBuscarAreaTrabajo;
    @FXML
    private Button btnBuscarEmpleado;
    @FXML
    private Button btnBuscarEst;
    @FXML
    private ComboBox<EmpleadoDTO> cbxEmpleadoID;
    @FXML
    private ComboBox<AreaTrabajoDTO> cbxAreaID;

    private EmpleadoService empleadoService = new EmpleadoService();
    private AreaTrabajoService areaTrabajoService = new AreaTrabajoService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTodos();
        
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
        cbxEmpleadoID.setItems(items2);
        
        ArrayList<AreaTrabajoDTO> areasTrabajos = new ArrayList<AreaTrabajoDTO>();
        Respuesta respuesta1 = areaTrabajoService.getAll();
        if(respuesta1.getEstado()==true){
            areasTrabajos = (ArrayList<AreaTrabajoDTO>) respuesta1.getResultado("AreasTrabajos");
        }
        ObservableList items3 = FXCollections.observableArrayList(areasTrabajos);   
        cbxAreaID.setItems(items3);
    }    
 
    public void cargarTodos(){
        ArrayList<TrabajoEmpleadoDTO> trabajosEmpledos = new ArrayList<TrabajoEmpleadoDTO>();
        Respuesta respuesta = trabajoEmpleadoService.getAll();
        if(respuesta.getEstado().equals(true)){
            trabajosEmpledos = (ArrayList<TrabajoEmpleadoDTO>) respuesta.getResultado("TrabajosEmpleados");
        }
        cargarTabla(trabajosEmpledos);
    }
    
      public void cargarTabla(ArrayList<TrabajoEmpleadoDTO> trabajosEmpleados){
        tvAreaTrabajo.getColumns().clear();
        if(!trabajosEmpleados.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(trabajosEmpleados);   
            
            TableColumn <TrabajoEmpleadoDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <TrabajoEmpleadoDTO, String>colAreaTrabajo = new TableColumn("Area Trabajo");
            colAreaTrabajo.setCellValueFactory(new PropertyValueFactory("areaTrabajo"));
            TableColumn <TrabajoEmpleadoDTO, String>colEmpleado = new TableColumn("Empleado");
            colEmpleado.setCellValueFactory(new PropertyValueFactory("empleado"));
            TableColumn<TrabajoEmpleadoDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(emp -> {
                String estadoString;
                if(emp.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvAreaTrabajo.getColumns().addAll(colId);
            tvAreaTrabajo.getColumns().addAll(colAreaTrabajo);
            tvAreaTrabajo.getColumns().addAll(colEmpleado);
            tvAreaTrabajo.getColumns().addAll(colEstado);
            addButtonToTable();
            tvAreaTrabajo.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<TrabajoEmpleadoDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<TrabajoEmpleadoDTO, Void>, TableCell<TrabajoEmpleadoDTO, Void>> cellFactory = new Callback<TableColumn<TrabajoEmpleadoDTO, Void>, TableCell<TrabajoEmpleadoDTO, Void>>() {
            @Override
            public TableCell<TrabajoEmpleadoDTO, Void> call(final TableColumn<TrabajoEmpleadoDTO, Void> param) {
                final TableCell<TrabajoEmpleadoDTO, Void> cell = new TableCell<TrabajoEmpleadoDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            TrabajoEmpleadoDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            TrabajoEmpleadoDTO data = getTableView().getItems().get(getIndex());
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

        tvAreaTrabajo.getColumns().add(colBtn);
    }
    
     public void modificar(TrabajoEmpleadoDTO trabajoEmpleado){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadTrabajoEmpleado", "Modificar");
        AppContext.getInstance().set("TrabajoEmpleadoEnCuestion", trabajoEmpleado);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TrabajosEmpleadosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
        
    public void ver(TrabajoEmpleadoDTO trabajoEmpleado){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadTrabajoEmpleado", "Ver");
        AppContext.getInstance().set("TrabajoEmpleadoEnCuestion", trabajoEmpleado);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TrabajosEmpleadosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actBuscarId(ActionEvent event) {
    if(!txtbuscarId.getText().isBlank()){
            ArrayList<TrabajoEmpleadoDTO> trabajosEmpleados = new ArrayList<TrabajoEmpleadoDTO>();
            Respuesta respuesta = trabajoEmpleadoService.getById(Long.valueOf(txtbuscarId.getText()));
            if(respuesta.getEstado().equals(true)){
                TrabajoEmpleadoDTO trabajo = (TrabajoEmpleadoDTO) respuesta.getResultado("TrabajoEmpleado");
                trabajosEmpleados.add(trabajo);
            }
            cargarTabla(trabajosEmpleados);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del trabajo empleado que desea buscar");
        }
    }

    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadTrabajoEmpleado", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TrabajosEmpleadosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
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
    private void actBuscarAreaTrabajo(ActionEvent event) {
        if(cbxAreaID.getValue()!=null){
            ArrayList<TrabajoEmpleadoDTO> trabajosEmpleados = new ArrayList<TrabajoEmpleadoDTO>();
            Respuesta respuesta = trabajoEmpleadoService.getByAreaTrabajo(cbxAreaID.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                trabajosEmpleados = (ArrayList<TrabajoEmpleadoDTO>) respuesta.getResultado("TrabajosEmpleados");
            }
            cargarTabla(trabajosEmpleados);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del area de trabajo que desea buscar");
        }
    }

    @FXML
    private void actBuscarEmpleado(ActionEvent event) {
        if(cbxEmpleadoID.getValue()!=null){
            ArrayList<TrabajoEmpleadoDTO> trabajosEmpleados = new ArrayList<TrabajoEmpleadoDTO>();
            Respuesta respuesta = trabajoEmpleadoService.getByEmpleado(cbxEmpleadoID.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                trabajosEmpleados = (ArrayList<TrabajoEmpleadoDTO>) respuesta.getResultado("TrabajosEmpleados");
            }
            cargarTabla(trabajosEmpleados);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del empleado que desea buscar");
        }
    }

    @FXML
    private void actBuscarEst(ActionEvent event) {
        if(estadoBuscar!=null){
            ArrayList<TrabajoEmpleadoDTO> trabajosEmpleados = new ArrayList<TrabajoEmpleadoDTO>();
            Respuesta respuesta = trabajoEmpleadoService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                trabajosEmpleados = (ArrayList<TrabajoEmpleadoDTO>) respuesta.getResultado("TrabajosEmpleados");
            }
            cargarTabla(trabajosEmpleados);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del trabajo empleado que desea buscar");
        }
    }

    @FXML
    private void actSelEmpleadoID(ActionEvent event) {
    }

    @FXML
    private void actSelAreaID(ActionEvent event) {
    }
    
}
