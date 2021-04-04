/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.UUID;

/**
 *
 * @author Abergel Clement
 */
public class RegionDTO {
    public double PercentagePop;
    public PopulationDTO SubPopulation;
    public final UUID Id;
    
    public RegionDTO(Region region) {
        PercentagePop = region.getPercentagePop();
        SubPopulation = new PopulationDTO(region.getPopulation());
        Id = region.GetId();
    }
}
