/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charl
 */
public abstract class GeometricForm {
    
    protected List<Point> points;
    protected Point center;
    
    public GeometricForm(List<Point> points){
        this.points = points;
        CalculateCenter();
    }
    
    private void CalculateCenter() {
        double sumX = 0;
        double sumY = 0;
        for(Point pt: points) {
            sumX += pt.getX();
            sumY += pt.getY();
        }
        
        center = new Point((int)(sumX / points.size()), (int)(sumY / points.size()));
    }
    
    public List<Point> GetPoints(){
        return new ArrayList<>(points);
    }
    
    public Point GetPoint(int index) {
        return new Point(points.get(index));
    }
    
    public Point GetCenter() {
        return new Point(center);
    }
}
