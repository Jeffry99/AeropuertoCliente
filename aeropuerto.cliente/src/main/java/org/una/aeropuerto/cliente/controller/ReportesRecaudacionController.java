/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.una.aeropuerto.cliente.App;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.dto.ServicioTipoDTO;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;
import org.una.aeropuerto.cliente.service.ServicioTipoService;
import org.una.aeropuerto.cliente.util.AppContext;
import org.una.aeropuerto.cliente.util.Busqueda;
import org.una.aeropuerto.cliente.util.GenerarTransacciones;
import org.una.aeropuerto.cliente.util.Mensaje;
import org.una.aeropuerto.cliente.util.ReporteServicioRegistradoDTO;
import org.una.aeropuerto.cliente.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Jeffry
 */
public class ReportesRecaudacionController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnVolver;
    @FXML
    private Label horaInicio;
    @FXML
    private Label horaDeFin;
    @FXML
    private Label txtEstado;
    @FXML
    private ComboBox<ServicioTipoDTO> cbServicio;
    @FXML
    private Button btnGenerar;
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFinal;
    
    private String ruta = "";

    Busqueda busquedas = new Busqueda();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initServicios();
    }    


    @FXML
    private void actVolver(ActionEvent event) {
        volver();
    }

    @FXML
    private void actGenerar(ActionEvent event) throws JRException, IOException {
        if(verificarCampos()){
            generarReporte();
        }
    }
    
    private void initServicios(){
        ServicioTipoService servicioService = new ServicioTipoService();
        ArrayList<ServicioTipoDTO> servicios = new ArrayList<ServicioTipoDTO>();
        Respuesta respuesta = servicioService.getByEstado(true);
        if(respuesta.getEstado()){
            servicios = (ArrayList<ServicioTipoDTO>) respuesta.getResultado("TiposServicios");
            ObservableList items = FXCollections.observableArrayList(servicios);
            cbServicio.setItems(items);
        }
    }
    
    private boolean verificarCampos(){
        if(cbServicio.getValue() == null){
            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione el servicio");
            return false;
        }
//        if(dpInicio.getValue() == null){
//            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la fecha de inicio");
//            return false;
//        }
//        if(dpFinal.getValue() == null){
//            Mensaje.showAndWait(Alert.AlertType.WARNING, "Faltan datos por ingresar", "Por favor seleccione la fecha de finalización");
//            return false;
//        }
        
        return true;
    }
    
    private void generarReporte() throws JRException, IOException{
        
        
        Map parameters = new HashMap<String, Object>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(dpInicio.getValue()).toString());
        parameters.put("fechaFinal", java.sql.Date.valueOf(dpFinal.getValue()).toString());
        parameters.put("servicioTipo", cbServicio.getValue().getNombre());
        
        ArrayList<ReporteServicioRegistradoDTO> listaCobros = obtenerListaCobros();
        
        if(listaCobros.isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Reporte", "Este tipo de servicio no tiene cobros, o no se encuentran en el rango de fechas especificado");
            return;
        }
        
        JasperPrint document = JasperFillManager.fillReport("src/main/resources/org/una/aeropuerto/recursos/jrxml/Recaudacion.jasper", parameters, new JRBeanCollectionDataSource(listaCobros));
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(document));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(seleccionarRuta() + "/Reporte.pdf"));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        //GenerarTransacciones.crearTransaccion("Se crea reporte", "Recaudación de Servicios");
        
        if(ruta != ""){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Reporte", "Se ha creado el reporte exitosamente");
            abrirReporte();
            volver();
        }
        ruta = "";
    }
    
    private void volver(){
//        try{
//            StackPane Contenedor = (StackPane) AppContext.getInstance().get("Contenedor");
//            Parent root = FXMLLoader.load(App.class.getResource("MenuPrincipal" + ".fxml"));
//            Contenedor.getChildren().clear();
//            Contenedor.getChildren().add(root);
//        }catch(IOException ex){}
    }
    
    private ArrayList<ReporteServicioRegistradoDTO> obtenerListaCobros(){
        ArrayList<ReporteServicioRegistradoDTO> serviciosR = new ArrayList();
        
        ServicioRegistradoService servicioService = new ServicioRegistradoService();
        ArrayList<ServicioRegistradoDTO> servicios = new ArrayList<ServicioRegistradoDTO>();
        Respuesta respuesta = servicioService.getByTipoAproximate(cbServicio.getValue().getNombre());
        if(respuesta.getEstado()){
            servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
            servicios = busquedas.busquedaRangoFechas(Date.valueOf(dpInicio.getValue()), Date.valueOf(dpFinal.getValue()), servicios);
        }
        
//        ArrayList<ServicioRegistradoDTO> servicioos = new ArrayList<ServicioRegistradoDTO>();
//        Respuesta respuestaa = servicioRegistradoService.getByTipoAproximate(txtTipo.getText());
//        if(respuesta.getEstado()){
//            servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
//        }
        
        servicios.forEach(x -> {
            ReporteServicioRegistradoDTO s = new ReporteServicioRegistradoDTO();
            s.casteo(x);
            serviciosR.add(s);
        });
        return serviciosR;
    }
    
    public String seleccionarRuta(){
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        java.awt.EventQueue.invokeLater(new Runnable(){
            public void run(){
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ReportesRecaudacionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(ReportesRecaudacionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ReportesRecaudacionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(ReportesRecaudacionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        f.showSaveDialog(null);

        if(f.getCurrentDirectory() != null){
            ruta = f.getSelectedFile().toString();
        }
        return ruta;
    }
    
    private void abrirReporte() throws IOException{
        File file = new File(ruta+"/Reporte.pdf");
        
        //first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            return;
        }
        
        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) desktop.open(file);
        
    }
}
