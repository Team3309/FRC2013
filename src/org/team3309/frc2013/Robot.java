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
import edu.wpi.first.wpilibj.command.Scheduler;

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
    private Scheduler scheduler;

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

    }

    public void disabledInit() {
    }
    private int frisbeesShot = 0;
    private double autonStartTime = 0;

    public void autonomousInit() {
        autonStartTime = Timer.getFPGATimestamp();
        mClimber.unlock();
        mShooter.tiltDown();
        mClimber.retractTipper();
        mClimber.disableClimbingMode();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        mDrive.stop();
        mDrive.disengagePto();
        mShooter.setTargetRpm(Shooter.PYRAMID_TARGET_RPM);
        while (!mShooter.isTargetSpeed()) {
            Timer.delay(.1);
            if (!this.isAutonomous()) {
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
        
        if(frisbeesShot > 3){
            mDrive.lowGear();
            mDrive.drive(.5, 0);
            Timer.delay(2);
            mDrive.stop();
        }
    }

    public void teleopInit() {
        mClimber.unlock();
        mShooter.tiltDown();
        mClimber.retractTipper();
        mClimber.disableClimbingMode();
        mDrive.highGear();
    }
    
    boolean climbingMode = false;

    boolean lastOpLeftBumper = false;
    
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
        } else if (driveXbox.getBack()) {
            climbingMode = false;
            System.out.println("exiting climbing mode");
            mDrive.disengagePto();
            mClimber.disableClimbingMode();
        }
        
        if (!climbingMode) {
            if(driveXbox.getRightBumper()){
                mDrive.lowGear();
                compressor.stop();
            }
            else{
                mDrive.highGear();
                compressor.start();
            }

            double throttle = driveXbox.getLeftY();
            double turn = driveXbox.getRightX()*.25;
            
            if(driveXbox.getLeftBumper()){ //invert controls so that lining up for a climb is easier
                throttle = -throttle;
            }
            
            mDrive.drive(throttle, turn);
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
            target = Shooter.PYRAMID_TARGET_RPM;
        }
        if (operatorXbox.getStart()) {
            target = 8000;
        }
        if (operatorXbox.getRightBumper()) {
            target = Shooter.PYRAMID_TARGET_RPM;
            if (mShooter.isTargetSpeed()) {
                mShooter.shoot();
                Timer.delay(.25);
            }
        }
        if(operatorXbox.getLeftBumper())
            mShooter.shoot();
        
        mShooter.setTargetRpm(target);
        if (target >= 2000) {
            compressor.stop();
        } else {
            compressor.start();
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
