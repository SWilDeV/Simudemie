package ca.ulaval.glo2004;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author melanietremblay
 */
public class Disease {
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
        reproductionRate = newReproductionRate;
    }
    
    public void setMortalityRate(double newMortalityRate) {
        mortalityRate = newMortalityRate;
    }
    
    public void setCureRate(double newCureRate) {
        cureRate = newCureRate;
    }
}
