/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charl
 */
public abstract class GeometricForm implements Serializable, Cloneable {
    
    protected List<Point> points = new ArrayList<>();
    protected List<Point> bounding = new ArrayList<>();
    protected Point center;
    private static final long serialVersionUID = 3L; 
    
    public GeometricForm(List<Point> points){
        this.points = points;
        RecalculateAll();
    }
    
    public void SetPosition(Point position) {
        float directionX = position.x - center.x;
        float directionY = position.y - center.y;
        
        for(int i = 0; i < points.size(); i++) {
            points.get(i).x += directionX;
            points.get(i).y += directionY;
        }
        
        RecalculateAll();
    }
    
    protected void RecalculateAll() {
        CalculateCenter();
        CalculateBoundingBox();
    }
    
    protected void CalculateCenter() {
        double sumX = 0;
        double sumY = 0;
        for(Point pt: points) {
            sumX += pt.getX();
            sumY += pt.getY();
        }
        
        center = new Point((int)(sumX / points.size()), (int)(sumY / points.size()));
    }
    
    protected void CalculateBoundingBox() {        
        int size = 10;
        
        for(int i = 0 ; i < points.size(); i++) {
            Vector2D pt = new Vector2D(points.get(i).x, points.get(i).y);
            Vector2D direction = GetNormalizedDirection(points.get(i), center);
            
            int sizeLeft = (int)Math.sqrt(Math.pow(points.get(0).x - points.get(1).x, 2) + Math.pow(points.get(0).y - points.get(1).y, 2));
            int sizeUp = (int)Math.sqrt(Math.pow(points.get(1).x - points.get(2).x, 2) + Math.pow(points.get(1).y - points.get(2).y, 2));
            
            direction.x *= sizeUp/size;
            direction.y *= sizeLeft/size;
            direction.SetMagnitude(size);

            pt.x += direction.x;
            pt.y += direction.y;
            
            bounding.add(i, pt.ToPoint());
        }
    }
    
    private Vector2D GetNormalizedDirection(Point point1, Point point2) {
        Vector2D direction = new Vector2D(point1.x - point2.x, point1.y - point2.y);
        return direction.Normalized();
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
    
    public List<Point> GetBoundingBox() {
        return new ArrayList<>(bounding);
    }
    
    @Override
    public GeometricForm clone() throws CloneNotSupportedException {
        GeometricForm form = null;
        try {
            form = (GeometricForm) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        
        List<Point> pointsClone = new ArrayList<>(points.size());
        points.forEach(pt -> { pointsClone.add((Point) pt.clone()); });
        form.points = pointsClone;
        
        List<Point> bbClone = new ArrayList<>(bounding.size());
        bounding.forEach(pt -> { bbClone.add((Point) pt.clone()); });
        form.bounding = pointsClone;
        
        return form;
    }
}