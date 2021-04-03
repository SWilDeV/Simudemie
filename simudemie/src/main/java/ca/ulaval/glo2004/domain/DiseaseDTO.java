/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

/**
 *
 * @author Sean
 */
public class DiseaseDTO {
    private double infectionRateDTO;
    private double mortalityRateDTO;
    private double cureRateDTO;
    
    public DiseaseDTO(double p_cureRate, double p_mortalityRate,
            double p_infectionRate) {
        
        infectionRateDTO = p_infectionRate;
        mortalityRateDTO = p_mortalityRate;
        cureRateDTO = p_cureRate;
    }
    
    public double getInfectionRateDTO() {
        return infectionRateDTO;
    }
    
    public double getMortalityRateDTO() {
        return mortalityRateDTO;
    }
    
    public double getCureRateDTO() {
        return cureRateDTO;
    }
    
    public void setInfectionRateDTO(double newReproductionRate) {
        if ( newReproductionRate >= 0 && newReproductionRate < 1){
            infectionRateDTO = newReproductionRate;
        }
    }
    
    public void setMortalityRateDTO(double newMortalityRate) {
        mortalityRateDTO = newMortalityRate;
    }
    
    public void setCureRateDTO(double newCureRate) {
        cureRateDTO = newCureRate;
    }
    
}
