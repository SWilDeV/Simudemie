/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.Link.LinkType;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author Abergel Clement
 */
public class WorldController implements java.io.Serializable {
    
    private World world = new World();
    private Simulation simulation;
    private Disease disease = new Disease(0.04, 0.02, 0.15);
    private final WorldDrawer worldDrawer;
    private List<HealthMesure> mesures = new ArrayList<>();
    private List<WorldObserver> observers = new ArrayList<>(); //TODO: Discuter de ou mettre l'observer. Ici ou dans simulation ? 
    
    public int GetElapsedDay() {
        return simulation.GetElapsedDay();
    }
    
    public List<CountryDTO> GetCountries() {
        return (List<CountryDTO>) world.getCountries().stream().map(e -> new CountryDTO((Country) e)).collect(Collectors.toList());
    }
    public List<Country> GetCountriesforSimulation() {
        return (List<Country>) world.getCountries();
    }
    
    public List<LinkDTO> GetLinks() {
        return (List<LinkDTO>) world.getLinks().stream().map(e -> new LinkDTO((Link) e)).collect(Collectors.toList());
    }
    
    public List<HealthMesureDTO> GetHealthMesures(){
        return (List<HealthMesureDTO>) mesures.stream().map(e -> new HealthMesureDTO((HealthMesure) e)).collect(Collectors.toList());
    }
    
    public CountryDTO FindCountryByPosition(Point position) {
        Country country = world.findCountryByPosition(position);
        if(country != null) {
            return new CountryDTO(country);
        }
        return null;
    }
    
    private HealthMesure FindMesureByUUID(UUID id) {  
        for(HealthMesure mesure: mesures) {
            if(mesure.getID() == id) {
                return mesure;
            }
        }
        return null;
    }
    
    public WorldController() {
        worldDrawer = new WorldDrawer(this);
        world = new World(this);
        simulation = new Simulation(this);
    }
    
    public World getWorld(){
        return world;
    }
    
