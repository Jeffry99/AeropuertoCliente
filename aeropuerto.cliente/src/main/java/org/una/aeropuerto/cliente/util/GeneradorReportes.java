/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.una.aeropuerto.cliente.dto.AerolineaDTO;
import org.una.aeropuerto.cliente.dto.AvionDTO;
import org.una.aeropuerto.cliente.dto.RutaDTO;
import org.una.aeropuerto.cliente.dto.ServicioRegistradoDTO;
import org.una.aeropuerto.cliente.dto.VueloDTO;
import org.una.aeropuerto.cliente.service.AvionService;
import org.una.aeropuerto.cliente.service.RutaService;
import org.una.aeropuerto.cliente.service.ServicioRegistradoService;
import org.una.aeropuerto.cliente.service.VueloService;
/**
 *
 * @author Jeffry
 */
public class GeneradorReportes {
    private final String tipoReporte;
    private String ruta;
    private final Date fechaInicio;
    private final Date fechaFinal;
    private Object parametro;
    private String alerta;
    private String tipoTransaccion;
    private ArrayList lista;
    private AerolineaDTO aerolinea;
    private RutaDTO rutaAvion;
    private String totalCobros;
    private String tipoFiltro;
    float total = 0;
    private String rutaJasper = "src/main/resources/org/una/aeropuerto/recursos/jrxml/";
    
    private final Map parameters = new HashMap<String, Object>();
    private final Busqueda busqueda = new Busqueda();
   
    public GeneradorReportes(String tipoReporte, String tipoFiltro, Object parametro, Date fechaInicio, Date fechaFinal){
        this.tipoReporte = tipoReporte;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.parametro = parametro;
        this.tipoFiltro = tipoFiltro;
        
        if(tipoReporte.equals("Recaudacion")){
            rutaJasper += "Recaudacion.jasper";
            tipoTransaccion = "Recaudación de Servicios";
            alerta = "La lista de cobros está vacía o no hay cobros en el rango de fechas especificado";
            
        }
        if(tipoReporte.equals("Recorrido")){
            rutaJasper += "Recorrido.jasper";
            tipoTransaccion = "Recorridos de Aviones";
            alerta = "La lista de recorridos está vacía o no hay recorridos en el rango de fechas especificado";
            if(tipoFiltro.equals("Aerolinea")){
                aerolinea = (AerolineaDTO)parametro;
            }
            if(tipoFiltro.equals("Zona")){
                rutaAvion = (RutaDTO)parametro;
            }
        }
        if(tipoReporte.equals("HorasExtra")){
            rutaJasper += "HorasExtra.jasper";
            tipoTransaccion = "Horas Extra";
            alerta = "La lista de horas está vacía o no hay horas en el rango de fechas especificado";
        }
        
        parameters.put("fechaInicio", fechaInicio.toString());
        parameters.put("fechaFinal", fechaFinal.toString());
        parameters.put("Parametro", parametro.toString());
    }
    
