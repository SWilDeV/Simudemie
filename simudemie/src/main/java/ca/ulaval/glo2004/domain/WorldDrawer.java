/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import java.awt.Dimension;
import java.awt.Graphics;


/**
 *
 * @author Sean
 */
public class WorldDrawer {
    
    //    Attributs
    private WorldController controller;
    private Dimension initialDimension;
    private float zoom;
    
    //   methodes
    public WorldDrawer(WorldController p_controller, Dimension p_intialDimension){
        controller = p_controller;
        initialDimension = p_intialDimension;
    }
    
    public void draw(Graphics g){
    }
    
    public void drawLink(Graphics g){
    }
    
    public void region(Graphics g){
    }
    
    public void setZoom(float p_zoom){
        zoom = p_zoom;
    }
    
    public float getZoom(){
        return zoom;
    }
    
    public void displayInfoBox(){
    }
}
    