/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author friarbots
 */
public class Shooter {

    private Victor motor = null;
    private DoubleSolenoid loaderPiston = null;
    private DoubleSolenoid tilterPiston = null;
    private Counter cntr = null;

    public Shooter(int motorChannel, int loaderForward, int loaderBackward, int tilterForward, int tilterReverse, int encoder) {
        motor = new Victor(motorChannel);
        loaderPiston = new DoubleSolenoid(loaderForward, loaderBackward);
        tilterPiston = new DoubleSolenoid(2, tilterForward, tilterReverse);
        //cntr = new Counter(encoder);
        //cntr.setSemiPeriodMode(true);
    }

    public void extendLoader() {
        loaderPiston.set(DoubleSolenoid.Value.kForward);
    }
    
    public void retractLoader(){
        loaderPiston.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void tiltUp(){
        tilterPiston.set(DoubleSolenoid.Value.kForward);
    }
    
    public void tiltDown(){
        tilterPiston.set(DoubleSolenoid.Value.kReverse);
    }

    public void setPercent(double perc) {
        motor.set(perc);
    }
}
