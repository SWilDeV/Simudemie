/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.Random;
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
    private int globalDeads = 0;
    private ArrayList<Integer> dataHistory = new ArrayList<Integer>();
    private final WorldController controller;
    //TODO: On a oublis de definir le type de DataHistory. J'ai mis int pour eviter les erreurs
    
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
        Random rand = new Random();
        int maxRand = countries.size();
        int index = rand.nextInt(maxRand);
        int counter = 0;
        for(Country country : countries) {
            if(index == counter){
                country.getPopulation().addPatientZero();
                controller.getWorld().updateCountryFromSimulation(country);
            }
            counter +=1;
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            //@Override
            public void run() {
                if(getIsRunning()){
                    elapsedDay +=1;
                    int globalInfected = 0;
                    for(Country country : countries) {
                        Population updated = UpdatePopulation(country);
                        if(updated.getTotalPopulation()>updated.getInfectedPopulation()){
                            country.setPopulation(updated);
                            controller.getWorld().updateCountryFromSimulation(country);
                        }else if (updated.getInfectedPopulation()== 0){
                            timer.cancel();
                            System.out.println("Miracle, la maladie a disparu");
                        }else{
                            timer.cancel();
                            System.out.println("end ! Des zombies partout!!");
                        }
                        globalInfected+=updated.getInfectedPopulation();
                    }
                    controller.NotifyTick(elapsedDay, globalDeads,globalInfected);
                }else{
                    timer.cancel();
                }
            }
        }, 0, 500);
    }
    
    public Population UpdatePopulation(Country country){
        //population
        Population population = country.getPopulation();
        int totalPop = population.getTotalPopulation();
        
        //infectedPop
        int previousDayInfectedPop = population.getInfectedPopulation();
        int newInfectedPop = calculation.Calculate(previousDayInfectedPop,0.15);
        int totalInfectedPop = newInfectedPop + previousDayInfectedPop;
        
        //non infected people
        int curedPop = calculation.Calculate(totalInfectedPop,0.05);
        if(curedPop>0){
            totalInfectedPop -= curedPop;
        }
        
        //dead population
        int previousDayDeadPop= population.getDeadPopulation();
        int newDeadPop = calculation.Calculate(totalInfectedPop,0.01);
        int totalDeadPop= previousDayDeadPop + newDeadPop;
        
        //total population
        int newTotalPop = totalPop - newDeadPop;
        int totalNonInfectedPop = newTotalPop-totalInfectedPop;
        if (totalNonInfectedPop <0){
            totalNonInfectedPop = 0;
            totalInfectedPop = newTotalPop;
        }
        
     
        //Sets
        population.setInfectedPopulation(totalInfectedPop);
        population.setNonInfectedPopulation(totalNonInfectedPop);
        population.setDeadPopulation(totalDeadPop);
        population.setTotalPopulation(newTotalPop);
        globalDeads+=newDeadPop;
        
        return population;
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
//    public Population UpdatePopulation(Country country){
//        //Gets
//        Population population = country.getPopulation();
//        int infectedPop = population.getInfectedPopulation();
//        int totalPop = population.getTotalPopulation();
//        int curedPop= population.getCuredPopulation();
//        
//        //Calculation
//        int newInfectedPop = calculation.Calculate(infectedPop,0.15) + infectedPop;
//        int newDeadPop = calculation.Calculate(infectedPop,0.01);
//      
//        int newNonInfectedPop = totalPop - newInfectedPop;
//        
//        int newCuredPop = calculation.Calculate(infectedPop,0.05);
//        if(newCuredPop>0){
//            newInfectedPop -= newCuredPop;
//            newNonInfectedPop += newCuredPop;
//            curedPop+=newCuredPop;
//        }
//        
//        int newTotalPop = totalPop;
//        if(newDeadPop > 0){
//            newTotalPop -= newDeadPop;
//        } else{
//            newTotalPop = newInfectedPop + newNonInfectedPop;
//        }
//        
//        int deadPop= population.getDeadPopulation() + newDeadPop;
//        
//        
//        //Sets
//        population.setInfectedPopulation(newInfectedPop);
//        population.setNonInfectedPopulation(newNonInfectedPop);
//        population.setDeadPopulation(deadPop);
//        population.setTotalPopulation(newTotalPop);
//        population.setCuredPopulation(curedPop);
//        return population;
//    }
    
}
