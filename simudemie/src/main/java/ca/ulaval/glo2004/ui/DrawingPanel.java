/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.ui;

import ca.ulaval.glo2004.main_window_Simulation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Abergel Clement
 */
public class DrawingPanel extends JPanel {
    
    public Dimension initialDimension;
    AffineTransform at = new AffineTransform();
    private MainWindow mainWindow; //main_window_Simulation
    private double zoom = 1.0;
    private double zoomIncr = 0.1;
    private Point mousePosition = new Point();
    
    public DrawingPanel(){
    }
    
    public DrawingPanel(MainWindow mainWindow, JPanel parentPanel) { //main_window_Simulation
        this.mainWindow = mainWindow;
        mousePosition = new Point(getWidth() / 2, getHeight() / 2);
        setVisible(true);
        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        initialDimension = new Dimension(parentPanel.getWidth(),parentPanel.getHeight());
        setPreferredSize(initialDimension);
    }
    
    public void zoom(double value, Point mousePosition) {
        zoom += value * zoomIncr;
        this.mousePosition = mousePosition;
//        if(value < 0) {
//            zoom -= 0.1;
//        } else {
//            zoom += 0.1;
//        }
        
        if(zoom < 0.01) {
            zoom = 0.01;
        }
        
        System.out.println(zoom);
        
        //double x = (1.0 - zoom)*getWidth()/2.0;
        //double y = (1.0 - zoom)*getHeight()/2.0;
//        at.setToTranslation(mousePosition.x, mousePosition.y);
//        at.scale(zoom, zoom);
//        at.setToTranslation(-mousePosition.x, -mousePosition.y);
        repaint();
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(initialDimension);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (mainWindow != null){
            Graphics2D g2 = (Graphics2D)g;
            at = g2.getTransform();
            super.paintComponent(g);
            at.setToTranslation(mousePosition.x, mousePosition.y);
            at.scale(zoom, zoom);
            at.setToTranslation(-mousePosition.x, -mousePosition.y);
            g2.setTransform(at);
            mainWindow.Draw(g);
        }
    }
}
