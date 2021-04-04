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
    
    public UUID GetId() {
        return id;
    }
    
    public Population getPopulation(){
        return subPopulation;
    }
    
    public void setPopulation(Population population) {
        this.subPopulation = population;
    }
    
    public void updateRegion(Region region){
      subPopulation.setTotalPopulation(region.getPopulation().getTotalPopulation());
      subPopulation.setCuredPopulation(region.getPopulation().getCuredPopulation());
      subPopulation.setInfectedPopulation(region.getPopulation().getInfectedPopulation());
      subPopulation.setNonInfectedPopulation(region.getPopulation().getNonInfectedPopulation());
      subPopulation.setDeadPopulation(region.getPopulation().getDeadPopulation());
    }

    public double getPercentagePop() {
        throw new UnsupportedOperationException("Not supported");
    }
    
    public void setPercentagePop(double percentagePop){    
    }
}
