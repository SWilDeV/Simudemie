/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author Abergel Clement
 */
public class CountryDTO {
    public List<RegionDTO> Regions;
    public PopulationDTO populationDTO;
    public GeometricForm Shape;
    public String Name;
    public Color Color;
    public final UUID Id;
    public boolean IsSelected;
    
    public CountryDTO(Country country) {
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
        Shape.SetPosition(position);
        
        List<Point> pts = Shape.GetPoints();
        int regionCount = Regions.size();
        int width = Utility.Distance(pts.get(0), pts.get(1));
        int height = Utility.Distance(pts.get(1), pts.get(2));
        
        int stepY = height / regionCount;
        int heightY = stepY;
        Point topLeft = Utility.GetTopLeftPoint(pts);
        Point bottomRight = Utility.GetBottomRightPoint(pts);

        for(int y = 0; y < regionCount; y++) {
            Point bt = new Point(topLeft.x + width, topLeft.y + (y+1) * heightY);
            if(y == (regionCount-1)) {
               bt.y = Utility.GetBottomRightPoint(pts).y;
            }
            
            Regions.get(y).ModifyShape(new Point(topLeft.x, y * stepY +  topLeft.y), bt);
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
