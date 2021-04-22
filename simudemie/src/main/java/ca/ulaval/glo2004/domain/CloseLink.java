/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
//import Link from Link.java;

import java.util.UUID;



/**
 *
 * @author melanietremblay
 */
public class CloseLink extends HealthMesure {
    
    private UUID concernedLink;
 
    public CloseLink(double p_adhesionRate, UUID linkId, double p_threshold) {
        super(p_adhesionRate, true, "bidon", p_threshold, 0.0, 0.0);
        concernedLink = linkId;
        
    }
    
    public UUID getConcernedLink() {
        return concernedLink;
    }
}    
