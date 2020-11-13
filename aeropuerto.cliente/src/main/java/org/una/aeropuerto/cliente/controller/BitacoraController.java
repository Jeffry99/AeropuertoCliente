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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.BitacoraAvionDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.BitacoraAvionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class BitacoraController implements Initializable {

    @FXML
    private TableView<BitacoraAvionDTO> tvEstados;
    @FXML
    private Button btnVolver;

    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnBuscarDistancia;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private Button btnBuscarUbicacion;
    @FXML
    private Button btnBuscarAvion;
    

    BitacoraAvionService bitacoraService = new BitacoraAvionService();
    BitacoraAvionDTO bitacora = new BitacoraAvionDTO();
    
    @FXML
    private Spinner<Double> spDistanciaMas;
    @FXML
    private Spinner<Double> spDistanciaMenos;
    @FXML
    private JFXComboBox<String> cbEstado;
    @FXML
    private JFXTextField txtUbicacion;
    @FXML
    private JFXComboBox<AvionDTO> cbAvion;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initAviones();
        ArrayList estados = new ArrayList();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);   
        cbEstado.setItems(items);
        
        SpinnerValueFactory<Double> value = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999999, 0);
        SpinnerValueFactory<Double> value2 = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999999, 0);
        spDistanciaMenos.setValueFactory(value);
        spDistanciaMas.setValueFactory(value2);
        if(!UsuarioAutenticado.getInstance().getRol().equals("gestor") && !UsuarioAutenticado.getInstance().getRol().equals("administrador")){
            btnAgregar.setVisible(false);
            btnAgregar.setDisable(true);
        }
        cargarTodos();
    }    

    public void cargarTodos(){
        ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList<BitacoraAvionDTO>();
        Respuesta respuesta = bitacoraService.getAll();
        if(respuesta.getEstado().equals(true)){
            bitacoras = (ArrayList<BitacoraAvionDTO>) respuesta.getResultado("BitacorasAvion");
            
        }
        cargarTabla(bitacoras);
    }
    
    public void cargarTabla(ArrayList<BitacoraAvionDTO> bitacoras){
        tvEstados.getColumns().clear();
        if(!bitacoras.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(bitacoras);   
            
            TableColumn <BitacoraAvionDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
          
            TableColumn <BitacoraAvionDTO, String>colCombustible = new TableColumn("Combustible");
            colCombustible.setCellValueFactory(new PropertyValueFactory("combustible"));
          
            TableColumn <BitacoraAvionDTO, String>colDistancia = new TableColumn("Distancia Recorrida");
            colDistancia.setCellValueFactory(new PropertyValueFactory("distanciaRecorrida"));
         
            TableColumn <BitacoraAvionDTO, String>colTiempo = new TableColumn("Tiempo en Tierra");
            colTiempo.setCellValueFactory(new PropertyValueFactory("tiempoTierra"));
            
            TableColumn <BitacoraAvionDTO, String>colUbicacion = new TableColumn("Ubicación");
            colUbicacion.setCellValueFactory(new PropertyValueFactory("ubicacion"));
           
            TableColumn <BitacoraAvionDTO, String>colAvion = new TableColumn("Avión");
            colAvion.setCellValueFactory(new PropertyValueFactory("avion"));
            
            TableColumn<BitacoraAvionDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(av -> {
            String estadoString;
                if(av.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvEstados.getColumns().addAll(colId);
            tvEstados.getColumns().addAll(colCombustible);
            tvEstados.getColumns().addAll(colDistancia);
            tvEstados.getColumns().addAll(colTiempo);
            tvEstados.getColumns().addAll(colUbicacion);
            tvEstados.getColumns().addAll(colAvion);
            if(UsuarioAutenticado.getInstance().getRol().equals("gestor") || UsuarioAutenticado.getInstance().getRol().equals("administrador")){
                addButtonToTable();
            }
            tvEstados.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<BitacoraAvionDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<BitacoraAvionDTO, Void>, TableCell<BitacoraAvionDTO, Void>> cellFactory = new Callback<TableColumn<BitacoraAvionDTO, Void>, TableCell<BitacoraAvionDTO, Void>>() {
            @Override
            public TableCell<BitacoraAvionDTO, Void> call(final TableColumn<BitacoraAvionDTO, Void> param) {
                final TableCell<BitacoraAvionDTO, Void> cell = new TableCell<BitacoraAvionDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            BitacoraAvionDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            BitacoraAvionDTO data = getTableView().getItems().get(getIndex());
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

        tvEstados.getColumns().add(colBtn);

    }
    public void ver(BitacoraAvionDTO bitacora){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadBitacora", "Ver");
        AppContext.getInstance().set("BitacoraEnCuestion", bitacora);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("BitacoraInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void modificar(BitacoraAvionDTO bitacora){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadBitacora", "Modificar");
        AppContext.getInstance().set("BitacoraEnCuestion", bitacora);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("BitacoraInformacion" + ".fxml"));
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
            Parent root = FXMLLoader.load(App.class.getResource("Bitacora" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actBorrar(ActionEvent event) {
        txtUbicacion.setText("");
        txtUbicacion.setPromptText("Buscar por Ubicación");
        cbEstado.setValue(null);
        cbAvion.setValue(null);
        cbEstado.setPromptText("Buscar por Estado");
        cbAvion.setPromptText("Buscar por Avión");
        spDistanciaMas.getValueFactory().setValue(Double.valueOf(0));
        spDistanciaMenos.getValueFactory().setValue(Double.valueOf(0));
        cargarTodos();
    }


    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadBitacora", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("BitacoraInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
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
    private void actBuscaDistancia(ActionEvent event) {
        if(validarBusquedas("Distancia")){
            ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList<BitacoraAvionDTO>();
            Respuesta respuesta = bitacoraService.getByDistanciaRecorridaRango(spDistanciaMas.getValue().floatValue(), spDistanciaMenos.getValue().floatValue());
            if(respuesta.getEstado().equals(true)){
                bitacoras = (ArrayList<BitacoraAvionDTO>) respuesta.getResultado("BitacorasAvion");
            }
            cargarTabla(bitacoras);
        }
    }


    @FXML
    private void actBuscarEstado(ActionEvent event) {
        if(validarBusquedas("Estado")){
            ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList<BitacoraAvionDTO>();
            Respuesta respuesta = bitacoraService.getByEstado(estado);
            if(respuesta.getEstado().equals(true)){
                bitacoras = (ArrayList<BitacoraAvionDTO>) respuesta.getResultado("BitacorasAvion");
            }
            cargarTabla(bitacoras);
        }
    }

    @FXML
    private void actBuscarUbicacion(ActionEvent event) {
        if(validarBusquedas("Ubicacion")){
            ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList<BitacoraAvionDTO>();
            Respuesta respuesta = bitacoraService.getByUbicacion(txtUbicacion.getText());
            if(respuesta.getEstado().equals(true)){
                bitacoras = (ArrayList<BitacoraAvionDTO>) respuesta.getResultado("BitacorasAvion");
            }
            cargarTabla(bitacoras);
        }
    }

    @FXML
    private void actBuscarAvion(ActionEvent event) { // FALTA IMPLEMENTAR BUSQUEDA POR AVION
        if(validarBusquedas("Avion")){
            ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList<BitacoraAvionDTO>();
            Respuesta respuesta = bitacoraService.getByAvion(cbAvion.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                bitacoras = (ArrayList<BitacoraAvionDTO>) respuesta.getResultado("BitacorasAvion");
            }
            cargarTabla(bitacoras);
        }
    }
    
    public boolean validarBusquedas(String tipo){
        if(tipo.equals("Distancia")){
            if(spDistanciaMenos.getValue() == null || spDistanciaMas.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Digite la distancia recorrida");
                return false;
            }
        }
        if(tipo.equals("Estado")){
            if(cbEstado.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja el estado de la bitácora");
                return false;
            }
        }
        if(tipo.equals("Avion")){
            if(cbAvion.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja el avión");
                return false;
            }
        }
        if(tipo.equals("Ubicacion")){
            if(txtUbicacion.getText().isEmpty()){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Digite la ubicación");
                return false;
            }
        }
        return true;
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
}
