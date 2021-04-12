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
            List<Point> pts = Utility.ToRectangle(points);
            form = new RegularForm(pts);
        } else {
            List<Point> pts = new ArrayList<>(points.size());
            points.forEach(pt -> {
                pts.add((Point) pt.clone());
            });
            form = new IrregularForm(pts);
        }
        
        return new Country(form, countryName, countryPop);
    }
}
