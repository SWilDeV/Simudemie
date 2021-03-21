/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Color;
import java.util.List;

/**
 *
 * @author charl
 */
public class Country {
    
    private List<Region> regionList;
    private Population population;
    private GeometricForm shape;
    private Color color;
    
    
    public Country(){
        shape = new RegularForm();
    }
    
    public Population getPopulation(){
        throw new UnsupportedOperationException("Not supported");
    }
    
    public void setPopulation(){
        
    }
    
    public void addRegion(){
        
    }
    
    public GeometricForm getShape(){
        return shape;
    }
    
    public void removeRegion(){
        
    }
    
    public void modifyRegion(){
        
    }
    
    public Color getColor(){
        throw new UnsupportedOperationException("Not supported");
    }
    
    public void setColor(){
        
    }
    
}
