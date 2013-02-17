/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author friarbots
 */
public class Drive {

    public static class Builder {

        private Drive drive;

        public Builder() {
            drive = new Drive();
        }

        public Builder left1(int num) {
            drive.left1 = new Victor(num);
            return this;
        }

        public Builder left2(int num) {
            drive.left2 = new Victor(num);
            return this;
        }

        public Builder right1(int num) {
            drive.right1 = new Victor(num);
            return this;
        }

        public Builder right2(int num) {
            drive.right2 = new Victor(num);
            return this;
        }

        public Builder driveShifter(int forward, int backwards) {
            drive.driveShifter = new DoubleSolenoid(forward, backwards);
            return this;
        }

        public Builder ptoShifter(int forward, int reverse) {
            drive.ptoShifter = new DoubleSolenoid(forward, reverse);
            return this;
        }

        public Drive build() {
            return drive;
        }
    }
    public static final int MODE_PERC = 0;
    public static final int MODE_RPM = 1;
    public static final int MODE_POS = 2;
    
    private int mode = 0;
    
    private Victor left1 = null;
    private Victor left2 = null;
    private Victor right1 = null;
    private Victor right2 = null;
    
    private DoubleSolenoid driveShifter = null;
    private DoubleSolenoid ptoShifter = null;
    
    private double gain = 1;

    double skim(double v) {
        // gain determines how much to skim off the top
        if (v > 1.0) {
            return -((v - 1.0) * gain);
        } else if (v < -1.0) {
            return -((v + 1.0) * gain);
        }
        return 0;
    }

    public void drive(double throttle, double turn) {
        double t_left = throttle + turn;
        double t_right = throttle - turn;

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        left = -left;

        left1.set(left);
        left2.set(left);
        right1.set(right);
        right2.set(right);
    }

    public void changeMode(int newmode) {
        this.mode = newmode;
    }

    public void highGear() {
        driveShifter.set(DoubleSolenoid.Value.kForward);
    }

    public void lowGear() {
        driveShifter.set(DoubleSolenoid.Value.kReverse);
    }

    public void engagePto() {
        ptoShifter.set(DoubleSolenoid.Value.kForward);
    }

    public void disengagePto() {
        ptoShifter.set(DoubleSolenoid.Value.kReverse);
    }
}
