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
 * @author Abergel Clement
 */
public class CountryDTO {
    public List<Region> Regions;
    public PopulationDTO populationDTO;
    public GeometricForm Shape;
    public String Name;
    public Color Color;
    public final UUID Id;
    public boolean IsSelected;
    
    public CountryDTO(Country country) {
        Regions = new ArrayList<>(country.GetRegions());
        populationDTO = new PopulationDTO(country.getPopulation());
        Name = country.getName();
        Color = country.getColor();
        
        GeometricForm countryShape = country.getShape();
        if(countryShape instanceof RegularForm) {
            Shape = new RegularForm(countryShape.GetPoints());
        } else {
            Shape = new IrregularForm(countryShape.GetPoints());
        }
        
        Id = country.GetId();
        IsSelected = country.IsSelected();
    }
    
    public PopulationDTO getPopulationDTO(){
        return populationDTO;
    }
   
    public void incrementTotalPopulationDTO(){
        int population = populationDTO.getTotalPopulationDTO();
        population +=1;
        populationDTO.setTotalPopulationDTO(population);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Name).append("\n");
        sb.append("---------\n");
        sb.append(populationDTO.toString());
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object other) {       
        if(other == null || !(other instanceof CountryDTO)){
            return false;
        }
        
        if(other == this) {
            return true;
        }
        
        CountryDTO countryDTO = (CountryDTO)other;
        return Id == countryDTO.Id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.Id);
        return hash;
    }
        
}
