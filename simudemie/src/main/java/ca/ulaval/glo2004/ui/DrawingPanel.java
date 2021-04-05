/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.ui;

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
    private MainWindow mainWindow;
    
    public DrawingPanel(){
    }
    
    public DrawingPanel(MainWindow mainWindow, JPanel parentPanel) {
        this.mainWindow = mainWindow;
        setVisible(true);
        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        initialDimension = new Dimension(mainWindow.getWidth(),mainWindow.getHeight());
        setPreferredSize(initialDimension);
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(mainWindow.getWidth(),mainWindow.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (mainWindow != null) {
            super.paintComponent(g);
            mainWindow.Draw(g);
        }
    }
}
