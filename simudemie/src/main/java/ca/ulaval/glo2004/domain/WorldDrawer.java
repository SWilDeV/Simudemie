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
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.List;


/**
 *
 * @author Sean
 */
public class WorldDrawer implements java.io.Serializable {
    
    //    Attributs
    private final WorldController controller;
    //private Dimension initialDimension; pour le zoom ?
    private double zoom = 1.0;
    private double zoomIncr = 0.025;
    AffineTransform at = new AffineTransform();
    

    public WorldDrawer(WorldController p_controller){
        controller = p_controller;
        //initialDimension = p_intialDimension;
    }
    
    public void Zoom(double amount, Point mousePosition, int width, int height) {
        zoom += amount * zoomIncr;
        if(zoom <= 0) {
            zoom = 0;
        }
        
        
        
        
//        double x = (1.0 - zoom)*width/2.0;
//        double y = (1.0 - zoom)*height/2.0;
        //at.setToTranslation(mousePosition.x, mousePosition.y);
//        at.setToTranslation(x, y);
//        at.scale(zoom, zoom);
        
//        at.translate(mousePosition.x, mousePosition.y);
//        at.scale(zoom, zoom);
//        at.translate(-mousePosition.x, -mousePosition.y);
    }
    
    public void draw(Graphics g) {
//        Graphics2D g2 = (Graphics2D)g;
//        g2.setTransform(at);
        
        drawCountries(g);
        drawLinks(g);
    }
    
    public void drawRegionInfo(Graphics g, Point mousePosition, RegionDTO region) {
        String totalPopulation = String.valueOf(region.SubPopulation.totalPopulationDTO);
        int boxWidth = totalPopulation.length() * 8 + 100;
        
        g.setColor(Color.WHITE);
        g.fillRect((int)mousePosition.getX(), (int)mousePosition.getY(), boxWidth, 100);
        g.setColor(Color.BLACK);
        g.drawRect((int)mousePosition.getX(), (int)mousePosition.getY(), boxWidth, 100);

        int tempY = (int)mousePosition.getY();
        for(String str: region.toString().split("\n")) {
            g.drawString(str, (int)mousePosition.getX() + 1, tempY += g.getFontMetrics().getHeight());        
        }
    }
    
    public void drawCountryInfos(Graphics g, Point mousePosition, CountryDTO country) {  
        
//        String totalPopulation = String.valueOf(country.populationDTO.totalPopulationDTO);
//        int boxWidth = totalPopulation.length() * 8 + 100;
//        
//        g.setColor(Color.WHITE);
//        g.fillRect((int)mousePosition.getX(), (int)mousePosition.getY(), boxWidth, 100);
//        g.setColor(Color.BLACK);
//        g.drawRect((int)mousePosition.getX(), (int)mousePosition.getY(), boxWidth, 100);
//
//        int tempY = (int)mousePosition.getY();
//        for(String str: country.toString().split("\n")) {
//            g.drawString(str, (int)mousePosition.getX() + 1, tempY += g.getFontMetrics().getHeight());        
//        }


        for(RegionDTO r: country.Regions) {
            if(Utility.IsInRectangle(r.Shape.GetPoints(), mousePosition)) {
                drawRegionInfo(g, mousePosition, r);
            }
        }
    }
    
    private Shape CreatePolygon(List<Point> pts) {
        int size =  pts.size();
            int[] pointsX = new int[size];
            int[] pointsY = new int[size];
            
            for(int i = 0; i < size; i++) {
                Point pt = pts.get(i);
                pointsX[i] = (int)pt.getX();
                pointsY[i] = (int)pt.getY();
            }
            
        //at.createTransformedShape(new Polygon(pointsX, pointsY, size));
        return at.createTransformedShape(new Polygon(pointsX, pointsY, size));
    }
    
