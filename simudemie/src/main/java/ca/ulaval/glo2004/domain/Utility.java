/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Point;
import java.util.List;

/**
 *
 * @author Abergel Clement
 */
public final class Utility {
    
    public static final boolean IsInRectangle(List<Point> points, Point position) {        
        Point p1 = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE); //En haut a gauche.
        Point p2 = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE); //En bas a droite.
        
        for(Point point: points) {
            if(point.getX() <= p1.getX() && point.getY() <= p1.getY()) {
                p1 = point;
            }
            
            if(point.getX() >= p2.getX() && point.getY() >= p2.getY()) {
                p2 = point;
            }
        }
        
        return position.getX() > p1.getX() && position.getX() < p2.getX() && position.getY() > p1.getY() && position.getY() < p2.getY();
    }
    
    public static final boolean AsCommonBorder(CountryDTO country1, CountryDTO country2) {
        return IsTouching(country1.Shape.GetPoints(), country2.Shape.GetBB()).Touched || IsTouching(country2.Shape.GetBB(), country1.Shape.GetPoints()).Touched;
    }
    
    public static final boolean AsCommonBorder(Country country1, Country country2) {
        return IsTouching(country1.getShape().GetPoints(), country2.getShape().GetBB()).Touched || IsTouching(country2.getShape().GetBB(), country1.getShape().GetPoints()).Touched;
    }
    
    public static final List<Point> GetTerrestBorderPoint(CountryDTO country1, CountryDTO country2) {
        CountryTouchResult result1 = IsTouching(country1.Shape.GetPoints(), country2.Shape.GetBB());
        CountryTouchResult result2 = IsTouching(country2.Shape.GetBB(), country1.Shape.GetPoints());
        
        if(result1.Touched) {
            return result1.GetPoints();
        }
        
        return result2.GetPoints();
    }
    
    public static final CountryTouchResult IsTouching(List<Point> countryPts, List<Point> otherBbPts) {      
        Point borderPointOne = null;
        Point borderPointTwo = null;
        int size = otherBbPts.size();
          
        boolean touch = false;
        
        for(int i = 0; i < size; i++) {
            Point bbPt1 = otherBbPts.get(i);
            Point bbPt2 = otherBbPts.get(0);

            if(i != (size-1)) {
                bbPt1 = otherBbPts.get(i);
                bbPt2 = otherBbPts.get(i+1);
            }

            if(Utility.IsInRectangle(countryPts, bbPt1) && Utility.IsInRectangle(countryPts, bbPt2)) {
                touch = true;
                borderPointOne = bbPt1;
                borderPointTwo = bbPt2;
                break;
            }
        }
        
        return new CountryTouchResult(touch, borderPointOne, borderPointTwo);
    }
}
