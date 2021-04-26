/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abergel Clement
 */
public class UndoRedo {
    public enum UndoRedoType { UNDO, REDO };
    
    public final World World;
    public final List<Disease> Diseases;
    public final int ElapsedDay;
    public final int CurrentDiseaseIndex;
    
    
    public UndoRedo(World world, List<Disease> diseases, int currentDiseaseIndex, int elapsedDay) {
        World = world;
        
        Diseases = new ArrayList<>(diseases.size());
        diseases.forEach(d -> { try {
            Diseases.add((Disease) d.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        ElapsedDay = elapsedDay;
        CurrentDiseaseIndex = currentDiseaseIndex;
    }
    
    public World getUndoRedoWorld() {
        return World;
    }
    
    public int getElapsedDay() {
        return ElapsedDay;
    }
    
    public UndoRedo(UndoRedo ur) throws CloneNotSupportedException {
        World = ur.World.clone();
        Diseases = new ArrayList<>(ur.Diseases.size());
        ur.Diseases.forEach(d -> { try {
            Diseases.add((Disease) d.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        ElapsedDay = ur.ElapsedDay;
        CurrentDiseaseIndex = ur.CurrentDiseaseIndex;
    }
}
