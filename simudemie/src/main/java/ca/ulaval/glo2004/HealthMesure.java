/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004;

/**
 *
 * @author melanietremblay
 */
public class HealthMesure {
    
    private double adhesionRate;
    private boolean isActive = false;
    
    public HealthMesure(double p_adhesionRate) {
        adhesionRate = p_adhesionRate;
    }
    
    public double getAdhesionRate() {
        return adhesionRate;
    }
    
    public void setAdhesionRate(double newAdhesionRate) {
        adhesionRate = newAdhesionRate;
    }
    
    public void activateMesure() {
        isActive = true;
    }
}