    public void generarReporte() throws JRException, IOException{
        
        lista = obtenerLista();
        if(lista.isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Reporte", alerta);
            return;
        }
        JasperPrint document = JasperFillManager.fillReport(rutaJasper, parameters, new JRBeanCollectionDataSource(lista));
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(document));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(seleccionarRuta() + "/Reporte.pdf"));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
        GenerarTransacciones.crearTransaccion("Se crea reporte", tipoTransaccion);
        
        if(!ruta.isEmpty()){
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Reporte", "Se ha creado el reporte exitosamente");
            abrirReporte();
        }
        ruta = "";
    }
    
    public void abrirReporte() throws IOException{
        File file = new File(ruta+"/Reporte.pdf");
        
        if(!Desktop.isDesktopSupported()){
            return;
        }
        
        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) desktop.open(file);
        
    }
    
    public String seleccionarRuta(){
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        java.awt.EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(GeneradorReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        f.showSaveDialog(null);

        if(f.getCurrentDirectory() != null){
            ruta = f.getSelectedFile().toString();
        }
        return ruta;
    }
    
    private ArrayList obtenerLista(){
        if(tipoReporte.equals("Recaudacion")){
            return obtenerServicios();
        }
        if(tipoReporte.equals("Recorrido")){
            return obtenerRecorridos();
        }
        if(tipoReporte.equals("HorasExtra")){
            return obtenerHorasExtra();
        }
        return null;
    }
    
    private ArrayList<ReporteServicioRegistradoDTO> obtenerServicios(){
        
        lista = new ArrayList<ReporteServicioRegistradoDTO>();   
           
        ArrayList<ReporteServicioRegistradoDTO> serviciosR = new ArrayList();

        ServicioRegistradoService servicioService = new ServicioRegistradoService();
        ArrayList<ServicioRegistradoDTO> servicios = new ArrayList();
        Respuesta respuesta = servicioService.getByTipoAproximate(parametro.toString());

        if(respuesta.getEstado()){
            servicios = (ArrayList<ServicioRegistradoDTO>) respuesta.getResultado("ServiciosAeropuerto");
            servicios = busqueda.busquedaRangoRecaudacion(fechaInicio, fechaFinal, servicios);
        }

        servicios.forEach(x -> {
            ReporteServicioRegistradoDTO servicio = new ReporteServicioRegistradoDTO();
            total += x.getCobro();
            servicio.cast(x);
            serviciosR.add(servicio);
        });
        totalCobros = "₡" + String.valueOf(total);
        parameters.put("totalCobros", totalCobros);
        return serviciosR;
    }
    
    private ArrayList<ReporteVueloDTO> obtenerRecorridos(){
        ArrayList<VueloDTO> vuelos = new ArrayList();
        ArrayList<ReporteVueloDTO> vuelosR = new ArrayList();
        lista = new ArrayList<ReporteVueloDTO>();
        if(tipoFiltro.equals("Aerolinea")){
            vuelos = busqueda.busquedaRangoRecorridos(fechaInicio, fechaFinal, obtenerVuelos());
        }
        if(tipoFiltro.equals("Zona")){
            vuelos = busqueda.busquedaRangoRecorridos(fechaInicio, fechaFinal, obtenerVuelosZona());
        }
        
        vuelos.forEach(x -> {
            ReporteVueloDTO vuelo = new ReporteVueloDTO();
            vuelo.cast(x);
            vuelosR.add(vuelo);
        });

        return vuelosR;
    }
    
    private ArrayList<AvionDTO> obtenerAviones(){
        AvionService avionService = new AvionService();
        ArrayList<AvionDTO> aviones = new ArrayList();
        Respuesta respuesta = avionService.getByAerolinea(aerolinea.getId());

        if(respuesta.getEstado()){
            aviones = (ArrayList<AvionDTO>) respuesta.getResultado("Aviones");
        }
        return aviones;
        
    }
    
    private ArrayList<VueloDTO> obtenerVuelosZona(){
        VueloService vueloService = new VueloService();
        ArrayList<VueloDTO> vuelos = new ArrayList();
        Respuesta respuesta = vueloService.getByRuta(rutaAvion.getId());
        if(respuesta.getEstado()){
            vuelos = (ArrayList<VueloDTO>) respuesta.getResultado("Vuelos");
        }
        return vuelos;
    }
    private ArrayList<VueloDTO> obtenerVuelos(){
        VueloService vueloService = new VueloService();
        ArrayList<VueloDTO> vuelos = new ArrayList();
        
        ArrayList<AvionDTO> aviones = obtenerAviones();
        
        aviones.forEach(x ->{
            Respuesta respuesta = vueloService.getByAvion(x.getId());
            
            if(respuesta.getEstado()){
                vuelos.addAll((ArrayList<VueloDTO>) respuesta.getResultado("Vuelos"));
            }
        });
        return vuelos;
    }
    
    private ArrayList<ReporteServicioRegistradoDTO> obtenerHorasExtra(){
        return null;
    }
}
