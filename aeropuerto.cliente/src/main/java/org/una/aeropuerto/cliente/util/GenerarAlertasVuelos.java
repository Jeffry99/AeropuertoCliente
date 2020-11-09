/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.aeropuerto.cliente.util;

import java.util.ArrayList;
import javafx.scene.control.Alert;
import org.una.aeropuerto.cliente.dto.AlertaGeneradaDTO;
import org.una.aeropuerto.cliente.dto.BitacoraAvionDTO;
import org.una.aeropuerto.cliente.dto.TipoAlertaDTO;
import org.una.aeropuerto.cliente.dto.VueloDTO;
import org.una.aeropuerto.cliente.service.AlertaGeneradaService;
import org.una.aeropuerto.cliente.service.BitacoraAvionService;
import org.una.aeropuerto.cliente.service.TipoAlertaService;
import org.una.aeropuerto.cliente.service.VueloService;

/**
 *
 * @author Pablo-VE
 */
public class GenerarAlertasVuelos {
    
    public static void generarAlerta(VueloDTO vuelo){
        boolean estadoVuelo = true;
        AlertaGeneradaService alertaService = new AlertaGeneradaService();
        AlertaGeneradaDTO alerta = new AlertaGeneradaDTO();
        ArrayList<String> alertas = new ArrayList();
        ArrayList<TipoAlertaDTO> tiposDeAlerta = new ArrayList<TipoAlertaDTO>();
        TipoAlertaService tipoAlertaService = new TipoAlertaService();
        Respuesta res = tipoAlertaService.getAll();
        if(res.getEstado()){
            tiposDeAlerta = (ArrayList<TipoAlertaDTO>) res.getResultado("TiposAlertas");
        }

        estadoVuelo = alertaDistanciaRecomendada(vuelo, tiposDeAlerta, alertaService);
        if(!estadoVuelo){
            alertas.add(tiposDeAlerta.get(0).getDescripcion()+". Distancia recomendada: "+String.valueOf(vuelo.getAvion().getTipoAvion().getDistanciaRecomendada())+" Km - Distancia del vuelo: "+String.valueOf(vuelo.getRuta().getDistancia())+" Km"); 
        }
        estadoVuelo = alertaCombustible(vuelo, tiposDeAlerta, alertaService);
        if(!estadoVuelo){
            alertas.add(tiposDeAlerta.get(1).getDescripcion()); 
        }
        estadoVuelo = alertaDistanciaMaxima(vuelo, tiposDeAlerta, alertaService);
        if(!estadoVuelo){
            alertas.add(tiposDeAlerta.get(0).getDescripcion()+". Distancia máxima: "+String.valueOf(vuelo.getAvion().getTipoAvion().getDistanciaMaxima())+" Km - Distancia del vuelo: "+String.valueOf(vuelo.getRuta().getDistancia())+" Km"); 
        }
        String alertasPresentadas = "";
        if(alertas.size()>0){
            for(int i=0; i<alertas.size(); i++){
                alertasPresentadas += "\n- Alerta "+String.valueOf(i+1)+": "+alertas.get(i);
            }
        }
        VueloService vueloService = new VueloService();
        if(alertas.size()<=0){
            vuelo.setEstado(2);
            res = vueloService.modificar(vuelo.getId(), vuelo);
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de vuelo", "Vuelo registrado y autorizado correctamente");
        }else{
            vuelo.setEstado(1);
            res = vueloService.modificar(vuelo.getId(), vuelo);
            Mensaje.showAndWait(Alert.AlertType.INFORMATION, "Registro de vuelo", "Vuelo registrado correctamente pero en espera de autorización ya que se presentaron las siguientes alertas: "+alertasPresentadas);
        }
    }

    private static boolean alertaDistanciaRecomendada(VueloDTO vuelo, ArrayList<TipoAlertaDTO> tiposDeAlerta, AlertaGeneradaService alertaService) {
        if(vuelo.getRuta().getDistancia()>= vuelo.getAvion().getTipoAvion().getDistanciaRecomendada()){
            AlertaGeneradaDTO alerta = new AlertaGeneradaDTO();
            alerta.setEstado(1);
            alerta.setTipoAlerta(tiposDeAlerta.get(0));
            alerta.setVuelo(vuelo);
            Respuesta res = alertaService.crear(alerta);
            return false;
        }
        return true;
    }
    
    private static boolean alertaCombustible(VueloDTO vuelo, ArrayList<TipoAlertaDTO> tiposDeAlerta, AlertaGeneradaService alertaService) {
        BitacoraAvionService bitacoraService = new BitacoraAvionService();
        Respuesta res = bitacoraService.getByAvion(vuelo.getAvion().getId());
        ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList();
        BitacoraAvionDTO bitacoraMayor = new BitacoraAvionDTO();
        if(res.getEstado()){
            bitacoras = (ArrayList<BitacoraAvionDTO>) res.getResultado("BitacorasAvion");
            if(bitacoras.size()>0){
                bitacoraMayor = bitacoras.get(0);
            }
            if(bitacoras.size()>1){
                for(int i=1; i<bitacoras.size(); i++){
                    if(bitacoras.get(i).getId()>bitacoraMayor.getId()){
                        bitacoraMayor = bitacoras.get(i);
                    }
                }
            }
        }
        
        if(bitacoraMayor.getCombustible()<100){
            AlertaGeneradaDTO alerta = new AlertaGeneradaDTO();
            alerta.setEstado(1);
            alerta.setTipoAlerta(tiposDeAlerta.get(1));
            alerta.setVuelo(vuelo);
            res = alertaService.crear(alerta);
            return false;
        }
        return true;
    }
    
    private static boolean alertaDistanciaMaxima(VueloDTO vuelo, ArrayList<TipoAlertaDTO> tiposDeAlerta, AlertaGeneradaService alertaService) {
        float distancia80 = (80*vuelo.getAvion().getTipoAvion().getDistanciaMaxima())/100;
        
        BitacoraAvionService bitacoraService = new BitacoraAvionService();
        Respuesta res = bitacoraService.getByAvion(vuelo.getAvion().getId());
        ArrayList<BitacoraAvionDTO> bitacoras = new ArrayList();
        BitacoraAvionDTO bitacoraMayor = new BitacoraAvionDTO();
        if(res.getEstado()){
            bitacoras = (ArrayList<BitacoraAvionDTO>) res.getResultado("BitacorasAvion");
            if(bitacoras.size()>0){
                bitacoraMayor = bitacoras.get(0);
            }
            if(bitacoras.size()>1){
                for(int i=1; i<bitacoras.size(); i++){
                    if(bitacoras.get(i).getId()>bitacoraMayor.getId()){
                        bitacoraMayor = bitacoras.get(i);
                    }
                }
            }
        }
        
        if(bitacoraMayor.getDistanciaRecorrida()>=distancia80){
            AlertaGeneradaDTO alerta = new AlertaGeneradaDTO();
            alerta.setEstado(1);
            alerta.setTipoAlerta(tiposDeAlerta.get(2));
            alerta.setVuelo(vuelo);
            res = alertaService.crear(alerta);
            return false;
        }
        return true;
    }
    
}
