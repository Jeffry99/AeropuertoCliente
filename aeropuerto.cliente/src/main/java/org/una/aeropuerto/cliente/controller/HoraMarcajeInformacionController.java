/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.EmpleadoDTO;
import org.una.aeropuerto.cliente.dto.HoraMarcajeDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.service.EmpleadoService;
import org.una.aeropuerto.cliente.service.HoraMarcajeService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class HoraMarcajeInformacionController implements Initializable {

    
    @FXML
    private Button btnVolver;

    /**
     * Initializes the controller class.
     */
    private String modalidad="";
    private HoraMarcajeService horaMarcajeService = new HoraMarcajeService();
    
    @FXML
    private Button btnMarcar;
    @FXML
    private Label lbFecha;
    @FXML
    private Label lbHora;
    @FXML
    private Label lbTipo;
    
    private EmpleadoDTO empleado = new EmpleadoDTO();
    @FXML
    private Rectangle rectangulo;
    @FXML
    private Label txtTipo;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        modalidad = (String) AppContext.getInstance().get("ModalidadHoraMarcaje");
        if(modalidad.equals("RolMarcaje")){
            pantalla="HoraMarcajeInformacion";
        }else{
            pantalla=null;
        }
        
        empleado=UsuarioAutenticado.getInstance().getUsuarioLogeado().getEmpleado();
        Respuesta res = horaMarcajeService.getByEmpleado(empleado.getId());
        if(res.getEstado()==null){
            lbFecha.setVisible(false);
            lbHora.setVisible(false);
            lbTipo.setVisible(false);
            rectangulo.setVisible(false);
            tipo=1;
            txtTipo.setText("Entrada");
        }else{
            ArrayList<HoraMarcajeDTO> horas = (ArrayList<HoraMarcajeDTO>) res.getResultado("HorasMarcajes");
            if(horas.isEmpty()){
                lbFecha.setVisible(false);
                lbHora.setVisible(false);
                lbTipo.setVisible(false);
                rectangulo.setVisible(false);
                tipo=1;
                txtTipo.setText("Entrada");
            }else{
                
                HoraMarcajeDTO ultima=new HoraMarcajeDTO();
                ultima=horas.get(0);
                if(horas.size()>=1){
                    for(int i=0; i<horas.size(); i++){
                        if(ultima.getId()<horas.get(i).getId()){
                            ultima=horas.get(i);
                        }
                    }
                }
                if(ultima.getTipo()==1){
                    tipo=2;
                    lbTipo.setText("Entrada");
                    txtTipo.setText("Salida");
                }else{
                    tipo=1;
                    lbTipo.setText("Salida");
                    txtTipo.setText("Entrada");
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                lbFecha.setText(formatter.format(ultima.getFechaRegistro()));
                formatter = new SimpleDateFormat("HH:mm:ss");
                lbHora.setText(formatter.format(ultima.getFechaRegistro()));
            }
            
        }
        
        
    }    

    
    
    
    
    private Boolean estado=true;
    
   

    
    Integer tipo;
    

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }

    
    private String pantalla="";
    
    public void volver(){
        
        try{
            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
            Contenedor.getChildren().clear();
            if(pantalla!=null){
                Parent root = FXMLLoader.load(App.class.getResource(pantalla + ".fxml"));
                Contenedor.getChildren().add(root);
            }   
        }catch(IOException ex){
            
        }
    }

    @FXML
    private void actMarcar(ActionEvent event) {
        HoraMarcajeDTO hora = new HoraMarcajeDTO();
        hora.setEmpleado(empleado);
        hora.setEstado(true);
        hora.setTipo(tipo);
        
        Respuesta res = horaMarcajeService.crear(hora);
        if(res.getEstado()==true){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de hora de marcaje", "Se registro la hora de marcaje con éxito"); 
            volver();
        }else{
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Registro de hora de marcaje", res.getMensaje());                  
        }
    }
    
}
