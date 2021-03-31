package ca.ulaval.glo2004.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author melanietremblay
 */
public class Disease implements java.io.Serializable {
    private double reproductionRate;
    private double mortalityRate;
    private double cureRate;
    
    public Disease(double p_reproductionRate, double p_mortalityRate,
            double p_cureRate) {
        
        reproductionRate = p_reproductionRate;
        mortalityRate = p_mortalityRate;
        cureRate = p_cureRate;
    }
    
    public double getReproductionRate() {
        return reproductionRate;
    }
    
    public double getMortalityRate() {
        return mortalityRate;
    }
    
    public double getCureRate() {
        return cureRate;
    }
    
    public void setReproductionRate(double newReproductionRate) {
        if ( newReproductionRate >= 0 && newReproductionRate < 1){
            reproductionRate = newReproductionRate;
        }
        
    }
    
    public void setMortalityRate(double newMortalityRate) {
        mortalityRate = newMortalityRate;
    }
    
    public void setCureRate(double newCureRate) {
        cureRate = newCureRate;
    }
}
