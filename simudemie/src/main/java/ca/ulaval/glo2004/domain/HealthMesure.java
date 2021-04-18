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
 * @author melanietremblay
 */
public class HealthMesure implements Serializable, Cloneable {
    
    protected double adhesionRate;
    protected boolean isActive = false;
    protected String mesureName;
    protected double threshold;
    private UUID id; 
    private static final long serialVersionUID = 6L; 
    
    public HealthMesure(double p_adhesionRate, boolean p_isActive, String p_mesureName, double p_threshold) {
        id = UUID.randomUUID();
        adhesionRate = p_adhesionRate;
        isActive = p_isActive;
        mesureName = p_mesureName;
        threshold = p_threshold;
    }
    
    public double getAdhesionRate() {
        return adhesionRate;
    }
    
    public UUID getID() {
        return id;
    }
    
    public void setAdhesionRate(double newAdhesionRate) {
        adhesionRate = newAdhesionRate;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public boolean getActive() {
        return isActive;
    }
    
    public String getName() {
        return mesureName;
    }
    
    public double getThreshold() {
        return threshold;
    }
    
    public void activateMesure() {
        isActive = true;
    }
    
    @Override
    public HealthMesure clone() throws CloneNotSupportedException {
        HealthMesure mesure = null;
        try {
            mesure = (HealthMesure) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        
        mesure.id = id;
        
        return mesure;
    }
}
