/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.Link.LinkType;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author Abergel Clement
 */
public class WorldController {
    
    private final World world = new World();
    private final Simulation simulation = new Simulation();
    private final WorldDrawer worldDrawer;
    
    public List<CountryDTO> GetCountries() {
        return (List<CountryDTO>) world.getCountries().stream().map(e -> new CountryDTO((Country) e)).collect(Collectors.toList());
    }
    
    public List<LinkDTO> GetLinks() {
        return (List<LinkDTO>) world.getLinks().stream().map(e -> new LinkDTO((Link) e)).collect(Collectors.toList());
    }
    
    public CountryDTO FindCountryByPosition(Point position) {
        Country country = world.findCountryByPosition(position);
        if(country != null) {
            return new CountryDTO(country);
        }
        
        return null;
    }
    
    public WorldController() {
        worldDrawer = new WorldDrawer(this);
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
    
    public void UpdateCountry() {
        
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
    
    public void RemoveLink() {
    }
    
    public void AddHealthMesure(HealthMesure mesure) {
        
    }
    
    public void ActiveMesures() {
        
    }
    
    public void StartSimulation() {
        if(!simulation.IsRunning()) {
            simulation.Simulate(); 
        }
    }
    
    public void StopSimlation() {
        if(simulation.IsRunning()) {
            simulation.PausePlay();
        }
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
