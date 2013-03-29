/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
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
    
    public static Drive getInstance(){
        if(instance == null){
            instance = new Drive.Builder()
                .left1(RobotMap.DRIVE_LEFT_1).left2(RobotMap.DRIVE_LEFT_2)
                .right1(RobotMap.DRIVE_RIGHT_1).right2(RobotMap.DRIVE_RIGHT_2)
                .driveShifter(RobotMap.DRIVE_SHIFTER_FORWARD, RobotMap.DRIVE_SHIFTER_REVERSE)
                .ptoShifter(RobotMap.DRIVE_SHIFTER_ENGAGE_PTO)
                .leftEncoder(RobotMap.DRIVE_ENCODER_LEFT_A, RobotMap.DRIVE_ENCODER_LEFT_B)
                .rightEncoder(RobotMap.DRIVE_ENCODER_RIGHT_A)
                .build();
        }
        return instance;
    }

    private static final int LOOP_TIME = 20;
    private static final int ENCODER_COUNTS = 360; //360 count
    private static final double FILTER_STRENGTH = .5; //must be between 0 and 1
    private static final double kP = .75, kI = .1, kD = 1;
    private double leftSpeed = 0;
    private double rightSpeed = 0;
    private double lastRightSpeed = 0;
    private double lastLeftSpeed = 0;
    private PIDController leftPid = null;
    private PIDController rightPid = null;
    private boolean leftNeg = false;
    private boolean rightNeg = false;
    
    private class LeftPID implements PIDSource, PIDOutput {

        public double pidGet() {
            return leftSpeed / MAX_RPM;
        }

        public void pidWrite(double d) {
            if (Math.abs(d) < .05) {
                setLeft(0);
            }
            if (leftNeg) {
                setLeft(-d);
            } else {
                setLeft(d);
            }
            SmartDashboard.putNumber("left output", d);
        }
    }

    private class RightPID implements PIDSource, PIDOutput {

        public double pidGet() {
            return rightSpeed / MAX_RPM;
        }

        public void pidWrite(double d) {
            if (Math.abs(d) < .05) {
                setRight(0);
            }
            if (rightNeg) {
                setRight(-d);
            } else {
                setRight(d);
            }
            SmartDashboard.putNumber("right output", d);
        }
    }

    public void run() {
        while (true) {
            try {
                int leftCount = leftEncoder.get();
                int rightCount = rightEncoder.get();
                leftEncoder.reset();
                rightEncoder.reset();
                leftSpeed = (60.0 / ENCODER_COUNTS) * leftCount / (LOOP_TIME) * 1000;
                rightSpeed = (60.0 / ENCODER_COUNTS) * rightCount / (LOOP_TIME) * 1000;

                //filtering code
                //http://www.chiefdelphi.com/forums/showpost.php?p=1132943&postcount=15
                leftSpeed = FILTER_STRENGTH * lastLeftSpeed + (1 - FILTER_STRENGTH) * leftSpeed;
                rightSpeed = FILTER_STRENGTH * lastRightSpeed + (1 - FILTER_STRENGTH) * rightSpeed;

                SmartDashboard.putNumber("left rpm", leftSpeed);
                SmartDashboard.putNumber("right rpm", rightSpeed);

                lastLeftSpeed = leftSpeed;
                lastRightSpeed = rightSpeed;
                
                Thread.sleep(LOOP_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static final int MODE_PERC = 0;
    public static final int MODE_RPM = 1;
    public static final int MODE_POS = 2;
    public static final int MAX_RPM = 1000;
    
    private Victor left1 = null;
    private Victor left2 = null;
    private Victor right1 = null;
    private Victor right2 = null;
    private DoubleSolenoid driveShifter = null;
    private Solenoid ptoShifter = null;
    private Encoder leftEncoder = null;
    private Counter rightEncoder = null;
    
    private double skimGain = .5;
    
    double skim(double v) {
        // gain determines how much to skim off the top
        if (v > 1.0) {
            return -((v - 1.0) * skimGain);
        } else if (v < -1.0) {
            return -((v + 1.0) * skimGain);
        }
        return 0;
    }

    public void setLeftRpm(double rpm) {
        this.leftPid.setSetpoint(Math.abs(rpm / MAX_RPM));
        if (rpm < 0) {
            this.leftNeg = true;
        } else {
            this.leftNeg = false;
        }
    }

    public void setRightRpm(double rpm) {
        this.rightPid.setSetpoint(Math.abs(rpm / MAX_RPM));
        if (rpm < 0) {
            this.leftNeg = true;
        } else {
            this.leftNeg = false;
        }
    }

    public void drive(double throttle, double turn) {
        turn = -turn;

        double t_left = throttle + turn;
        double t_right = throttle - turn;

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        setLeft(-left);
        setRight(right);
        
        //System.out.println("pto counts:"+leftEncoder.get());
    }

    public void highGear() {
        driveShifter.set(DoubleSolenoid.Value.kReverse);
    }

    public void lowGear() {
        driveShifter.set(DoubleSolenoid.Value.kForward);
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
    
    public void stop(){
        drive(0,0);
    }

    private void setLeft(double val) {
        left1.set(val);
        left2.set(val);
    }

    private void setRight(double val) {
        right1.set(val);
        right2.set(val);
    }
    
    public void setPto(double val){
        setLeft(val);
    }
    
    public Encoder getPtoEncoder(){
        return leftEncoder;
    }
    
    private void onBuild() {
        this.leftEncoder.start();
        this.rightEncoder.start();
        //new Thread(this).start();
        LeftPID left = new LeftPID();
        RightPID right = new RightPID();
        this.leftPid = new PIDController(kP, kI, kD, left, left);
        this.rightPid = new PIDController(kP, kI, kD, right, right);
        //this.leftPid.enable();
        //this.rightPid.enable();
        SmartDashboard.putData("left pid", this.leftPid);
        SmartDashboard.putData("right pid", this.rightPid);
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
            drive.leftEncoder = new Encoder(a,b);
            drive.leftEncoder.setReverseDirection(true);
            return this;
        }

        public Builder rightEncoder(int a) {
            drive.rightEncoder = new Counter(a);
            drive.rightEncoder.setReverseDirection(true);
            return this;
        }
        
        public Drive build() {
            drive.onBuild();
            return drive;
        }
    }
}
