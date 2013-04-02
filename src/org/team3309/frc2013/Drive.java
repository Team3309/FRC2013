/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
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
                    .rightEncoder(RobotMap.DRIVE_ENCODER_RIGHT_A)
                    .gyro(RobotMap.DRIVE_GYRO)
                    .build();
        }
        return instance;
    }
    
    public void run() {
        /*lastTime = Timer.getFPGATimestamp();
        lastTheta = getAngle();
        while (true) {
            double theta = getAngle();
            angularRate = (theta - lastTheta)/(Timer.getFPGATimestamp() - lastTime);
            lastTime = Timer.getFPGATimestamp();
            lastTheta = theta;
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }*/
        while(true){
            SmartDashboard.putNumber("gyro voltage", gyro.getVoltage());
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //private PIDController pid = new PIDController(.03, 0,0,null, null);

    private Victor left1 = null;
    private Victor left2 = null;
    private Victor right1 = null;
    private Victor right2 = null;
    private DoubleSolenoid driveShifter = null;
    private Solenoid ptoShifter = null;
    private Encoder leftEncoder = null;
    private Counter rightEncoder = null;
    
    
    private AnalogChannel gyro = null;
    private double gyroKp = 0.02;
    private double gyroVoltageOffset = 2.5;
    private static final double MAX_ANGULAR_RATE_OF_CHANGE = 720; //max turning speed commandable by the joystick
    
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

    /*public void driveStraight(double throttle) {
        System.out.println("attempting to drive straight");
        double turn = getAngle() * gyroKp;
        SmartDashboard.putNumber("gyro compensation", turn);

        SmartDashboard.putNumber("Gyro", getAngle());

        double t_left = throttle + turn;
        double t_right = throttle - turn;

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        setLeft(-left);
        setRight(right);
    }*/
    
    public void drive(double throttle, double turn) {
        if(Math.abs(throttle) < .1 && Math.abs(turn) < .1){ //do nothing when there is no driving commands - do this to prevent the waggle - James
            setLeft(0);
            setRight(0);
            return;
        }
        
        double omega = getAngularRateOfChange();
        double desiredOmega = turn*MAX_ANGULAR_RATE_OF_CHANGE;
        
        SmartDashboard.putNumber("desired omega", desiredOmega);
        
        turn = (omega - desiredOmega) * gyroKp;
        SmartDashboard.putNumber("turn output", turn);
        
        SmartDashboard.putNumber("Gyro", getAngularRateOfChange());

        double t_left = throttle + turn;
        double t_right = throttle - turn;

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        SmartDashboard.putNumber("left", -left);
        SmartDashboard.putNumber("right", right);
        
        setLeft(-left);
        setRight(right);
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

    public Encoder getPtoEncoder() {
        return leftEncoder;
    }

    public double getAngularRateOfChange(){
        double rate = (gyro.getVoltage() - gyroVoltageOffset) / .007; //7mV per degree per sec
        return rate;
        //return Math.abs(rate) < 2 ? 0 : rate; //if the gyro rate is less than 2 degrees per second, return 0
    }
    
    public void resetGyro(){
        gyroVoltageOffset = gyro.getVoltage();
    }
    
    private void onBuild() {
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
            drive.leftEncoder = new Encoder(a, b);
            drive.leftEncoder.setReverseDirection(true);
            return this;
        }

        public Builder rightEncoder(int a) {
            drive.rightEncoder = new Counter(a);
            drive.rightEncoder.setReverseDirection(true);
            return this;
        }

        public Builder gyro(int port) {
            drive.gyro = new AnalogChannel(port);
            return this;
        }

        public Drive build() {
            drive.onBuild();
            return drive;
        }
    }
}
