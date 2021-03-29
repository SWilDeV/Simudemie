/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.Link.LinkType;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author Abergel Clement
 */
public class WorldController {
    
    private final World world = new World();
    private final Simulation simulation;
    private final Disease disease = new Disease(0.0, 0.0, 0.0);
    private final WorldDrawer worldDrawer;
    private final List<HealthMesure> mesures = new ArrayList<>();
    
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
        simulation = new Simulation(this);
    }
    
    public void Draw(Graphics g) {
        worldDrawer.draw(g);
    }
    
    public void DrawCountryInfo(Graphics g, Point mousePosition, Country country) {
        worldDrawer.drawCountryInfos(g, mousePosition, country);
    }
    
    public void AddCountry(List<Point> points) {
        Country country = CountryFactory.CreateCountry(points);
        world.addCountry(country);
    }
    
    public void UpdateCountry(CountryDTO country) {
        world.updateCountry(country);
    }
    
    public void RemoveCountry(UUID countryId) {
        world.removeCountry(countryId);
    }
    
    public void AddRegion(UUID countryId, Region region) { // Un region ID ?
        world.addRegion(countryId, region);
    }
    
    public void UpdateRegion() {
        
    }
    
    public void RemoveRegion(UUID countryId) {
        
    }
    
    public void AddLink(UUID firstCountryId, UUID secondCountryId, LinkType type) {
        if(firstCountryId != secondCountryId) {
            world.Addlink(firstCountryId, secondCountryId, type);
        } else {
            System.out.println("Impossible de link le meme pays!");
        }
    }
    
    public void RemoveLink(UUID linkId) {
        world.RemoveLink(linkId);
    }
    
    
    public void ActiveMesures() {
        
    }
    
    
    public void UpdateDisease(double cureRate, double mortalityRate, double reproductionRate){
        disease.setReproductionRate(reproductionRate);
        disease.setMortalityRate(mortalityRate);
        disease.setCureRate(cureRate);
        System.out.println("Update le curerate : " + cureRate);
    }
    
    public void AddMesure(double adhesionRate, boolean active, String mesureName){
        //TODO: limits for adhasionRate 
        HealthMesure mesure = new CustomMeasure(adhesionRate, active, mesureName);
        mesures.add(mesure);
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

    public void StartSimulation() {
        if(!simulation.getIsRunning()) {
            simulation.Simulate(); 
        }
    }
    
    public void pauseSimulation() {
            simulation.Pause();
    }
    
    public void resetSimulation() {
            simulation.Reset();
    }
    
    public void Save() {
    
    }
    
    public void Load() {
        
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
    
}
