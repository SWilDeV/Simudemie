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
public class CountryFactory {
    public static Country CreateCountry(final List<Point> points, String countryName, int countryPop) {
        GeometricForm form = null;
        
        if(points.size() == 2) {
            final Point pt1 = new Point((int)points.get(1).getX(), (int)points.get(0).getY());
            final Point pt3 = new Point((int)points.get(0).getX(), (int)points.get(1).getY());

            List<Point> arrangedPoints = new ArrayList<Point>() {{
                add(points.get(0));
                add(pt1);
                add(points.get(1));
                add(pt3);
            }};

            form = new RegularForm(arrangedPoints);
        } else {
            form = new IrregularForm(points);
        }
        
        return new Country(form, countryName, countryPop);
    }
}
