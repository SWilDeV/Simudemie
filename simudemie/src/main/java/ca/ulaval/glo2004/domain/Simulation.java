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
import java.util.logging.Level;
import java.util.logging.Logger;
import  mathematical_model.Calculation;

/**
 *
 * @author Abergel Clement
 */
public class Simulation implements Serializable {
    private Calculation calculation = new Calculation();
    
    private boolean isRunning = false;
    private int elapsedDay = 0;
    private ArrayList<World> undoRedoHistory = new ArrayList<World>();
    private transient final WorldController controller;
    private Timer timer = new Timer();
    private static final long serialVersionUID = 4L; 
    
    private int undoRedoIndex = 0;
    
    public Simulation(WorldController p_controller){
        controller = p_controller;
    }
    
    public Simulation(WorldController p_controller, int elapsedDay) {
        controller = p_controller;
        this.elapsedDay = elapsedDay;
    }
    
    public int GetElapsedDay() {
        return this.elapsedDay;
    }
    
    public int GetMaxDay() {
        return undoRedoHistory.size();
    }
    
    public boolean getIsRunning() {
        return this.isRunning;
    }
    
    private void SetRunning(boolean running) {
        this.isRunning = running;
    }
    
    public void Simulate(int timeStep) {
        
        List<Country> countries = controller.GetCountriesforSimulation();
        int countryListSize = countries.size();
        
        if(countryListSize > 0) {
            System.out.println("demarré");
            SetRunning(true);
            
            int undoRedoSize = GetMaxDay();
            if(undoRedoSize > 0) {
                undoRedoHistory.subList(undoRedoIndex, undoRedoSize).clear();
            }
            
            //Initialiser le patient zero
            if(controller.getWorld().getWorldPopulation().getInfectedPopulation() == 0){
                initializePatientZero(countries);
                controller.getWorld().updateWorldPopulation(); 
            }
                    
            //Timer faisant office de boucle principale
            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    if(getIsRunning()){                        
                        //UPDATE DES PAYS ET LEURS REGIONS
                        for(Country country : countries) {
                            
                            //PRISE EN COMPTE DES LIENS ENTRE PAYS
                            List<Link> LinkList = controller.getWorld().getLinks();
                            if(LinkList.size()>0){
                                for(Link link:LinkList){
                                    updateCountriesWithLinks(controller.GetCountry(link.getCountry1Id()), controller.GetCountry(link.getCountry2Id()));
                                }
                            }
                            
                        }   
                        controller.getWorld().updateWorldPopulation();
                        
                        List<Country> countries2 = controller.GetCountriesforSimulation();
                        for(Country country : countries2) {
                            //boucle pour la propagation interregionale
                            if (controller.getWorld().getWorldPopulation().getInfectedPopulation() == 0){
                                timer.cancel();
                                System.out.println("Miracle, maladie éradiquée");
                            }
                            List<Region> regions = country.GetRegions();
                            if(regions.size() >1){
                                propagateBetweenRegions(country);
                            }   
                            
                            //deuxieme  boucle pour Update individuel des regions
                            for(Region region:regions){
                               Region regionUpdated = updateRegions(region); 
                               if(controller.getWorld().getWorldPopulation().getTotalPopulation()>0){
                                    controller.getWorld().updateRegionFromSimulation(country, regionUpdated);
                               }else{
                                    timer.cancel();
                                    System.out.println("end ! Des zombies partout!!");
                                }
                            }
                            controller.getWorld().updateCountryFromSimulation(country);
                        }
                        
                        //UPDATE DE LA POPULATION MONDIALE
                        updateWorldPopulation();
                        
                        elapsedDay +=1;
                        
                        try {
                            AddUndoRedoWorld(controller.getWorld().clone());
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        timer.cancel();
                    }
                }
            }, 0, timeStep);
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
    
    public void Pause() {
        if(isRunning){
            timer.cancel();
            timer.purge();
            this.isRunning = false;
        }
    }
    
    public void Reset() {
        if(isRunning){
            this.isRunning = false;
        }
        
        elapsedDay = 0;
        undoRedoIndex = 0;
        ClearUndoRedo();
    }
    
    public Population UpdatePopulation(Region region){
        Disease disease = controller.getDisease();
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
//            splitNewInfectedInRegions(country1, newInfectedPop1);
            country1.splitNewInfectedInRegions(newInfectedPop1);
        }
        if(newInfectedPop2 > 0){
//            splitNewInfectedInRegions(country2, newInfectedPop2);
              country2.splitNewInfectedInRegions(newInfectedPop2);
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
        
        
        int newInfectedPop = calculation.Calculate(countryInfectedPop,0.05);
        return newInfectedPop;
    }
    
    public void AddUndoRedoWorld(World world) { //TODO: Demande ce qu'il entend par undo. Undo une action, ou une jouurnee ?
        undoRedoHistory.add(world);
        undoRedoIndex++;
    }
    
    public World Undo() {
        if(undoRedoIndex - 1 >= 0) {
            undoRedoIndex--;
            elapsedDay--;
        }
        
        return GetWorld(undoRedoIndex);
    }
    
    public World Redo() {
        
        if(undoRedoIndex + 1 < undoRedoHistory.size()) {
            undoRedoIndex++;
            elapsedDay++;
        }
        
        return GetWorld(undoRedoIndex);
    }
    
    public World SpecificRedo(int position) {
        undoRedoIndex = position;
        elapsedDay = position;
        
        return GetWorld(undoRedoIndex);
    }
    
    public void ClearUndoRedo() {
        undoRedoHistory.clear();
        undoRedoIndex = 0;
    }
    
    private World GetWorld(int undoPosition) {
        if(undoPosition < 0 || undoRedoHistory.isEmpty() || undoPosition >= undoRedoHistory.size()) {
            return null;
        }
        
        return undoRedoHistory.get(undoPosition);
    }
}
