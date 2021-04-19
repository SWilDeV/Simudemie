/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author charl
 */
public class HealthMesureDTO implements Serializable {
    public double AdhesionRate;
    public boolean IsActive = false;
    public String MesureName;
    public final UUID Id; 
    public double threshold;
    public double effectTransmissionRate;
    public double effectReproductionRate;
    
    
    public HealthMesureDTO(HealthMesure mesure){
        AdhesionRate = mesure.getAdhesionRate();
        IsActive = mesure.getActive();
        MesureName = new String(mesure.getName());   
        Id = mesure.getID();
        threshold = mesure.getThreshold();
        effectTransmissionRate = mesure.getEffectTransmissionRate();
        effectReproductionRate = mesure.getEffectReproductionRate();
    }
    
}
