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
}
