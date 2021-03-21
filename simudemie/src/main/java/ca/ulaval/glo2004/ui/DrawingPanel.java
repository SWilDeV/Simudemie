/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.ui;

import ca.ulaval.glo2004.main_window_Simulation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Abergel Clement
 */
public class DrawingPanel extends JPanel implements Serializable {
    
    public Dimension initialDimension;
    private JPanel parentPanel;
    private main_window_Simulation mainWindow;
    
    public DrawingPanel(){
    }
    
    public DrawingPanel(main_window_Simulation mainWindow, JPanel parentPanel) {
        this.mainWindow = mainWindow;
        this.parentPanel = parentPanel;
        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        int width = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        setPreferredSize(new Dimension(parentPanel.getWidth(),parentPanel.getHeight()));
        setVisible(true);
        int height = (int)(width*0.5);
        initialDimension = new Dimension(parentPanel.getWidth(),parentPanel.getHeight());
    }
    
    @Override public Dimension getPreferredSize()
    {
        return new Dimension(parentPanel.getWidth(),parentPanel.getHeight());
    }
//    
    @Override
    protected void paintComponent(Graphics g)
    {
        if (mainWindow != null){
            super.paintComponent(g);
            mainWindow.worldController.Draw(g);
        }
    }
}
