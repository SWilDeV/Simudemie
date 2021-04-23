/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain;

/**
 *
 * @author Abergel Clement
 */
public class UndoRedo {
    public final World World;
    public final Disease Disease;
    public final int ElapsedDay;
    
    public UndoRedo(World world, Disease disease, int elapsedDay) {
        World = world;
        Disease = disease;
        ElapsedDay = elapsedDay;
    }
    
    public UndoRedo(UndoRedo ur) throws CloneNotSupportedException {
        World = ur.World.clone();
        Disease = ur.Disease.clone();
        ElapsedDay = ur.ElapsedDay;
    }
}
