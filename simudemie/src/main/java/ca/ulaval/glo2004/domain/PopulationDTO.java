/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

/**
 *
 * @author Sean
 */
public class PopulationDTO {
    public int totalPopulation;
    public int infectedPopulation;
    public int curedPopulation;
    public int deadPopulation;
    public int nonInfectedPopulation;
    
    public PopulationDTO(Population population) {
        totalPopulation = population.getTotalPopulation();
        infectedPopulation = population.getInfectedPopulation();
        curedPopulation = population.getCuredPopulation();
        deadPopulation = population.getDeadPopulation();
        nonInfectedPopulation = population.getNonInfectedPopulation();
    }
}
