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

/**
 *
 * @author Abergel Clement
 */
public class WorldController {
    
    private World world = new World();
    private Simulation simulation = new Simulation();
    private WorldDrawer worldDrawer;
    
    public List<Country> GetCountries() {
        return world.getCountries();
    }
    
    public List<Link> GetLinks() {
        return world.getLinks();
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
    
    public void AddCountry(Country country) {
        world.addCountry(country);
    }
    
    public void RemoveCountry(UUID countryId) {
        world.removeCountry(countryId);
    }
    
    public void AddRegion(UUID countryId, Region region) {
        world.addRegion(countryId, region);
    }
    
    public void AddLink(UUID firstCountryId, UUID secondCountryId, LinkType type) {
        if(firstCountryId != secondCountryId) {
            world.addlink(firstCountryId, secondCountryId, type);
        } else {
            System.out.println("Impossible de link le meme pays!");
        }
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
