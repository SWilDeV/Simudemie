/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;


/**
 *
 * @author Sean
 */
public class WorldDrawer {
    
    //    Attributs
    private WorldController controller;
    //private Dimension initialDimension; pour le zoom ?
    private float zoom;
    
    //   methodes
    public WorldDrawer(WorldController p_controller){
        controller = p_controller;
        //initialDimension = p_intialDimension;
    }
    
    public void draw(Graphics g){
        drawRegular(g);
        drawIrregular(g);
    }
    
    public void drawRegular(Graphics g) {
        List<Country> countries = controller.GetCountries();
        for(Country country : countries) {
            GeometricForm form = country.getShape();
            
            Random rand = new Random();
            float x = (float) (rand.nextFloat()* 180.0);
            float y = (float) (rand.nextFloat() * 180.0);
            g.setColor(new Color((int)x,(int)y,57));
            g.fillRect((int)x, (int)y, 200, 200); // FOR SQUARE
        }
    }
    
    public void drawIrregular(Graphics g) {
        
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
    