/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;
//import Link from Link.java;


/**
 *
 * @author melanietremblay
 */
public class CloseLink extends HealthMesure {
    
    private Link concernedLink;
    
    public CloseLink(double p_adhesionRate, Link p_concernedLink, double p_threshold) {
        super(p_adhesionRate, true, "bidon", p_threshold);
        concernedLink = p_concernedLink;
    }
    
    public Link getConcernedLink() {
        return concernedLink;
    }
    
    public void setConcernedLink(Link newLink) {
        concernedLink = newLink;
    }
}
