/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author charl
 */
public class Country implements Serializable {
    
    private List<Region> regions ;
    private Population population;
    private int PopulationNumber;
    private GeometricForm shape;
    private Color color;
    private String name;
    private final UUID id;
    private boolean isSelected;
    
    public Country(GeometricForm form, String countryName, int countryPop){
        id = UUID.randomUUID();
        shape = form;
        name = countryName;
        regions = new ArrayList<>() ;
        population = new Population(countryPop);
        PopulationNumber = countryPop;
    }
    
    public UUID GetId() {
        return id;
    }
    
    public boolean IsSelected() {
        return isSelected;
    }
    
    public List<Region> GetRegions() {
        return regions;
    }
    
    public Population getPopulation(){
        return population;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String countryName) {
        name = countryName;
    }
    
    public void setPopulation(Population population) {
        this.population = population;
    }
    
    public void addRegionsToList(int population){
        int numberOfRegion=4;   //A changer !!
        for(int i =0; i< numberOfRegion; i++){
            regions.add(new Region(population));
        }
        //regions.add(region);
    }
    public void addRegion(Region region){
        regions.add(region);
    }
    
    public void setPopulationToRegion(Population population){
        for(Region region:regions){
            region.setPopulation(population);
        }  
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
    
    public void fromCountryDTO(CountryDTO countryDTO){
        if(countryDTO.Shape instanceof RegularForm) {
            shape = new RegularForm(countryDTO.Shape.GetPoints());
        } else {
            shape = new IrregularForm(countryDTO.Shape.GetPoints());
        }

        population.setTotalPopulation(countryDTO.getPopulationDTO().getTotalPopulationDTO());
        population.setCuredPopulation(countryDTO.getPopulationDTO().getCuredPopulationDTO());
        population.setInfectedPopulation(countryDTO.getPopulationDTO().getInfectedPopulationDTO());
        population.setNonInfectedPopulation(countryDTO.getPopulationDTO().getNonInfectedPopulationDTO());
        population.setDeadPopulation(countryDTO.getPopulationDTO().getDeadPopulationDTO());
    }
    
    public void fromCountry(Country country){
        for(Region region :regions){
            population.setTotalPopulation(region.getPopulation().getTotalPopulation());
            population.setCuredPopulation(region.getPopulation().getCuredPopulation());
            population.setInfectedPopulation(region.getPopulation().getInfectedPopulation());
            population.setNonInfectedPopulation(region.getPopulation().getNonInfectedPopulation());
            population.setDeadPopulation(region.getPopulation().getDeadPopulation());
        }
        
    }
    
    public Color getColor() {
        double percentageInfected = this.population.getInfectedPopulation() / this.population.getTotalPopulation(); 
        //TODO: mel à retravailler, car je ne suis pas certaine que c'est la bonne facon de faire
        if (percentageInfected >= 0.25 && percentageInfected < 0.5) {
            this.color = new Color(255,255,0); 
        } else if (percentageInfected >= 0.5) {
            this.color = new Color(255,0,0);
        } else {
            this.color = new Color(50,205,50);
        }
        return this.color;
    }
    
    public void SetPosition(Point position) {
        shape.SetPosition(position);
    }
    
    public void SetSelectionState(boolean select) {
        isSelected = select;
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
