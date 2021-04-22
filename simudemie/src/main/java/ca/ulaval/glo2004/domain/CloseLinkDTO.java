/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author melanietremblay
 */
public class CloseLinkDTO implements Serializable {
    public UUID ConcernedLink;
    public double AdhesionRate;
    public double Threshold;
    
    public CloseLinkDTO(CloseLink closedLink) {
        ConcernedLink = closedLink.getConcernedLink();
        AdhesionRate = closedLink.getAdhesionRate();
        Threshold = closedLink.getThreshold();
    }
}
