/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import static ca.ulaval.glo2004.domain.Utility.IsTouching;
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
    
    public static final boolean AsCommonLandBorder(CountryDTO country1, CountryDTO country2) {
        return IsTouching(country1.Shape.GetBoundingBox(), country2.Shape.GetPoints()).Touched ||
                IsTouching(country2.Shape.GetBoundingBox(), country1.Shape.GetPoints()).Touched;
    }
    
    public static final boolean AsCommonLandBorder(Country country1, Country country2) {        
        return IsTouching(country1.getShape().GetBoundingBox(), country2.getShape().GetPoints()).Touched ||
                IsTouching(country2.getShape().GetBoundingBox(), country1.getShape().GetPoints()).Touched;
    }
    
    public static final List<Point> GetLandBorderPoints(CountryDTO country1, CountryDTO country2) {
        CountryTouchResult resultOne = IsTouching(country1.Shape.GetBoundingBox(), country2.Shape.GetPoints());
        CountryTouchResult resultTwo = IsTouching(country2.Shape.GetBoundingBox(), country1.Shape.GetPoints());

        if(resultOne.Touched) {
            return resultOne.GetPoints();
        }
        
        return resultTwo.GetPoints();
    }
    
    public static final CountryTouchResult IsTouching(List<Point> countryBb, List<Point> otherPts) {    //other est-il dans la bb de country ?
        Point borderPointOne = null;
        Point borderPointTwo = null;
        int size = otherPts.size();
          
        boolean touch = false;
        
        for(int i = 0; i < size; i++) {
            Point pt1 = otherPts.get(i);
            Point pt2 = otherPts.get(0);

            if(i != (size-1)) {
                pt1 = otherPts.get(i);
                pt2 = otherPts.get(i+1);
            }

            if(Utility.IsInRectangle(countryBb, pt1) && Utility.IsInRectangle(countryBb, pt2)) {
                borderPointOne = pt1;
                borderPointTwo = pt2; 
                touch = true;
                break;
            }
        }
        
        return new CountryTouchResult(touch, borderPointOne, borderPointTwo);
    }
}
