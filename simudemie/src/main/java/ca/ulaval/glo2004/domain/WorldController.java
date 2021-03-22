/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.awt.Graphics;
import java.util.List;

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
    
    public WorldController() {
        worldDrawer = new WorldDrawer(this);
    }
    
    public void Draw(Graphics g) {
        worldDrawer.draw(g);
    }
    
    public void AddCountry(Country country) {
        world.addCountry(country);
    }
    
    public void AddRegion(Country country, Region region) {
        world.addRegion(country, region);
    }
    
    public void AddLink(Link link) {
        world.addlink(link);
    }
    
    public void AddHealthMesure(HealthMesure mesure) {
        
    }
    
    public void ActiveMesures() {
        
    }
    
    public void StartSimulation() {
        if(!simulation.IsRunning()) {
            simulation.Simulate(); 
        } else {
            simulation.PausePlay();
        }
    }
    
    public void StopSimlation() {
        
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
    
    public void DisplayInfo() { //Maybe rename: DisplayCountryInfo ?
        
    }
}
