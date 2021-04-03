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
    private double infectionRate;
    private double mortalityRate;
    private double cureRate;
    
    public Disease(double p_cureRate, double p_mortalityRate,
            double p_infectionRate ) {
        
        infectionRate = p_infectionRate;
        mortalityRate = p_mortalityRate;
        cureRate = p_cureRate;
    }
    
    public double getInfectionRate() {
        return infectionRate;
    }
    
    public double getMortalityRate() {
        return mortalityRate;
    }
    
    public double getCureRate() {
        return cureRate;
    }
    
    public void setInfectionRate(double newInfectionRate) {
        if ( newInfectionRate >= 0 && newInfectionRate < 1){
            infectionRate = newInfectionRate;
        }
        
    }
    
    public void setMortalityRate(double newMortalityRate) {
        mortalityRate = newMortalityRate;
    }
    
    public void setCureRate(double newCureRate) {
        cureRate = newCureRate;
    }
    
    public void updateFromDTO(DiseaseDTO diseaseDTO){
        infectionRate = diseaseDTO.getInfectionRateDTO();
        mortalityRate = diseaseDTO.getMortalityRateDTO();
        cureRate = diseaseDTO.getCureRateDTO();
    }
}
