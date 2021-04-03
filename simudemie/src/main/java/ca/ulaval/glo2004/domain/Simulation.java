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
    //private Disease disease = new Disease();
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
        List<Country> countries = controller.GetCountriesforSimulation();
        int countryListSize = countries.size();
        
        //Fake creation de regions
//        for(Country country:countries){
//            country.addRegionsToList(1993);
//        }
        
        if(countryListSize>0){
            System.out.println("demarré");
            SetRunning(true);
            
            //Initialiser le patient zero
            initializePatientZero(countries);
                    
            //Timer faisant office de boucle principale
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    if(getIsRunning()){
                        elapsedDay +=1;
                        int globalInfected = 0;
                        for(Country country : countries) {
                            List<Region> regions = country.GetRegions();
                            for(Region region:regions){
                               Population regionUpdated = UpdatePopulation(region);
//                            Population updated = UpdatePopulation(country);
//                            if (country[index].getInfectedPopulation()== 0){
//                                timer.cancel();
//                            }
                                if(regionUpdated.getTotalPopulation()>regionUpdated.getInfectedPopulation()){
                                    region.setPopulation(regionUpdated);
                                    controller.getWorld().updateRegionFromSimulation(country, region);
                                }else{
                                    timer.cancel();
                                    System.out.println("end ! Des zombies partout!!");
                                }
                                globalInfected+=regionUpdated.getInfectedPopulation(); 
                            }
                            controller.getWorld().updateCountryFromSimulation(country);
                            
                        }
                        System.out.println(countries.size());
                        controller.NotifyTick(elapsedDay, globalDeads,globalInfected);
                    }else{
                        timer.cancel();
                    }
                }
            }, 0, 1000);
        }else{
            System.out.println("Veuillez ajouter au moins un pays");
        }
    }
    
    public Population UpdatePopulation(Region region){
        double infectionRate = controller.getDisease().getInfectionRate()/100;
        double curedRate = controller.getDisease().getCureRate()/100;
        double mortalityRate = controller.getDisease().getMortalityRate()/100;
        
        //population
        Population population = region.getPopulation();
        int totalPop = population.getTotalPopulation();
        
        //infectedPop
        int previousDayInfectedPop = population.getInfectedPopulation();
        int newInfectedPop = calculation.Calculate(previousDayInfectedPop,infectionRate);
        int totalInfectedPop = newInfectedPop + previousDayInfectedPop;
        
        //cured people
        int curedPop = calculation.Calculate(totalInfectedPop,curedRate);
        if(curedPop>0){
            totalInfectedPop -= curedPop;
        }
        
        //dead population
        int previousDayDeadPop= population.getDeadPopulation();
        int newDeadPop = calculation.Calculate(totalInfectedPop,mortalityRate);
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
//    public Population UpdatePopulation(Country country){
//        //population
//        Population population = country.getPopulation();
//        int totalPop = population.getTotalPopulation();
//        
//        //infectedPop
//        int previousDayInfectedPop = population.getInfectedPopulation();
//        int newInfectedPop = calculation.Calculate(previousDayInfectedPop,0.15);
//        int totalInfectedPop = newInfectedPop + previousDayInfectedPop;
//        
//        //non infected people
//        int curedPop = calculation.Calculate(totalInfectedPop,0.05);
//        if(curedPop>0){
//            totalInfectedPop -= curedPop;
//        }
//        
//        //dead population
//        int previousDayDeadPop= population.getDeadPopulation();
//        int newDeadPop = calculation.Calculate(totalInfectedPop,0.01);
//        int totalDeadPop= previousDayDeadPop + newDeadPop;
//        
//        //total population
//        int newTotalPop = totalPop - newDeadPop;
//        int totalNonInfectedPop = newTotalPop-totalInfectedPop;
//        if (totalNonInfectedPop <0){
//            totalNonInfectedPop = 0;
//            totalInfectedPop = newTotalPop;
//        }
//        
//     
//        //Sets
//        population.setInfectedPopulation(totalInfectedPop);
//        population.setNonInfectedPopulation(totalNonInfectedPop);
//        population.setDeadPopulation(totalDeadPop);
//        population.setTotalPopulation(newTotalPop);
//        globalDeads+=newDeadPop;
//        
//        return population;
//    }
    
    
    public void initializePatientZero(List<Country> countries ){
        //Initialiser le patient zero
        Random rand = new Random();
        int maxRand = countries.size();
        int index = rand.nextInt(maxRand);
        int counter = 0;
        for(Country country : countries) {
            if(index == counter){
                List<Region>regionList = country.GetRegions();
                int maxRand2 = regionList.size();
                int index2 = rand.nextInt(maxRand2);
                int counter2 = 0;
                
                for (Region region:regionList){
                    if (index2 == counter2){
                        region.getPopulation().addPatientZero();
                        controller.getWorld().updateCountryFromSimulation(country);
                    }
                    counter2 +=1;
                }                
            }
            counter +=1;
        }
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
