/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * @author Abergel Clement
 */
public class DrawingPanel extends JPanel  {
    
    public Dimension initialDimension;
    private MainWindow mainWindow;
    private BufferedImage image_background = null;
    
    private AffineTransform at;
    private double zoom = 1.0;
    private final double zoomIncr = 0.05;
    private final double zoomMin = 0.1;
    private int zoomPointX;
    private int zoomPointY;
    
    public DrawingPanel() {
    }
    
    public DrawingPanel(MainWindow mainWindow, JPanel parentPanel) {
        this.mainWindow = mainWindow;
        setVisible(true);
        initialDimension = new Dimension(mainWindow.getWidth(),mainWindow.getHeight());
        setPreferredSize(initialDimension);
        setBackground(new Color(0, 103, 190));
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(mainWindow.getWidth(),mainWindow.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        
        if (image_background != null){
            g2d.drawImage(image_background, 0, 0, this);
        }
        
        if (mainWindow != null) {           
            at = g2d.getTransform();
            at.translate(zoomPointX, zoomPointY);
            at.scale(zoom, zoom);
            at.translate(-zoomPointX, -zoomPointY);
            g2d.setTransform(at);
            mainWindow.Draw(g2d);

        }
    }
    
    public void ResetZoom() {
        zoom = 1.0;
        repaint();
    }
    
    public void Zoom(MouseWheelEvent e) {
        zoomPointX = e.getX();
        zoomPointY = e.getY();
        
        zoom += zoomIncr * e.getPreciseWheelRotation();
        if(zoom < zoomMin) {
            zoom = zoomIncr;
        }
        
        repaint();
    }
    
    public void MovePosition(Point position) {
        zoomPointX = position.x;
        zoomPointY = position.y;
        repaint();
    }
    
    public double GetZoom() {
        return zoom;
    }
    
    public Point GetPointOnPanel(Point position) throws NoninvertibleTransformException {
        //TODO: ISSUE -> Il faut pouvoir map les coordonnes du zoom pour cliquer sur les pays.
        double x = position.getX() / zoom;
        double y = position.getY() / zoom;
        
        return new Point((int)x, (int)y);
    }
    
    public void SaveScreenShot(File file){
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        paint(img.getGraphics());
        //File outputfile = new File("saved.png");
        try{
            String path = file.getAbsolutePath() + ".png";
            File f = new File(path);
            ImageIO.write(img, "png", f);
	} 
        catch (IOException e) {
            e.printStackTrace();
	}
    }
    
    public void loadImageBackground(File bgImage){ 
        try {
            image_background = ImageIO.read(bgImage);
            repaint();   
        }
        catch(IOException e1) {
            e1.printStackTrace();
        }
    }
    
    
    
}
