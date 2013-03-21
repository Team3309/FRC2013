/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.team3309.frc2013.commands.AutoShootCommand;
import org.team3309.frc2013.commands.ExtendTraveller;
import org.team3309.frc2013.commands.FakeCommand;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot {

    private Drive mDrive = null;
    private Climber mClimber = null;
    private Shooter mShooter = null;
    private XboxController driveXbox = new XboxController(1);
    private XboxController operatorXbox = new XboxController(2);
    private Compressor compressor = null;
    private AutoShootCommand teleopAutoShoot = new AutoShootCommand(4, Shooter.PYRAMID_TARGET_RPM);
    private AutoShootCommand autonAutoShoot = new AutoShootCommand(3, Shooter.PYRAMID_TARGET_RPM);
    private Scheduler scheduler;
    private JoystickButton extendButton;
    private ExtendTraveller extendCommand;

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    public void robotInit() {
        scheduler = Scheduler.getInstance();

        mDrive = Drive.getInstance();

        mShooter = Shooter.getInstance();

        mClimber = Climber.getInstance();

        compressor = new Compressor(RobotMap.PRESSURE_SWITCH, RobotMap.COMPRESSOR_RELAY);
        compressor.start();

        extendButton = new JoystickButton(driveXbox, XboxController.BUTTON_A);
        extendCommand = new ExtendTraveller();
    }

    public void disabledInit() {
    }
    private int frisbeesShot = 0;
    private double autonStartTime = 0;

    public void autonomousInit() {
        autonStartTime = Timer.getFPGATimestamp();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        mDrive.drive(0, 0);
        mShooter.setTargetRpm(Shooter.PYRAMID_TARGET_RPM);
        while (!mShooter.isTargetSpeed()) {
            Timer.delay(.1);
            if (!this.isAutonomous()) {
                return;
            }
        }
        if (mShooter.isTargetSpeed()) {
            mShooter.shoot();
            Timer.delay(2);
            frisbeesShot++;
        } else if (Timer.getFPGATimestamp() - autonStartTime > 7 && frisbeesShot == 0) {
            mShooter.shoot();
            Timer.delay(2);
        }
    }

    public void teleopInit() {
        JoystickButton autoShootButton = new JoystickButton(operatorXbox, XboxController.BUTTON_X);
        //autoShootButton.whenPressed(teleopAutoShoot);
    }
    boolean climbingMode = false;

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        scheduler.run();

        if (driveXbox.getStart()) {
            climbingMode = true;
            System.out.println("entering climbing mode");
            mDrive.engagePto();
            mClimber.enableClimbingMode();
            extendButton.whenPressed(extendCommand);
        } else if (driveXbox.getBack()) {
            climbingMode = false;
            System.out.println("exiting climbing mode");
            mDrive.disengagePto();
            mClimber.disableClimbingMode();
            extendCommand.cancel();
            extendButton.whenPressed(new FakeCommand());
        }

        if (!climbingMode) {
            if (driveXbox.getLeftBumper()) {
                mDrive.lowGear();
            } else if (driveXbox.getRightBumper()) {
                mDrive.highGear();
            }

            double throttle = Math.abs(driveXbox.getLeftY()) * driveXbox.getLeftY();
            double turn = Math.abs(driveXbox.getRightX()) * driveXbox.getRightX();
            mDrive.drive(throttle, turn);

            if (driveXbox.getAButton()) {
                mDrive.engagePto();
            } else if (driveXbox.getBButton()) {
                mDrive.disengagePto();
            }
        }

        if (climbingMode) {
            mClimber.runTraveller(-driveXbox.getLeftY());
            if(driveXbox.getLeftBumper())
                mClimber.unlock();
            else if(driveXbox.getRightBumper())
                mClimber.lock();
        }

        double opLeftStick = -operatorXbox.getLeftY();
        if (Math.abs(opLeftStick) < .1) {
            opLeftStick = 0;
        }
        opLeftStick *= 1000;
        
        double target = 0;

        if (operatorXbox.getYButton()) {
            target = 4000;
        }
        if (operatorXbox.getStart()) {
            target = 8000;
        }
        if (operatorXbox.getXButton()) {
            target = Shooter.PYRAMID_TARGET_RPM;
            if (mShooter.isTargetSpeed()) {
                mShooter.shoot();
                Timer.delay(.5);
            }
        }
        target += opLeftStick;
        
        mShooter.setTargetRpm(target);
        if (target > 2000) {
            compressor.stop();
        } else {
            compressor.start();
        }

        if (operatorXbox.getRightBumper()) {
            mShooter.extendLoader();
        } else if (operatorXbox.getLeftBumper()) {
            mShooter.retractLoader();
        }
        if (operatorXbox.getBButton()) {
            mShooter.tiltUp();
        } else if (operatorXbox.getAButton()) {
            mShooter.tiltDown();
        }

        if (driveXbox.getXButton()) {
            mClimber.tip();
        } else if (driveXbox.getYButton()) {
            mClimber.retractTipper();
        }

        mClimber.setDumper(operatorXbox.getRightY());
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
