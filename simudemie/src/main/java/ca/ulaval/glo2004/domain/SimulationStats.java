/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Abergel Clement
 */
public class SimulationStats {

    private static final String captionDeathChart = "Morts";
    private static final String captionInfectedChart = "Infectés";
    private static final String captionUninfectedChart = "Sains";
    
    public SimulationStats() {
    }
    
    private static XYSeriesCollection BakeSeries(XYSeries death, XYSeries infected, XYSeries unInfected) {
        return new XYSeriesCollection() {
            {
                addSeries(death);
                addSeries(infected);
                addSeries(unInfected);
            }
        };
    }
    
    public static XYSeriesCollection GetWorldStats(List<UndoRedo> undoRedos, int to) {
        XYSeries deathNum = new XYSeries(captionDeathChart);
        XYSeries infectedNum = new XYSeries(captionInfectedChart);
        XYSeries uninfectedNum = new XYSeries(captionUninfectedChart);
        
        for(int i = 0; i < to; i++) {
            UndoRedo ur = undoRedos.get(i);

            int day = ur.ElapsedDay;
            List<Country> cs = ur.World.getCountries();
            int deads = 0;
            int infected = 0;
            int PopTot = 0;
            for(Country c: cs) {
                deads += c.getPopulation().getDeadPopulation();
                infected += c.getPopulation().getInfectedPopulation();
                PopTot += c.getPopulation().getTotalPopulation();
            }

            int nonInfected = PopTot - deads - infected;
            if (nonInfected < 0) nonInfected = 0;

            deathNum.add(day, deads);
            infectedNum.add(day, infected);
            uninfectedNum.add(day, nonInfected);
        }
        
        return BakeSeries(deathNum, infectedNum, uninfectedNum);
    }
    
    public static XYSeriesCollection GetCountryStats(UUID id, List<UndoRedo> undoRedos, int to) {
        XYSeries deathNum = new XYSeries(captionDeathChart);
        XYSeries infectedNum = new XYSeries(captionInfectedChart);
        XYSeries uninfectedNum = new XYSeries(captionUninfectedChart);
        
        for(int i = 0; i < to; i++) {
            UndoRedo ur = undoRedos.get(i);

            int elapsedDay = ur.ElapsedDay;

            Country country = ur.World.FindCountryByUUID(id);
            if(country != null) {
                Population population = country.getPopulation();
                int dead = population.getDeadPopulation();
                int infected = population.getInfectedPopulation();

                int uninfected = population.getTotalPopulation() - dead - infected;
                if (uninfected < 0) uninfected = 0;

                deathNum.add(elapsedDay, dead);
                infectedNum.add(elapsedDay, infected);
                uninfectedNum.add(elapsedDay, uninfected);
            }    
        }
        
        return BakeSeries(deathNum, infectedNum, uninfectedNum);
    }
    
    public static XYSeriesCollection GetRegionStats(UUID countryId, UUID regionId, List<UndoRedo> undoRedos, int to) {
        XYSeries deathNum = new XYSeries(captionDeathChart);
        XYSeries infectedNum = new XYSeries(captionInfectedChart);
        XYSeries uninfectedNum = new XYSeries(captionUninfectedChart);
        
        for(int i = 0; i < to; i++) {
            UndoRedo ur = undoRedos.get(i);
            Country country = ur.World.FindCountryByUUID(countryId);
            Region region = country.FindRegionByUUID(regionId);

            int day = ur.ElapsedDay;
            int dead = 0;
            int infected = 0;

            Population population = region.getPopulation();
            dead = population.getDeadPopulation();
            infected = population.getInfectedPopulation();

            int uninfected = population.getTotalPopulation() - dead - infected;
            if (uninfected < 0) uninfected = 0;

            deathNum.add(day, dead);
            infectedNum.add(day, infected);
            uninfectedNum.add(day, uninfected);
        }
        
        return BakeSeries(deathNum, infectedNum, uninfectedNum);
    }
}
