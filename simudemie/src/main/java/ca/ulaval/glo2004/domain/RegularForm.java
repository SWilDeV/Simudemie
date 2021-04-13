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
public class RegularForm extends GeometricForm implements Cloneable {
    
    public RegularForm(List<Point> points){
        super(points);
    }
    
    @Override
    public RegularForm clone() throws CloneNotSupportedException {
        RegularForm form = null;
        try {
            form = (RegularForm) super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        
        List<Point> pointsClone = new ArrayList<>(points.size());
        points.forEach(pt -> { pointsClone.add((Point) pt.clone()); });
        form.points = pointsClone;
        
        List<Point> bbClone = new ArrayList<>(bounding.size());
        bounding.forEach(pt -> { bbClone.add((Point) pt.clone()); });
        form.bounding = bbClone;
        
        return form;
    }

    @Override
    public boolean Contain(Point position) {
        return Utility.IsInRectangle(points, position);
    }

    @Override
    public void SetPointPosition(int index, Point position) {
        points.set(index, position);
        List<Point> pts = new ArrayList<Point>() {
            {
                add(points.get(0));
                add(points.get(2));
            }
        };
        this.points = Utility.ToRectangle(pts);
        RecalculateAll();
    }
}
