/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
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
    private double autonStartTime = 0;

    public ShootAuton(Robot r) {
        this.robot = r;
    }

    protected void initialize() {
        autonStartTime = Timer.getFPGATimestamp();
    }

    protected void execute() {
        mShooter.setTargetRpm(Shooter.PYRAMID_TARGET_RPM);
        while (!mShooter.isTargetSpeed()) {
            Timer.delay(.1);
            if (!robot.isAutonomous()) {
                return;
            }
        }
        if (mShooter.isTargetSpeed() && frisbeesShot <= 3) {
            mShooter.extendLoader();
            Timer.delay(2);
            mShooter.retractLoader();
            Timer.delay(.5);
            frisbeesShot++;
        } else if (Timer.getFPGATimestamp() - autonStartTime > 7 && frisbeesShot == 0) {
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
