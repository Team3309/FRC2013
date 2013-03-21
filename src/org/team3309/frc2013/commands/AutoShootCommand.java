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
    
    private int targetRpm;
    private int frisbees;
    
    public AutoShootCommand(int frisbees, int targetRpm){
        this.frisbees = frisbees;
        this.targetRpm = targetRpm;
    }
    
    protected void initialize() {
        mShooter = Shooter.getInstance();
    }

    protected void execute() {
        System.out.println("autoshoot command running");
        mShooter.setTargetRpm(targetRpm);
        int attempts = 0;
        int frisbeesLeft = frisbees;
        while(frisbeesLeft > 0 && attempts < 7){
            while(!mShooter.isTargetSpeed())
                Timer.delay(.1);
            System.out.println("autoshoot still running");
            System.out.println("frisbees left - "+frisbeesLeft);
            System.out.println("attempts left - "+attempts);
            if(mShooter.isTargetSpeed()){
                mShooter.shoot();
                Timer.delay(.1);
            }
            frisbeesLeft--;
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
