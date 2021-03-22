/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Abergel Clement
 */
public class CountryDTO {
    public List<Region> Regions;
    public Population Population;
    // public final UUID Id;
    
    public CountryDTO(Country country) {
    }
}
