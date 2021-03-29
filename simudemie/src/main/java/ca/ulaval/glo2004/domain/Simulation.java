/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Abergel Clement
 */
public class Simulation {
    
    public Simulation() {  
    }
    private boolean isRunning = false;
    private int elapsedDay = 0;
    //TODO: On a oublis de definir le type de DataHistory. J'ai mis int pour eviter les erreuurs
    private ArrayList<Integer> dataHistory = new ArrayList<Integer>();
    
    
    
    public boolean getIsRunning() {
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
    
    public void Simulate() {
        System.out.println("demarré");
        SetRunning(true);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            //@Override
            public void run() {
                if(getIsRunning()){
                    //CODE VIENT ICI
                    elapsedDay +=1;
                    System.out.println(elapsedDay);
                    
                }else{
                    t.cancel();
                }
            }
        }, 0, 1000);
    }
    
    public int previousDay() {
        return elapsedDay -=1;
    }
    public int nextDay() {
        return elapsedDay +=1;
    }
    
    public void Pause() {
        if(isRunning == true){
            this.isRunning=false;
        }
        
        
    }
}
