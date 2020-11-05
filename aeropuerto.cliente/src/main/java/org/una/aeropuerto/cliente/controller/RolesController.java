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
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.service.RolService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class RolesController implements Initializable {

    @FXML
    private TableView<RolDTO> tvRoles;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnBuscarId;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private Button btnBuscarDescripcion;
    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnBuscarNombre;
    @FXML
    private ComboBox<String> cbxEstado;
    @FXML
    private Button btnBuscarEstado;

    /**
     * Initializes the controller class.
     */
    private RolService rolService = new RolService();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList estados = new ArrayList();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);   
        cbxEstado.setItems(items);
        
        cargarTodos();
    }    

    @FXML
    private void actVolver(ActionEvent event) {
    }


    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadRol", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("RolesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actBuscarId(ActionEvent event) {
        if(!txtId.getText().isBlank()){
            ArrayList<RolDTO> roles = new ArrayList<RolDTO>();
            Respuesta respuesta = rolService.getById(Long.valueOf(txtId.getText()));
            if(respuesta.getEstado().equals(true)){
                RolDTO rol = (RolDTO) respuesta.getResultado("Rol");
                roles.add(rol);
            }
            cargarTabla(roles);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del rol que desea buscar");
        }
    }

    @FXML
    private void actBuscarDescripcion(ActionEvent event) {
        if(!txtDescripcion.getText().isBlank()){
            ArrayList<RolDTO> roles = new ArrayList<RolDTO>();
            Respuesta respuesta = rolService.getByDescripcionAproximate(txtDescripcion.getText());
            if(respuesta.getEstado().equals(true)){
                roles = (ArrayList<RolDTO>) respuesta.getResultado("Roles");
            }
            cargarTabla(roles);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del rol que desea buscar");
        }
    }

    @FXML
    private void actBuscarNombre(ActionEvent event) {
        if(!txtNombre.getText().isBlank()){
            ArrayList<RolDTO> roles = new ArrayList<RolDTO>();
            Respuesta respuesta = rolService.getByNombreAproximate(txtNombre.getText());
            if(respuesta.getEstado().equals(true)){
                roles = (ArrayList<RolDTO>) respuesta.getResultado("Roles");
            }
            cargarTabla(roles);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del rol que desea buscar");
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
            ArrayList<RolDTO> roles = new ArrayList<RolDTO>();
            Respuesta respuesta = rolService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                roles = (ArrayList<RolDTO>) respuesta.getResultado("Roles");
            }
            cargarTabla(roles);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del rol que desea buscar");
        }
    }
    
    public void cargarTodos(){
        ArrayList<RolDTO> roles = new ArrayList<RolDTO>();
        Respuesta respuesta = rolService.getAll();
        if(respuesta.getEstado().equals(true)){
            roles = (ArrayList<RolDTO>) respuesta.getResultado("Roles");
        }
        cargarTabla(roles);
    }
    
    public void cargarTabla(ArrayList<RolDTO> roles){
        tvRoles.getColumns().clear();
        if(!roles.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(roles);   
            
            TableColumn <RolDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <RolDTO, String>colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn <RolDTO, String>colDescripcion = new TableColumn("Descripción");
            colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
            TableColumn<RolDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(rol -> {
                String estadoString;
                if(rol.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvRoles.getColumns().addAll(colId);
            tvRoles.getColumns().addAll(colNombre);
            tvRoles.getColumns().addAll(colDescripcion);
            tvRoles.getColumns().addAll(colEstado);
            addButtonToTable();
            tvRoles.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<RolDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<RolDTO, Void>, TableCell<RolDTO, Void>> cellFactory = new Callback<TableColumn<RolDTO, Void>, TableCell<RolDTO, Void>>() {
            @Override
            public TableCell<RolDTO, Void> call(final TableColumn<RolDTO, Void> param) {
                final TableCell<RolDTO, Void> cell = new TableCell<RolDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            RolDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            RolDTO data = getTableView().getItems().get(getIndex());
                            ver(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    HBox pane = new HBox(btn, btn2);
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

        tvRoles.getColumns().add(colBtn);

    }
    
    public void ver(RolDTO rol){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadRol", "Ver");
        AppContext.getInstance().set("RolEnCuestion", rol);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("RolesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void modificar(RolDTO rol){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadRol", "Modificar");
        AppContext.getInstance().set("RolEnCuestion", rol);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("RolesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
}