    private void drawRegion(Graphics g, CountryDTO country) {
        
        for(RegionDTO r: country.Regions) {
            List<Point> pts = r.Shape.GetPoints();
            
            Shape poly = CreatePolygon(pts);
            
            Graphics2D g2 = (Graphics2D)g;
            
            g2.setColor(Color.green);
            g2.fill(poly);
            //g.fillPolygon(poly);
            g2.setColor(Color.black);
            g2.draw(poly);
            //g.drawPolygon(poly);
        }
        
//        List<Point> pts = country.Shape.GetPoints();
//        int width = Utility.Distance(pts.get(0), pts.get(1));
//        int height = Utility.Distance(pts.get(1), pts.get(2));
//        
//        int regionCount = country.Regions.size();
        
//        if(regionCount > 0) {   
//            int stepY = height / regionCount;
//            int heightY = stepY;
//            Point topLeft = Utility.GetTopLeftPoint(pts);
//            Point bottomRight = Utility.GetBottomRightPoint(pts);
//
//            for(int y = 0; y < regionCount; y++) {
//                if(y == (regionCount-1)) {
//                    heightY = Utility.Distance(bottomRight, new Point(topLeft.x + width, y * stepY +  topLeft.y));
//                }
//                
//                g.setColor(Color.green);
//                g.fillRect(topLeft.x, y * stepY +  topLeft.y, width, heightY);
//                g.setColor(Color.black);
//                g.drawRect(topLeft.x, y * stepY +  topLeft.y, width, heightY);
//            }
//        }
    }
    
    private void drawCountries(Graphics g) {
        List<CountryDTO> countries = controller.GetCountries();
        for(CountryDTO country : countries) {
            GeometricForm form = country.Shape;
            
            Graphics2D g2 = (Graphics2D)g;
            Shape poly = CreatePolygon(form.GetPoints());
            g2.setColor(country.Color);
            g2.fill(poly);
            //g.fillPolygon(poly);
            
            drawRegion(g, country);
            
            g2.setColor(Color.BLACK);
            if(country.IsSelected) {
                g2.setColor(Color.YELLOW);
            }
            //g2.setStroke(new BasicStroke(5));
            //g.drawPolygon(poly);
            g2.draw(poly);
            
            g.setColor(Color.BLACK);
            Point center = country.Shape.GetCenter();
            g.drawString(country.Name, (int)center.getX(), ((int)center.getY()-20));
            String totPop = Integer.toString(country.getPopulationDTO().getTotalPopulationDTO());
            g.drawString(totPop, (int)center.getX(), (int)center.getY());
            
//            if(false) { //Show BoundingBox
//                List<Point> pts = country.Shape.GetBoundingBox();
//                int bbSize = pts.size();
//                int[] bbPointsX = new int[size];
//                int[] bbPointsY = new int[size];
//            
//                for(int i = 0; i < size; i++) {
//                    Point pt = pts.get(i);
//                    bbPointsX[i] = (int)pt.getX();
//                    bbPointsY[i] = (int)pt.getY();
//                }
//                
//                g.setColor(Color.red);
//                g.drawPolygon(bbPointsX, bbPointsY, bbSize);
//            }
        }
    }
    
    private void drawLinks(Graphics g) {
        List<LinkDTO> links = controller.GetLinks();
        Graphics2D g2 = (Graphics2D) g;

        for(LinkDTO link: links) {
            if(link.LinkType == Link.LinkType.TERRESTRE) {
                List<Point> pt = Utility.GetLandBorderPoints(link.Country1, link.Country2);
                Line2D line = new Line2D.Float(pt.get(0).x, pt.get(0).y, pt.get(1).x, pt.get(1).y);
                
                if(link.IsSelected) {
                    g2.setColor(Color.YELLOW);
                    g2.setStroke(new BasicStroke(8));
                    g2.draw(line);
                }
                
                g2.setColor(Color.cyan);
                g2.setStroke(new BasicStroke(5));
                g2.draw(line);
            } else {
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
                
                if(link.IsSelected) {
                    g2.setColor(Color.YELLOW);
                    g2.setStroke(new BasicStroke(5));
                    g2.draw(path);
                }

                g2.setColor(Link.GetColor(link.LinkType));
                g2.setStroke(new BasicStroke(2));
                g2.draw(path);
            }
        }
        
        g2.setStroke(new BasicStroke(1));
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
    
    public double getZoom(){
        return zoom;
    }
    
    public void displayInfoBox(){
    }
}
    