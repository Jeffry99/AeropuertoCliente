/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.AlertaGeneradaDTO;
import org.una.aeropuerto.cliente.dto.TipoAlertaDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.AlertaGeneradaService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class AlertasController implements Initializable {

    @FXML
    private Button btnBuscarVuelo;
    @FXML
    private JFXTextField txtBuscarVuelo;
    @FXML
    private TableView<AlertaGeneradaDTO> tvAlertas;
    @FXML
    private Button btnVolver;
    @FXML
    private JFXComboBox<String> cbxBuscarEstado;
    
    private String rolUsuario="";
    
    private AlertaGeneradaService alertaService = new AlertaGeneradaService();
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private ImageView btnInformacion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rolUsuario=UsuarioAutenticado.getInstance().getRol();
         if(rolUsuario.equals("administrador")){
            txtBuscarVuelo.setEditable(false);
            cbxBuscarEstado.setDisable(false);
            btnInformacion.setVisible(true);
            btnInformacion.setDisable(false);
            cargarTodasAlertas(); 
        }else{
            AppContext.getInstance().set("ControllerAlertas", this);
            initEstados();
            cargarTodasAlertas(); 
         }
            
        
    }    

    public void initEstados(){
        ArrayList estados = new ArrayList();
        estados.add("Autorizada");
        estados.add("No autorizada");
        estados.add("Sin autorizar");
        ObservableList items = FXCollections.observableArrayList(estados);   
        cbxBuscarEstado.setItems(items);
    }
    


    @FXML
    private void actVolver(ActionEvent event) {
    }

    
    public void cargarTodasAlertas(){
        ArrayList<AlertaGeneradaDTO> alertas = new ArrayList<AlertaGeneradaDTO>();
        Respuesta respuesta = alertaService.getAll();
        if(respuesta.getEstado().equals(true)){
            alertas = (ArrayList<AlertaGeneradaDTO>) respuesta.getResultado("AlertasGeneradas");
            
        }
        cargarTablaAlertas(alertas);
    }
    private void cargarTablaAlertas(ArrayList<AlertaGeneradaDTO> alertas){
        tvAlertas.getColumns().clear();
        if(!alertas.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(alertas);   
            
            TableColumn<AlertaGeneradaDTO, String> colVuelo = new TableColumn("Código de vuelo");
            colVuelo.setCellValueFactory(av -> {
            String vuelo=av.getValue().getVuelo().getId().toString();
                return new ReadOnlyStringWrapper(vuelo);
            });
            
            TableColumn<AlertaGeneradaDTO, String> colTipo = new TableColumn("Tipo de alerta");
            colTipo.setCellValueFactory(av -> {
            String tipo=av.getValue().getTipoAlerta().getDescripcion();
                return new ReadOnlyStringWrapper(tipo);
            });
            
            TableColumn<AlertaGeneradaDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(av -> {
            String estadoString;
                if(av.getValue().getEstado()==2){
                    estadoString = "Autorizada";
                }else{
                    if(av.getValue().getEstado()==3){
                       estadoString = "No autorizada"; 
                    }else{
                        estadoString="Sin autorizar";
                    }
                    
                }
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvAlertas.getColumns().addAll(colVuelo);
            tvAlertas.getColumns().addAll(colTipo);
            tvAlertas.getColumns().addAll(colEstado);
            addButtonToTableAlertas();
            tvAlertas.setItems(items);
        }
    }
    private void addButtonToTableAlertas() {
        TableColumn<AlertaGeneradaDTO, Void> colBtn = new TableColumn("Acciones");
        Callback<TableColumn<AlertaGeneradaDTO, Void>, TableCell<AlertaGeneradaDTO, Void>> cellFactory = new Callback<TableColumn<AlertaGeneradaDTO, Void>, TableCell<AlertaGeneradaDTO, Void>>() {
            @Override
            public TableCell<AlertaGeneradaDTO, Void> call(final TableColumn<AlertaGeneradaDTO, Void> param) {
                final TableCell<AlertaGeneradaDTO, Void> cell = new TableCell<AlertaGeneradaDTO, Void>() {

                    private final Button btn = new Button("Ver");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                                AlertaGeneradaDTO data = getTableView().getItems().get(getIndex());
                                try{
                                    Stage stage = new Stage();
                                    AppContext.getInstance().set("ModalidadAlerta", "Ver");
                                    AppContext.getInstance().set("AlertaGenerada", data);
                                    Parent root = FXMLLoader.load(App.class.getResource("AlertasInformacion" + ".fxml"));
                                    stage.setScene(new Scene(root));
                                    stage.setTitle("Alerta generada");
                                    stage.initModality(Modality.WINDOW_MODAL);
                                    stage.initOwner(
                                        ((Node)event.getSource()).getScene().getWindow() );
                                    stage.show();
                                }catch(IOException ex){
                                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
                                };
                            }catch(Exception ex){}
                        });
                    }
                    private final Button btn1 = new Button("Autorizar");

                    {
                        btn1.setOnAction((ActionEvent event) -> {
                            try{
                                AlertaGeneradaDTO data = getTableView().getItems().get(getIndex());
                                try{
                                    Stage stage = new Stage();
                                    AppContext.getInstance().set("ModalidadAlerta", "Autorizar");
                                    AppContext.getInstance().set("AlertaGenerada", data);
                                    Parent root = FXMLLoader.load(App.class.getResource("AlertasInformacion" + ".fxml"));
                                    stage.setScene(new Scene(root));
                                    stage.setTitle("Autorizar alerta");
                                    stage.initModality(Modality.WINDOW_MODAL);
                                    stage.initOwner(
                                        ((Node)event.getSource()).getScene().getWindow() );
                                    stage.show();
                                }catch(IOException ex){
                                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
                                };
                            }catch(Exception ex){}
                        });
                    }
                    HBox pane = new HBox(btn, btn1);
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
        tvAlertas.getColumns().add(colBtn);

    }
    @FXML
    private void actBuscarEstado(ActionEvent event) {
        if(rolUsuario.equals("administrador")){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de botón", "fxID: btnBuscarEstado \n"+
                                                                                     "Acción: actBuscarEstado");
        }else{
            if(estado!=null){
                ArrayList<AlertaGeneradaDTO> alertas = new ArrayList<AlertaGeneradaDTO>();
                Respuesta respuesta = alertaService.getByEstado(estado);
                if(respuesta.getEstado().equals(true)){
                    alertas = (ArrayList<AlertaGeneradaDTO>) respuesta.getResultado("AlertasGeneradas");
                }
                cargarTablaAlertas(alertas);
            }else{
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado de la alerta que desea buscar");
            }
        }
    }
    @FXML
    private void actBuscarVuelo(ActionEvent event) {
         if(rolUsuario.equals("administrador")){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de botón", "fxID: btnBuscarVuelo \n"+
                                                                                     "Acción: actBuscarVuelo");
        }else{
            if(!txtBuscarVuelo.getText().isBlank()){
                ArrayList<AlertaGeneradaDTO> alertas = new ArrayList<AlertaGeneradaDTO>();
                Respuesta respuesta = alertaService.getByVuelo(Long.valueOf(txtBuscarVuelo.getText()));
                if(respuesta.getEstado().equals(true)){
                    alertas = (ArrayList<AlertaGeneradaDTO>) respuesta.getResultado("AlertasGeneradas");
                }
                cargarTablaAlertas(alertas);
            }else{
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del vuelo, para las alertas que desea buscar");
            } 
        }
        
    }
    
    private Integer estado = null;
    @FXML
    private void actSelEstado(ActionEvent event) {
        if(cbxBuscarEstado.getValue().equals("Autorizada")){
            estado = 2;
        }else{
            if(cbxBuscarEstado.getValue().equals("No autorizada")){
                estado = 3;
            }else{
                estado=1;
            }
        }
    }

    @FXML
    private void ActtxtBuscarVuelo(MouseEvent event) {
        if(rolUsuario.equals("administrador")){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de text field", "fxID: txtBuscarVuelo \n"+
                "Acción: usado para almacenar el dato digitado por el usuario para buscar la alerta segun el código de vuelo");
        }
    }

    @FXML
    private void actcbxBuscarEstado(MouseEvent event) {
         if(rolUsuario.equals("administrador")){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información del combo box", "fxID: txtBuscarEstado \n"+
                "Acción: usada para mostrar los datos de las alertas por estado que hay en el sistema");
        }
    }

    @FXML
    private void acttvAlertas(MouseEvent event) {
         if(rolUsuario.equals("administrador")){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de table view", "fxID: txtBuscarVuelo \n"+
                "Acción: usada para mostrar los datos de las alertas de los vuelos que hay en el sistema");
        }
    }

    @FXML
    private void actVerInformacionPantalla(MouseEvent event) {
         Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de la vista", "FXML: Alertas \n"+
                                                         "Controller: AlertassController \n\n"+
                                                         "Información de este botón \n"+
                                                         "fxID: btnInformacion \n"+
                                                         "Acción: actVerInformacion");
    }
    
}
