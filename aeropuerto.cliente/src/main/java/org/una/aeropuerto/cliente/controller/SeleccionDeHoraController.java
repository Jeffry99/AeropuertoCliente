/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import javax.json.bind.annotation.JsonbDateFormat;
import org.una.aeropuerto.cliente.util.AppContext;

/**
 * FXML Controller class
 *
 * @author Pablo-VE
 */
public class SeleccionDeHoraController implements Initializable {

    @FXML
    private Button btnAm;
    @FXML
    private Button btnPm;
    @FXML
    private JFXButton btn12;
    @FXML
    private JFXButton btn6;
    @FXML
    private JFXButton btn3;
    @FXML
    private JFXButton btn9;
    @FXML
    private JFXButton btn11;
    @FXML
    private JFXButton btn10;
    @FXML
    private JFXButton btn8;
    @FXML
    private JFXButton btn7;
    @FXML
    private JFXButton btn1;
    @FXML
    private JFXButton btn2;
    @FXML
    private JFXButton btn4;
    @FXML
    private JFXButton btn5;
    @FXML
    private Label lbTitulo;
    @FXML
    private Label horaMin1;
    @FXML
    private Label horaMin2;
    @FXML
    private Label horaMin3;
    @FXML
    private Label horaMin4;
    @FXML
    private Label lbHora;
    @FXML
    private Label lbMinutos;
    @FXML
    private Label lbTipo;
    @FXML
    private Label horaMin1112;
    @FXML
    private RadioButton btnMin00;
    @FXML
    private RadioButton btnMin15;
    @FXML
    private RadioButton btnMin30;
    @FXML
    private RadioButton btnMin45;
    @FXML
    private Button btnGuardar;

    /**
     * Initializes the controller class.
     */
    private HorariosInformacionController controllerHorarios;
    private String modalidad="";
    private String modalidadHorarios="";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        modalidad=(String) AppContext.getInstance().get("ModalidadSeleccionDeHora");
        if(modalidad.equals("Horarios")){
            controllerHorarios=(HorariosInformacionController) AppContext.getInstance().get("ControllerHorarios");
            modalidadHorarios=(String) AppContext.getInstance().get("ModalidadSeleccionDeHoraHorarios");
            if(modalidadHorarios.equals("Inicio")){
                lbTitulo.setText("Hora de inicio");
            }else{
                lbTitulo.setText("Hora final");
            }
        }else{
            lbTitulo.setText("Hora de vuelo");
        }
        
        // TODO
    }    

    
    
    @FXML
    private void actAm(ActionEvent event) {
        lbTipo.setText("AM");
    }

    @FXML
    private void actPm(ActionEvent event) {
        lbTipo.setText("PM");
    }

   
    public void setLabelMinutos(String h){
        horaMin1.setText(h);
        horaMin2.setText(h);
        horaMin3.setText(h);
        horaMin4.setText(h);
    }

    @FXML
    private void act12(ActionEvent event) {
        lbHora.setText("12");
        setLabelMinutos("12");
    }

    @FXML
    private void act6(ActionEvent event) {
        lbHora.setText("06");
        setLabelMinutos("06");
    }

    @FXML
    private void act3(ActionEvent event) {
        lbHora.setText("03");
        setLabelMinutos("03");
    }
    

    @FXML
    private void act9(ActionEvent event) {
        lbHora.setText("09");
        setLabelMinutos("09");
    }

    @FXML
    private void act11(ActionEvent event) {
        lbHora.setText("11");
        setLabelMinutos("11");
    }

    @FXML
    private void act10(ActionEvent event) {
        lbHora.setText("10");
        setLabelMinutos("10");
    }

    @FXML
    private void act8(ActionEvent event) {
        lbHora.setText("08");
        setLabelMinutos("08");
    }

    @FXML
    private void act7(ActionEvent event) {
        lbHora.setText("07");
        setLabelMinutos("07");
    }

    @FXML
    private void act1(ActionEvent event) {
        lbHora.setText("01");
        setLabelMinutos("01");
    }

    @FXML
    private void act2(ActionEvent event) {
        lbHora.setText("02");
        setLabelMinutos("02");
    }

    @FXML
    private void act4(ActionEvent event) {
        lbHora.setText("04");
        setLabelMinutos("04");
    }

    @FXML
    private void act5(ActionEvent event) {
        lbHora.setText("05");
        setLabelMinutos("05");
    }

    @FXML
    private void actMin00(ActionEvent event) {
        lbMinutos.setText("00");
        btnMin15.setSelected(false);
        btnMin30.setSelected(false);
        btnMin45.setSelected(false);
    }

    @FXML
    private void actMin15(ActionEvent event) {
        lbMinutos.setText("15");
        btnMin00.setSelected(false);
        btnMin30.setSelected(false);
        btnMin45.setSelected(false);
    }

    @FXML
    private void actMin30(ActionEvent event) {
        lbMinutos.setText("30");
        btnMin15.setSelected(false);
        btnMin00.setSelected(false);
        btnMin45.setSelected(false);
    }

    @FXML
    private void actMin45(ActionEvent event) {
        lbMinutos.setText("45");
        btnMin15.setSelected(false);
        btnMin30.setSelected(false);
        btnMin00.setSelected(false);
    }
    
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date horaAGuardarHorarios;
    
 
    Calendar horaGuardar;
    
    public int getHora(){
        int hora = Integer.valueOf(lbHora.getText());
        if(hora==12){
            hora=0;
        }
        if(lbTipo.getText().equals("PM")){
            hora+=12;
        }
        return hora;
    }
    
    public int getMinutos(){
        if(lbMinutos.getText().equals("00")){
            return 0;
        }else{
            return Integer.valueOf(lbMinutos.getText());
        }
    }
    
    

    @FXML
    private void actGuardar(ActionEvent event) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,getHora());
        cal.set(Calendar.MINUTE,getMinutos());
        
        if(modalidad.equals("Horarios")){
            horaAGuardarHorarios = cal.getTime();
            System.out.println("Hora a guardar: "+horaAGuardarHorarios);
            String hora = lbHora.getText()+":"+lbMinutos.getText()+" "+lbTipo.getText();
            controllerHorarios.setHora(horaAGuardarHorarios, hora, modalidadHorarios);
            Stage stage = (Stage) btn1.getScene().getWindow();
            stage.close();
        }
        
        
    }
    
}
