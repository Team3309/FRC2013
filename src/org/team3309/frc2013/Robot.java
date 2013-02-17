/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    private Drive mDrive;
    private Shooter mShooter;
    private XboxController driveXbox = new XboxController(1);
    private XboxController operatorXbox = new XboxController(2);
    //private Compressor compressor = new Compressor(RobotMap.PRESSURE_SWITCH, RobotMap.COMPRESSOR_RELAY);

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        mDrive = new Drive.Builder()
                .left1(RobotMap.DRIVE_LEFT_1).left2(RobotMap.DRIVE_LEFT_2)
                .right1(RobotMap.DRIVE_RIGHT_1).right2(RobotMap.DRIVE_RIGHT_2)
                .driveShifter(RobotMap.DRIVE_SHIFTER_LEFT_FORWARD, RobotMap.DRIVE_SHIFTER_LEFT_REVERSE)
                .ptoShifter(RobotMap.DRIVE_SHIFTER_PTO_FORWARD, RobotMap.DRIVE_SHIFTER_PTO_REVERSE)
                .build();

        mShooter = new Shooter(RobotMap.SHOOTER_MOTOR, RobotMap.SHOOTER_PISTON_FORWARD, RobotMap.SHOOTER_PISTON_REVERSE, 0);
        //compressor.start();
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
            mDrive.lowGear();
        } else if (driveXbox.getRightBumper()) {
            mDrive.highGear();
        }

        double throttle = Math.abs(driveXbox.getLeftY())*driveXbox.getLeftY();
        double turn = Math.abs(driveXbox.getRightX())*driveXbox.getRightX();
        //System.out.println("throttle: "+throttle+"\t turn:"+turn);
        mDrive.drive(throttle, turn);

        mShooter.setPercent(operatorXbox.getLeftY());
        if (operatorXbox.getRightBumper()) {
            mShooter.shoot();
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
