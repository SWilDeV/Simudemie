/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.Link.LinkType;
import java.awt.Graphics2D;
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
    private List<WorldObserver> observers = new ArrayList<>(); //TODO: Discuter de ou mettre l'observer. Ici ou dans simulation ? 
    private Simulation simulation;
    private Disease disease = new Disease("ebola",0.04, 0.02, 0.9); //TODO Fix undo/redo
    private final WorldDrawer worldDrawer;
    
    
    public WorldController() throws CloneNotSupportedException {
        worldDrawer = new WorldDrawer(this);
        world = new World(this);
        simulation = new Simulation(this);
        
        AddUndoRedo();
    }
    
    
    public int GetElapsedDay() {
        return simulation.GetElapsedDay();
    }
    
    public int GetUndoRedoSize() { //TODO: Peut etre changer le nom ? Cela fait reference au nombre de UNDO REDO fait.
        return simulation.GetUndoRedoSize();
    }
    
    public int GetUndoRedoPosition() {
        return simulation.GetUndoRedoPosition();
    }
    
    public CountryDTO GetCountryDTO(UUID id) {
        Country c = world.FindCountryByUUID(id);
        if(c != null) {
            return new CountryDTO(c);
        }
        
        return null;
    }
    
    public Country GetCountry(UUID id) {
        return world.FindCountryByUUID(id);
    }
    
    public List<LinkDTO> GetLinks() {
        return (List<LinkDTO>) world.getLinks().stream().map(e -> new LinkDTO((Link) e)).collect(Collectors.toList());
    }
    
    public Link GetLink(UUID id) {
        return world.FindLinkByUUID(id);
    }
    
    public LinkDTO GetLinkDTO(UUID id) {
        Link l = world.FindLinkByUUID(id);
        if(l != null) {
            return new LinkDTO(l);
        }
        
        return null;
    }
    
    public List<CountryDTO> GetCountries() {
        return (List<CountryDTO>) world.getCountries().stream().map(e -> new CountryDTO((Country) e)).collect(Collectors.toList());
    }
    
    public List<Country> GetCountriesforSimulation() {
        return (List<Country>) world.getCountries();
    }
    
    public List<HealthMesureDTO> GetHealthMesures(UUID countryId) {
        List<Country> countries = world.getCountries();
        for(Country c: countries) {
            if(c.GetId().equals(countryId)) {
                return c.GetMesures().stream().map(m -> new HealthMesureDTO((HealthMesure)m)).collect(Collectors.toList());
            }
        }
        
        return null;
    }
    
    public List<CloseLinkDTO> getClosedLinks() {
        return (List<CloseLinkDTO>) world.getClosedLinks().stream().map(e -> new CloseLinkDTO((CloseLink) e)).collect(Collectors.toList());
    }
    
    public CountryDTO FindCountryByPosition(Point position) {
        Country country = world.findCountryByPosition(position);
        if(country != null) {
            return new CountryDTO(country);
        }
        return null;
    }
    
    public Simulation getSimulation(){
        return simulation;
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
    
    //Regions
    public void NotifyOnRegionCreated() {
        for(WorldObserver ob: observers) {
            ob.OnRegionCreated();
        }
    }
    
    public void NotifyOnRegionUpdated() {
        for(WorldObserver ob: observers) {
            ob.OnRegionUpdated();
        }
    }
    
    public void NotifyOnRegionDestroy() {
        for(WorldObserver ob: observers) {
            ob.OnRegionDestroy();
        }
    }
    //Measures
    public void NotifyOnMesureCreated() {
        for(WorldObserver ob: observers) {
            ob.OnMesuresCreated();
        }
    }
    
    public void NotifyOnMesureUpdated() {
        for(WorldObserver ob: observers) {
            ob.OnMesuresUpdated();
        }
    }
    
    public void NotifyOnMesureDestroy() {
        for(WorldObserver ob: observers) {
            ob.OnMesuresDestroy();
        }
    }
    
    //Links
    public void NotifyOnLinkCreated() {
        for(WorldObserver ob: observers) {
            ob.OnLinkCreated();
        }
    }
    
    public void NotifyLinksUpdated() {
        for(WorldObserver ob: observers) {
            ob.OnLinksUpdated();
        }
    }
    
    public void NotifyOnLinkDestroy() {
        for(WorldObserver ob: observers) {
            ob.OnLinkDestroyed();
        }
    }
    
    //Disease
    public void NotifyDiseaseCreated(DiseaseDTO disease) {
        for(WorldObserver ob: observers) {
            ob.OnDiseaseCreated(disease);
        }
    }
    
    public void NotifyOnDiseaseUpdated() {
        for(WorldObserver ob: observers) {
            ob.OnDiseaseUpdated();
        }
    }
    
    public void NotifyOnDiseaseDestroy() {
        for(WorldObserver ob: observers) {
            ob.OnDiseaseDestroyed();
        }
    }
    
    //Country
    public void NotifyCountryCreated(CountryDTO contry) {
        for(WorldObserver ob: observers) {
            ob.OnCountryCreated(contry);
        }
    }
    
    public void NotifyOnCountryUpdated() {
        for(WorldObserver ob: observers) {
            ob.OnCountryUpdated();
        }
    }
    
    public void NotifyOnCountryDestroy() {
        for(WorldObserver ob: observers) {
            ob.OnCountryDestroy();
        }
    }
    
    //Simulation
    public void NotifyOnSimulationUndoRedo() {
        for(WorldObserver ob: observers) {
            ob.OnSimulationUndoRedo();
        }
    }
    
    public void NotifySimulationStarted() {
        for(WorldObserver ob: observers) {
            ob.OnSimulationStarted();
        }
    }
    
    public void NotifyOnSimulationReset() {
        for(WorldObserver ob: observers) {
            ob.OnSimulationReset();
        }
    }
    
    public void NotifiyOnSimulationStopped() {
        for(WorldObserver ob: observers) {
            ob.OnSimulationStopped();
        }
    }
    
    public void NotifyProjectLoaded() {
        for(WorldObserver ob: observers) {
            ob.OnProjectLoaded();
        }
    }
    
    public void Draw(Graphics2D g2d, List<Point> points) {
        worldDrawer.draw(g2d, points);
    }
    
    public void DrawCountryInfo(Graphics2D g2d, Point mousePosition, CountryDTO country) {
        worldDrawer.drawCountryInfos(g2d, mousePosition, country);
    }
    
    public void AddCountry(List<Point> points, String countryName, int countryPop) {
        Country country = CountryFactory.CreateCountry(points, countryName, countryPop);
        world.addCountry(country);
    }
        
//    public void AddCountryIrregular(List<Point> points, String countryName, int countryPop) {
//        Country country = CountryFactory.CreateCountry(points, countryName, countryPop);
//        world.addCountryIrregular(country);
//    }
//        
    public void UpdateSelectionStateCountry(UUID id, boolean select) {
        world.UpdateSelectionStateCountry(id, select);
    }
    
    public void UpdateCountry(CountryDTO country) {
        world.updateCountry(country);
    }
    
    public void UpdateTotalPopulation(UUID countryId, int totalPopulation) {
        world.UpdateTotalPopulation(countryId, totalPopulation);
    }
    
    public void RemoveCountry(UUID countryId) {
        world.removeCountry(countryId);
    }
    
    public void UpdateSelectionStateRegion(UUID countryId, UUID regionId, boolean select) {
        world.UpdateSelectionStateRegion(countryId, regionId, select);
    }
    
    public void AddRegion(UUID countryId, List<Point> points, String name) {
        world.addRegion(countryId, points, name);
    }
    
    public void UpdateRegion(UUID countryId, RegionDTO region) {
        world.UpdateRegion(countryId, region);
    }
    
    public void RemoveRegion(UUID countryId, UUID regionId) {
        world.RemoveRegion(countryId, regionId);
    }
    
    public void AddLink(UUID firstCountryId, UUID secondCountryId, LinkType type, double transmissionRate) {
        if(firstCountryId != secondCountryId && transmissionRate >=0 && transmissionRate <= 100) {
            world.Addlink(firstCountryId, secondCountryId, type, transmissionRate/100);
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
    
    public void setAllLinksTransmissionRate(double borderTransmissionRate, double waterTransmissionRate, 
                                            double airTransmissionRate, double regionTransmissionRate) {
        
        if (borderTransmissionRate >=0 && borderTransmissionRate <= 1 && 
            waterTransmissionRate >=0 && waterTransmissionRate <= 1 &&
            airTransmissionRate >=0 && airTransmissionRate <= 1 && 
            regionTransmissionRate >=0 && regionTransmissionRate <= 1) 
        {
            world.setLinksTransmissionRate(borderTransmissionRate, waterTransmissionRate,airTransmissionRate);
            //world.setRegionLinkTransmissionRate(regionTransmissionRate);
        }
        
    }
    
    public void ActiveMesures() {
        
    }
    
//    public void UpdateDisease(String name,double infectionRate, double mortalityRate, double cureRate){
//        //simulation.updateDisease(disease);
//        disease.setInfectionRate(infectionRate);
//        disease.setMortalityRate(mortalityRate);
//        disease.setCureRate(cureRate);
//        System.out.println("mortalityRate: "+ disease.getMortalityRate() +", curedRate: "+ disease.getCureRate() + ", infectedtRate : " + disease.getInfectionRate());
//    }
//    
    public Disease getDisease(){
        return disease;
    }
//    
//    public DiseaseDTO GetDiseaseDTO(){
//        return new DiseaseDTO(disease);
//    }
    public List<Disease> getDiseaseList(){
        return simulation.getDiseaseList();
    }
    
    public void UpdateDisease(UUID id, String diseaseName, double infectionRate,double mortalityRate,double cureRate){
        simulation.UpdateDisease(id, diseaseName, infectionRate, mortalityRate, cureRate);
    }
    
    public void createDisease(String diseaseName,double infectionRate,double mortalityRate,double cureRate){
        simulation.createDisease(diseaseName,infectionRate, mortalityRate, cureRate);
    }
    
    public void setCurrentDiseaseIndex(int index){
        simulation.setCurrentDiseaseIndex(index);
        simulation.setcurrentDisease(index);
    }
    
    public void AddMesure(UUID countryId, double adhesionRate, boolean active, String mesureName, double threshold,
                          double effectTransmissionRate, double effectReproductionRate) 
    {
        double reproductionRate = disease.getInfectionRate()/disease.getCureRate();
        
        if (adhesionRate >= 0 && adhesionRate <= 1 && threshold >= 0 && threshold <=1 && 
            effectTransmissionRate >=0 && effectTransmissionRate <=1 && effectReproductionRate >= 0 && effectReproductionRate < reproductionRate) 
        {
            world.AddMesure(countryId, adhesionRate, active, mesureName, threshold, effectTransmissionRate, effectReproductionRate);
        }
    }
    
    public void RemoveMesure(UUID countryId, UUID mesureId) {
        world.RemoveMesure(countryId, mesureId);
    }
    
    public void addCloseLink(UUID linkId, double adhesionRate, double threshold) {
        if (adhesionRate >= 0 && adhesionRate <= 1 && threshold >= 0 && threshold <=1) {
            world.addCloseLink(linkId, adhesionRate, threshold);
        }
    }
    
    public void removeCloseLink(UUID linkId) {
        world.removeCloseLink(linkId);
    }
    
    //public void updateCloseLink(UUID linkId) {
        
        //if (world.FindLinkByUUID(linkId).isOpen()) {
            //world.FindLinkByUUID(linkId).closeLink();
        //} else {
            //world.FindLinkByUUID(linkId).openLink();
       // }
        //CloseLink closedLink = new CloseLink(0.95, world.FindLinkByUUID(linkId));
    //}
    
    public boolean IsRunning() {
        return simulation.getIsRunning();
    }

    public void StartSimulation(int timeStep) throws NotAllPopulationAssign {
        if(!simulation.getIsRunning()) {
            world.ValidateRegions();
            simulation.Simulate(timeStep);
            NotifySimulationStarted();
        }
    }
    
    public void pauseSimulation() {
        simulation.Pause();
        NotifiyOnSimulationStopped();
    }
    
    public void resetSimulation() {
        simulation.Reset();
        NotifyOnSimulationReset();
    }
    
    public void save(File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeInt(simulation.GetElapsedDay());
            out.writeObject(world);
            out.writeObject(disease);
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
    
    public void newProjet() throws CloneNotSupportedException {        
        world.clearWorld();
        simulation.Reset();
    }
    
    public void CreateJEPG() {
        
    }
    
    public void AddUndoRedo() throws CloneNotSupportedException {
        simulation.AddUndoRedoWorld(world, disease);
    }
    
    public void Undo() {
        simulation.Pause();
        UndoRedo ur = simulation.Undo();
        if(ur != null) ApplyUndoRedo(ur);
    }
    
    public void UndoRedoAt(int position) {
        simulation.Pause();    
        UndoRedo ur = simulation.SpecificRedo(position);
        if(ur != null) ApplyUndoRedo(ur);
    }
    
    public void Redo() {
        simulation.Pause();
        UndoRedo ur = simulation.Redo();
        if(ur != null) ApplyUndoRedo(ur);
    }
    
    private void ApplyUndoRedo(UndoRedo ur) {
        world.LoadWorld(ur.World);
        disease = ur.Disease;
        simulation.SetElapsedDay(ur.ElapsedDay);
        NotifyOnSimulationUndoRedo();
    }
}
