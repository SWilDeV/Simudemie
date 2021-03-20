/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.ArrayList;


/**
 *
 * @author Abergel Clement
 */
public class Simulation {
    
    private boolean isRunning = false;
    private int elapsedDay = 0;
    //TODO: On a oublis de definir le type de DataHistory. J'ai mis int pour eviter les erreuurs
    private ArrayList<Integer> dataHistory = new ArrayList<Integer>();
    
    public boolean IsRunning() {
        return this.isRunning;
    }
    
    public ArrayList<Integer> GetDataHistory() {
        return dataHistory; //TODO: DTO ou un deep copy?
    }
    
    public int GetElapsedDay() {
        return this.elapsedDay;
    }
    
    private void SetRunning(boolean running) {
        this.isRunning = running;
    }
    
    public Simulation() {  
    }
    
    public void Simulate() {
        
    }
    
    public void previousDay() {
        
    }
    
    public void PausePlay() {
        
    }
}
