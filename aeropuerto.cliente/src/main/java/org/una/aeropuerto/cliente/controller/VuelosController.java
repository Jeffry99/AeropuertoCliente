/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.DatePicker;
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
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.dto.VueloDTO;
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
    private Button btnBuscarAvion;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private Button btnBuscarFecha;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private Button btnBuscarRuta;
    @FXML
    private JFXComboBox<String> cbEstado;
    @FXML
    private JFXComboBox<AvionDTO> cbAvion;
    @FXML
    private JFXComboBox<RutaDTO> cbRuta;
    /**
     * Initializes the controller class.
     */
    private VueloService vueloService = new VueloService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initEstados();
        initAviones();
        initRutas();
        cargarTodos();
        if(!UsuarioAutenticado.getInstance().getRol().equals("gestor") && !UsuarioAutenticado.getInstance().getRol().equals("administrador")){
            btnAgregar.setVisible(false);
            btnAgregar.setDisable(true);
        }
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
            colId.setPrefWidth(50);
            
            TableColumn <VueloDTO, String>colRuta = new TableColumn("Ruta");
            colRuta.setCellValueFactory(av -> {
                String origen = av.getValue().getRuta().getOrigen()+" - "+av.getValue().getRuta().getDestino();
                return new ReadOnlyStringWrapper(origen);
            });
            colRuta.setPrefWidth(300);
            
            TableColumn<VueloDTO, String> colFecha = new TableColumn("Fecha");
            colFecha.setCellValueFactory(av -> {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String fecha = formatter.format(av.getValue().getFecha());
                return new ReadOnlyStringWrapper(fecha);
            });
            colFecha.setPrefWidth(75);
            
            
            TableColumn<VueloDTO, String> colHora = new TableColumn("Hora");
            colHora.setCellValueFactory(av -> {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                String hora = formatter.format(av.getValue().getFecha());
                return new ReadOnlyStringWrapper(hora);
            });
            colHora.setPrefWidth(75);
            
            TableColumn<VueloDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(av -> {
                int e = av.getValue().getEstado();
                String estado="";
                if(e==1){
                    estado="En revisión";
                }else{
                    if(e==2){
                        estado="Autorizado";
                    }else{
                        if(e==3){
                            estado="No autorizado";
                        }else{
                            if(e==4){
                                estado="Cancelado";
                            }
                        }
                    }
                }
                return new ReadOnlyStringWrapper(estado);
            });
            colEstado.setPrefWidth(150);
            
            
            tvVuelos.getColumns().addAll(colId);
            tvVuelos.getColumns().addAll(colRuta);
            tvVuelos.getColumns().addAll(colFecha);
            tvVuelos.getColumns().addAll(colHora);
            tvVuelos.getColumns().addAll(colEstado);
            if(UsuarioAutenticado.getInstance().getRol().equals("gestor") || UsuarioAutenticado.getInstance().getRol().equals("administrador")){
                addButtonToTable();
            }
            tvVuelos.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<VueloDTO, Void> colBtn = new TableColumn("Acciones");
        colBtn.setPrefWidth(175);

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

        tvVuelos.getColumns().add(colBtn);

    }
    public void ver(VueloDTO vuelo){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadVuelo", "Ver");
        AppContext.getInstance().set("VueloEnCuestion", vuelo);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("VuelosInformacion" + ".fxml"));
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
            Parent root = FXMLLoader.load(App.class.getResource("VuelosInformacion" + ".fxml"));
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
    private Integer estado = null;
    @FXML
    private void actEstados(ActionEvent event) {
        if(cbEstado.getValue().equals("En revisión")){
            estado = 1;
        }else{
            if(cbEstado.getValue().equals("Autorizado")){
                estado = 2;
            }else{
                if(cbEstado.getValue().equals("No autorizado")){
                    estado = 3;
                }else{
                    estado=4;
                }
            }
        }
    }

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
            Parent root = FXMLLoader.load(App.class.getResource("VuelosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actRuta(ActionEvent event) {
        /*if(validarBusquedas("Ruta")){
            ArrayList<VueloDTO> vuelos = new ArrayList<VueloDTO>();
            Respuesta respuesta = vueloService.getByRuta(cbRuta.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                vuelos = (ArrayList<VueloDTO>) respuesta.getResultado("Vuelos");
            }
            cargarTabla(vuelos);
        }*/
    }
    
    public void volver() {
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Parent root = FXMLLoader.load(App.class.getResource("MenuPrincipal" + ".fxml"));
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
        estados.add("En revisión");
        estados.add("Autorizado");
        estados.add("No autorizado");
        estados.add("Cancelado");
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
            rutas = (ArrayList<RutaDTO>) respuesta.getResultado("Rutas");
            ObservableList items = FXCollections.observableArrayList(rutas);
            cbRuta.setItems(items);
        }
    }
    
    
    
    
}
