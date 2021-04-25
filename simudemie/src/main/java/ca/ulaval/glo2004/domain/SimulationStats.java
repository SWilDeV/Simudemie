/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author melanietremblay
 */
public class SimulationStats {
    
    
    private ArrayList<UndoRedo> undoRedoHistory = new ArrayList<>();
    
    public SimulationStats(Simulation simulation) {
        undoRedoHistory = simulation.GetUndoRedoHistory();
    }
    
    public XYSeriesCollection getWorldStats() {
        XYSeries deathNum = new XYSeries("Morts");
        XYSeries infectedNum = new XYSeries("Infectés");
        XYSeries uninfectedNum = new XYSeries("Sains");
        
        for (UndoRedo undoRedo : undoRedoHistory) {
            World world = undoRedo.getUndoRedoWorld();
            Population worldPop = world.getWorldPopulation();
            int elapsedDay = undoRedo.getElapsedDay();
            int dead = worldPop.getDeadPopulation();
            int infected = worldPop.getInfectedPopulation();
            int uninfected = worldPop.getTotalPopulation() - dead - infected;
            System.out.println(elapsedDay + " " + worldPop.getTotalPopulation());
            
            deathNum.add(elapsedDay, dead);
            infectedNum.add(elapsedDay, infected);
            uninfectedNum.add(elapsedDay, uninfected);
        }
        deathNum.remove(0);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(deathNum);
        dataset.addSeries(infectedNum);
        dataset.addSeries(uninfectedNum);

        return dataset;
    }
    
    public XYSeriesCollection getCountryStats(UUID id) {
        XYSeries deathNum = new XYSeries("Morts");
        XYSeries infectedNum = new XYSeries("Infectés");
        XYSeries uninfectedNum = new XYSeries("Sains");
        
        for (UndoRedo undoRedo : undoRedoHistory) {
            World world = undoRedo.getUndoRedoWorld();
            int elapsedDay = undoRedo.getElapsedDay();
            int dead;
            int infected;
            int uninfected;
            
            List<Country> countryList = world.getCountries();
            for (Country country : countryList) {
                if (country.GetId() == id) {
                    Population population = country.getPopulation();
                    dead = population.getDeadPopulation();
                    infected = population.getInfectedPopulation();
                    uninfected = population.getTotalPopulation() - dead - infected;
                    
                    deathNum.add(elapsedDay, dead);
                    infectedNum.add(elapsedDay, infected);
                    uninfectedNum.add(elapsedDay, uninfected);
                }
            }  
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(deathNum);
        dataset.addSeries(infectedNum);
        dataset.addSeries(uninfectedNum);

        return dataset;
    }
}
