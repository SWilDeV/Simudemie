/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.io.Serializable;
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
public class Simulation implements Serializable {
    private Calculation calculation = new Calculation();
    
    private boolean isRunning = false;
    private int elapsedDay = 0;
    private ArrayList<Integer> dataHistory = new ArrayList<Integer>();
    private transient final WorldController controller;
    private Disease disease;
    private static final long serialVersionUID = 4L; 
    //TODO: On a oublis de definir le type de DataHistory. J'ai mis int pour eviter les erreurs
    
    public Simulation(WorldController p_controller){
        controller = p_controller;
        disease = controller.getDisease();
    }
    
    public ArrayList<Integer> GetDataHistory() {
        return dataHistory; //TODO: DTO ou un deep copy?
    }
    
    public int GetElapsedDay() {
        return this.elapsedDay;
    }
    
    public boolean getIsRunning() {
        return this.isRunning;
    }
    
    private void SetRunning(boolean running) {
        this.isRunning = running;
    }
    
    public void Simulate() {
        
        List<Country> countries = controller.GetCountriesforSimulation();
        int countryListSize = countries.size();
        
        if(countryListSize>0){
            System.out.println("demarré");
            SetRunning(true);
            
            //Initialiser le patient zero
            if(controller.getWorld().getWorldPopulation().getInfectedPopulation() == 0){
                initializePatientZero(countries);
                controller.getWorld().updateWorldPopulation(); 
            }
                    
            //Timer faisant office de boucle principale
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    if(getIsRunning()){
                        elapsedDay +=1;
                        //UPDATE DES PAYS ET LEURS REGIONS
                        for(Country country : countries) {
                            System.out.println(country.GetRegions().size());

                            List<Region> regions = country.GetRegions();
                            if (controller.getWorld().getWorldPopulation().getInfectedPopulation() == 0){
                                timer.cancel();
                                System.out.println("Miracle, maladie éradiquée");
                            }
                            //Update individuel des regions
                            for(Region region:regions){
                               Region regionUpdated = updateRegions(region); 
                               
                               if(regionUpdated.getPopulation().getTotalPopulation()>regionUpdated.getPopulation().getInfectedPopulation()){
                                    controller.getWorld().updateRegionFromSimulation(country, regionUpdated);
                               }else{
                                    timer.cancel();
                                    System.out.println("end ! Des zombies partout!!");
                                }
                            }
                            //deuxieme  boucle pour la propagation interregionale
//                            if(regions.size() >1){
//                                propagateBetweenRegions(country);
//                            }
                            controller.getWorld().updateCountryFromSimulation(country);
                        }
                        //PRISE EN COMPTE DES LIENS ENTRE PAYS
                        List<Link> LinkList = controller.getWorld().getLinks();
                        if(LinkList.size()>0){
                            for(Link link:LinkList){
                                updateCountriesWithLinks(link.getCountry1(),link.getCountry2());
                            }
                        }
                        //UPDATE DE LA POPULATION MONDIALE
                        updateWorldPopulation();
                    }else{
                        timer.cancel();
                    }
                }
            }, 0, 500);
        }else{
            System.out.println("Veuillez ajouter au moins un pays");
        }
    }
    public void propagateBetweenRegions(Country country){
        List<Region> regions = country.GetRegions();
        int infectedToPropagateBetweenRegions = calculateInfected(country);
        Random rand = new Random();
        int index = rand.nextInt(regions.size());
        int counter = 0;
        for(Region region:regions){
            if(counter == index){
                Population pop = region.getPopulation();
                int infected = pop.getInfectedPopulation();
                infected += infectedToPropagateBetweenRegions;
                pop.setInfectedPopulation(infected);
                region.setPopulation(pop);
                controller.getWorld().updateRegionFromSimulation(country, region);
            }
        counter +=1;
        }
    }
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
    
    public int nextDay() {
        return elapsedDay +=1;
    }
    
    public void Pause() {
        if(isRunning == true){
            this.isRunning=false;
        }
    }
    
    public int previousDay() {
        return elapsedDay -=1;
    }
    
    public void Reset() {
        if(isRunning == true){
            this.isRunning=false;
        }
        elapsedDay = 0;
        System.out.println("Timer Reset");
    }
    
    public void splitNewInfectedInRegions(Country country, int newInfected){
        List<Region> regionList = country.GetRegions();
        while (newInfected >0){
            Random rand = new Random();
            int index = rand.nextInt(regionList.size());

            int counter = 0;
            for(Region region:regionList){
                if(index == counter){
                    Population population = region.getPopulation();
                    int infected = population.getInfectedPopulation();

                    Random randPop = new Random();
                    int randInfected = randPop.nextInt(newInfected);

                    if(randInfected == 0){
                        newInfected = 0;
                    }else{
                        infected +=randInfected;
                        population.setInfectedPopulation(infected);
                        region.setPopulation(population);
                        newInfected -=randInfected;
                    }
                }
                counter +=1;
            }
        }
    }
    
    public Population UpdatePopulation(Region region){
        double infectionRate = disease.getInfectionRate();
        double curedRate = disease.getCureRate();
        double mortalityRate = disease.getMortalityRate();
        
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
        
        return population;
    }
    
    public void updateCountriesWithLinks(Country country1, Country country2){
        double tauxAdhesion =1.0; 
        
        //Get populations
        Population population1 = country1.getPopulation();
        int previousInfectedPop1 = population1.getInfectedPopulation();
        Population population2 = country2.getPopulation();
        int previousInfectedPop2 = population2.getInfectedPopulation();
        
        //Calculate country new total infected that could go in other country
        int newInfectedPop1 = calculation.Calculate(previousInfectedPop2,0.05);
        int newInfectedPop2 = calculation.Calculate(previousInfectedPop1,0.05);
        
        //Country new total infected population
        int newTotalInfected1 = previousInfectedPop1 + newInfectedPop1;
        int newTotalInfected2 = previousInfectedPop2 + newInfectedPop2;
        
        //Set country new total infected pop
        population1.setInfectedPopulation(newTotalInfected1);
        population2.setInfectedPopulation(newTotalInfected2);
        country1.setPopulation(population1);
        country2.setPopulation(population2);
        
        //Now dispatch new infected in random regions 
        if(newInfectedPop1 > 0){
            splitNewInfectedInRegions(country1, newInfectedPop1);
        }
        if(newInfectedPop2 > 0){
            splitNewInfectedInRegions(country2, newInfectedPop2);
        }
        
        //Update countryList in World
        controller.getWorld().updateCountryFromSimulation(country1);
        controller.getWorld().updateCountryFromSimulation(country2);
    }
    
    public Region updateRegions(Region region){
        Population regionUpdated = UpdatePopulation(region);
        region.setPopulation(regionUpdated);
        return region;
    }
    
    public void updateWorldPopulation(){
        controller.getWorld().updateWorldPopulation(); 
        int globalInfected = controller.getWorld().getWorldPopulation().getInfectedPopulation();
        int globalPop = controller.getWorld().getWorldPopulation().getTotalPopulation();
        int globalDeads = controller.getWorld().getWorldPopulation().getDeadPopulation();
        controller.NotifyTick(elapsedDay, globalDeads,globalInfected,globalPop);
    }
    
    public int calculateInfected(Country country){
        int countryInfectedPop = country.getPopulation().getInfectedPopulation();
        
        
        int newInfectedPop = calculation.Calculate(countryInfectedPop,0.1);
        return newInfectedPop;
    }
    
    
}
