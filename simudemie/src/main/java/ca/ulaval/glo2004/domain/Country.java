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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author charl
 */
public class Country implements Serializable {
    
    private List<Region> regions = new ArrayList<>();
    private Population population;
    private GeometricForm shape;
    private Color color;
    private String name;
    private final UUID id;
    private boolean isSelected;
    
    public Country(GeometricForm form, String countryName, int countryPop){
        id = UUID.randomUUID();
        shape = form;
        name = countryName;
        
        population = new Population(countryPop);
        
        this.addRegion(1.0);
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
    
    public UUID GetId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Population getPopulation(){
        return population;
    }
    
    public GeometricForm getShape(){
        return shape;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    public boolean IsSelected() {
        return isSelected;
    }
    
    public void setName(String countryName) {
        name = countryName;
    }
    
    public void setPopulation(Population population) {
        this.population = population;
    }
    
    public void updateCountryPopulation(Country country){
        int totalPopulation =0;
        int infectedPopulation =0;
        int nonInfectedPopulation =0;
        int deadPopulation =0;
        for(Region region:regions){
            nonInfectedPopulation += region.getPopulation().getNonInfectedPopulation();
            infectedPopulation += region.getPopulation().getInfectedPopulation();
            totalPopulation += region.getPopulation().getTotalPopulation();
            deadPopulation += region.getPopulation().getDeadPopulation();
        }
        population.setTotalPopulation(totalPopulation);
        population.setInfectedPopulation(infectedPopulation);
        population.setNonInfectedPopulation(nonInfectedPopulation);
        population.setDeadPopulation(deadPopulation);
    }
    
    public void SetPosition(Point position) {
        shape.SetPosition(position);
    }
    
    public void SetSelectionState(boolean select) {
        isSelected = select;
    }
    
    public void splitNewInfectedInRegions(int newInfected){
        while (newInfected >0){
            Random rand = new Random();
            int index = rand.nextInt(regions.size());

            int counter = 0;
            for(Region region:regions){
                if(index == counter){
                    Population population = region.getPopulation();
                    int infected = population.getInfectedPopulation();

                    Random randPop = new Random();
                    int randInfected = randPop.nextInt(newInfected);

                    if(randInfected == 0){
                        newInfected = 0;
                    }else{
                        infected +=randInfected;
                        population.setInfectedPopulation(infected);
                        region.setPopulation(population);
                        newInfected -=randInfected;
                    }
                }
                counter +=1;
            }
        }
    }
    
    
    
    /////////////////REGIONS////////////////////////
    
    public void addRegion(double popPercentage) {
//        double total_percentage = 0.0;
//        total_percentage = regions.stream().map(r -> r.getPercentagePop()).reduce(total_percentage, (accumulator, _item) -> accumulator + _item);
//        
//        double rest_percentage_to_allow = 1-popPercentage;
//        if(rest_percentage_to_allow >= 0) {
//            if(popPercentage < rest_percentage_to_allow) {
//                popPercentage = rest_percentage_to_allow;
//            }
//        } else {
//            popPercentage = 0;
//        }
        
        Region region = new Region(population.getTotalPopulation(), popPercentage);
        regions.add(region);
    }
    
//    public void addRegionsToList(int population){
//        long numberOfRegion=4;   //A changer !!
//        int regionPop = (int)Math.round(population/numberOfRegion);       
//        for(int i =0; i< numberOfRegion; i++){
//            regions.add(new Region(population.getTotalPopulation(), regionPop));
//        }
//    }
    
    public Region FindRegionByUUID(UUID id) {
        try {
            return regions.stream().filter(c -> c.GetId().equals(id)).findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
    }
    
    public List<Region> GetRegions() {
        return regions;
    }
    
    public void UpdateRegion(RegionDTO region) {
        Region r = FindRegionByUUID(region.Id);
        if(r != null) {
            r.SetPercentage(population.getTotalPopulation(), region.PercentagePop);
        }
    }
    
    public void ValidateRegions() {
        double totalPercentage = 0.0;
        for(Region r: regions) {
            totalPercentage += r.getPercentagePop();
            if(totalPercentage > 1) {
                r.SetPercentage(population.getTotalPopulation(), 0.0);
            }
        }
    }
    
    public void removeRegion(UUID regionId){//faudrait ajouter un index ou un id de la region
        Region r = FindRegionByUUID(regionId);
        if(r != null) {
            regions.remove(r);
        }
    }
    
    public void setPopulationToRegion(Population population){//faudrait ajouter un index ou un id de la region
        for(Region region:regions){
            //if(region.id == id){
            region.setPopulation(population);
            //}
        }  
    }
//    
//    public void updateRegion(Region region) {
//        Region r = FindRegionByUUID(region.GetId());
//        if(r != null){
//            r.updateRegion(region);
//        }
//    }
}
