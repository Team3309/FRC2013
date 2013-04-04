/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc2013.Drive;
import org.team3309.frc2013.Robot;
import org.team3309.frc2013.Shooter;

/**
 *
 * @author vmagr_000
 */
public class ShootAuton extends Command {

    private Robot robot = null;
    private Shooter mShooter = Shooter.getInstance();
    private int frisbeesShot = 0;
    private Drive mDrive = null;

    public ShootAuton(Robot r) {
        this.robot = r;
        mDrive = Drive.getInstance();
    }

    protected void initialize() {
        frisbeesShot = 0;
    }

    protected void execute() {
        if(frisbeesShot <= 3)
            mShooter.setTargetRpm(Shooter.PYRAMID_TARGET_RPM);
        while (frisbeesShot <= 3 && !mShooter.isTargetSpeed()) {
            Timer.delay(.1);
            if (!robot.isAutonomous()) {
                return;
            }
        }
        if (mShooter.isTargetSpeed() && frisbeesShot <= 3) {
            mDrive.stop();
            mShooter.extendLoader();
            Timer.delay(1);
            mShooter.retractLoader();
            Timer.delay(.5);
            frisbeesShot++;
        }
        
        System.out.println("frisbees shot: "+frisbeesShot);
        
        if(frisbeesShot > 3){
            mDrive.enablePid();
            mDrive.driveStraight(-3000);
            mShooter.setTargetRpm(0);
        }
    }

    protected boolean isFinished() {
        return robot.isOperatorControl();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
