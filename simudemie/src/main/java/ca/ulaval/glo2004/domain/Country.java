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
import java.util.stream.Collectors;

/**
 *
 * @author charl
 */
public class Country implements Serializable  {
    
    private List<HealthMesure> mesures = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();
    private Population population;
    private GeometricForm shape;
    private Color color;
    private String name;
    private final UUID id;
    private boolean isSelected;
    private static final long serialVersionUID = 2L; 
    
    public Country(GeometricForm form, String countryName, int countryPop){
        id = UUID.randomUUID();
        shape = form;
        name = countryName;
        
        population = new Population(countryPop);
        
        Point topLeft = Utility.GetTopLeftPoint(shape.GetPoints());
        List<Point> pts = new ArrayList<Point>(){
            {
                add(new Point(topLeft.x + 10, topLeft.y + 10));
                add(new Point(topLeft.x + 50, topLeft.y + 50));
            }  
        };
        
        addRegion(Utility.ToRectangle(pts), "Region 1", 1.0);
    }
    
    public void AddMesure(HealthMesure mesure) {
        mesures.add(mesure);
    }
    
    public void RemoveMesure(UUID mesureId) {
        HealthMesure mesure = null;
        for(HealthMesure m: mesures) {
            if(m.getID().equals(mesureId)) {
                mesure = m;
                break;
            }
        }
        
        mesures.remove(mesure);
    }
    
    public List<HealthMesure> GetMesures() {
        return mesures;
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
        
        regions = countryDTO.Regions.stream().map(r -> r.toRegion()).collect(Collectors.toList());
        population.setTotalPopulation(countryDTO.getPopulationDTO().getTotalPopulationDTO());
        population.setCuredPopulation(countryDTO.getPopulationDTO().getCuredPopulationDTO());
        population.setInfectedPopulation(countryDTO.getPopulationDTO().getInfectedPopulationDTO());
        population.setNonInfectedPopulation(countryDTO.getPopulationDTO().getNonInfectedPopulationDTO());
        population.setDeadPopulation(countryDTO.getPopulationDTO().getDeadPopulationDTO());
    }
    
    public Color getColor() {
        double p = 1.0;
        if(population.getTotalPopulation() > 0) {
            if(population.getInfectedPopulation() > 0) {
                p = (population.getInfectedPopulation()) / (double)(population.getTotalPopulation() + population.getDeadPopulation());
                p = (population.getInfectedPopulation() + population.getDeadPopulation()) / (double)(population.getTotalPopulation() + population.getDeadPopulation());
                if(p > 1) {
                    p = 1;
                }
            } else {
                p = 0.0;
            }
        }
        
        return Utility.GetColorGradient(p);
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
    
    public void UpdateSelectionStateRegion(UUID regionId, boolean select) {
        Region region = regions.stream().filter(r -> r.GetId().equals(regionId)).findFirst().get();
        if(region != null) {
            region.SetSelectionState(select);
        }
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
    
    public void addRegion(List<Point> points, String regionName, double popPercentage) {       
        RegularForm form = new RegularForm(points);
        Region region = new Region(form, regionName, population.getTotalPopulation(), popPercentage);
        regions.add(region);
    }
    
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
    
    public void ValidateRegions() throws NotAllPopulationAssign {
        double totalPercentage = 0.0;
        for(Region r: regions) {
            totalPercentage += r.getPercentagePop();
            if(totalPercentage > 1) {
                throw new NotAllPopulationAssign("Les régions ont plus de population que le pays!");
            }
        }
        
        if(totalPercentage != 1.0) {
            throw new NotAllPopulationAssign("La population total n'as pas été complètement assigné.");
        }
    }
    
    public void removeRegion(UUID regionId){//faudrait ajouter un index ou un id de la region
        Region r = FindRegionByUUID(regionId);
        if(r != null) {
            regions.remove(r);
        }
    }
    
    public void setPopulationToRegion(Population population){ //faudrait ajouter un index ou un id de la region
        for(Region region:regions){
            region.setPopulation(population);
        }  
    }
}
