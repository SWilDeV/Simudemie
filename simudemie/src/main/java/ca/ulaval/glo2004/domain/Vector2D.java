/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Point;

/**
 *
 * @author Abergel Clement
 */
public class Vector2D {  
    public double x = 0;
    public double y = 0;
        
    public Vector2D() {

    }

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
//    public void Set(double x, double y) {
//        this.x = x;
//        this.y = y;
//    }
//    
//    public void SetX(double x) {
//        this.x = x;
//    }
//    
//    public void SetY(double y) {
//        this.y = y;
//    }
//    
//    public double GetX() {
//        return x;
//    }
//    
//    public double GetY() {
//        return y;
//    }

    public double GetSqrtMagnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public void SetMagnitude(double newMag) {
        double mag = GetSqrtMagnitude();
        x = x * newMag / mag;
        y = y * newMag / mag;
    }

    public Vector2D Normalized() {
        double mag = GetSqrtMagnitude();
        return new Vector2D(x / mag, y / mag);
    }

    public Point ToPoint() {
        return new Point((int)x, (int)y);
    }
}

