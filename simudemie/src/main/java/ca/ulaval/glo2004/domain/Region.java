/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Color;
import java.io.Serializable;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author charl
 */
public class Region implements Serializable {
    
    private String name;
    private double percentagePop;
    private Population subPopulation;
    private RegularForm shape;
    private final UUID id;
    private boolean isSelected = false;
    
    public Region(RegularForm form, String name, int countryPopulation, double percentage) {
        name = this.name;
        shape = form;
        id = UUID.randomUUID();
        SetPercentage(countryPopulation, percentage);
    }
    
    public Region(UUID id, RegularForm form, Population population, double percentage, boolean isSelected, String name) {
        shape = form;
        this.id = id;
        this.name = name;
        subPopulation = population;
        percentagePop = percentage;
        this.isSelected = isSelected;
    }
    
    public String GetName() {
        return name;
    }
    
    public boolean IsSelected() {
        return isSelected;
    }
    
    public void SetSelectionState(boolean select) {
        isSelected = select;
    }
    
    public void ModifyShape(Point p1, Point p2) {
        List<Point> pts = new ArrayList<Point>(){
            {
                add(p1);
                add(p2);
            }
        };
        shape = new RegularForm(Utility.ToRectangle(pts));
    }
    
    public RegularForm GetShape() {
        return shape;
    }
    
    public UUID GetId() {
        return id;
    }
    
    public Population getPopulation(){
        return subPopulation;
    }
    
    public void SetPercentage(int countryPopulation, double percentage) {
        percentagePop = percentage;
        subPopulation = new Population((int)(countryPopulation * percentage));
    }
    
    public void setPopulation(Population population) {
        this.subPopulation = population;
    }
    
    public void updateRegion(Region region){
      subPopulation.setTotalPopulation(region.getPopulation().getTotalPopulation());
      subPopulation.setCuredPopulation(region.getPopulation().getCuredPopulation());
      subPopulation.setInfectedPopulation(region.getPopulation().getInfectedPopulation());
      subPopulation.setNonInfectedPopulation(region.getPopulation().getNonInfectedPopulation());
      subPopulation.setDeadPopulation(region.getPopulation().getDeadPopulation());
    }

    public double getPercentagePop() {
        return percentagePop;
    }
    
    public Color GetColor() {
        double p = 1.0;
        if(subPopulation.getTotalPopulation() > 0) {
            if(subPopulation.getInfectedPopulation() > 0) {
                p = (subPopulation.getInfectedPopulation() + subPopulation.getDeadPopulation()) / (double)(subPopulation.getTotalPopulation() + subPopulation.getDeadPopulation());
                if(p > 1) {
                    p = 1;
                }
            } else {
                p = 0.0;
            }
        }
        
        return Utility.GetColorGradient(p);
    }
}
