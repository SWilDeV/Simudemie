/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Path2D;
import java.awt.geom.GeneralPath;
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
        drawCountries(g);
        drawLinks(g);
    }
    
    public void drawCountryInfos(Graphics g, Point mousePosition, Country country) {
        g.setColor(Color.WHITE);
        g.drawRect((int)mousePosition.getX(), (int)mousePosition.getY(), 20, 20);
        g.setColor(Color.BLACK);
        g.drawRect((int)mousePosition.getX(), (int)mousePosition.getY(), 20, 20);
        Point center = country.getShape().GetCenter();
        g.drawString("France", (int)mousePosition.getX(), (int)mousePosition.getY());
    }
    
    private void drawCountries(Graphics g) {
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
            g.setColor(Color.black);
            g.drawPolygon(poly);
            
            g.setColor(Color.BLACK);
            Point center = country.getShape().GetCenter();
            g.drawString("France", (int)center.getX(), (int)center.getY());
        }
    }
    
    private void drawLinks(Graphics g) {
        List<Link> links = controller.GetLinks();
        Graphics2D g2 = (Graphics2D) g;
        for(Link link: links) {
            GeneralPath path = new GeneralPath();
            Point countryPoint1 = link.getCountry1().getShape().GetCenter();
            Point countryPoint2 = link.getCountry2().getShape().GetCenter();
            Point mid = new Point((int)((countryPoint1.getX() + countryPoint2.getX()) / 2.0),
                                  (int)((countryPoint1.getY() + countryPoint2.getY() - 100) / 2.0));
            
            path.moveTo(countryPoint1.getX(), countryPoint1.getY());
            path.curveTo(countryPoint1.getX(), countryPoint1.getY(),
                                             mid.getX(), mid.getY(),
                                             countryPoint2.getX(), countryPoint2.getY());
            g2.setColor(link.GetColor());
            g2.setStroke(new BasicStroke(2));
            g2.draw(path);
        }
    }
    
    public void drawRegular(Graphics g) {
    }
    
    public void drawIrregular(Graphics g) {
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
    