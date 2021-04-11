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
import java.util.UUID;
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
    private ArrayList<UndoRedo> undoRedoHistory = new ArrayList<>();
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
    
    public void SetElapsedDay(int day) {
        elapsedDay = day;
    }
    
    public int GetUndoRedoPosition() {
        return undoRedoIndex;
    }
    
    public int GetUndoRedoSize() {
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
            
            int undoRedoSize = GetUndoRedoSize();
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
                        
                        //PRISE EN COMPTE DES LIENS ENTRE PAYS
                        
//                        List<Link> LinkList = controller.getWorld().getLinks();
//                        List<Country> countries = controller.GetCountriesforSimulation();
//                        if(LinkList.size()>0){
//                            for(Link link:LinkList){
//                                UUID region1ID = UUID.randomUUID();
//                                UUID region2ID= UUID.randomUUID();
//                                UUID country1ID= UUID.randomUUID();
//                                UUID country2ID= UUID.randomUUID();
//                                
//                                Boolean region1Found = false;
//                                Boolean region2Found = false;
//                                for(Country country:countries){
//                                    if(link.getCountry1Id() == country.getRegion0().GetId()){
//                                        region1Found = true;
//                                        region1ID = link.getCountry1Id();
//                                        country1ID = country.GetId();
//                                    }
//                                }
//                                if (region1Found){
//                                    for(Country country:countries){
//                                        if(link.getCountry2Id() == country.getRegion0().GetId()){
//                                        region2Found = true;
//                                        region2ID = link.getCountry2Id();
//                                        country2ID = country.GetId();
//                                        }
//                                    }
//                                }
//                                if (region1Found && region2Found){
//                                    updateRegionsWithLinks(controller.getWorld().FindCountryByUUID(country1ID).FindRegionByUUID(region1ID), controller.getWorld().FindCountryByUUID(country2ID).FindRegionByUUID(region2ID));
//                                }
//                                region1Found = false;
//                                region2Found = false;
////                                updateCountriesWithLinks(controller.GetCountry(link.getCountry1Id()), controller.GetCountry(link.getCountry2Id()));
//                                   
//                            }
//                        }
                        //controller.getWorld().updateWorldPopulation();
                        List<Link> LinkList = controller.getWorld().getLinks();
                        if(LinkList.size()>0){
                            for(Link link:LinkList) {
                            updateCountriesWithLinks(controller.GetCountry(link.getCountry1Id()), controller.GetCountry(link.getCountry2Id()));
                            }
                        }
                        
                        
                        
                        List<Country> countries2 = controller.GetCountriesforSimulation();
                        for(Country country : countries2) {
                            
                            if (controller.getWorld().getWorldPopulation().getInfectedPopulation() == 0){
                                timer.cancel();
                                System.out.println("Miracle, maladie éradiquée");
                            }
                            
                            List<RegionLink> regionLinks = country.GetLinks();
                            for(RegionLink link:regionLinks){
                                updateRegionsWithLinks(country.FindRegionByUUID(link.GetRegion1Id()),country.FindRegionByUUID(link.GetRegion2Id()));
                            }
                            
                            //deuxieme  boucle pour Update individuel des regions
                            List<Region> regions2 = country.GetRegions();
                            for(Region region:regions2){
                               //Region regionUpdated = updateRegions(region); 
                               if(controller.getWorld().getWorldPopulation().getTotalPopulation()>0){
                                    UpdatePopulation(region);
                               }else{
                                    timer.cancel();
                                    System.out.println("end ! Des zombies partout!!");
                                }
                            }
                            System.out.println(country.GetLinks());
                        }
                        
                        //UPDATE DE LA POPULATION MONDIALE
                        updateUIWithWorldPopulation();
                        
                        elapsedDay +=1;
                        
                        try {
                            AddUndoRedoWorld(controller.getWorld(), controller.getDisease());
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
    public void update(){
        /* 
        Sources d'infections : 
        Region elle-meme 
            UpdateRegion() 
            ->region qui est updatee individuellement : OK
        
        Interregional
            updateRegionsWithLinks()
            ->prend deux regions et propage en en fonction des liens
            
        
        Interpays    
            UpdateCountriesWithLinks
            prend deux pays, determine un montant aleatoire de pop qui
            peu voyager,
        
        */
        
    }
    
    public void updateRegionsWithLinks(Region region1, Region region2){
        Population pop1 = region1.getPopulation();
        Population pop2 = region2.getPopulation();
        
        //Get populations
        int previousInfectedPop1 = pop1.getInfectedPopulation();
        int previousInfectedPop2 = pop2.getInfectedPopulation();
        
        //Calculate region new total infected that could go in other region
        int newInfectedPop1 = calculation.Calculate(previousInfectedPop2,0.05);
        int newInfectedPop2 = calculation.Calculate(previousInfectedPop1,0.05);
        
        //region new total infected population
        int newTotalInfected1 = previousInfectedPop1 + newInfectedPop1 - newInfectedPop2;
        int newTotalInfected2 = previousInfectedPop2 + newInfectedPop2 - newInfectedPop1;
        
        //Set country new total infected pop
        pop1.setInfectedPopulation(newTotalInfected1);
        pop2.setInfectedPopulation(newTotalInfected2);
        
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
    
    public void UpdatePopulation(Region region){
//    public Population UpdatePopulation(Region region){
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
        
//        return population;
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
        int newTotalInfected1 = previousInfectedPop1 + newInfectedPop1 - newInfectedPop2;
        int newTotalInfected2 = previousInfectedPop2 + newInfectedPop2 - newInfectedPop1;
        
        
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
    }
    
//    public Region updateRegions(Region region){
//        Population regionUpdated = UpdatePopulation(region);
//        region.setPopulation(regionUpdated);
//        return region;
//    }
    
    public void updateUIWithWorldPopulation(){
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
    
    public void AddUndoRedoWorld(World world, Disease disease) throws CloneNotSupportedException { //TODO: Demande ce qu'il entend par undo. Undo une action, ou une jouurnee ?
        undoRedoHistory.add(new UndoRedo(world.clone(), disease.clone(), elapsedDay));
        undoRedoIndex++;
    }
    
    public UndoRedo Undo() {
        if(undoRedoIndex - 1 >= 0) {
            undoRedoIndex--;
        }
        
        return GetUndoRedo(undoRedoIndex);
    }
    
    public UndoRedo Redo() {
        
        if(undoRedoIndex + 1 < undoRedoHistory.size()) {
            undoRedoIndex++;
        }
        
        return GetUndoRedo(undoRedoIndex);
    }
    
    public UndoRedo SpecificRedo(int position) {
        undoRedoIndex = position;
        return GetUndoRedo(undoRedoIndex);
    }
    
    public void ClearUndoRedo() {
        undoRedoHistory.clear();
        undoRedoIndex = 0;
    }
    
    private UndoRedo GetUndoRedo(int undoPosition) {
        if(undoPosition < 0 || undoRedoHistory.isEmpty() || undoPosition >= undoRedoHistory.size()) {
            return null;
        }
        
        return undoRedoHistory.get(undoPosition);
    }
}
