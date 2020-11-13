/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.una.aeropuerto.cliente.dto.AlertaGeneradaDTO;
import org.una.aeropuerto.cliente.dto.BitacoraAvionDTO;
import org.una.aeropuerto.cliente.dto.UsuarioAutenticado;
import org.una.aeropuerto.cliente.dto.VueloDTO;
import org.una.aeropuerto.cliente.service.AlertaGeneradaService;
import org.una.aeropuerto.cliente.service.BitacoraAvionService;
import org.una.aeropuerto.cliente.service.VueloService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class AlertasInformacionController implements Initializable {

    @FXML
    private Label lbTituloDetalle;
    @FXML
    private JFXTextArea descripcionAlerta;
    @FXML
    private Label matriculaAvion;
    @FXML
    private Label idVuelo;
    @FXML
    private Label fechaVuelo;
    @FXML
    private Label horaVuelo;
    @FXML
    private Label ruta;
    @FXML
    private Label distanciaRuta;
    @FXML
    private Label tipoAvion;
    @FXML
    private Label distanciaRecomendada;
    @FXML
    private Label distanciaRecorrida;
    @FXML
    private Label combustibleActual;
    @FXML
    private Label ubicacionActual;
    @FXML
    private Label fechaAlerta;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAutorizar;
    @FXML
    private Button btnRechazar;
    @FXML
    private Label estado;

    private String rolUsuario="";
    String modalidad="";
    AlertaGeneradaDTO alerta = new AlertaGeneradaDTO();
    
    SimpleDateFormat formaHora = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formaFecha = new SimpleDateFormat("dd/MM/yyyy");
    @FXML
    private ImageView btnInformacion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         rolUsuario=UsuarioAutenticado.getInstance().getRol();
        if(rolUsuario.equals("administrador")){
            descripcionAlerta.setEditable(false);
            btnInformacion.setVisible(true);
            btnInformacion.setDisable(false);
        }else{
            modalidad=(String) AppContext.getInstance().get("ModalidadAlerta");
            alerta=(AlertaGeneradaDTO) AppContext.getInstance().get("AlertaGenerada");
            if(modalidad.equals("Ver")){
                btnAutorizar.setDisable(true);
                btnRechazar.setDisable(true);
                btnAutorizar.setVisible(false);
                btnRechazar.setVisible(false);
            }

            descripcionAlerta.setText(alerta.getTipoAlerta().getDescripcion());
            if(alerta.getEstado()==2){
             estado.setText(alerta.getAutorizacion());
            }else{
                if(alerta.getEstado()==3){
                     estado.setText(alerta.getAutorizacion());
                }else{
                    if(alerta.getEstado()==1){
                        estado.setText("Sin autorizar");
                    }
                }
            }
            idVuelo.setText(alerta.getVuelo().getId().toString());
            fechaVuelo.setText(formaFecha.format(alerta.getVuelo().getFecha()));
            horaVuelo.setText(formaHora.format(alerta.getVuelo().getFecha()));
            ruta.setText(alerta.getVuelo().getRuta().getOrigen()+" - "+alerta.getVuelo().getRuta().getDestino());
            distanciaRuta.setText(String.valueOf(alerta.getVuelo().getRuta().getDistancia())+" Km");
            fechaAlerta.setText(formaFecha.format(alerta.getFechaModificacion())+" a las "+formaHora.format(alerta.getFechaModificacion()));
            matriculaAvion.setText(alerta.getVuelo().getAvion().getMatricula());
            tipoAvion.setText(alerta.getVuelo().getAvion().getTipoAvion().getNombre());
            distanciaRecomendada.setText(String.valueOf(alerta.getVuelo().getAvion().getTipoAvion().getDistanciaRecomendada())+" Km");

            BitacoraAvionService bitacoraService = new BitacoraAvionService();
            Respuesta res = bitacoraService.getByAvion(alerta.getVuelo().getAvion().getId());
            ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList<BitacoraAvionDTO>();
            if(res.getEstado()){
             bitacoras = (ArrayList<BitacoraAvionDTO>) res.getResultado("BitacorasAvion");
             BitacoraAvionDTO bitacoraMayor = new BitacoraAvionDTO();
             if(bitacoras.size()>0){
                 bitacoraMayor=bitacoras.get(0);
                 if(bitacoras.size()>1){
                     for(int i = 1; i<bitacoras.size(); i++){
                         if(bitacoras.get(i).getId()>bitacoraMayor.getId()){
                             bitacoraMayor = bitacoras.get(i);
                         }
                     }
                 }
             }
             distanciaRecorrida.setText(String.valueOf(bitacoraMayor.getDistanciaRecorrida()));
             combustibleActual.setText(String.valueOf(bitacoraMayor.getCombustible()));
             ubicacionActual.setText(bitacoraMayor.getUbicacion());
            } 
        }
        
        
    }    

    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }

    AlertaGeneradaService alertaService = new AlertaGeneradaService();
    VueloService vueloService = new VueloService();
    
    @FXML
    private void actAutorizar(ActionEvent event) {
        if(rolUsuario.equals("administrador")){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de botón", "fxID: btnAutorizar \n"+
                                                                                     "Acción: actAutorizar");
        }else{
            alerta.setEstado(2);
            alerta.setAutorizacion("Autorizada por "+UsuarioAutenticado.getInstance().getUsuarioLogeado().getEmpleado().getNombre());
            Respuesta res = alertaService.modificar(alerta.getId(), alerta);
            if(res.getEstado()){
                autorizacionVuelo();
                if(res.getEstado()){
                    AlertasController aController = (AlertasController) AppContext.getInstance().get("ControllerAlertas");
                    aController.cargarTodasAlertas();
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Autorización de vuelo", "Se ha autorizado esta alerta");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Autorizar solicitud de vuelo", "Ocurrió un error al modificar el estado del vuelo");
                }
            }else{
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Autorizar solicitud de vuelo", "Ocurrió un error al modificar el estado de la alerta");
            }    
        }
        
    
    }

    @FXML
    private void actRechazar(ActionEvent event) {
        if(rolUsuario.equals("administrador")){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de botón", "fxID: btnAutorizar \n"+
                                                                                     "Acción: actAutorizar");
        }else{
            alerta.setEstado(3);
            alerta.setAutorizacion("No autorizada por "+UsuarioAutenticado.getInstance().getUsuarioLogeado().getEmpleado().getNombre());
            Respuesta res = alertaService.modificar(alerta.getId(), alerta);
            if(res.getEstado()){
                autorizacionVuelo();
                if(res.getEstado()){
                    AlertasController aController = (AlertasController) AppContext.getInstance().get("ControllerAlertas");
                    aController.cargarTodasAlertas();
                    Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Autorización de vuelo", "Se ha rechazado esta alerta");
                    volver();
                }else{
                    Mensaje.showAndWait(Alert.AlertType.ERROR, "Rechazar solicitud de vuelo", "Ocurrió un error al modificar el estado del vuelo");
                }
            }else{
                Mensaje.showAndWait(Alert.AlertType.ERROR, "Rechazar solicitud de vuelo", "Ocurrió un error al modificar el estado de la alerta");
            }
        }
    }
    
    public void volver() {
        Stage stage = new Stage();
        stage = (Stage) btnAutorizar.getScene().getWindow();
        stage.close();
    }
    
    private void autorizacionVuelo(){
        VueloDTO vuelo = alerta.getVuelo();
        ArrayList<AlertaGeneradaDTO> alertas = new ArrayList<AlertaGeneradaDTO>();
        Respuesta respuesta = alertaService.getByVuelo(vuelo.getId());
        boolean ban=true;
        int autorizacion = 1;
        if(respuesta.getEstado().equals(true)){
            alertas = (ArrayList<AlertaGeneradaDTO>) respuesta.getResultado("AlertasGeneradas");
            for(int i=0; i<alertas.size(); i++){
                if(alertas.get(i).getEstado()!=2){
                    if(alertas.get(i).getEstado()==3){
                        autorizacion=3;
                    }
                    ban=false;
                }
            }
        }
        if(ban){
            vuelo.setEstado(2);
        }else{
            vuelo.setEstado(autorizacion);
        }
        vueloService = new VueloService();
        respuesta = vueloService.modificar(vuelo.getId(), vuelo);
        
    }

    @FXML
    private void acttxtDescripcion(MouseEvent event) {
        if(rolUsuario.equals("administrador")){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de text field", "fxID: descripcionAlerta \n"+
                            "Acción: usado para ver o cambiar la descripcion de la alerta generada");
        }
    }

    @FXML
    private void actVerInformacion(MouseEvent event) {
        Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Información de la vista", "FXML: AlertasInformacion \n"+
                                                            "Controller: AlertasController \n\n"+
                                                            "Información de este botón \n"+
                                                            "fxID: btnInformacion \n"+
                                                            "Acción: actVerInformacion");
    }

    
}
