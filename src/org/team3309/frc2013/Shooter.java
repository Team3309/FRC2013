/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author friarbots
 */
public class Shooter implements Runnable {

    private Victor motor = null;
    private DoubleSolenoid loaderPiston = null;
    private DoubleSolenoid tilterPiston = null;
    private Counter cntr = null;
    private double speed = 0;
    
    private double lastSpeed = 0;
    private double targetRpm = 0;
    
    public static final double MAX_RPM = 4000;
    
    private static Shooter instance;
    
    public static Shooter getInstance(){
        if(instance == null){
            instance = new Shooter(RobotMap.SHOOTER_MOTOR,
                RobotMap.SHOOTER_LOADER_FORWARD, RobotMap.SHOOTER_LOADER_REVERSE,
                RobotMap.SHOOTER_TILTER_FORWARD, RobotMap.SHOOTER_TILTER_REVERSE,
                RobotMap.SHOOTER_PHOTOSENSOR_CHANNEL);
        }
        return instance;
    }
    
    private Shooter(int motorChannel, int loaderForward, int loaderBackward, int tilterForward, int tilterReverse, int encoder) {
        motor = new Victor(motorChannel);
        loaderPiston = new DoubleSolenoid(loaderForward, loaderBackward);
        tilterPiston = new DoubleSolenoid(2, tilterForward, tilterReverse);
        cntr = new Counter(encoder);
        //cntr.setSemiPeriodMode(true);
        cntr.start();
        new Thread(this).start();
    }

    public void extendLoader() {
        loaderPiston.set(DoubleSolenoid.Value.kForward);
    }
    
    public void retractLoader(){
        loaderPiston.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void tiltUp(){
        tilterPiston.set(DoubleSolenoid.Value.kForward);
    }
    
    public void tiltDown(){
        tilterPiston.set(DoubleSolenoid.Value.kReverse);
    }

    public void setPercent(double perc) {
        motor.set(perc);
        SmartDashboard.putNumber("shooter perc", perc);
    }
    
    public void setTargetRpm(double rpm){
        //pid.setSetpoint(rpm / MAX_RPM);
        targetRpm = rpm;
        SmartDashboard.putNumber("shooter target", rpm);
    }
    
    public double getRpm(){
        return speed;
    }
    
    /**
     * Is the shooter at its target speed?
     * @return 
     */
    public boolean isTargetSpeed(){
        return Math.abs(speed - targetRpm) < 15;
    }
    
    public void shoot(){
        extendLoader();
        Timer.delay(.1);
        retractLoader();
        Timer.delay(.25);
        extendLoader();
    }
    
    public void unjam(){
        tiltDown();
        Timer.delay(.1);
        tiltUp();
    }
    
    public double getTargetRpm(){
        return targetRpm;
    }
    
    int infinityCounts = 0;
    
    public void run(){
        while(true){
            try {
                double period = cntr.getPeriod();
                speed = 60 / period;
                // do this so that we use the last speed if the loop is running too fast and didn't get a period
                if(period == Double.POSITIVE_INFINITY && infinityCounts <= 10){
                    speed = lastSpeed;
                    infinityCounts++;
                }
                else if(period == Double.POSITIVE_INFINITY && infinityCounts > 10){
                    speed = 0;
                    infinityCounts++;
                }
                else{
                    infinityCounts = 0;
                }
                
                lastSpeed = speed;
                
                if(speed < targetRpm)
                    motor.set(1);
                else if(speed > targetRpm)
                    motor.set(0);
                else if(targetRpm == 0)
                    motor.set(0);
                
                SmartDashboard.putNumber("shooter rpm", speed);
                SmartDashboard.putNumber("shooter period", period);
                SmartDashboard.putNumber("shooter counts", cntr.get());
                
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
