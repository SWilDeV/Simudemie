/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import  mathematical_model.Calculation;

/**
 *
 * @author Abergel Clement
 */
public class Simulation {
    private Calculation calculation = new Calculation();
    private boolean isRunning = false;
    private int elapsedDay = 0;
    private ArrayList<Integer> dataHistory = new ArrayList<Integer>();
    private final WorldController controller;
    //TODO: On a oublis de definir le type de DataHistory. J'ai mis int pour eviter les erreuurs
    
    public Simulation(WorldController p_controller){
        controller = p_controller;
        
        
        //POUR TESTER CALCULATION. A ENLEVER LORSQUE TERMINÉ
        //calculation.Calculate();
    }
    
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
        List<Country> countries = controller.GetCountriesforSimulation();
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            //@Override
            public void run() {
                if(getIsRunning()){
                    //CODE VIENT ICI
                    elapsedDay +=1;
                    //controller.printDay();
                    for(Country country : countries) {
                        System.out.println("Pop: "+country.getPopulation().getTotalPopulation());
                        country.incrementTotalPopulation();
                        controller.getWorld().updateCountryFromSimulation(country);
                        
                        
                    }
                    
                }else{
                    timer.cancel();
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
    
    public void Reset() {
        if(isRunning == true){
            this.isRunning=false;
        }
        elapsedDay = 0;
        System.out.println("Timer Reset");
    }
    
}
