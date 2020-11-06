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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class AerolineasController implements Initializable {
    
    private AerolineaService aerolineaService = new AerolineaService();
    @FXML
    private TableView<AerolineaDTO> tvAerolineas;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBuscarId;
    @FXML
    private Button btnBuscarNom;
    @FXML
    private Button btnBuscarRes;
    @FXML
    private Button btnBuscarEst;
    @FXML
    private JFXTextField txtbuscarId;
    @FXML
    private JFXTextField txtbuscarNom;
    @FXML
    private JFXComboBox<String> cbxEstado;
    @FXML
    private JFXTextField txtbuscarRes;
    

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
        cbxEstado.setItems(items);
    }    
    
    public void cargarTodos(){
        ArrayList<AerolineaDTO> aerolineas = new ArrayList<AerolineaDTO>();
        Respuesta respuesta = aerolineaService.getAll();
        if(respuesta.getEstado().equals(true)){
            aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
        }
        cargarTabla(aerolineas);
    }
    
    public void cargarTabla(ArrayList<AerolineaDTO> aerolineas){
        tvAerolineas.getColumns().clear();
        if(!aerolineas.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(aerolineas);   
            
            TableColumn <AerolineaDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <AerolineaDTO, String>colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn <AerolineaDTO, String>colResponsable = new TableColumn("Responsable");
            colResponsable.setCellValueFactory(new PropertyValueFactory("responsable"));
            TableColumn<AerolineaDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(emp -> {
                String estadoString;
                if(emp.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvAerolineas.getColumns().addAll(colId);
            tvAerolineas.getColumns().addAll(colNombre);
            tvAerolineas.getColumns().addAll(colResponsable);
            tvAerolineas.getColumns().addAll(colEstado);
            addButtonToTable();
            tvAerolineas.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<AerolineaDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<AerolineaDTO, Void>, TableCell<AerolineaDTO, Void>> cellFactory = new Callback<TableColumn<AerolineaDTO, Void>, TableCell<AerolineaDTO, Void>>() {
            @Override
            public TableCell<AerolineaDTO, Void> call(final TableColumn<AerolineaDTO, Void> param) {
                final TableCell<AerolineaDTO, Void> cell = new TableCell<AerolineaDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            AerolineaDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            AerolineaDTO data = getTableView().getItems().get(getIndex());
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

        tvAerolineas.getColumns().add(colBtn);
    }
    
     public void modificar(AerolineaDTO aerolinea){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAerolinea", "Modificar");
        AppContext.getInstance().set("AerolineaEnCuestion", aerolinea);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AerolineasInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
        
    public void ver(AerolineaDTO aerolinea){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAerolinea", "Ver");
        AppContext.getInstance().set("AerolineaEnCuestion", aerolinea);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AerolineasInformacion" + ".fxml"));
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
    private void actBuscarEst(ActionEvent event) {
        if(estadoBuscar!=null){
            ArrayList<AerolineaDTO> aerolineas = new ArrayList<AerolineaDTO>();
            Respuesta respuesta = aerolineaService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
            }
            cargarTabla(aerolineas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de la aerolinea que desea buscar");
        }
    }
    

    @FXML
    private void actBuscarNom(ActionEvent event) {
        if(!txtbuscarNom.getText().isBlank()){
            ArrayList<AerolineaDTO> aerolineas = new ArrayList<AerolineaDTO>();
            Respuesta respuesta = aerolineaService.getByNombreAproximate(txtbuscarNom.getText());
            if(respuesta.getEstado().equals(true)){
                aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
            }
            cargarTabla(aerolineas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre de la aerolinea que desea buscar");
        }
    }
    
    @FXML
    private void actBuscarId(ActionEvent event) {
     if(!txtbuscarId.getText().isBlank()){
            ArrayList<AerolineaDTO> aerolineas = new ArrayList<AerolineaDTO>();
            Respuesta respuesta = aerolineaService.getById(Long.valueOf(txtbuscarId.getText()));
            if(respuesta.getEstado().equals(true)){
                AerolineaDTO aero = (AerolineaDTO) respuesta.getResultado("Aerolineas");
                aerolineas.add(aero);
            }
            cargarTabla(aerolineas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del empleado que desea buscar");
        }
    }
    
    @FXML
    private void actBuscarRes(ActionEvent event) {
        if(!txtbuscarRes.getText().isBlank()){
            ArrayList<AerolineaDTO> aerolineas = new ArrayList<AerolineaDTO>();
            Respuesta respuesta = aerolineaService.getByResponsableAproximate(txtbuscarRes.getText());
            if(respuesta.getEstado().equals(true)){
                aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
            }
            cargarTabla(aerolineas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del responsable que desea buscar");
        }
    }

    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAerolinea", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AerolineasInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }




    




    
}
