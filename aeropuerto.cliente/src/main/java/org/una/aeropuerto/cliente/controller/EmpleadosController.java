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
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class EmpleadosController implements Initializable {

    @FXML
    private TableView<EmpleadoDTO> tvEmpleados;
    @FXML
    private Button btnVolver;

    /**
     * Initializes the controller class.
     */
    
    private EmpleadoService empleadoService = new EmpleadoService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTodos();
        // TODO
    }    

    @FXML
    private void actVolver(ActionEvent event) {
    }
    
    public void cargarTodos(){
        ArrayList<EmpleadoDTO> empleados = new ArrayList<EmpleadoDTO>();
        Respuesta respuesta = empleadoService.getAll();
        if(respuesta.getEstado().equals(true)){
            empleados = (ArrayList<EmpleadoDTO>) respuesta.getResultado("Empleados");
        }
        cargarTabla(empleados);
    }
    
    public void cargarTabla(ArrayList<EmpleadoDTO> empleados){
        tvEmpleados.getColumns().clear();
        if(empleados.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(empleados);   
            
            TableColumn colId = new TableColumn("Id");
            colId.setCellValueFactory(new PropertyValueFactory("id"));
            TableColumn colNombre = new TableColumn("Nombre");
            colId.setCellValueFactory(new PropertyValueFactory("nombre"));
            TableColumn colCedula = new TableColumn("Cedula");
            colId.setCellValueFactory(new PropertyValueFactory("cedula"));
            TableColumn<EmpleadoDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(emp -> {
                String estadoString;
                if(emp.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
            tvEmpleados.getColumns().addAll(colId);
            tvEmpleados.getColumns().addAll(colNombre);
            tvEmpleados.getColumns().addAll(colCedula);
            tvEmpleados.getColumns().addAll(colEstado);
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

                    private final Button btn = new Button("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            EmpleadoDTO data = getTableView().getItems().get(getIndex());
                            //modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    
                    private final Button btn2 = new Button("Ver");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            EmpleadoDTO data = getTableView().getItems().get(getIndex());
                            //ver(data);
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

        tvEmpleados.getColumns().add(colBtn);

    }
    
}
