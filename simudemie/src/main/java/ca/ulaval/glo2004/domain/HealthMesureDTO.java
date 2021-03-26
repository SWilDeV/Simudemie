/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.UUID;

/**
 *
 * @author charl
 */
public class HealthMesureDTO {
    public double AdhesionRate;
    public boolean IsActive = false;
    public String MesureName;
    public final UUID Id; 
    
    
    public HealthMesureDTO(HealthMesure mesure){
        AdhesionRate = mesure.getAdhesionRate();
        IsActive = mesure.getActive();
        MesureName = new String(mesure.getName());   
        Id = mesure.getID();
    }
    
}
