/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.UUID;

/**
 *
 * @author Sean
 */
public class DiseaseDTO {
    private double infectionRateDTO;
    private double mortalityRateDTO;
    private double cureRateDTO;
    private UUID id;
    private String name;
    
    public DiseaseDTO(Disease disease) {
        infectionRateDTO = disease.getInfectionRate();
        mortalityRateDTO = disease.getMortalityRate();
        cureRateDTO = disease.getCureRate();
        name = disease.getName();
        id = disease.getId();
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
    
    public UUID getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    
    public void set(String name,double infectionRate, double mortalityRate, double cureRate){
        
    }
    
    public void setInfectionRateDTO(double newReproductionRate) {
        if ( newReproductionRate >= 0 && newReproductionRate <= 1){
            infectionRateDTO = newReproductionRate;
        }else{
            System.out.println("valeur de taux d'infection non comprise entre 0 et 100");
        }
    }
    
    public void setMortalityRateDTO(double newMortalityRate) {
        if ( newMortalityRate >= 0 && newMortalityRate <= 1){
            mortalityRateDTO = newMortalityRate;
        }else{
            System.out.println("valeur de taux de mortalité non comprise entre 0 et 100");
        }
    }
    
    public void setCureRateDTO(double newCureRate) {
        if ( newCureRate >= 0 && newCureRate <= 1){
            cureRateDTO = newCureRate;
        }else{
            System.out.println("valeur de taux de guérison non comprise entre 0 et 100");
        }
    }
    
}
