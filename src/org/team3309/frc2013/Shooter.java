/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author friarbots
 */
public class Shooter implements Runnable {
    
    public static final int PYRAMID_TARGET_RPM = 3500;

    private Victor motor = null;
    private Solenoid loaderPiston = null;
    private Solenoid tilterPiston = null;
    private Counter cntr = null;
    private double speed = 0;
    
    private double lastSpeed = 0;
    private double targetRpm = 0;
    
    private static Shooter instance;
    
    public static Shooter getInstance(){
        if(instance == null){
            instance = new Shooter(RobotMap.SHOOTER_MOTOR,
                RobotMap.SHOOTER_LOADER,
                RobotMap.SHOOTER_TILTER,
                RobotMap.SHOOTER_PHOTOSENSOR_CHANNEL);
        }
        return instance;
    }
    
    private Shooter(int motorChannel, int loader, int tilter, int encoder) {
        motor = new Victor(motorChannel);
        loaderPiston = new Solenoid(loader);
        tilterPiston = new Solenoid(tilter);
        cntr = new Counter(encoder);
        //cntr.setSemiPeriodMode(true);
        cntr.start();
        new Thread(this).start();
    }

    public void extendLoader() {
        loaderPiston.set(true);
    }
    
    public void retractLoader(){
        loaderPiston.set(false);
    }
    
    public void tiltUp(){
        tilterPiston.set(false);
    }
    
    public void tiltDown(){
        tilterPiston.set(true);
    }

    public void setPercent(double perc) {
        motor.set(perc);
    }
    
    public void setTargetRpm(double rpm){
        //pid.setSetpoint(rpm / MAX_RPM);
        targetRpm = rpm;
    }
    
    public double getRpm(){
        return speed;
    }
    
    /**
     * Is the shooter at its target speed?
     * @return 
     */
    public boolean isTargetSpeed(){
        return Math.abs(speed - targetRpm) < 100;
    }
    
    public void shoot(){
        extendLoader();
        Timer.delay(.5);
        retractLoader();
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
                
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
