/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2013.Climber;

/**
 *
 * @author vmagr_000
 */
public class ExtendTraveller extends Command {
    
    private Climber mClimber = null;

    protected void initialize() {
        mClimber = Climber.getInstance();
    }

    protected void execute() {
        if(!mClimber.getTopLimit()){
            mClimber.runTraveller(.5);
            System.out.println("running extendtraveller command");
        }
    }

    protected boolean isFinished() {
        return mClimber.getTopLimit();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
