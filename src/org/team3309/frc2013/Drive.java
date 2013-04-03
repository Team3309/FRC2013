/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author friarbots
 */
public class Drive implements Runnable {

    private static Drive instance;

    public static Drive getInstance() {
        if (instance == null) {
            instance = new Drive.Builder()
                    .left1(RobotMap.DRIVE_LEFT_1).left2(RobotMap.DRIVE_LEFT_2)
                    .right1(RobotMap.DRIVE_RIGHT_1).right2(RobotMap.DRIVE_RIGHT_2)
                    .driveShifter(RobotMap.DRIVE_SHIFTER_FORWARD, RobotMap.DRIVE_SHIFTER_REVERSE)
                    .ptoShifter(RobotMap.DRIVE_SHIFTER_ENGAGE_PTO)
                    .leftEncoder(RobotMap.DRIVE_ENCODER_LEFT_A, RobotMap.DRIVE_ENCODER_LEFT_B)
                    .rightEncoder(RobotMap.DRIVE_ENCODER_RIGHT_A, RobotMap.DRIVE_ENCODER_RIGHT_B)
                    .gyro(RobotMap.DRIVE_GYRO)
                    .build();
        }
        return instance;
    }

    public void run() {
        while (true) {
            SmartDashboard.putNumber("Left encoder", leftEncoder.get());
            SmartDashboard.putNumber("Right encoder", rightEncoder.get());
            SmartDashboard.putNumber("Gyro", gyro.getAngularRateOfChange());
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private boolean straightPidEnabled = false;

    private class StraightPID implements PIDSource, PIDOutput {

        public double pidGet() {
            return (leftEncoder.get() + rightEncoder.get()) / 2;
        }

        public void pidWrite(double d) {
            if(straightPidEnabled){
                drive(d, 0);
            }
        }
    }
    private Victor left1 = null;
    private Victor left2 = null;
    private Victor right1 = null;
    private Victor right2 = null;
    private DoubleSolenoid driveShifter = null;
    private Solenoid ptoShifter = null;
    private Encoder leftEncoder = null;
    private Encoder rightEncoder = null;
    
    private PIDController straightPid = null;
    
    
    private boolean gyroEnabled = true;
    private static final double KP_LOW_GEAR = .001;
    private static final double KP_STOPPED = .01;
    private static final double KP_LOW_SPEED = .03;
    private static final double KP_DEFAULT = .02;
    private double gyroKp = KP_DEFAULT;
    
    private boolean isLowGear = false;
    
    private SuperGyro gyro;
    private static final double MAX_ANGULAR_RATE_OF_CHANGE = 720; //max turning speed commandable by the joystick - in deg/s
    private double skimGain = .25;

    double skim(double v) {
        // gain determines how much to skim off the top
        if (v > 1.0) {
            return -((v - 1.0) * skimGain);
        } else if (v < -1.0) {
            return -((v + 1.0) * skimGain);
        }
        return 0;
    }

    public void drive(double throttle, double turn) {
        throttle = -throttle; //flip throttle so that a positive value will make the robot drive forward

        if (gyroEnabled) {
            if (Math.abs(throttle) < .1 && Math.abs(turn) < .1) //do nothing when there is no driving commands - do this to prevent the waggle - James
            {
                gyroKp = KP_STOPPED;
            } else if (Math.abs(throttle) < .5) {
                gyroKp = KP_LOW_SPEED;
            } if(isLowGear){
                gyroKp = KP_LOW_GEAR;
            } else {
                gyroKp = KP_DEFAULT;
            }

            double omega = gyro.getAngularRateOfChange();
            double desiredOmega = turn * MAX_ANGULAR_RATE_OF_CHANGE;

            turn = (omega - desiredOmega) * gyroKp;
        }
        else
            turn = -turn;

        double t_left = throttle + turn;
        double t_right = throttle - turn;

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        setLeft(-left);
        setRight(right);
    }

    public void highGear() {
        driveShifter.set(DoubleSolenoid.Value.kReverse);
        gyroKp = KP_DEFAULT;
        isLowGear = false;
    }

    public void lowGear() {
        driveShifter.set(DoubleSolenoid.Value.kForward);
        gyroKp = KP_LOW_GEAR;
        isLowGear = true;
    }

    public void engagePto() {
        lowGear();
        driveShifter.set(DoubleSolenoid.Value.kOff);
        Timer.delay(.1);
        ptoShifter.set(true);
    }

    public void disengagePto() {
        ptoShifter.set(false);
    }

    public void stop() {
        drive(0, 0);
    }

    private void setLeft(double val) {
        left1.set(val);
        left2.set(val);
    }

    private void setRight(double val) {
        right1.set(val);
        right2.set(val);
    }

    public void setPto(double val) {
        setLeft(val);
    }

    public void resetGyro() {
        gyro.reset();
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void disablePid() {
        straightPid.disable();
        straightPidEnabled = false;
    }

    public void enablePid() {
        straightPid.enable();
        straightPidEnabled = true;
    }

    public void driveStraight(int counts) {
        straightPid.setSetpoint(counts);
    }
    
    public void enableGyro(){
        gyroEnabled = true;
        System.out.println("gyro enabled");
    }
    
    public void disableGyro(){
        gyroEnabled = false;
        System.out.println("gyro disabled");
    }

    private void onBuild() {
        leftEncoder.start();
        rightEncoder.start();

        StraightPID straight = new StraightPID();
        straightPid = new PIDController(0.001, 0, 0.02, straight, straight);

        SmartDashboard.putData("Straight PID", straightPid);

        straightPid.enable();

        new Thread(this).start();
    }

    private static class Builder {

        private Drive drive;

        public Builder() {
            drive = new Drive();
        }

        public Builder left1(int num) {
            drive.left1 = new Victor(num);
            return this;
        }

        public Builder left2(int num) {
            drive.left2 = new Victor(num);
            return this;
        }

        public Builder right1(int num) {
            drive.right1 = new Victor(num);
            return this;
        }

        public Builder right2(int num) {
            drive.right2 = new Victor(num);
            return this;
        }

        public Builder driveShifter(int forward, int backwards) {
            drive.driveShifter = new DoubleSolenoid(forward, backwards);
            return this;
        }

        public Builder ptoShifter(int port) {
            drive.ptoShifter = new Solenoid(port);
            return this;
        }

        public Builder leftEncoder(int a, int b) {
            drive.leftEncoder = new Encoder(a, b, true, CounterBase.EncodingType.k1X);
            return this;
        }

        public Builder rightEncoder(int a, int b) {
            drive.rightEncoder = new Encoder(a, b, false, CounterBase.EncodingType.k1X);
            return this;
        }

        public Builder gyro(int port) {
            drive.gyro = new SuperGyro(port);
            return this;
        }

        public Drive build() {
            drive.onBuild();
            return drive;
        }
    }
}
