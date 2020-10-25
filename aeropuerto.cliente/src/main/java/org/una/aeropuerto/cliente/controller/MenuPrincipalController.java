/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class MenuPrincipalController implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem btnUsuarios;
    @FXML
    private MenuItem btnRoles;
    @FXML
    private MenuItem btnHorarios;
    @FXML
    private MenuItem btnHoraMarcaje;
    @FXML
    private MenuItem btnTransacciones;
    @FXML
    private StackPane Contenedor;
    @FXML
    private Menu TituloUsuario;
    @FXML
    private MenuItem btnAerolineas;
    @FXML
    private MenuItem btnAviones;
    @FXML
    private MenuItem btnEmpleados;
    @FXML
    private MenuItem btnTiposAviones;
    @FXML
    private Button btnRegistrarMarcaje;
    @FXML
    private Menu TituloAdministracion;
    private Menu TituloAviones;
    @FXML
    private MenuItem btnServiciosRegistrados;
    @FXML
    private MenuItem btnAreasTrabajos;
    @FXML
    private MenuItem btnTrabajosEmpleados;
    @FXML
    private MenuItem btnRutas;
    @FXML
    private MenuItem btnBitacoraAviones;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppContext.getInstance().set("Contenedor", this.Contenedor);
        TituloUsuario.setText(UsuarioAutenticado.getInstance().getUsuarioLogeado().getEmpleado().getNombre());
        
        if(UsuarioAutenticado.getInstance().getUsuarioLogeado().getRol().getNombre().equals("Marcaje")){
            TituloAdministracion.setDisable(true);
            TituloAviones.setDisable(true);
            btnRegistrarMarcaje.setDisable(true);
            
            TituloAdministracion.setVisible(false);
            TituloAviones.setVisible(false);
            btnRegistrarMarcaje.setVisible(false);
            registrarMarcaje("RolMarcaje");
        }
        
    }   
    
    public void registrarMarcaje(String modalidad){
        try{
            AppContext.getInstance().set("ModalidadHoraMarcaje", modalidad);
            Parent root = FXMLLoader.load(App.class.getResource("HoraMarcajeInformacion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }


    @FXML
    private void actUsuarios(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("Usuarios" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
            GenerarTransacciones.crearTransaccion("Se observan usuarios", "MenuPrincipal");
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actRoles(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("Roles" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
            GenerarTransacciones.crearTransaccion("Se observan roles", "MenuPrincipal");
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actHorarios(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("Horarios" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
            GenerarTransacciones.crearTransaccion("Se observan horarios", "MenuPrincipal");
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actHoraMarcaje(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("HoraMarcaje" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
            GenerarTransacciones.crearTransaccion("Se observan horas de marcaje", "MenuPrincipal");
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actTransacciones(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("Transacciones" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
            GenerarTransacciones.crearTransaccion("Se observan transacciones", "MenuPrincipal");
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actVerInformacion(ActionEvent event) {
    }

    @FXML
    private void actCerrarSesion(ActionEvent event) {
    }

    @FXML
    private void actAviones(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("Aviones" + ".fxml"));
			Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
	}
    @FXML
    private void actAerolineas(ActionEvent event) {
         try{
            Parent root = FXMLLoader.load(App.class.getResource("Aerolineas" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actAreasTrabajos(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("AreasTrabajo" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actTrabajosEmpleados(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TrabajosEmpleados" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }


    @FXML
    private void actRutas(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("Rutas" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actTiposAviones(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("TiposAvion" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
            GenerarTransacciones.crearTransaccion("Se observan transacciones", "MenuPrincipal");
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actRegistrarMarcaje(ActionEvent event) {
        registrarMarcaje("OtroRol");
    }
    @FXML
    private void actEmpleados(ActionEvent event) {
    }

    @FXML
    private void actServiciosRegistrados(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("Servicios" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
            GenerarTransacciones.crearTransaccion("Se observan servicios", "MenuPrincipal");
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }

    @FXML
    private void actBitacora(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(App.class.getResource("Bitacora" + ".fxml"));
            Contenedor.getChildren().clear();
            Contenedor.getChildren().add(root);
            GenerarTransacciones.crearTransaccion("Se observan bitácoras", "MenuPrincipal");
        }catch(IOException ex){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :c", "Se ha producido un error inesperado en la aplicación");
        }
    }
}