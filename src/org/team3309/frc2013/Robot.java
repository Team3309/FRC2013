/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot {

    private Drive mDrive;
    private Shooter mShooter;
    private XboxController driveXbox = new XboxController(1);
    private Joystick operatorXbox = new Joystick(2);
    private Compressor compressor = new Compressor(RobotMap.PRESSURE_SWITCH, RobotMap.COMPRESSOR_RELAY);

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    public void robotInit() {
        mDrive = new Drive.Builder()
                .left1(RobotMap.DRIVE_LEFT_1).left2(RobotMap.DRIVE_LEFT_2)
                .right1(RobotMap.DRIVE_RIGHT_1).right2(RobotMap.DRIVE_RIGHT_2)
                .driveShifter(RobotMap.DRIVE_SHIFTER_FORWARD, RobotMap.DRIVE_SHIFTER_REVERSE)
                .ptoShifter(RobotMap.DRIVE_SHIFTER_PTO_FORWARD, RobotMap.DRIVE_SHIFTER_PTO_REVERSE)
                .thirdPosShifter(RobotMap.DRIVE_SHIFTER_THIRD_POS)
                .leftEncoder(RobotMap.DRIVE_ENCODER_LEFT_A)
                .rightEncoder(RobotMap.DRIVE_ENCODER_RIGHT_A)
                .build();

        mShooter = new Shooter(RobotMap.SHOOTER_MOTOR, RobotMap.SHOOTER_LOADER_FORWARD, RobotMap.SHOOTER_LOADER_REVERSE, RobotMap.SHOOTER_TILTER_FORWARD, RobotMap.SHOOTER_TILTER_REVERSE, 0);
        compressor.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        if (driveXbox.getLeftBumper()) {
            System.out.println("Shifting to low gear");
            mDrive.lowGear();
        } else if (driveXbox.getRightBumper()) {
            System.out.println("Shifting to high gear");
            mDrive.highGear();
        }
        else if(driveXbox.getA()){
            System.out.println("Shifting to PTO");
            mDrive.engagePto();
        }
        else if(driveXbox.getB()){
            System.out.println("Shifting out of PTO");
            mDrive.disengagePto();
        }

        double throttle = Math.abs(driveXbox.getLeftY()) * driveXbox.getLeftY();
        double turn = Math.abs(driveXbox.getRightX()) * driveXbox.getRightX();
        //System.out.println("throttle: "+throttle+"\t turn:"+turn);
        mDrive.drive(throttle, turn);
        //mDrive.setRightRpm(-500);

        mShooter.setPercent(-operatorXbox.getY());
        if (operatorXbox.getTrigger()) {
            //System.out.println("extending loader");
            mShooter.extendLoader();
        }
        else if(!operatorXbox.getTrigger()){
            //System.out.println("retracting loader");
            mShooter.retractLoader();
        }
        /*else if(operatorXbox.getRightBumper()){
            System.out.println("retracting loader");
            mShooter.retractLoader();
        }*/
        if (operatorXbox.getRawButton(6)) {
            //System.out.println("tilting up");
            mShooter.tiltUp();
        } else if (operatorXbox.getRawButton(7)) {
            //System.out.println("tilting down");
            mShooter.tiltDown();
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
