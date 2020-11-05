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
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.TipoAvionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class AvionesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private AvionService avionService = new AvionService();
    @FXML
    private TableView<AvionDTO> tvAviones;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnAgregar;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private Button btnBuscarMatricula;
    @FXML
    private TextField txtMatricula;
    @FXML
    private ComboBox<AerolineaDTO> cbAerolinea;
    @FXML
    private ComboBox<TipoAvionDTO> cbTipoAvion;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private Button btnBuscarTipoAvion;
    @FXML
    private Button btnBuscarAerolinea;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarTodos();
        initAerolineas();
        initTipoAvion();
        initEstados();
    }    
    public void cargarTodos(){
        ArrayList<AvionDTO> aviones = new ArrayList<AvionDTO>();
        Respuesta respuesta = avionService.getAll();
        if(respuesta.getEstado().equals(true)){
            aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            
        }
        cargarTabla(aviones);
    }
    
    public void cargarTabla(ArrayList<AvionDTO> aviones){
        tvAviones.getColumns().clear();
        if(!aviones.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(aviones);   
            
            TableColumn <AvionDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <AvionDTO, String>colMatricula = new TableColumn("Matricula");
            colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
            TableColumn <AvionDTO, String>colAerolinea = new TableColumn("Aerolinea");
            colAerolinea.setCellValueFactory(av -> {
                String aerolinea = av.getValue().getAerolinea().getNombre();
                return new ReadOnlyStringWrapper(aerolinea);
            });
            TableColumn<AvionDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(av -> {
            String estadoString;
                if(av.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvAviones.getColumns().addAll(colId);
            tvAviones.getColumns().addAll(colMatricula);
            tvAviones.getColumns().addAll(colAerolinea);
            tvAviones.getColumns().addAll(colEstado);
            addButtonToTable();
            tvAviones.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<AvionDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<AvionDTO, Void>, TableCell<AvionDTO, Void>> cellFactory = new Callback<TableColumn<AvionDTO, Void>, TableCell<AvionDTO, Void>>() {
            @Override
            public TableCell<AvionDTO, Void> call(final TableColumn<AvionDTO, Void> param) {
                final TableCell<AvionDTO, Void> cell = new TableCell<AvionDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            AvionDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            AvionDTO data = getTableView().getItems().get(getIndex());
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

        tvAviones.getColumns().add(colBtn);

    }
    public void ver(AvionDTO avion){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAvion", "Ver");
        AppContext.getInstance().set("AvionEnCuestion", avion);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void modificar(AvionDTO avion){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAvion", "Modificar");
        AppContext.getInstance().set("AvionEnCuestion", avion);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actVolver(ActionEvent event) {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("Empleados" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actBorrar(ActionEvent event) {
        txtMatricula.setText("");
        cbEstado.setValue(null);
        cbTipoAvion.setValue(null);
        cbAerolinea.setValue(null);
        cargarTodos();
    }


    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadAvion", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actBuscarMatricula(ActionEvent event) {
        if(validarBusquedas("Matricula")){
            ArrayList<AvionDTO> aviones = new ArrayList<AvionDTO>();
            Respuesta respuesta = avionService.getByMatriculaAproximate(txtMatricula.getText());
            if(respuesta.getEstado().equals(true)){
                aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            }
            cargarTabla(aviones);
        }
    }

    @FXML
    private void actBuscarEstado(ActionEvent event) {
        if(validarBusquedas("Estado")){
            ArrayList<AvionDTO> aviones = new ArrayList<AvionDTO>();
            Respuesta respuesta = avionService.getByEstado(estado);
            if(respuesta.getEstado().equals(true)){
                aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            }
            cargarTabla(aviones);
        }
    }

    @FXML
    private void actBuscarTipoAvion(ActionEvent event) {
        if(validarBusquedas("TipoAvion")){
            ArrayList<AvionDTO> aviones = new ArrayList<AvionDTO>();
            Respuesta respuesta = avionService.getByTipoAvion(cbTipoAvion.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            }
            cargarTabla(aviones);
        }
    }

    @FXML
    private void actBuscarAerolinea(ActionEvent event) {
        if(validarBusquedas("Aerolinea")){
            ArrayList<AvionDTO> aviones = new ArrayList<AvionDTO>();
            Respuesta respuesta = avionService.getByAerolinea(cbAerolinea.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            }
            cargarTabla(aviones);
        }
    }
    
    public void initAerolineas(){
        AerolineaService aerolineaService = new AerolineaService();
        ArrayList<AerolineaDTO> aerolineas;
        Respuesta respuesta = aerolineaService.getByEstado(true);
        if(respuesta.getEstado()){
            aerolineas = (ArrayList<AerolineaDTO>) respuesta.getResultado("Aerolineas");
            ObservableList items = FXCollections.observableArrayList(aerolineas);
            cbAerolinea.setItems(items);
        }
    }
    
    public void initTipoAvion(){
        TipoAvionService tipoAvionService = new TipoAvionService();
        ArrayList<TipoAvionDTO> tiposAvion;
        Respuesta respuesta = tipoAvionService.getByEstado(true);
        if(respuesta.getEstado()){
            tiposAvion = (ArrayList<TipoAvionDTO>) respuesta.getResultado("TiposAviones");
            ObservableList items = FXCollections.observableArrayList(tiposAvion);
            cbTipoAvion.setItems(items);
        }
    }
    public void initEstados(){
        ArrayList<String> estados = new ArrayList<String>();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);
        cbEstado.setItems(items);
    }

    private boolean estado;
    @FXML
    private void actEstados(ActionEvent event) {
        if(cbEstado.getValue().equals("Activo")){
            estado = true;
        }else{
            if(cbEstado.getValue().equals("Inactivo")){
                estado = false;
            }
        }
    }
    
    public boolean validarBusquedas(String tipo){
        if(tipo.equals("Matricula")){
            if(txtMatricula.getText().isEmpty()){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escriba al menos una letra de la matricula del avión");
                return false;
            }
        }
        if(tipo.equals("Estado")){
            if(cbEstado.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja el estado de los aviones");
                return false;
            }
        }
        if(tipo.equals("Aerolinea")){
            if(cbAerolinea.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja la aerolinea de los aviones");
                return false;
            }
        }
        if(tipo.equals("TipoAvion")){
            if(cbTipoAvion.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja el tipo de avión");
                return false;
            }
        }
        return true;
    }
}
