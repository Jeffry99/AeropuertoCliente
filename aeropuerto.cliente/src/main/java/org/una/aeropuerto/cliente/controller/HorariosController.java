/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

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
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.HorarioDTO;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.HorarioService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class HorariosController implements Initializable {

    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnBuscarId;
    @FXML
    private ComboBox<String> cbxEstado;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private ComboBox<EmpleadoDTO> cbxEmpleado;
    @FXML
    private Button btnBuscarEmpleado;
    @FXML
    private Button btnBuscarEmpleadoEstado;
    @FXML
    private TableView<HorarioDTO> tvHorarios;

    private HorarioService horarioService = new HorarioService();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initEmpleados();
        initEstado();
        cargarTodos();
                
        // TODO
    }    
    
    public void initEstado(){
        ArrayList estados = new ArrayList();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);   
        cbxEstado.setItems(items);
    }
    
    public void initEmpleados(){
        EmpleadoService empleadoService = new EmpleadoService();
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta = empleadoService.getAll();
        if(respuesta.getEstado()){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
            ObservableList items = FXCollections.observableArrayList(empleados);
            cbxEmpleado.setItems(items);
        }
    }

    @FXML
    private void actVolver(ActionEvent event) {
    }

    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadHorario", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("HorariosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void ver(HorarioDTO horario){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadHorario", "Ver");
        AppContext.getInstance().set("HorarioEnCuestion", horario);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("HorariosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    public void modificar(HorarioDTO horario){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadHorario", "Modificar");
        AppContext.getInstance().set("HorarioEnCuestion", horario);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("HorariosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
        
    }

    @FXML
    private void actBuscarId(ActionEvent event) {
        if(!txtId.getText().isBlank()){
            ArrayList<HorarioDTO> horarios = new ArrayList<HorarioDTO>();
            Respuesta respuesta = horarioService.getById(Long.valueOf(txtId.getText()));
            if(respuesta.getEstado().equals(true)){
                HorarioDTO horario = (HorarioDTO) respuesta.getResultado("Horario");
                horarios.add(horario);
            }
            cargarTabla(horarios);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del horario que desea buscar");
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
            ArrayList<HorarioDTO> horarios = new ArrayList<HorarioDTO>();
            Respuesta respuesta = horarioService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                horarios = (ArrayList<HorarioDTO>) respuesta.getResultado("Horarios");
            }
            cargarTabla(horarios);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del horario que desea buscar");
        }
    }

    @FXML
    private void actBuscarEmpleado(ActionEvent event) {
        if(cbxEmpleado.getValue()!=null){
            ArrayList<HorarioDTO> horarios = new ArrayList<HorarioDTO>();
            Respuesta respuesta = horarioService.getByEmpleado(cbxEmpleado.getValue().getId());
            if(respuesta.getEstado().equals(true)){
                horarios = (ArrayList<HorarioDTO>) respuesta.getResultado("Horarios");
            }
            cargarTabla(horarios);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el empleado del horario que desea buscar");
        }
    }

    @FXML
    private void actBuscarEmpleadoEstado(ActionEvent event) {
        if(estadoBuscar!=null){
            if(cbxEmpleado.getValue()!=null){
                ArrayList<HorarioDTO> horarios = new ArrayList<HorarioDTO>();
                Respuesta respuesta = horarioService.getByEmpleadoAndEstado(cbxEmpleado.getValue().getId(), estadoBuscar);
                if(respuesta.getEstado().equals(true)){
                    horarios = (ArrayList<HorarioDTO>) respuesta.getResultado("Horarios");
                }
                cargarTabla(horarios);
            }else{
                Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el empleado del horario que desea buscar");
            }
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del horario que desea buscar");
        }
    }
    
    public void cargarTodos(){
        ArrayList<HorarioDTO> horarios = new ArrayList<HorarioDTO>();
        Respuesta respuesta = horarioService.getAll();
        if(respuesta.getEstado().equals(true)){
            horarios = (ArrayList<HorarioDTO>) respuesta.getResultado("Horarios");
        }
        cargarTabla(horarios);
    }
    
    public void cargarTabla(ArrayList<HorarioDTO> horarios){
        tvHorarios.getColumns().clear();
        if(!horarios.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(horarios);   
            
            TableColumn <HorarioDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn<HorarioDTO, String> colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(hor -> {
                String nombreUsuario;
                nombreUsuario = hor.getValue().getEmpleado().getNombre();
                return new ReadOnlyStringWrapper(nombreUsuario);
            });
            TableColumn<HorarioDTO, String> colDiaInicio = new TableColumn("Día inicio");
            colDiaInicio.setCellValueFactory(hor -> {
                String dia=getDia(hor.getValue().getDiaInicio());
                return new ReadOnlyStringWrapper(dia);
            });
            TableColumn<HorarioDTO, String> colHoraInicio = new TableColumn("Hora inicio");
            colHoraInicio.setCellValueFactory(hor -> {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String hora = formatter.format(hor.getValue().getHoraInicio());
                return new ReadOnlyStringWrapper(hora);
            });
            TableColumn<HorarioDTO, String> colDiaFinal = new TableColumn("Día final");
            colDiaFinal.setCellValueFactory(hor -> {
                String dia=getDia(hor.getValue().getDiaFinal());
                return new ReadOnlyStringWrapper(dia);
            });
            TableColumn<HorarioDTO, String> colHoraFinal = new TableColumn("Hora final");
            colHoraFinal.setCellValueFactory(hor -> {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String hora = formatter.format(hor.getValue().getHoraFinal());
                return new ReadOnlyStringWrapper(hora);
            });
            TableColumn<HorarioDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(hor -> {
                String estadoString;
                if(hor.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
 
            tvHorarios.getColumns().addAll(colId);
            tvHorarios.getColumns().addAll(colNombre);
            tvHorarios.getColumns().addAll(colDiaInicio);
            tvHorarios.getColumns().addAll(colHoraInicio);
            tvHorarios.getColumns().addAll(colDiaFinal);
            tvHorarios.getColumns().addAll(colHoraFinal);
            tvHorarios.getColumns().addAll(colEstado);
            addButtonToTable();
            tvHorarios.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<HorarioDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<HorarioDTO, Void>, TableCell<HorarioDTO, Void>> cellFactory = new Callback<TableColumn<HorarioDTO, Void>, TableCell<HorarioDTO, Void>>() {
            @Override
            public TableCell<HorarioDTO, Void> call(final TableColumn<HorarioDTO, Void> param) {
                final TableCell<HorarioDTO, Void> cell = new TableCell<HorarioDTO, Void>() {
                    private final Button btn = new Button("Editar");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            HorarioDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    private final Button btn2 = new Button("Ver");
                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            HorarioDTO data = getTableView().getItems().get(getIndex());
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
        tvHorarios.getColumns().add(colBtn);
    }
 
    
    public String getDia(int d){
        if(d==1){
            return "Lunes";
        }else{
            if(d==2){
                return "Martes";
            }else{
                if(d==3){
                    return "Miércoles";
                }else{
                    if(d==4){
                        return "Jueves";
                    }else{
                        if(d==5){
                            return "Viernes";
                        }else{
                            if(d==6){
                                return "Sábado";
                            }else{
                                if(d==7){
                                    return "Domingo";
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    
}
