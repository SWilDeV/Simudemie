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
public class IrregularForm extends GeometricForm implements Cloneable {
    
    public IrregularForm(List<Point> points) {        
        super(points);
    }
    
    
    @Override
    public IrregularForm clone() throws CloneNotSupportedException {
        IrregularForm form = null;
        try {
            form = (IrregularForm) super.clone();
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
}
