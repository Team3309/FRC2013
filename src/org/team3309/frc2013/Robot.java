/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.team3309.frc2013.commands.AutoShootCommand;

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
    
    private AutoShootCommand teleopAutoShoot = new AutoShootCommand(4);
    private AutoShootCommand autonAutoShoot = new AutoShootCommand(3);

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    public void robotInit() {
        mDrive = Drive.getInstance();

        mShooter = Shooter.getInstance();
        
        mClimber = Climber.getInstance();
        
        compressor = new Compressor(RobotMap.PRESSURE_SWITCH, RobotMap.COMPRESSOR_RELAY);
        compressor.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    
    public void teleopInit(){
        JoystickButton autoShootButton = new JoystickButton(operatorXbox, XboxController.BUTTON_X);
        autoShootButton.whenPressed(teleopAutoShoot);
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
        else if(driveXbox.getAButton()){
            System.out.println("Shifting to PTO");
            mDrive.engagePto();
        }
        else if(driveXbox.getBButton()){
            System.out.println("Shifting out of PTO");
            mDrive.disengagePto();
        }

        double throttle = Math.abs(driveXbox.getLeftY()) * driveXbox.getLeftY();
        double turn = Math.abs(driveXbox.getRightX()) * driveXbox.getRightX();
        mDrive.drive(throttle, turn);
        //mDrive.setRightRpm(-500);

        double target = -operatorXbox.getLeftY();
        if(Math.abs(target) < .1)
            target = 0;
        target *= Shooter.MAX_RPM;
        
        if(operatorXbox.getYButton())
            target = 4200;
        mShooter.setTargetRpm(target);
        if(target > 2000)
            compressor.stop();
        else
            compressor.start();
        
        //mShooter.setPercent(target);
        if (operatorXbox.getRightBumper()) {
            //System.out.println("extending loader");
            mShooter.extendLoader();
        }
        else if(operatorXbox.getLeftBumper()){
            //System.out.println("retracting loader");
            mShooter.retractLoader();
        }
        if (operatorXbox.getBButton()) {
            //System.out.println("tilting up");
            mShooter.tiltUp();
        } else if (operatorXbox.getAButton()) {
            //System.out.println("tilting down");
            mShooter.tiltDown();
        }
        
        if(driveXbox.getXButton())
            mClimber.tip();
        else if(driveXbox.getYButton())
            mClimber.retractTipper();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
