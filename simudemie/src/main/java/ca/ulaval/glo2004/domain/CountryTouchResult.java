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
    public final Point Pt1;
    public final Point Pt2;
    
    public CountryTouchResult(boolean touched, Point pt1, Point pt2) {
        Touched = touched;
        Pt1 = pt1;
        Pt2 = pt2;
    }
    
    public List<Point> GetPoints() {
        return new ArrayList<Point>() {{
            add(Pt1);
            add(Pt2);
        }};
    }
}
