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
 * @author Abergel Clement
 */
public class CountryTouchResult {
    public final boolean Touched;
    public final Point PointOne;
    public final Point PointTwo;
    
    public CountryTouchResult(boolean touched, Point pt1, Point pt2) {
        Touched = touched;
        PointOne = pt1;
        PointTwo = pt2;
    }
    
    public List<Point> GetPoints() {
        if(PointOne == null || PointTwo == null) {
            return null;
        }

        return new ArrayList<Point>() {{
            add(PointOne);
            add(PointTwo);
        }};
    }
}
