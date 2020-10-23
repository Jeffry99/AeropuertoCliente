/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.RutaDTO;
import org.una.aeropuerto.cliente.dto.VueloDTO;
import org.una.aeropuerto.cliente.service.AerolineaService;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.RutaService;
import org.una.aeropuerto.cliente.service.VueloService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class VuelosController implements Initializable {

    @FXML
    private TableView<VueloDTO> tvVuelos;
    @FXML
    private Button btnVolver;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnBuscarAvion;
    @FXML
    private Button btnAgregar;
    @FXML
    private ComboBox<RutaDTO> cbRuta;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private Button btnBuscarFecha;
    @FXML
    private ComboBox<AvionDTO> cbAvion;
    @FXML
    private DatePicker dpFecha;
    private VueloService vueloService = new VueloService();
    @FXML
    private Button btnBuscarRuta;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarTodos();
        initEstados();
        initAviones();
        initRutas();
    }    
    public void cargarTodos(){
        ArrayList<VueloDTO> vuelos = new ArrayList<VueloDTO>();
        Respuesta respuesta = vueloService.getAll();
        if(respuesta.getEstado().equals(true)){
            vuelos = (ArrayList<VueloDTO>) respuesta.getResultado("Vuelos");
            
        }
        cargarTabla(vuelos);
    }
    
    public void cargarTabla(ArrayList<VueloDTO> vuelos){
        tvVuelos.getColumns().clear();
        if(!vuelos.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(vuelos);   
            
            TableColumn <VueloDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            
            TableColumn <VueloDTO, String>colAvion = new TableColumn("Avión");
            colAvion.setCellValueFactory(av -> {
                String avion = av.getValue().getAvion().getMatricula();
                return new ReadOnlyStringWrapper(avion);
            });
            
            TableColumn <VueloDTO, String>colOrigen = new TableColumn("Origen");
            colOrigen.setCellValueFactory(av -> {
                String origen = av.getValue().getRuta().getOrigen();
                return new ReadOnlyStringWrapper(origen);
            });
            
            TableColumn <VueloDTO, String>colDestino = new TableColumn("Destino");
            colDestino.setCellValueFactory(av -> {
                String destino = av.getValue().getRuta().getDestino();
                return new ReadOnlyStringWrapper(destino);
            });
            
            TableColumn <VueloDTO, String>colFecha = new TableColumn("Fecha");
            colFecha.setCellValueFactory(new PropertyValueFactory("fecha"));
            
            TableColumn<VueloDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(av -> {
            String estadoString;
                if(av.getValue().isEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvVuelos.getColumns().addAll(colId);
            tvVuelos.getColumns().addAll(colOrigen);
            tvVuelos.getColumns().addAll(colDestino);
            tvVuelos.getColumns().addAll(colAvion);
            tvVuelos.getColumns().addAll(colFecha);
            tvVuelos.getColumns().addAll(colEstado);
            addButtonToTable();
            tvVuelos.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<VueloDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<VueloDTO, Void>, TableCell<VueloDTO, Void>> cellFactory = new Callback<TableColumn<VueloDTO, Void>, TableCell<VueloDTO, Void>>() {
            @Override
            public TableCell<VueloDTO, Void> call(final TableColumn<VueloDTO, Void> param) {
                final TableCell<VueloDTO, Void> cell = new TableCell<VueloDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            VueloDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            VueloDTO data = getTableView().getItems().get(getIndex());
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

        tvVuelos.getColumns().add(colBtn);

    }
    public void ver(VueloDTO vuelo){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadVuelo", "Ver");
        AppContext.getInstance().set("VueloEnCuestion", vuelo);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesVuelosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void modificar(VueloDTO vuelo){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadVuelo", "Modificar");
        AppContext.getInstance().set("VueloEnCuestion", vuelo);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesVuelosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    @FXML
    private void actVolver(ActionEvent event) {
        volver();
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

    @FXML
    private void actBorrar(ActionEvent event) {
        cbAvion.setValue(null);
        cbEstado.setValue(null);
        cbRuta.setValue(null);
        dpFecha.setValue(null);
    }

    @FXML
    private void actBuscarAvion(ActionEvent event) {
        if(validarBusquedas("Avion")){
            ArrayList<VueloDTO> vuelos = new ArrayList<VueloDTO>();
            Respuesta respuesta = vueloService.getByAvion(cbAvion.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                vuelos = (ArrayList<VueloDTO>) respuesta.getResultado("Vuelos");
            }
            cargarTabla(vuelos);
        }
    }

    @FXML
    private void actBuscarRuta(ActionEvent event) {
        if(validarBusquedas("Ruta")){
            ArrayList<VueloDTO> vuelos = new ArrayList<VueloDTO>();
            Respuesta respuesta = vueloService.getByRuta(cbRuta.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                vuelos = (ArrayList<VueloDTO>) respuesta.getResultado("Vuelos");
            }
            cargarTabla(vuelos);
        }
    }
    
    @FXML
    private void actBuscarEstado(ActionEvent event) {
        if(validarBusquedas("Estado")){
            ArrayList<VueloDTO> vuelos = new ArrayList<VueloDTO>();
            Respuesta respuesta = vueloService.getByEstado(estado);
            if(respuesta.getEstado().equals(true)){
                vuelos = (ArrayList<VueloDTO>) respuesta.getResultado("Vuelos");
            }
            cargarTabla(vuelos);
        }
    }

    @FXML
    private void actBuscarFecha(ActionEvent event) {
        /*if(validarBusquedas("Fecha")){
            ArrayList<VueloDTO> vuelos = new ArrayList<VueloDTO>();
            Respuesta respuesta = vueloService.getByFecha(dpFecha.getValue().);
            if(respuesta.getEstado().equals(true)){
                vuelos = (ArrayList<VueloDTO>) respuesta.getResultado("Vuelos");
            }
            cargarTabla(vuelos);
        }*/
    }
    
    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadVuelo", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesVuelosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actRuta(ActionEvent event) {
    }
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("AvionesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }
    
    public boolean validarBusquedas(String tipo){
        if(tipo.equals("Avion")){
            if(cbAvion.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja el avion del vuelo");
                return false;
            }
        }
        if(tipo.equals("Estado")){
            if(cbEstado.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja el estado de los vuelos");
                return false;
            }
        }
        if(tipo.equals("Ruta")){
            if(cbRuta.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja la ruta del vuelo");
                return false;
            }
        }
        if(tipo.equals("Fecha")){
            if(dpFecha.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja la fecha del vuelo");
                return false;
            }
        }
        return true;
    }

    public void initEstados(){
        ArrayList<String> estados = new ArrayList<String>();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);
        cbEstado.setItems(items);
    }
    
    public void initAviones(){
        AvionService avionService = new AvionService();
        ArrayList<AvionDTO> aviones;
        Respuesta respuesta = avionService.getByEstado(true);
        if(respuesta.getEstado()){
            aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
            ObservableList items = FXCollections.observableArrayList(aviones);
            cbAvion.setItems(items);
        }
    }
    
    public void initRutas(){
        RutaService rutaService = new RutaService();
        ArrayList<RutaDTO> rutas;
        Respuesta respuesta = rutaService.getByEstado(true);
        if(respuesta.getEstado()){
            rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Aviones");
            ObservableList items = FXCollections.observableArrayList(rutas);
            cbAvion.setItems(items);
        }
    }
    
    
    
    
}
