/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2013.Drive;
import org.team3309.frc2013.Shooter;

/**
 *
 * @author vmagr_000
 */
public class TrollAuton extends Command {

    private Drive mDrive = Drive.getInstance();
    
    private boolean finished = false;

    public TrollAuton() {
    }

    protected void initialize() {
    }

    protected void execute() {
        mDrive.highGear();
        mDrive.driveStraight(-3309);
        Shooter.getInstance().setTargetRpm(0);
    }

    protected boolean isFinished() {
        return finished;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
