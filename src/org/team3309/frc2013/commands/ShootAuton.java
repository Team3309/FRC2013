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
    private Drive mDrive = null;

    public ShootAuton(Robot r) {
        this.robot = r;
        mDrive = Drive.getInstance();
    }

    protected void initialize() {
        mShooter.tiltUp();
        mDrive.stop();
        
        mShooter.setTargetRpm(Shooter.PYRAMID_TARGET_RPM);
        Timer.delay(3);
    }

    protected void execute() {
        mShooter.setTargetRpm(Shooter.PYRAMID_TARGET_RPM);
        while (!mShooter.isTargetSpeed()) {
            if (!robot.isAutonomous()) {
                return;
            }
        }
        if (mShooter.isTargetSpeed()) {
            mShooter.shoot();
            Timer.delay(2);
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
