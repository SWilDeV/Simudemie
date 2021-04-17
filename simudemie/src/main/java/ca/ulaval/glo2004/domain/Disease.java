package ca.ulaval.glo2004.domain;

import java.io.Serializable;
import java.util.UUID;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author melanietremblay
 */
public class Disease implements Serializable, Cloneable {
    private double infectionRate;
    private double mortalityRate;
    private double cureRate;
    private String name;
    private UUID id;
    
    public Disease() {
    }
    
    public Disease(String p_name, double p_cureRate, double p_mortalityRate, double p_infectionRate ) {
        id = UUID.randomUUID();
        name = p_name;
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
    
    public UUID getId() {
        return id;
    }
    
    public double getMortalityRate() {
        return mortalityRate;
    }
    
    public String getName(){
        return name;
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
    
    public void setName(String p_name){
        name= p_name;
    }
    
    public void updateFromDTO(DiseaseDTO diseaseDTO){
        name = diseaseDTO.getName();
        infectionRate = diseaseDTO.getInfectionRateDTO();
        mortalityRate = diseaseDTO.getMortalityRateDTO();
        cureRate = diseaseDTO.getCureRateDTO();
    }
    
    @Override
    public Disease clone() throws CloneNotSupportedException {
        Disease disease = null;
        try {
            disease = (Disease) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        
        return disease;
    }
}
