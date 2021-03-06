/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.List;


/**
 *
 * @author Sean
 */
public class WorldDrawer implements java.io.Serializable {
    
    private final WorldController controller;
    private final int pointsRadius = 10;
    
    private Stroke dashStroke;
    private Stroke plainStoke;

    public WorldDrawer(WorldController p_controller){
        controller = p_controller;
        
        float[] dashingPattern1 = {2f, 2f};
        dashStroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
        plainStoke = new BasicStroke(1f);
    }
    
    public void draw(Graphics2D g2d, List<Point> mousePoints) {
        drawCountries(g2d);
        drawLinks(g2d);
        drawMousePoints(g2d, mousePoints);
    }
    
    private void DrawCircleAtPosition(Graphics2D g2d, Point position, int radius) {
        int x = position.x-(radius/2);
        int y = position.y-(radius/2);
        g2d.fillOval(x, y, radius, radius);
    }
    
    public void drawMousePoints(Graphics2D g2d, List<Point> points) {
        drawPoints(g2d, points, pointsRadius);
        
        if(points.size() > 1) {
            for(int i = 1; i < points.size(); i++) {
                Point pt1 = points.get(i-1);
                Point pt2 = points.get(i);
                g2d.drawLine(pt1.x, pt1.y, pt2.x, pt2.y);
            }
        }
    }
    
    public void drawPoints(Graphics2D g2d, List<Point> points, int radius) {
        g2d.setColor(Color.black);
        points.forEach(pt -> {
            DrawCircleAtPosition(g2d, pt, radius);
        });
    }
    
    public void drawRegionInfo(Graphics2D g2d, Point mousePosition, RegionDTO region) {
        String totalPopulation = String.valueOf(region.SubPopulation.totalPopulationDTO);
        int boxWidth = totalPopulation.length() * 8 + 100;
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect((int)mousePosition.getX(), (int)mousePosition.getY(), boxWidth, 100);
        g2d.setColor(Color.BLACK);
        g2d.drawRect((int)mousePosition.getX(), (int)mousePosition.getY(), boxWidth, 100);

        int tempY = (int)mousePosition.getY();
        for(String str: region.toString().split("\n")) {
            g2d.drawString(str, (int)mousePosition.getX() + 1, tempY += g2d.getFontMetrics().getHeight());        
        }
    }
    
    public void drawCountryInfos(Graphics2D g2d, Point mousePosition, CountryDTO country) {  
        
        boolean onRegion = false;
        
        for(RegionDTO r: country.Regions) {
            if(Utility.IsInRectangle(r.Shape.GetPoints(), mousePosition)) {
                drawRegionInfo(g2d, mousePosition, r);
                onRegion = true;
                break;
            }
        }
        
        if (!onRegion) {
            String totalPopulation = String.valueOf(country.populationDTO.totalPopulationDTO);
            int boxWidth = totalPopulation.length() * 8 + 100;

            g2d.setColor(Color.WHITE);
            g2d.fillRect((int)mousePosition.getX(), (int)mousePosition.getY(), boxWidth, 100);
            g2d.setColor(Color.BLACK);
            g2d.drawRect((int)mousePosition.getX(), (int)mousePosition.getY(), boxWidth, 100);

            int tempY = (int)mousePosition.getY();
            for(String str: country.toString().split("\n")) {
                g2d.drawString(str, (int)mousePosition.getX() + 1, tempY += g2d.getFontMetrics().getHeight());        
            }
        }
    }
    
    private Polygon CreatePolygon(List<Point> pts) {
        int size =  pts.size();
            int[] pointsX = new int[size];
            int[] pointsY = new int[size];
            
            for(int i = 0; i < size; i++) {
                Point pt = pts.get(i);
                pointsX[i] = (int)pt.getX();
                pointsY[i] = (int)pt.getY();
            }
            
        return new Polygon(pointsX, pointsY, size);
    }
    
