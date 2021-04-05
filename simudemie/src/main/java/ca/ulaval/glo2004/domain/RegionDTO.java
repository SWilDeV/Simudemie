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
import java.util.UUID;

/**
 *
 * @author Abergel Clement
 */
public class RegionDTO implements Serializable {
    
    public String Name;
    public double PercentagePop;
    public RegularForm Shape;
    public PopulationDTO SubPopulation;
    public final Color Color;
    public final UUID Id;
    public boolean IsSelected;
    
    public RegionDTO(Region region) {
        Name = region.GetName();
        PercentagePop = region.getPercentagePop();
        Shape = region.GetShape();
        SubPopulation = new PopulationDTO(region.getPopulation());
        Id = region.GetId();
        Color = region.GetColor();
        IsSelected = region.IsSelected();
    }
    
    public void ModifyShape(Point p1, Point p2) {
        List<Point> pts = new ArrayList<Point>(){
            {
                add(p1);
                add(p2);
            }
        };
        
        Shape = new RegularForm(Utility.ToRectangle(pts));
    }
    
    public Region toRegion() {
        return new Region(Id, Shape, SubPopulation.toPopulation(), PercentagePop, IsSelected, Name);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Region").append("\n");
        sb.append("---------\n");
        sb.append(SubPopulation.toString());
        
        return sb.toString();
    }
}
