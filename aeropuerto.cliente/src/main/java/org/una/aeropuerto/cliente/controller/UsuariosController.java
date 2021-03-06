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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.RolDTO;
import org.una.aeropuerto.cliente.dto.UsuarioDTO;
import org.una.aeropuerto.cliente.service.RolService;
import org.una.aeropuerto.cliente.service.UsuarioService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class UsuariosController implements Initializable {

    @FXML
    private TableView<UsuarioDTO> tvUsuarios;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnBuscarId;
    @FXML
    private Button btnBuscarCedula;
    @FXML
    private Button btnBuscarNombre;
    @FXML
    private Button btnBuscarEstado;
    @FXML
    private Button btnBuscarRol;
    @FXML
    private AnchorPane anPane;
    @FXML
    private JFXTextField txtCedula;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXComboBox<String> cbxEstado;
    @FXML
    private JFXComboBox<RolDTO> cbxRol;
    /**
     * Initializes the controller class.
     */
    private RolService rolService = new RolService();
    private UsuarioService usuarioService = new UsuarioService();
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList estados = new ArrayList();
        estados.add("Activo");
        estados.add("Inactivo");
        ObservableList items = FXCollections.observableArrayList(estados);   
        cbxEstado.setItems(items);
        
        ArrayList<RolDTO> roles = new ArrayList();
        Respuesta respuesta = rolService.getByEstado(true);
        if(respuesta.getEstado()==true){
            roles = (ArrayList<RolDTO>) respuesta.getResultado("Roles");
        }
        ObservableList items2 = FXCollections.observableArrayList(roles);   
        cbxRol.setItems(items2);
        
        cargarTodos();
    }    

    @FXML
    private void actVolver(ActionEvent event) {
    }


    @FXML
    private void actAgregar(ActionEvent event) {
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadEmpleadoUsuario", "Agregar");
        try{
            Parent root = FXMLLoader.load(App.class.getResource("EmpleadosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }

    @FXML
    private void actBuscarId(ActionEvent event) {
        if(!txtId.getText().isBlank()){
            ArrayList<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
            Respuesta respuesta = usuarioService.getById(Long.valueOf(txtId.getText()));
            if(respuesta.getEstado().equals(true)){
                UsuarioDTO usuario = (UsuarioDTO) respuesta.getResultado("Usuario");
                usuarios.add(usuario);
            }
            cargarTabla(usuarios);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el id del usuario que desea buscar");
        }
    }

    @FXML
    private void actBuscarCedula(ActionEvent event) {
        if(!txtCedula.getText().isBlank()){
            ArrayList<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
            Respuesta respuesta = usuarioService.getByCedula(txtCedula.getText());
            if(respuesta.getEstado().equals(true)){
                usuarios = (ArrayList<UsuarioDTO>) respuesta.getResultado("Usuarios");
            }
            cargarTabla(usuarios);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite la cédula del usuario que desea buscar");
        }
    }

    @FXML
    private void actBuscarNombre(ActionEvent event) {
        if(!txtNombre.getText().isBlank()){
            ArrayList<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
            Respuesta respuesta = usuarioService.getByNombre(txtNombre.getText());
            if(respuesta.getEstado().equals(true)){
                usuarios = (ArrayList<UsuarioDTO>) respuesta.getResultado("Usuarios");
            }
            cargarTabla(usuarios);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor digite el nombre del usuario que desea buscar");
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
            ArrayList<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
            Respuesta respuesta = usuarioService.getByEstado(estadoBuscar);
            if(respuesta.getEstado().equals(true)){
                usuarios = (ArrayList<UsuarioDTO>) respuesta.getResultado("Usuarios");
            }
            cargarTabla(usuarios);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el estado del usuario que desea buscar");
        }
    }
    
    private Long rolBuscar;
    
    @FXML
    private void actSelRol(ActionEvent event) {
        rolBuscar=cbxRol.getValue().getId();
    }

    @FXML
    private void actBuscarRol(ActionEvent event) {
        if(rolBuscar!=null){
            ArrayList<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
            Respuesta respuesta = usuarioService.getByRol(rolBuscar);
            if(respuesta.getEstado().equals(true)){
                usuarios = (ArrayList<UsuarioDTO>) respuesta.getResultado("Usuarios");
            }
            cargarTabla(usuarios);
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el rol del usuario que desea buscar");
        }
    }
    
    public void cargarTodos(){
        ArrayList<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
        Respuesta respuesta = usuarioService.getAll();
        if(respuesta.getEstado().equals(true)){
            usuarios = (ArrayList<UsuarioDTO>) respuesta.getResultado("Usuarios");
        }
        cargarTabla(usuarios);
    }
    
    public void cargarTabla(ArrayList<UsuarioDTO> usuarios){
        tvUsuarios.getColumns().clear();
        if(!usuarios.isEmpty()){
            ObservableList items = FXCollections.observableArrayList(usuarios);   
            
            TableColumn<UsuarioDTO, String> colNombre = new TableColumn("Nombre");
            colNombre.setCellValueFactory(usu -> {
                String nombreUsuario;
                nombreUsuario = usu.getValue().getEmpleado().getNombre();
                return new ReadOnlyStringWrapper(nombreUsuario);
            });
            TableColumn<UsuarioDTO, String> colCedula = new TableColumn("Cedula");
            colCedula.setCellValueFactory(usu -> {
                String cedula;
                cedula = usu.getValue().getEmpleado().getCedula();
                return new ReadOnlyStringWrapper(cedula);
            });
            TableColumn<UsuarioDTO, String> colRol = new TableColumn("Rol");
            colRol.setCellValueFactory(usu -> {
                String rol;
                rol = usu.getValue().getRol().getNombre();
                return new ReadOnlyStringWrapper(rol);
            });
            TableColumn<UsuarioDTO, String> colEstado = new TableColumn("Estado");
            colEstado.setCellValueFactory(usu -> {
                String estadoString;
                if(usu.getValue().getEstado())
                    estadoString = "Activo";
                else
                    estadoString = "Inactivo";
                return new ReadOnlyStringWrapper(estadoString);
            });
 
            tvUsuarios.getColumns().addAll(colNombre);
            tvUsuarios.getColumns().addAll(colCedula);
            tvUsuarios.getColumns().addAll(colRol);
            tvUsuarios.getColumns().addAll(colEstado);
            addButtonToTable();
            tvUsuarios.setItems(items);
        }
    }
    
    private void addButtonToTable() {
        TableColumn<UsuarioDTO, Void> colBtn = new TableColumn("Acciones");

        Callback<TableColumn<UsuarioDTO, Void>, TableCell<UsuarioDTO, Void>> cellFactory = new Callback<TableColumn<UsuarioDTO, Void>, TableCell<UsuarioDTO, Void>>() {
            @Override
            public TableCell<UsuarioDTO, Void> call(final TableColumn<UsuarioDTO, Void> param) {
                final TableCell<UsuarioDTO, Void> cell = new TableCell<UsuarioDTO, Void>() {
                    
                    
                    /*Image ima=null; 
                    {
                        try {
                            ima = new Image(new File("src/main/resources/org/una/aeropuerto/cliente/recursos/editar.png").toURI().toURL().toExternalForm());
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(UsuariosController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    ImageView ivEditar = new ImageView(ima);
                    
                    
                        ivEditar.setFitHeight(25);
                        ivEditar.setFitWidth(25);
                        ivEditar.setOnMouseClicked(event -> {
                            try{
                            UsuarioDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }*/
                    
                    private final Button btn = new Button("Editar");
                    {
                        //btn.setGraphic(ivEditar);
                        btn.setOnAction((ActionEvent event) -> {
                            try{
                            UsuarioDTO data = getTableView().getItems().get(getIndex());
                            modificar(data);
                            }catch(Exception ex){}
                        });
                    }
                    private final Button btn2 = new Button("Ver");
                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            try{
                            UsuarioDTO data = getTableView().getItems().get(getIndex());
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
        tvUsuarios.getColumns().add(colBtn);
    }
    
    public void ver(UsuarioDTO usuario){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadEmpleadoUsuario", "Ver");
        AppContext.getInstance().set("UsuarioEnCuestion", usuario);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("EmpleadosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
    
    public void modificar(UsuarioDTO usuario){
        StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
        AppContext.getInstance().set("ModalidadEmpleadoUsuario", "Modificar");
        AppContext.getInstance().set("UsuarioEnCuestion", usuario);
        try{
            Parent root = FXMLLoader.load(App.class.getResource("EmpleadosInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        };
    }
}
