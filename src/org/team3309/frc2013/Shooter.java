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
    private DoubleSolenoid piston = null;
    private Counter cntr = null;

    public Shooter(int motorChannel, int pistonForward, int pistonBackward, int encoder) {
        motor = new Victor(motorChannel);
        piston = new DoubleSolenoid(pistonForward, pistonBackward);
    }

    public void shoot() {
        piston.set(DoubleSolenoid.Value.kForward);
        Timer.delay(.5);
        piston.set(DoubleSolenoid.Value.kReverse);
    }

    public void setPercent(double perc) {
        motor.set(perc);
    }
}
