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
import org.una.aeropuerto.cliente.dto.ServicioTipoDTO;
import org.una.aeropuerto.cliente.service.AreaTrabajoService;
import org.una.aeropuerto.cliente.service.ServicioTipoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class ServiciosTiposController implements Initializable {

    @FXML
    private TableView<ServicioTipoDTO> tvTipoServicios;
    @FXML
    private Button btnVolver;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnBuscarID;
    @FXML
    private Button btnAgregar;
    @FXML
    private ComboBox<AreaTrabajoDTO> cbAreaTrabajo;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private Button btnBuscarAreaTrabajo;
    @FXML
    private Button btnBuscarDescripcion;
    @FXML
    private TextField txtDescripcion;
        
    private ServicioTipoService servicioTipoService = new ServicioTipoService();
    
    private AreaTrabajoService areaTrabajoService = new AreaTrabajoService();
    @FXML
    private TextField txtBuscarID;
    @FXML
    private Button btnBuscarNombre;
    @FXML
    private TextField txtNombre;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTodos();
        
        ArrayList estados = new ArrayList();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);   
        cbEstado.setItems(items);
        
        ArrayList<AreaTrabajoDTO> areasTrabajos = new ArrayList<AreaTrabajoDTO>();
        Respuesta respuesta = areaTrabajoService.getAll();
        if(respuesta.getEstado()==true){
            areasTrabajos = (ArrayList<AreaTrabajoDTO>) respuesta.getResultado("AreasTrabajos");
        }
        ObservableList items2 = FXCollections.observableArrayList(areasTrabajos);   
        cbAreaTrabajo.setItems(items2);
    }    
    
      public void cargarTodos(){
        ArrayList<ServicioTipoDTO> tiposServicios = new ArrayList<ServicioTipoDTO>();
        Respuesta respuesta = servicioTipoService.getAll();
        if(respuesta.getEstado().equals(true)){
            tiposServicios = (ArrayList<ServicioTipoDTO>) respuesta.getResultado("TiposServicios");
        }
        cargarTabla(tiposServicios);
    }
      
    public void cargarTabla(ArrayList<ServicioTipoDTO> trabajosEmpleados){
        tvTipoServicios.getColumns().clear();
        if(!trabajosEmpleados.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(trabajosEmpleados);   
            
            TableColumn <ServicioTipoDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <ServicioTipoDTO, String>colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn <ServicioTipoDTO, String>colDescripcion = new TableColumn("Descripcion");
            colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
            TableColumn<ServicioTipoDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(emp -> {
                String estadoString;
                if(emp.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvTipoServicios.getColumns().addAll(colId);
            tvTipoServicios.getColumns().addAll(colNombre);
            tvTipoServicios.getColumns().addAll(colDescripcion);
            tvTipoServicios.getColumns().addAll(colEstado);
            addButtonToTable();
            tvTipoServicios.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<ServicioTipoDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<ServicioTipoDTO, Void>, TableCell<ServicioTipoDTO, Void>> cellFactory = new Callback<TableColumn<ServicioTipoDTO, Void>, TableCell<ServicioTipoDTO, Void>>() {
            @Override
            public TableCell<ServicioTipoDTO, Void> call(final TableColumn<ServicioTipoDTO, Void> param) {
                final TableCell<ServicioTipoDTO, Void> cell = new TableCell<ServicioTipoDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            ServicioTipoDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            ServicioTipoDTO data = getTableView().getItems().get(getIndex());
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

        tvTipoServicios.getColumns().add(colBtn);
    }
    
     public void modificar(ServicioTipoDTO servicioTipo){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadServicioTipo", "Modificar");
        AppContext.getInstance().set("ServicioTipoEnCuestion", servicioTipo);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("ServiciosTiposInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
        
    public void ver(ServicioTipoDTO servicioTipo){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadServicioTipo", "Ver");
        AppContext.getInstance().set("ServicioTipoEnCuestion", servicioTipo);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("ServiciosTiposInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }
    
    private Boolean estadoBuscar;
    @FXML
    private void actEstados(ActionEvent event) {
        if(cbEstado.getValue().equals("Activo")){
            estadoBuscar=true;
        }else{
            estadoBuscar=false;
        }
    }

    @FXML
    private void actBorrar(ActionEvent event) {
    }

    @FXML
    private void actBuscarID(ActionEvent event) {
           if(!txtBuscarID.getText().isBlank()){
            ArrayList<ServicioTipoDTO> serviciosTiposDTO = new ArrayList<ServicioTipoDTO>();
            Respuesta respuesta = servicioTipoService.getById(Long.valueOf(txtBuscarID.getText()));
            if(respuesta.getEstado().equals(true)){
                ServicioTipoDTO servicioTipo = (ServicioTipoDTO) respuesta.getResultado("TipoServicio");
                serviciosTiposDTO.add(servicioTipo);
            }
            cargarTabla(serviciosTiposDTO);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del tipo de servicio que desea buscar");
        }
    }

    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadServicioTipo", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("ServiciosTiposInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actAreaTrabajo(ActionEvent event) {
        
        
    }

    @FXML
    private void actBuscarEstado(ActionEvent event) {
         if(estadoBuscar!=null){
            ArrayList<ServicioTipoDTO> servicioTipo = new ArrayList<ServicioTipoDTO>();
            Respuesta respuesta = servicioTipoService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                servicioTipo = (ArrayList<ServicioTipoDTO>) respuesta.getResultado("TiposServicios");
            }
            cargarTabla(servicioTipo);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del tipo servicio que desea buscar");
        }
    }

    @FXML
    private void actBuscarAreaTrabajo(ActionEvent event) {
        if(cbAreaTrabajo.getValue()!=null){
            ArrayList<ServicioTipoDTO> trabajosEmpleados = new ArrayList<ServicioTipoDTO>();
            Respuesta respuesta = servicioTipoService.getByAreaTrabajo(cbAreaTrabajo.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                trabajosEmpleados = (ArrayList<ServicioTipoDTO>) respuesta.getResultado("TiposServicios");
            }
            cargarTabla(trabajosEmpleados);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del empleado que desea buscar");
        }
    }

    @FXML
    private void actBuscarDescripcion(ActionEvent event) {
        if(!txtDescripcion.getText().isBlank()){
            ArrayList<ServicioTipoDTO> tiposServices = new ArrayList<ServicioTipoDTO>();
            Respuesta respuesta = servicioTipoService.getByDescripcionAproximate(txtDescripcion.getText());
            if(respuesta.getEstado().equals(true)){
                tiposServices = (ArrayList<ServicioTipoDTO>) respuesta.getResultado("TiposServicios");
            }
            cargarTabla(tiposServices);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la descripcion del tipo de servicio que desea buscar");
        }
    }

    @FXML
    private void actBuscarNombre(ActionEvent event) {
        if(!txtNombre.getText().isBlank()){
            ArrayList<ServicioTipoDTO> tiposServices = new ArrayList<ServicioTipoDTO>();
            Respuesta respuesta = servicioTipoService.getByNombreAproximate(txtNombre.getText());
            if(respuesta.getEstado().equals(true)){
                tiposServices = (ArrayList<ServicioTipoDTO>) respuesta.getResultado("TiposServicios");
            }
            cargarTabla(tiposServices);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del tipo de servicio que desea buscar");
        }
    }
    
}
