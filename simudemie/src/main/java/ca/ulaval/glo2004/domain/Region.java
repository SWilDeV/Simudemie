/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author charl
 */
public class Region {
    
    private double percentagePop;
    private Population subPopulation;
    private RegularForm shape;
    private final UUID id;
    
    public Region(RegularForm form, int countryPopulation, double percentage) {
        shape = form;
        id = UUID.randomUUID();
        SetPercentage(countryPopulation, percentage);
    }
    
    public Region(UUID id, RegularForm form, Population population, double percentage) {
        shape = form;
        this.id = id;
        subPopulation = population;
        percentagePop = percentage;
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
}
