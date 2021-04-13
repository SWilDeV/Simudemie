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

    @Override
    public boolean Contain(Point position) {
      int i;
      int j;
      boolean result = false;
      for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
        Point pointI = points.get(i);
        Point pointJ = points.get(j);
        
        if ((pointI.y > position.y) != (pointJ.y > position.y) &&
            (position.x < (pointJ.x - pointI.x) * (position.y - pointI.y) / (pointJ.y-pointI.y) + pointI.x)) {
          result = !result;
         }
      }
      return result;
    }

    @Override
    public void SetPointPosition(int index, Point position) {
        points.set(index, (Point)position.clone());
        RecalculateAll();
    }
}
