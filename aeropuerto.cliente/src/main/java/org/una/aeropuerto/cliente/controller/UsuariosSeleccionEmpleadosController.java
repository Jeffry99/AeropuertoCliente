/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class UsuariosSeleccionEmpleadosController implements Initializable {

    @FXML
    private TableView<EmpleadoDTO> tvEmpleados;

    /**
     * Initializes the controller class.
     */
    private EmpleadoService empleadoService = new EmpleadoService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
            Respuesta respuesta = empleadoService.getByEstado(true);
            if(respuesta.getEstado().equals(true)){
                empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
            }
            cargarTabla(empleados);
    }

    public void cargarTabla(ArrayList<EmpleadoDTO> empleados){
        tvEmpleados.getColumns().clear();
        if(!empleados.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(empleados);   
            
            TableColumn <EmpleadoDTO, Long>colId = new TableColumn("ID");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn <EmpleadoDTO, String>colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn <EmpleadoDTO, String>colCedula = new TableColumn("Cédula");
            colCedula.setCellValueFactory(new PropertyValueFactory("cedula"));
            tvEmpleados.getColumns().addAll(colId);
            tvEmpleados.getColumns().addAll(colNombre);
            tvEmpleados.getColumns().addAll(colCedula);
            addButtonToTable();
            tvEmpleados.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<EmpleadoDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<EmpleadoDTO, Void>, TableCell<EmpleadoDTO, Void>> cellFactory = new Callback<TableColumn<EmpleadoDTO, Void>, TableCell<EmpleadoDTO, Void>>() {
            @Override
            public TableCell<EmpleadoDTO, Void> call(final TableColumn<EmpleadoDTO, Void> param) {
                final TableCell<EmpleadoDTO, Void> cell = new TableCell<EmpleadoDTO, Void>() {

                    private final Button btn = new Button("Seleccionar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            EmpleadoDTO data = getTableView().getItems().get(getIndex());
                            setEmpleado(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    
                    HBox pane = new HBox(btn);

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

        tvEmpleados.getColumns().add(colBtn);

    }
    
    public void setEmpleado(EmpleadoDTO empleado){
        UsuariosInformacionController usuController = (UsuariosInformacionController) AppContext.getInstance().get("controllerUsuarios");
        usuController.setEmpleado(empleado);
    }
    
}
