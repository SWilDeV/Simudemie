/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.ui;

import ca.ulaval.glo2004.main_window_Simulation;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Abergel Clement
 */
public class DrawingPanel extends JPanel {
    
    public Dimension initialDimension;
    private MainWindow mainWindow; //main_window_Simulation
    
    public DrawingPanel(){
    }
    
    public DrawingPanel(MainWindow mainWindow, JPanel parentPanel) { //main_window_Simulation
        this.mainWindow = mainWindow;
        setVisible(true);
        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        initialDimension = new Dimension(parentPanel.getWidth(),parentPanel.getHeight());
        setPreferredSize(initialDimension);
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
            super.paintComponent(g);
            mainWindow.Draw(g);
        }
    }
}
