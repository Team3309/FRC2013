/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2013.Shooter;

/**
 *
 * @author vmagr_000
 */
public class AutoShootCommand extends Command{
    
    private boolean isFinished = false;
    
    private Shooter mShooter = null;
    
    private static final int targetRpm = 4200;
    private int frisbees;
    
    public AutoShootCommand(int frisbees){
        this.frisbees = frisbees;
    }
    
    protected void initialize() {
        mShooter = Shooter.getInstance();
    }

    protected void execute() {
        mShooter.setTargetRpm(targetRpm);
        int attempts = 0;
        int frisbeesLeft = frisbees;
        double lastRpm = mShooter.getTargetRpm();
        while(frisbeesLeft > 0 && attempts < 5){
            if(mShooter.isTargetSpeed())
                mShooter.shoot();
            double rpm = mShooter.getRpm();
            if(lastRpm - rpm > 100) //this means that it shot and didn't jam
                frisbeesLeft--;
            else{ //we jammed
                mShooter.unjam();
                attempts++;
            }
        }
        isFinished = true;
    }

    protected boolean isFinished() {
        return isFinished;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
