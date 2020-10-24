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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.dto.TipoAvionDTO;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class ServiciosController implements Initializable {
    private ServicioRegistradoService servicioRegistradoService = new ServicioRegistradoService();
    @FXML
    private TableView<ServicioRegistradoDTO> tvServicios;
    @FXML
    private Button btnVolver;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnBuscarTipo;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtTipo;
    private ComboBox<TipoAvionDTO> cbTipoAvion;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private Button btnBuscarCobro;
    private Label lblAvion;
    @FXML
    private TextField txtMinimo;
    @FXML
    private TextField txtMaximo;
    @FXML
    private ComboBox<String> cbEstadoCobro;
    @FXML
    private Button btnBuscarEstadoCobro;
    private ComboBox<AvionDTO> cbAvion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarTodos();
    }    
    
    public void cargarTodos(){
        ArrayList<ServicioRegistradoDTO> servicios = new ArrayList<ServicioRegistradoDTO>();
        Respuesta respuesta = servicioRegistradoService.getAll();
        if(respuesta.getEstado().equals(true)){
            servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
            
        }
        cargarTabla(servicios);
    }
    
    public void cargarTabla(ArrayList<ServicioRegistradoDTO> servicios){
        tvServicios.getColumns().clear();
        if(!servicios.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(servicios);
            
            TableColumn <ServicioRegistradoDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            
            TableColumn <ServicioRegistradoDTO, String>colTipoServicio = new TableColumn("Tipo de Servicio");
            colTipoServicio.setCellValueFactory(ts -> {
                String tipoServicio = ts.getValue().getServicioTipo().getNombre();
                return new ReadOnlyStringWrapper(tipoServicio);
            });
            
            TableColumn <ServicioRegistradoDTO, String>colCobro = new TableColumn("Aerolinea");
            colId.setCellValueFactory(new PropertyValueFactory("cobro"));
            /*colCobro.setCellValueFactory(sr -> {
                String cobro = String.valueOf(sr.getValue().getCobro());
                return new ReadOnlyStringWrapper(cobro);
            });*/
            
            TableColumn<ServicioRegistradoDTO, String> colEstadoCobro = new TableColumn("Estado de Cobro");
            colEstadoCobro.setCellValueFactory(av -> {
            String estadoString;
                if(av.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            
            TableColumn<ServicioRegistradoDTO, String> colResponsable = new TableColumn("Responsable");
            colResponsable.setCellValueFactory(sr -> {
                String responsable = String.valueOf(sr.getValue().getResponsable().getNombre());
                return new ReadOnlyStringWrapper(responsable);
            });
            
            TableColumn<ServicioRegistradoDTO, String> colAvion = new TableColumn("Avión");
            colEstadoCobro.setCellValueFactory(sr -> {
                String avion = String.valueOf(sr.getValue().getAvion().getMatricula());
                return new ReadOnlyStringWrapper(avion);
            });
            
            tvServicios.getColumns().addAll(colId);
            tvServicios.getColumns().addAll(colTipoServicio);
            tvServicios.getColumns().addAll(colCobro);
            tvServicios.getColumns().addAll(colEstadoCobro);
            tvServicios.getColumns().addAll(colResponsable);
            tvServicios.getColumns().addAll(colAvion);
            addButtonToTable();
            tvServicios.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<ServicioRegistradoDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<ServicioRegistradoDTO, Void>, TableCell<ServicioRegistradoDTO, Void>> cellFactory = new Callback<TableColumn<ServicioRegistradoDTO, Void>, TableCell<ServicioRegistradoDTO, Void>>() {
            @Override
            public TableCell<ServicioRegistradoDTO, Void> call(final TableColumn<ServicioRegistradoDTO, Void> param) {
                final TableCell<ServicioRegistradoDTO, Void> cell = new TableCell<ServicioRegistradoDTO, Void>() {

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            ServicioRegistradoDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            ServicioRegistradoDTO data = getTableView().getItems().get(getIndex());
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

        tvServicios.getColumns().add(colBtn);

    }
    public void ver(ServicioRegistradoDTO servicio){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadServicioRegistrado", "Ver");
        AppContext.getInstance().set("ServicioRegistradoEnCuestion", servicio);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesServiciosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void modificar(ServicioRegistradoDTO servicio){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadServicioRegistrado", "Modificar");
        AppContext.getInstance().set("ServicioRegistradoEnCuestion", servicio);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesServiciosInformacion" + ".fxml"));
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
            Parent root = FXMLLoader.load(App.class.getResource("AvionesInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            
        }
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
        txtTipo.setText("");
        cbEstado.setValue(null);
        cbTipoAvion.setValue(null);
        txtMinimo.setText(null);
        txtMaximo.setText(null);
    }
    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadServicioRegistrado", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AvionesServiciosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }   
    
    public boolean validarBusquedas(String tipo){
        if(tipo.equals("Tipo")){
            if(txtTipo.getText().isEmpty()){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escriba al menos una letra del nombre del tipo de servicio");
                return false;
            }
        }
        if(tipo.equals("Estado")){
            if(cbEstado.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja el estado de los servicios");
                return false;
            }
        }
        if(tipo.equals("EstadoCobro")){
            if(cbEstadoCobro.getValue() == null){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Escoja el estado de cobro");
                return false;
            }
        }
        if(tipo.equals("Monto")){
            if(txtMinimo.getText().isEmpty() || txtMaximo.getText().isEmpty()){
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Falta información", "Especifique el rango de cobro");
                return false;
            }
        }
        return true;
    }
    @FXML
    private void actBuscarTipo(ActionEvent event) {
        if(validarBusquedas("Tipo")){
            ArrayList<ServicioRegistradoDTO> servicios = new ArrayList<ServicioRegistradoDTO>();
            Respuesta respuesta = servicioRegistradoService.getByTipoAproximate(txtTipo.getText());
            if(respuesta.getEstado().equals(true)){
                servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
            }
            cargarTabla(servicios);
        }
    }

    @FXML
    private void actBuscarEstado(ActionEvent event) {
        if(validarBusquedas("Estado")){
            ArrayList<ServicioRegistradoDTO> servicios = new ArrayList<ServicioRegistradoDTO>();
            Respuesta respuesta = servicioRegistradoService.getByEstado(estado);
            if(respuesta.getEstado().equals(true)){
                servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
            }
            cargarTabla(servicios);
        }
    }

    @FXML
    private void actBuscarCobro(ActionEvent event) {
        if(validarBusquedas("Monto")){
            ArrayList<ServicioRegistradoDTO> servicios = new ArrayList<ServicioRegistradoDTO>();
            Respuesta respuesta = servicioRegistradoService.getByCobroRango("ds");
            if(respuesta.getEstado().equals(true)){
                servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
            }
            cargarTabla(servicios);
        }
    }
    
    private Boolean estadoCobro;
    @FXML
    private void actEstadoCobro(ActionEvent event) {
        if(cbEstadoCobro.getValue().equals("Activo")){
            estadoCobro = true;
        }else{
            if(cbEstadoCobro.getValue().equals("Inactivo")){
                estadoCobro = false;
            }
        }
    }

    @FXML
    private void actBuscarEstadoCobro(ActionEvent event) {
        if(validarBusquedas("EstadoCobro")){
            ArrayList<ServicioRegistradoDTO> servicios = new ArrayList<ServicioRegistradoDTO>();
            Respuesta respuesta = servicioRegistradoService.getByEstadoCobro(estadoCobro);
            if(respuesta.getEstado().equals(true)){
                servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
            }
            cargarTabla(servicios);
        }
    }
    
}
