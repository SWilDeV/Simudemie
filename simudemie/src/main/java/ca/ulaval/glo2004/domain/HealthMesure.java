/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.UUID;

/**
 *
 * @author melanietremblay
 */
public abstract class HealthMesure {
    
    protected double adhesionRate;
    protected boolean isActive = false;
    protected String mesureName;
    private final UUID id; 
    
    public HealthMesure(double p_adhesionRate, boolean p_isActive, String p_mesureName) {
        id = UUID.randomUUID();
        adhesionRate = p_adhesionRate;
        isActive = p_isActive;
        mesureName = p_mesureName;
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
    
    public void activateMesure() {
        isActive = true;
    }
}