    private void drawRegion(Graphics2D g2d, CountryDTO country) { 
        for(RegionDTO r: country.Regions) {
            List<Point> pts = r.Shape.GetPoints();
            g2d.setStroke(dashStroke);
 
            Polygon poly = CreatePolygon(pts);
           
            g2d.setColor(r.Color);
            g2d.fillPolygon(poly);
            g2d.setColor(Color.black);
            if(r.IsSelected) {
                g2d.setColor(Color.YELLOW);
            }
            
            g2d.drawPolygon(poly);
        }
    }
    
    private void drawCountries(Graphics2D g2d) {
        List<CountryDTO> countries = controller.GetCountries();
        for(CountryDTO country : countries) {            
            GeometricForm form = country.Shape;
            
            Polygon poly = CreatePolygon(form.GetPoints());
            g2d.setColor(country.Color);
            g2d.fillPolygon(poly);
            
            drawRegion(g2d, country);
            
            g2d.setColor(Color.BLACK);
            if(country.IsSelected) {
                g2d.setColor(Color.YELLOW);
                
                if(country.Shape instanceof IrregularForm) {
                    drawPoints(g2d, form.GetPoints(), pointsRadius);
                } else {
                    DrawCircleAtPosition(g2d, country.Shape.GetPoint(0), pointsRadius);
                    DrawCircleAtPosition(g2d, country.Shape.GetPoint(2), pointsRadius);
                }
            }
            
            g2d.setStroke(plainStoke);
            g2d.drawPolygon(poly);
            
            g2d.setColor(Color.BLACK);
            Point center = country.Shape.GetCenter();
            g2d.drawString(country.Name, (int)center.getX(), ((int)center.getY()-20));
            String totPop = Integer.toString(country.getPopulationDTO().getTotalPopulationDTO());
            g2d.drawString(totPop, (int)center.getX(), (int)center.getY());
            
//            if(true) {
//                g2d.setColor(Color.red);
//                poly = CreatePolygon(country.Shape.GetBoundingBox());
//                g2d.drawPolygon(poly);
//            }
        }
    }
    
    private void drawLinks(Graphics2D g2d) {
        List<LinkDTO> links = controller.GetLinks();

        for(LinkDTO link: links) {
            CountryDTO Country1 = controller.GetCountryDTO(link.Country1Id);
            CountryDTO Country2 = controller.GetCountryDTO(link.Country2Id);
            if(link.LinkType == Link.LinkType.TERRESTRE) {
                List<Point> pt = Utility.GetLandBorderPoints(Country1, Country2);
                Line2D line = new Line2D.Float(pt.get(0).x, pt.get(0).y, pt.get(1).x, pt.get(1).y);
                
                if(link.IsSelected) {
                    g2d.setColor(Color.YELLOW);
                    g2d.setStroke(new BasicStroke(8));
                    g2d.draw(line);
                }
                
                g2d.setColor(Color.cyan);
                g2d.setStroke(new BasicStroke(5));
                g2d.draw(line);
            } else {
                GeneralPath path = new GeneralPath();
                Point countryPoint1 = Country1.Shape.GetCenter();
                Point countryPoint2 = Country2.Shape.GetCenter();
                int offset = Link.GetDrawOffset(link.LinkType);
                Point mid = new Point((int)((countryPoint1.getX() + countryPoint2.getX()) * 0.5) + offset,
                                      (int)((countryPoint1.getY() + countryPoint2.getY() - 100) * 0.5) + offset);

                path.moveTo(countryPoint1.getX(), countryPoint1.getY());
                path.curveTo(countryPoint1.getX(), countryPoint1.getY(),
                                                 mid.getX(), mid.getY(),
                                                 countryPoint2.getX(), countryPoint2.getY());
                
                if(link.IsSelected) {
                    g2d.setColor(Color.YELLOW);
                    g2d.setStroke(new BasicStroke(5));
                    g2d.draw(path);
                }

                g2d.setColor(Link.GetColor(link.LinkType));
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(path);
            }
        }
        
        g2d.setStroke(new BasicStroke(1));
    }

    void DrawSelectedRegion(Graphics2D g2d, List<Point> points) {
        drawMousePoints(g2d, points);
    }
}
    