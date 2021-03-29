/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author charl
 */
public class Country {
    
    private List<Region> regions = new ArrayList<Region>();
    private Population population;
    private GeometricForm shape;
    private Color color;
    
    private final UUID id;
    
    public void incrementTotalPopulation(){
        int pop = population.getTotalPopulation();
        pop +=1;
        population.setTotalPopulation(pop);
    }
    public Country(GeometricForm form){
        id = UUID.randomUUID();
        shape = form;
        population = new Population();
    }
    
    public UUID GetId() {
        return id;
    }
    
    public List<Region> GetRegions() {
        return regions;
    }
    
    public Population getPopulation(){
        return population; //TODO : DTO
    }
    
    public void setPopulation(Population population) {
        this.population = population;
    }
    
    public void addRegion(Region region){
        regions.add(region);
    }
    
    public GeometricForm getShape(){
        return shape;
    }
    
    public void removeRegion(Region region){
        regions.remove(region);
    }
    
    public void modifyRegion(Region region){
        if (regions.contains(region)) {
            int index = regions.indexOf(region);
            regions.set(index, region);
        }
    }
    
    public void fromDTO(CountryDTO countryDTO){
        population.setTotalPopulation(countryDTO.getPopulationDTO().getTotalPopulationDTO());
    }
    
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported");
    }
    
    public void setColor(){
        
    }
    
    @Override
    public boolean equals(Object other) {       
        if(other == null || !(other instanceof Country)){
            return false;
        }
        
        if(other == this) {
            return true;
        }
        
        Country country = (Country)other;
        return id == country.GetId();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
}
