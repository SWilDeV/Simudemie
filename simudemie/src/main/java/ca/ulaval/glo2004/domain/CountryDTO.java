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
import java.util.stream.Collectors;

/**
 *
 * @author Abergel Clement
 */
public class CountryDTO implements Serializable {
    public List<HealthMesureDTO> Mesures;
    public List<RegionDTO> Regions = new ArrayList<>();
    public PopulationDTO populationDTO;
    public GeometricForm Shape;
    public String Name;
    public Color Color;
    public final UUID Id;
    public boolean IsSelected;
    
    public CountryDTO(Country country) {
        Mesures = country.GetMesures().stream().map(m -> new HealthMesureDTO((HealthMesure)m)).collect(Collectors.toList());
        Regions = country.GetRegions().stream().map(r -> new RegionDTO((Region)r)).collect(Collectors.toList());
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
    
    public RegionDTO GetRegionDTO(UUID id) {
        return Regions.stream().filter(r -> r.Id.equals(id)).findFirst().get();     
    }
    
    public PopulationDTO getPopulationDTO(){
        return populationDTO;
    }
   
    public void incrementTotalPopulationDTO(){
        int population = populationDTO.getTotalPopulationDTO();
        population +=1;
        populationDTO.setTotalPopulationDTO(population);
    }
    
    public void SetPosition(Point position) {
        Point prevPosition = Shape.GetPoint(0);
        
        Shape.SetPosition(position);
        
        Point newPosition = Shape.GetPoint(0);
        Point diff = new Point(newPosition.x - prevPosition.x, newPosition.y - prevPosition.y);
        
        for(RegionDTO r: Regions) {
            List<Point> pts = new ArrayList<>();
            for(Point pt: r.Shape.GetPoints()) {
                pt.x += diff.x;
                pt.y += diff.y;
                pts.add(pt);
            }
            
            r.Shape = new IrregularForm(pts);
        }
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
