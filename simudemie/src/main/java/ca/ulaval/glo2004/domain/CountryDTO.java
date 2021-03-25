/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Abergel Clement
 */
public class CountryDTO {
    public List<Region> Regions;
    public Population Population;
    public GeometricForm Shape;
    public final UUID Id;
    
    public CountryDTO(Country country) {
        Regions = new ArrayList<>(country.GetRegions());
        Population = country.getPopulation();
        
        GeometricForm countryShape = country.getShape();
        if(countryShape instanceof RegularForm) {
            Shape = new RegularForm(countryShape.GetPoints());
        } else {
            Shape = new IrregularForm(countryShape.GetPoints());
        }
        
        Id = country.GetId();
    }
}
