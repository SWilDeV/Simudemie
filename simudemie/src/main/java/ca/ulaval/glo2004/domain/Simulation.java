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
public class Simulation implements java.io.Serializable {
    private Calculation calculation = new Calculation();
    private boolean isRunning = false;
    private int elapsedDay = 0;
    private ArrayList<Integer> dataHistory = new ArrayList<Integer>();
    private final WorldController controller;
    //TODO: On a oublis de definir le type de DataHistory. J'ai mis int pour eviter les erreuurs
    
    public Simulation(WorldController p_controller){
        controller = p_controller;
        //POUR TESTER CALCULATION. A ENLEVER LORSQUE TERMINÉ
//        int success = calculation.Calculate(1,0.1);
//        System.out.println(success); 
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
        for(Country country : countries) {
        country.getPopulation().addPatientZero();
        controller.getWorld().updateCountryFromSimulation(country);
        }
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            //@Override
            public void run() {
                if(getIsRunning()){
                    elapsedDay +=1;
                    controller.printDay();
                    for(Country country : countries) {
                        //country.incrementTotalPopulation();
                        Population updated = UpdatePopulation(country);
                        country.setPopulation(updated);
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
    public int calculateInfectedPopulation(Country country){
        int infectedPop = country.getPopulation().getInfectedPopulation();
        return calculation.Calculate(infectedPop,0.1);
    }
    
    public Population UpdatePopulation(Country country){
        //Gets
        Population population = country.getPopulation();
        int infectedPop = population.getInfectedPopulation();
        int totalPop = population.getTotalPopulation();
        int curedPop= population.getCuredPopulation();
        
        //Calculation
        int newInfectedPop = calculation.Calculate(infectedPop,0.15) + infectedPop;
        int newDeadPop = calculation.Calculate(infectedPop,0.01);
      
        int newNonInfectedPop = totalPop - newInfectedPop;
        
        int newCuredPop = calculation.Calculate(infectedPop,0.05);
        if(newCuredPop>0){
            newInfectedPop -= newCuredPop;
            newNonInfectedPop += newCuredPop;
            curedPop+=newCuredPop;
        }
        
        int newTotalPop = totalPop;
        if(newDeadPop > 0){
            newTotalPop -= newDeadPop;
        } else{
            newTotalPop = newInfectedPop + newNonInfectedPop;
        }
        
        int deadPop= population.getDeadPopulation() + newDeadPop;
        
        
        //Sets
        population.setInfectedPopulation(newInfectedPop);
        population.setNonInfectedPopulation(newNonInfectedPop);
        population.setDeadPopulation(deadPop);
        population.setTotalPopulation(newTotalPop);
        population.setCuredPopulation(curedPop);
        return population;
    }
    
}
