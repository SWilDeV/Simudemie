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
<<<<<<< HEAD
    public List<HealthMesureDTO> Mesures;
=======
>>>>>>> 51330a06a59bd12b7c29c856d9f521f0350aaed8
    public List<RegionDTO> Regions;
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
            
            List<Point> pts = new ArrayList<Point>() {
                {
                    add(new Point(r.Shape.GetPoint(0).x + diff.x, r.Shape.GetPoint(0).y + diff.y));
                    add(new Point(r.Shape.GetPoint(2).x + diff.x, r.Shape.GetPoint(2).y + diff.y));
                }
            };
            
            r.Shape = new RegularForm(Utility.ToRectangle(pts));
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
