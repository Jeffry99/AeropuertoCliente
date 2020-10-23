package org.una.aeropuerto.cliente.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import org.una.aeropuerto.cliente.dto.RutaDTO;
import org.una.aeropuerto.cliente.service.RutaService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class RutasController implements Initializable {

    @FXML
    private TableView<RutaDTO> tvRutas;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnBuscarId;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtbuscarId;
    @FXML
    private TextField txtbuscarDist;
    @FXML
    private TextField txtbuscarOri;
    @FXML
    private ComboBox<String> cbxEstado;
    @FXML
    private Button btnBuscarDist;
    @FXML
    private Button btnBuscarOri;
    @FXML
    private Button btnBuscarEst;
    @FXML
    private TextField txtbuscarDes;
    @FXML
    private Button btnBuscarDes;

    private RutaService rutaService = new RutaService();

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
        ArrayList<RutaDTO> rutas = new ArrayList<RutaDTO>();
        Respuesta respuesta = rutaService.getAll();
        if(respuesta.getEstado().equals(true)){
            rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Rutas");
        }
        cargarTabla(rutas);
    }
     public void cargarTabla(ArrayList<RutaDTO> aerolineas){
        tvRutas.getColumns().clear();
        if(!aerolineas.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(aerolineas);   
            
            TableColumn <RutaDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <RutaDTO, String>colDistancia = new TableColumn("Distancia");
            colDistancia.setCellValueFactory(new PropertyValueFactory("distancia"));
            TableColumn <RutaDTO, String>colOrigen = new TableColumn("Origen");
            colOrigen.setCellValueFactory(new PropertyValueFactory("origen"));
            TableColumn <RutaDTO, String>colDestino = new TableColumn("Destino");
            colDestino.setCellValueFactory(new PropertyValueFactory("destino"));
            TableColumn<RutaDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(emp -> {
                String estadoString;
                if(emp.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvRutas.getColumns().addAll(colId);
            tvRutas.getColumns().addAll(colDistancia);
            tvRutas.getColumns().addAll(colOrigen);
            tvRutas.getColumns().addAll(colDestino);
            tvRutas.getColumns().addAll(colEstado);
            addButtonToTable();
            tvRutas.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<RutaDTO, Void> colBtn = new TableColumn("Acciones");

    Callback<TableColumn<RutaDTO, Void>, TableCell<RutaDTO, Void>> cellFactory = new Callback<TableColumn<RutaDTO, Void>, TableCell<RutaDTO, Void>>() {
          @Override
            public TableCell<RutaDTO, Void> call(final TableColumn<RutaDTO, Void> param) {
                final TableCell<RutaDTO, Void> cell = new TableCell<RutaDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            RutaDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            RutaDTO data = getTableView().getItems().get(getIndex());
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

        tvRutas.getColumns().add(colBtn);
    }
    
    
    public void modificar(RutaDTO aerolinea){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadRuta", "Modificar");
        AppContext.getInstance().set("RutaEnCuestion", aerolinea);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("RutasInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
        
    public void ver(RutaDTO aerolinea){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadRuta", "Ver");
        AppContext.getInstance().set("RutaEnCuestion", aerolinea);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("RutasInformacion" + ".fxml"));
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
            ArrayList<RutaDTO> rutas = new ArrayList<RutaDTO>();
            Respuesta respuesta = rutaService.getById(Long.valueOf(txtbuscarId.getText()));
            if(respuesta.getEstado().equals(true)){
                RutaDTO aero = (RutaDTO) respuesta.getResultado("Rutas");
                rutas.add(aero);
            }
            cargarTabla(rutas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id de la ruta que desea buscar");
        }
    }

    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadRuta", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("RutasInformacion" + ".fxml"));
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
    private void actBuscarDist(ActionEvent event) {
        if(!txtbuscarDist.getText().isBlank()){
            ArrayList<RutaDTO> rutas = new ArrayList<RutaDTO>();
            Respuesta respuesta = rutaService.getByDistanciaRango(Float.parseFloat(txtbuscarDist.getText()));
            if(respuesta.getEstado().equals(true)){
                rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Rutas");
            }
            cargarTabla(rutas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la distancia de la ruta que desea buscar");
        }
    }

    @FXML
    private void actBuscarOri(ActionEvent event) {
        if(!txtbuscarOri.getText().isBlank()){
            ArrayList<RutaDTO> rutas = new ArrayList<RutaDTO>();
            Respuesta respuesta = rutaService.getByOrigenAproximate(txtbuscarOri.getText());
            if(respuesta.getEstado().equals(true)){
                rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Rutas");
            }
            cargarTabla(rutas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el origen de la ruta que desea buscar");
        }
    }

    @FXML
    private void actBuscarEst(ActionEvent event) {
        if(estadoBuscar!=null){
            ArrayList<RutaDTO> rutas = new ArrayList<RutaDTO>();
            Respuesta respuesta = rutaService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Rutas");
            }
            cargarTabla(rutas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de la ruta que desea buscar");
        }
    }

    @FXML
    private void actBuscarDes(ActionEvent event) {
       if(!txtbuscarDes.getText().isBlank()){
            ArrayList<RutaDTO> rutas = new ArrayList<RutaDTO>();
            Respuesta respuesta = rutaService.getByDestinoAproximate(txtbuscarDes.getText());
            if(respuesta.getEstado().equals(true)){
                rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Rutas");
            }
            cargarTabla(rutas);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el destino de la ruta que desea buscar");
        } 
    }
    
}
