/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;


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
    
    public void draw(Graphics g) { //J'ai mis tout ici car on peux cree un pays regulier ou irregulier de la meme facons. - Clement
        List<Country> countries = controller.GetCountries();
        for(Country country : countries) {
            GeometricForm form = country.getShape();
            int size = form.GetPoints().size();
            int[] pointsX = new int[size];
            int[] pointsY = new int[size];
            
            for(int i = 0; i < size; i++) {
                Point pt = form.GetPoint(i);
                pointsX[i] = (int)pt.getX();
                pointsY[i] = (int)pt.getY();
            }
            
            Polygon poly = new Polygon(pointsX, pointsY, size);
            g.setColor(Color.red);
            g.fillPolygon(poly);
            g.setColor(Color.black); //Petite bordure :)
            g.drawPolygon(poly);
        }
    }
    
    public void drawRegular(Graphics g) {
        
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
    