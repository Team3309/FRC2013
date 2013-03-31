/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2013.Shooter;

/**
 *
 * @author vmagr_000
 */
public class ExtendRetractLoader extends Command{
    
    private boolean finished = false;
    private boolean shouldExtend = false;

    protected void initialize() {
        finished = false;
        shouldExtend = !shouldExtend;
    }

    protected void execute() {
        if(shouldExtend)
            Shooter.getInstance().extendLoader();
        else
            Shooter.getInstance().retractLoader();
        finished = true;
    }

    protected boolean isFinished(){
        return finished;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