    public void Subscribe(WorldObserver observer) {
        if(!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void Unsubscribe(WorldObserver observer) {
        if(observers.contains(observer)) {
            observers.remove(observer);
        }
    }
    
    public void NotifyTick(int day, int deads,int infected,int PopTot) {
        for(WorldObserver ob: observers) {
            ob.OnSimulationTick(day, deads, infected,PopTot);
        }
    }
    
    public void NotifyLinksUpdated() {
        for(WorldObserver ob: observers) {
            ob.OnLinksUpdated();
        }
    }
    
    public void NotifyCountryCreated(CountryDTO contry) {
        for(WorldObserver ob: observers) {
            ob.OnCountryCreated(contry);
        }
    }
    
    public void NotifySimulationStarted() {
        for(WorldObserver ob: observers) {
            ob.OnSimulationStarted();
        }
    }
    
    public void NotifyProjectLoaded() {
        for(WorldObserver ob: observers) {
            ob.OnProjectLoaded();
        }
    }
    
    public void Draw(Graphics g) {
        worldDrawer.draw(g);
    }
    
    public void DrawCountryInfo(Graphics g, Point mousePosition, CountryDTO country) {
        worldDrawer.drawCountryInfos(g, mousePosition, country);
    }
    
    public void AddCountry(List<Point> points, String countryName, int countryPop) {
        Country country = CountryFactory.CreateCountry(points, countryName, countryPop);
        world.addCountry(country);
    }
    
    public void UpdateSelectionStateCountry(UUID id, boolean select) {
        world.UpdateSelectionStateCountry(id, select);
    }
    
    public void UpdateCountry(CountryDTO country) {
        world.updateCountry(country);
    }
    
    public void RemoveCountry(UUID countryId) {
        world.removeCountry(countryId);
    }
    
    public void UpdateSelectionStateRegion(UUID countryId, UUID regionId, boolean select) {
        world.UpdateSelectionStateRegion(countryId, regionId, select);
    }
    
    public void AddRegion(UUID countryId, String name, double popPercentage) { // Un region ID ?
        world.addRegion(countryId, name, popPercentage);
    }
    
    public void UpdateRegion(UUID countryId, RegionDTO region) {
        world.UpdateRegion(countryId, region);
    }
    
    public void RemoveRegion(UUID countryId, UUID regionId) {
        world.RemoveRegion(countryId, regionId);
    }
    
    public void AddLink(UUID firstCountryId, UUID secondCountryId, LinkType type) {
        if(firstCountryId != secondCountryId) {
            world.Addlink(firstCountryId, secondCountryId, type);
        } else {
            System.out.println("Impossible de link le meme pays!");
        }
    }
    
    public void UpdateSelectionStateLink(UUID linkId, boolean select) {
        world.UpdateSelectionStateLink(linkId, select);
    }
    
    public void RemoveLink(UUID linkId) {
        world.RemoveLink(linkId);
    }
    
    public void ActiveMesures() {
        
    }
    
    public void UpdateDiseaseFromDTO(double infectionRate, double mortalityRate, double cureRate){
        disease.setInfectionRate(infectionRate);
        disease.setMortalityRate(mortalityRate);
        disease.setCureRate(cureRate);
        System.out.println("mortalityRate: "+ disease.getMortalityRate() +", curedRate: "+ disease.getCureRate() + ", infectedtRate : " + disease.getInfectionRate());
    }
    
    public Disease getDisease(){
        return disease;
    }
    
    public DiseaseDTO GetDiseaseDTO(){
        return new DiseaseDTO(disease);
    }
    
    public void AddMesure(double adhesionRate, boolean active, String mesureName){
        //TODO: limits for adhasionRate
        if (adhesionRate >=0 && adhesionRate <= 100){
            HealthMesure mesure = new CustomMeasure(adhesionRate, active, mesureName);
        mesures.add(mesure);
        }
    }
    
    public void UpdateMesure(double adhesionRate, boolean active, UUID id){
        HealthMesure mesure = FindMesureByUUID(id);
        if (mesure != null){
            mesure.setAdhesionRate(adhesionRate);
            mesure.setActive(active);
        }
    }
    
    public void RemoveMesure(UUID id){
        HealthMesure mesure = FindMesureByUUID(id);
        if (mesure != null){
            mesures.remove(mesure);
        }
    }
    
    public boolean IsRunning() {
        return simulation.getIsRunning();
    }

    public void StartSimulation(int timeStep) {
        if(!simulation.getIsRunning()) {
            world.ValidateRegions();
            NotifySimulationStarted();
            simulation.Simulate(timeStep);
        }
    }
    
    public void pauseSimulation() {
        simulation.Pause();
    }
    
    public void resetSimulation() {
            simulation.Reset();
    }
    
    public void save(File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeInt(simulation.GetElapsedDay());
            out.writeObject(world);
            out.writeObject(disease);
            //out.writeObject(mesures);
            out.close();
            fileOut.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void load(File openedFile) {
        try {
            FileInputStream fileIn = new FileInputStream(openedFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            int elapsedDay = in.readInt();
            world = (World) in.readObject();
            disease = (Disease) in.readObject();
            //mesures = (ArrayList) in.readObject();
            world.SetWorldController(this);
            simulation = new Simulation(this, elapsedDay);
            NotifyProjectLoaded();
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }
    
    public void newProjet() {
        world.clearWorld();
        simulation.Reset();
        mesures.clear();
    }
    
    public void CreateJEPG() {
        
    }
    
    public void Undo() {
        
    }
    
    public void Redo() {
        
    }
    
    public void GetCountryInfo(UUID countryId) { //Maybe rename: DisplayCountryInfo ?
        world.getInfos(countryId);
    }
    
    public void zoom(double amount, Point mousePosition, int width, int height) {
        worldDrawer.Zoom(amount, mousePosition, width, height);
    }
}
