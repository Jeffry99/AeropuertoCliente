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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.TipoAvionService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class TiposAvionController implements Initializable {
  
    private TipoAvionService tipoAvionesService = new TipoAvionService();

    @FXML
    private Button btnVolver;
    @FXML
    private Button btnBuscarId;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBuscarDist;
    @FXML
    private Button btnBuscarNom;
    @FXML
    private Button btnBuscarEst;
    @FXML
    private TableView<TipoAvionDTO> tvtTiposAviones;
    @FXML
    private JFXComboBox<String> cbxEstado;
    @FXML
    private JFXTextField txtbuscarNombre;
    @FXML
    private JFXTextField txtbuscarId;
    @FXML
    private Spinner<Double> spDistanciaMas;
    @FXML
    private Spinner<Double> spDistanciaMenos;

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
        if(!UsuarioAutenticado.getInstance().getRol().equals("gestor") && !UsuarioAutenticado.getInstance().getRol().equals("administrador")){
            btnAgregar.setVisible(false);
            btnAgregar.setDisable(true);
        }
    }   
    
    public void cargarTodos(){
        ArrayList<TipoAvionDTO> tiposAvion = new ArrayList<TipoAvionDTO>();
        Respuesta respuesta = tipoAvionesService.getAll();
        if(respuesta.getEstado().equals(true)){
            tiposAvion = (ArrayList<TipoAvionDTO>) respuesta.getResultado("TiposAviones");
        }
        cargarTabla(tiposAvion);
    }

    public void cargarTabla(ArrayList<TipoAvionDTO> tiposAvion){
        tvtTiposAviones.getColumns().clear();
        if(!tiposAvion.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(tiposAvion);   
            
            TableColumn <TipoAvionDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <TipoAvionDTO, String>colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn <TipoAvionDTO,Float>colDistancia = new TableColumn("Distancia Rec");
            colDistancia.setCellValueFactory(new PropertyValueFactory("distanciaRecomendada"));
            TableColumn<TipoAvionDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(emp -> {
                String estadoString;
                if(emp.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvtTiposAviones.getColumns().addAll(colId);
            tvtTiposAviones.getColumns().addAll(colNombre);
            tvtTiposAviones.getColumns().addAll(colDistancia);
            tvtTiposAviones.getColumns().addAll(colEstado);
            if(UsuarioAutenticado.getInstance().getRol().equals("gestor") || UsuarioAutenticado.getInstance().getRol().equals("administrador")){
                addButtonToTable();
            }
            tvtTiposAviones.setItems(items);
        }
    }
      private void addButtonToTable() {
        TableColumn<TipoAvionDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<TipoAvionDTO, Void>, TableCell<TipoAvionDTO, Void>> cellFactory = new Callback<TableColumn<TipoAvionDTO, Void>, TableCell<TipoAvionDTO, Void>>() {
            @Override
            public TableCell<TipoAvionDTO, Void> call(final TableColumn<TipoAvionDTO, Void> param) {
                final TableCell<TipoAvionDTO, Void> cell = new TableCell<TipoAvionDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            TipoAvionDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            TipoAvionDTO data = getTableView().getItems().get(getIndex());
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

        tvtTiposAviones.getColumns().add(colBtn);
    }
      
    public void modificar(TipoAvionDTO tipoAvion){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadTipoAvion", "Modificar");
        AppContext.getInstance().set("TipoAvionEnCuestion", tipoAvion);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TiposAvionesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
   
    public void ver(TipoAvionDTO tipoAvion){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadTipoAvion", "Ver");
        AppContext.getInstance().set("TipoAvionEnCuestion", tipoAvion);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TiposAvionesInformacion" + ".fxml"));
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
            ArrayList<TipoAvionDTO> tipoAviones = new ArrayList<TipoAvionDTO>();
            Respuesta respuesta = tipoAvionesService.getById(Long.valueOf(txtbuscarId.getText()));
            if(respuesta.getEstado().equals(true)){
                TipoAvionDTO aero = (TipoAvionDTO) respuesta.getResultado("TipoAvion");
                tipoAviones.add(aero);
            }
            cargarTabla(tipoAviones);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del tipo de avion que desea buscar");
        }
    }

    @FXML
    private void actAgregar(ActionEvent event) {
    StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadTipoAvion", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TiposAvionesInformacion" + ".fxml"));
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
        if(spDistanciaMenos.getValue() == null || spDistanciaMas.getValue() == null){
            ArrayList<TipoAvionDTO> tipoAviones = new ArrayList<TipoAvionDTO>();
            Respuesta respuesta = tipoAvionesService.getByDistanciaRango(spDistanciaMas.getValue().floatValue(), spDistanciaMenos.getValue().floatValue());
            if(respuesta.getEstado().equals(true)){
                tipoAviones = (ArrayList<TipoAvionDTO>) respuesta.getResultado("TiposAviones");
            }
            cargarTabla(tipoAviones);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la distancia del tipo de avion que desea buscar");
        }
    }

    @FXML
    private void actBuscarNom(ActionEvent event) {
        if(!txtbuscarNombre.getText().isBlank()){
            ArrayList<TipoAvionDTO> tipoAviones = new ArrayList<TipoAvionDTO>();
            Respuesta respuesta = tipoAvionesService.getByNombreAproximate(txtbuscarNombre.getText());
            if(respuesta.getEstado().equals(true)){
                tipoAviones = (ArrayList<TipoAvionDTO>) respuesta.getResultado("TiposAviones");
            }
            cargarTabla(tipoAviones);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del tipo de avion que desea buscar");
        }
    }

    @FXML
    private void actBuscarEst(ActionEvent event) {
        if(estadoBuscar!=null){
            ArrayList<TipoAvionDTO> tipoAvion = new ArrayList<TipoAvionDTO>();
            Respuesta respuesta = tipoAvionesService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                tipoAvion = (ArrayList<TipoAvionDTO>) respuesta.getResultado("TiposAviones");
            }
            cargarTabla(tipoAvion);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del tipo de avion que desea buscar");
        }
    }
    
}
