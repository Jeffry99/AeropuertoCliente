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
import org.una.aeropuerto.cliente.service.AreaTrabajoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class AreasTrabajoController implements Initializable {

    @FXML
    private TableView<AreaTrabajoDTO> tvAreasTrabajo;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBuscarId;
    @FXML
    private TextField txtbuscarId;
    @FXML
    private TextField txtbuscarNombre;
    @FXML
    private ComboBox<String> cbxEstado;
    @FXML
    private Button btnBuscarDescripcion;
    @FXML
    private Button btnBuscarEst;
   
    private AreaTrabajoService AreaTrabajoService = new AreaTrabajoService();
    @FXML
    private TextField txtbuscarDescripcion;
    @FXML
    private Button btnBuscarNombre;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTodos();
        
        ArrayList estados = new ArrayList();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);   
        cbxEstado.setItems(items);
    }    
    public void cargarTodos(){
        ArrayList<AreaTrabajoDTO> AreasTrabajo = new ArrayList<AreaTrabajoDTO>();
        Respuesta respuesta = AreaTrabajoService.getAll();
        if(respuesta.getEstado().equals(true)){
            AreasTrabajo = (ArrayList<AreaTrabajoDTO>) respuesta.getResultado("AreasTrabajos");
        }
        cargarTabla(AreasTrabajo);
    }
    
     public void cargarTabla(ArrayList<AreaTrabajoDTO> aerolineas){
        tvAreasTrabajo.getColumns().clear();
        if(!aerolineas.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(aerolineas);   
            
            TableColumn <AreaTrabajoDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <AreaTrabajoDTO, String>colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn <AreaTrabajoDTO, String>colDescripcion = new TableColumn("Descripcion");
            colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
            TableColumn<AreaTrabajoDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(emp -> {
                String estadoString;
                if(emp.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvAreasTrabajo.getColumns().addAll(colId);
            tvAreasTrabajo.getColumns().addAll(colNombre);
            tvAreasTrabajo.getColumns().addAll(colDescripcion);
            tvAreasTrabajo.getColumns().addAll(colEstado);
            addButtonToTable();
            tvAreasTrabajo.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<AreaTrabajoDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<AreaTrabajoDTO, Void>, TableCell<AreaTrabajoDTO, Void>> cellFactory = new Callback<TableColumn<AreaTrabajoDTO, Void>, TableCell<AreaTrabajoDTO, Void>>() {
            @Override
            public TableCell<AreaTrabajoDTO, Void> call(final TableColumn<AreaTrabajoDTO, Void> param) {
                final TableCell<AreaTrabajoDTO, Void> cell = new TableCell<AreaTrabajoDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            AreaTrabajoDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            AreaTrabajoDTO data = getTableView().getItems().get(getIndex());
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

        tvAreasTrabajo.getColumns().add(colBtn);
    }
    
         public void modificar(AreaTrabajoDTO areaTrabajo){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAreaTrabajo", "Modificar");
        AppContext.getInstance().set("AreaTrabajoEnCuestion", areaTrabajo);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AreasTrabajosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
        
    public void ver(AreaTrabajoDTO areaTrabajo){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAreaTrabajo", "Ver");
        AppContext.getInstance().set("AreaTrabajoEnCuestion", areaTrabajo);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AreasTrabajosInformacion" + ".fxml"));
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
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAreaTrabajo", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AreasTrabajosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actBuscarId(ActionEvent event) {
     if(!txtbuscarId.getText().isBlank()){
            ArrayList<AreaTrabajoDTO> aerolineas = new ArrayList<AreaTrabajoDTO>();
            Respuesta respuesta = AreaTrabajoService.getById(Long.valueOf(txtbuscarId.getText()));
            if(respuesta.getEstado().equals(true)){
                AreaTrabajoDTO area = (AreaTrabajoDTO) respuesta.getResultado("AreaTrabajo");
                aerolineas.add(area);
            }
            cargarTabla(aerolineas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del area de trabajo que desea buscar");
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
    private void actBuscarDescripcion(ActionEvent event) {
        if(!txtbuscarDescripcion.getText().isBlank()){
            ArrayList<AreaTrabajoDTO> aerolineas = new ArrayList<AreaTrabajoDTO>();
            Respuesta respuesta = AreaTrabajoService.getByDescripcionAproximate(txtbuscarDescripcion.getText());
            if(respuesta.getEstado().equals(true)){
                aerolineas = (ArrayList<AreaTrabajoDTO>) respuesta.getResultado("AreasTrabajos");
            }
            cargarTabla(aerolineas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la descripcion de area de trabajo que desea buscar");
        }
    }

    @FXML
    private void actBuscarEst(ActionEvent event) {
        if(estadoBuscar!=null){
            ArrayList<AreaTrabajoDTO> aerolineas = new ArrayList<AreaTrabajoDTO>();
            Respuesta respuesta = AreaTrabajoService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                aerolineas = (ArrayList<AreaTrabajoDTO>) respuesta.getResultado("AreasTrabajos");
            }
            cargarTabla(aerolineas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del area de trabajo que desea buscar");
        }
    }

    @FXML
    private void actBuscarNombre(ActionEvent event) {
        if(!txtbuscarNombre.getText().isBlank()){
            ArrayList<AreaTrabajoDTO> aerolineas = new ArrayList<AreaTrabajoDTO>();
            Respuesta respuesta = AreaTrabajoService.getByNombreAproximate(txtbuscarNombre.getText());
            if(respuesta.getEstado().equals(true)){
                aerolineas = (ArrayList<AreaTrabajoDTO>) respuesta.getResultado("AreasTrabajos");
            }
            cargarTabla(aerolineas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del area de trabajo que desea buscar");
        }
    }
    
}
