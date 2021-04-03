/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.UUID;

/**
 *
 * @author charl
 */
public class Region {
    
    private double percentagePop;
    private Population subPopulation;
    private final UUID id;
    
    public Region(int population){
        id = UUID.randomUUID();
        subPopulation = new Population(population);
    }
    
    public Population getPopulation(){
        return subPopulation;
    }
    
    public void setPopulation(Population population) {
        this.subPopulation = population;
    }
    
    
    //////////////////////////////////////////////////
    public double getPercentagePop() {
        throw new UnsupportedOperationException("Not supported");
    }
    
    public void setPercentagePop(double percentagePop){
        
    }
    
    
}
