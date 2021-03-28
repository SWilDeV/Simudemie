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
import java.awt.geom.GeneralPath;
import java.util.List;


/**
 *
 * @author Sean
 */
public class WorldDrawer {
    
    //    Attributs
    private final WorldController controller;
    //private Dimension initialDimension; pour le zoom ?
    private float zoom;
    

    public WorldDrawer(WorldController p_controller){
        controller = p_controller;
        //initialDimension = p_intialDimension;
    }
    
    public void draw(Graphics g) {
        drawCountries(g);
        drawLinks(g);
    }
    
    public void drawCountryInfos(Graphics g, Point mousePosition, Country country) { //TODO: A finir
        g.setColor(Color.WHITE);
        g.drawRect((int)mousePosition.getX(), (int)mousePosition.getY(), 20, 20);
        g.setColor(Color.BLACK);
        g.drawRect((int)mousePosition.getX(), (int)mousePosition.getY(), 20, 20);
        Point center = country.getShape().GetCenter();
        g.drawString("France", (int)mousePosition.getX(), (int)mousePosition.getY());
    }
    
    private void drawCountries(Graphics g) {
        List<CountryDTO> countries = controller.GetCountries();
        for(CountryDTO country : countries) {
            GeometricForm form = country.Shape;
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
            Point center = country.Shape.GetCenter();
            //g.drawString("France", (int)center.getX(), (int)center.getY());
            String topPop = Integer.toString(country.population.totalPopulation) ;
            g.drawString(topPop, (int)center.getX(), (int)center.getY());
        }
    }
    
    private void drawLinks(Graphics g) {
        List<LinkDTO> links = controller.GetLinks();
        Graphics2D g2 = (Graphics2D) g;
        for(LinkDTO link: links) {
            GeneralPath path = new GeneralPath();
            Point countryPoint1 = link.Country1.Shape.GetCenter();
            Point countryPoint2 = link.Country2.Shape.GetCenter();
            int offset = Link.GetDrawOffset(link.LinkType);
            Point mid = new Point((int)((countryPoint1.getX() + countryPoint2.getX()) * 0.5) + offset,
                                  (int)((countryPoint1.getY() + countryPoint2.getY() - 100) * 0.5) + offset);
            
            path.moveTo(countryPoint1.getX(), countryPoint1.getY());
            path.curveTo(countryPoint1.getX(), countryPoint1.getY(),
                                             mid.getX(), mid.getY(),
                                             countryPoint2.getX(), countryPoint2.getY());
            g2.setColor(Link.GetColor(link.LinkType));
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
    