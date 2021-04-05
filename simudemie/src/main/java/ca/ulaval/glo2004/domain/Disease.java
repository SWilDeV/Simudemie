package ca.ulaval.glo2004.domain;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author melanietremblay
 */
public class Disease implements Serializable {
    private double infectionRate;
    private double mortalityRate;
    private double cureRate;
    
    public Disease() {
    }
    
    public Disease(double p_cureRate, double p_mortalityRate,
            double p_infectionRate ) {
        infectionRate = p_infectionRate;
        mortalityRate = p_mortalityRate;
        cureRate = p_cureRate;
    }
    
     public double getCureRate() {
        return cureRate;
    }
    
    public double getInfectionRate() {
        return infectionRate;
    }
    
    public double getMortalityRate() {
        return mortalityRate;
    }
    
    public void setCureRate(double newCureRate) {
        if ( newCureRate >= 0 && newCureRate <= 1){
            cureRate = newCureRate;
        }else{
            System.out.println("valeur de taux de guérison non comprise entre 0 et 100");
        }
    }
    
    public void setInfectionRate(double newInfectionRate) {
        if ( newInfectionRate >= 0 && newInfectionRate <= 1){
            infectionRate = newInfectionRate;
        }else{
            System.out.println("valeur de taux d'infection non comprise entre 0 et 100");
        }
    }
    
    public void setMortalityRate(double newMortalityRate) {
        if ( newMortalityRate >= 0 && newMortalityRate <= 1){
            mortalityRate = newMortalityRate;
        }else{
            System.out.println("valeur de taux de mortalité non comprise entre 0 et 100");
        }
    }
    
    public void updateFromDTO(DiseaseDTO diseaseDTO){
        infectionRate = diseaseDTO.getInfectionRateDTO();
        mortalityRate = diseaseDTO.getMortalityRateDTO();
        cureRate = diseaseDTO.getCureRateDTO();
    }
}
