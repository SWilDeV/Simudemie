/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    private List<Disease> diseaseList = new ArrayList<>();
    private final Disease defaultDisease = new Disease("ebola",0.01, 0.10, 0.20);
    
    private Disease currentDisease;
    private int currentDiseaseIndex = 0;
    private int currentCountryPatientZeroIndex = 0;
    private int nbOfPatientZero = 1;
    private boolean isRunning = false;
    private int elapsedDay = 0;
    private transient final WorldController controller;
    private Timer timer = new Timer();
    private static final long serialVersionUID = 4L; 
    
    
    private ArrayList<UndoRedo> undoRedoHistory = new ArrayList<>();
    private UndoRedo defaultState = null;
    private int undoRedoIndex = 0;
    
    public Simulation(WorldController p_controller){
        controller = p_controller;
        addDisease(defaultDisease);
        setcurrentDisease(currentDiseaseIndex);
    }
    
    public Simulation(WorldController p_controller, int elapsedDay) {
        controller = p_controller;
        this.elapsedDay = elapsedDay;
    }
    
    public Simulation(WorldController p_controller, List<Disease> diseases, int elapsedDay) {
        controller = p_controller;
        diseaseList = diseases;
        this.elapsedDay = elapsedDay;
        
        setcurrentDisease(currentDiseaseIndex);
    }
    
    public boolean HasDisease() {
        return currentDiseaseIndex != -1;
    }
    
    public void SetDefaultState(UndoRedo ur) {
        defaultState = ur;
    }
    
    public UndoRedo GetDefaultState() {
        return defaultState;
    }
    
    public void printdiseases(){
        for (Disease d : diseaseList){
            System.out.println(d.getName());
        }
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
    
    public void SetDiseases(List<Disease> diseases) {
        diseaseList = diseases;
    }
    
    public void RemoveCurrentDisease() {
        diseaseList.remove(currentDiseaseIndex);
        if(diseaseList.isEmpty()) {
            currentDiseaseIndex = -1;
        } else {
            setCurrentDiseaseIndex(currentDiseaseIndex);
        }
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
                initializePatientZero(countries, getCurrentCountryPatientZeroIndex(), getCurrentNbOfPatientZero());
                controller.getWorld().updateWorldPopulation(); 
            }
                    
            //Timer faisant office de boucle principale
            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    if(getIsRunning()){  

                        List<Link> LinkList = controller.getWorld().getLinks();
                        if(LinkList.size()>0){
                            for(Link link:LinkList) {
                            updateCountriesWithLinks(link, controller.GetCountry(link.getCountry1Id()), controller.GetCountry(link.getCountry2Id()));
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
                                updateRegionsWithLinks(link, country.FindRegionByUUID(link.GetRegion1Id()),country.FindRegionByUUID(link.GetRegion2Id()));
                            }
                            
                            //deuxieme  boucle pour Update individuel des regions
                            List<Region> regions2 = country.GetRegions();
                            for(Region region:regions2){
                               if(controller.getWorld().getWorldPopulation().getTotalPopulation()>0){
                                    UpdatePopulation(region);
                               }else{
                                    timer.cancel();
                                    System.out.println("end ! Des zombies partout!!");
                                }
                            }
                        }
                        
                        //UPDATE DE LA POPULATION MONDIALE
                        updateUIWithWorldPopulation();
                        
                        elapsedDay +=1;
                        
                        try {
                            AddUndoRedoWorld(controller.getWorld());
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
    
    public void updateRegionsWithLinks(RegionLink link, Region region1, Region region2){ 
        double transmissionRate = link.getTransmissionRate(); 
        
        Population pop1 = region1.getPopulation();
        Population pop2 = region2.getPopulation();
        
        //Get populations
        int previousInfectedPop1 = pop1.getInfectedPopulation();
        int previousInfectedPop2 = pop2.getInfectedPopulation();
        
        //(if !link.isOpen()) {
            //double infectedPop1 = (double)previousInfectedPop1/(double)pop1;
            //double infectedPop2 = (double)previousInfectedPop2/(double)pop2;
            //boolean thresholdMet = (infectedPop1 >= closeLink.getThreshold()) && (infectedPop2 >= closeLink.getThreshold());
            //if (link == closeLink.getConcernedLink() && thresholdMet) {
                //transmission rate = transmission rate * closeLink.getAdhesionRate();
            //}
        //}
        
        //Calculate region new total infected that could go in other region
        int newInfectedPop1 = calculation.Calculate(previousInfectedPop2, transmissionRate);
        int newInfectedPop2 = calculation.Calculate(previousInfectedPop1, transmissionRate);
        
        //region new total infected population
        int newTotalInfected1 = previousInfectedPop1 + newInfectedPop1 - newInfectedPop2;
        int newTotalInfected2 = previousInfectedPop2 + newInfectedPop2 - newInfectedPop1;
        
        //Set country new total infected pop
        pop1.setInfectedPopulation(newTotalInfected1);
        pop2.setInfectedPopulation(newTotalInfected2);
        
    }
        
    public void initializePatientZero(List<Country> countries, int idx, int nbOfPatientZero){
        //Initialiser le patient zero
        int counter = 0;
        Random rand = new Random();
        for(Country country : countries) {
            if(idx == counter){
                List<Region>regionList = country.GetRegions();
                int maxRand2 = regionList.size();
                int index2 = rand.nextInt(maxRand2);
                int counter2 = 0;
                
                for (Region region:regionList){
                    if (index2 == counter2){
                        region.getPopulation().addPatientZeroV2(nbOfPatientZero);
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
        
        double infectionRate = currentDisease.getInfectionRate();
        double curedRate = currentDisease.getCureRate();
        double mortalityRate = currentDisease.getMortalityRate();
    
        //population
        Population population = region.getPopulation();
        int totalPop = population.getTotalPopulation();
        
        //infectedPop
        int previousDayInfectedPop = population.getInfectedPopulation();
        
        //percentage infected
        double percentInfected = (double)previousDayInfectedPop/(double)totalPop;
        
        //ajout effet des mesures s'ils sont actives
        if (!region.GetMesures().isEmpty()) {
            for (HealthMesure mesure: region.GetMesures()) {
                if (percentInfected >= mesure.getThreshold() && mesure.getActive()) {
                    
                    double reproductionRate = infectionRate/curedRate;
                    double newReproductionRate = reproductionRate - mesure.getEffectReproductionRate();
                    curedRate = (infectionRate/newReproductionRate) * (1 - mesure.getAdhesionRate());  //mise à jour du cure rate en tenant compte de l'adhésion
                    
                    infectionRate = infectionRate * (1 - mesure.getEffectTransmissionRate()); //diminution du taux de transmission 
                    infectionRate = infectionRate * (1 - mesure.getAdhesionRate()); //prise en compte de l'adhésion
                    
                    
                }
            }
        }
        
        int newInfectedPop = calculation.Calculate(previousDayInfectedPop,infectionRate);
        int totalInfectedPop = newInfectedPop + previousDayInfectedPop;
        
        //cured people
        int curedPop = calculation.Calculate(totalInfectedPop,curedRate);
        if(curedPop>0){
            totalInfectedPop -= curedPop;
            if(totalInfectedPop<0){
                totalInfectedPop=0;
            }
        }
        
        //dead population
        int previousDayDeadPop= population.getDeadPopulation();
        int newDeadPop = calculation.Calculate(totalInfectedPop,mortalityRate);
        int totalDeadPop= previousDayDeadPop + newDeadPop;
        
        //total population
        int newTotalPop = totalPop - newDeadPop;
        if(newTotalPop<0){
            newTotalPop=0;
        }
        
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
    
    public void updateCountriesWithLinks(Link link, Country country1, Country country2){
        double transmissionRate = link.getTransmissionRate();
           
        
        //Get populations
        Population population1 = country1.getPopulation();
        int previousInfectedPop1 = population1.getInfectedPopulation();
        Population population2 = country2.getPopulation();
        int previousInfectedPop2 = population2.getInfectedPopulation();
        
        double percentageInfected1 = (double)previousInfectedPop1/(double)population1.getTotalPopulation();
        double percentageInfected2 = (double)previousInfectedPop2/(double)population2.getTotalPopulation();
        
        List <CloseLink> closedLinks = controller.getWorld().getClosedLinks();
        for (CloseLink closeLink: closedLinks) {
            boolean thresholdMet = (percentageInfected1 >= closeLink.getThreshold()) || 
                                     (percentageInfected2 >= closeLink.getThreshold());
            if ((link.GetId() == closeLink.getConcernedLink()) && thresholdMet) {
                transmissionRate = transmissionRate * closeLink.getAdhesionRate();
                System.out.println("mesure applied");
            }
        }
        
        //Calculate country new total infected that could go in other country
        int newInfectedPop1 = calculation.Calculate(previousInfectedPop2, transmissionRate);
        int newInfectedPop2 = calculation.Calculate(previousInfectedPop1, transmissionRate);
        
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
            country1.removeTravellingInfectedInRegions(newInfectedPop2);
        }
        if(newInfectedPop2 > 0){
//            splitNewInfectedInRegions(country2, newInfectedPop2);
              country2.splitNewInfectedInRegions(newInfectedPop2);
              country2.removeTravellingInfectedInRegions(newInfectedPop1);

        }
    }
    
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
    
    public void AddUndoRedoWorld(World world) throws CloneNotSupportedException { //TODO: Demande ce qu'il entend par undo. Undo une action, ou une jouurnee ?
        undoRedoHistory.add(CreateUndoRedo(world));
        undoRedoIndex++;
    }
    
    public ArrayList GetUndoRedoHistory() {
        return undoRedoHistory;
    }
    
    public UndoRedo CreateUndoRedo(World world) throws CloneNotSupportedException {
        return new UndoRedo(world.clone(), diseaseList, currentDiseaseIndex, elapsedDay);
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
    
    public UndoRedo GetLastUndoRedo() {
        if(undoRedoHistory.isEmpty()) {
            return null;
        }
        
        return undoRedoHistory.get(undoRedoHistory.size() - 1);
    }
    
    public void createDisease(String diseaseName,double infectionRate, double mortalityRate, double cureRate){
        Disease d = new Disease(diseaseName,cureRate, mortalityRate, infectionRate);
        addDisease(d);
    }
   
    public void addDisease(Disease disease){
        diseaseList.add(disease);
        controller.NotifyDiseaseCreated(new DiseaseDTO(disease));
        System.out.println("Create disease : " + disease.getName());
    }
    
    public Disease FindDiseaseByUUID(UUID diseaseId){
         try {
            return diseaseList.stream().filter(d -> d.getId().equals(diseaseId)).findAny().get();
        } catch(NoSuchElementException e) {
            return null;
        }
     }
     
    public List<Disease> getDiseaseList(){
         return diseaseList;
     }
    
    public int getCurrentDiseaseIndex(){
         return currentDiseaseIndex;
     }
    
    public int getCurrentCountryPatientZeroIndex(){
         return currentCountryPatientZeroIndex;
     }
    
    public int getCurrentNbOfPatientZero(){
         return nbOfPatientZero;
     }
    
    public Disease getCurrentDisease(){
         return currentDisease;
     }
     
    public DiseaseDTO GetDiseaseDTO(){
        return new DiseaseDTO(currentDisease);
    }
     
    public void removeDisease(UUID diseaseId){
        Disease disease = FindDiseaseByUUID(diseaseId);
        if(disease != null) {
            diseaseList.remove(disease);
            controller.NotifyOnDiseaseDestroy();
        }
    }
     
    public void setcurrentDisease(int index){
        currentDisease = diseaseList.get(index);
    }
    
    public void setCurrentCountryPatientZero(int index){
        //currentCountryPatientZero = controller.GetCountries(index);
    }
    
    public void setCurrentCountryPatientZeroIndex(int index){
        currentCountryPatientZeroIndex = index;
    }
    
    public void setNbOfPatientZero(int nbPatient){
        nbOfPatientZero = nbPatient;
    }
                
    public void setCurrentDiseaseByUUID(UUID id){
        Disease d = FindDiseaseByUUID(id);
        if(d != null){
            currentDisease.setName(d.getName());
            currentDisease.setInfectionRate(d.getInfectionRate());
            currentDisease.setMortalityRate(d.getMortalityRate());
            currentDisease.setCureRate(d.getCureRate());
            System.out.println("mortalityRate: "+ currentDisease.getMortalityRate() +", curedRate: "+ currentDisease.getCureRate() + ", infectedtRate : " + currentDisease.getInfectionRate());

        }
    }
     
    public void setCurrentDiseaseIndex(int index){
         currentDiseaseIndex = index;
     }
     
    public void updateDiseaseDTO(DiseaseDTO disease) {
        Disease d = FindDiseaseByUUID(disease.getId());
        if(d != null){
            d.updateFromDTO(disease);
        }
    }
    public void UpdateDisease(UUID id, String name,double infectionRate, double mortalityRate, double cureRate){
        Disease d = FindDiseaseByUUID(id);
        if(d != null){
            d.update(name,infectionRate, mortalityRate, cureRate);
        }
    }
}
