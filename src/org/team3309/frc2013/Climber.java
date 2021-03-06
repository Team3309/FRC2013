/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author vmagr_000
 */
public class Climber implements Runnable {

    private static Climber instance = null;

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber(RobotMap.CLIMBER_TIPPER_FORWARD, RobotMap.CLIMBER_TIPPER_REVERSE, RobotMap.DUMPER, RobotMap.CLIMBER_TOP_LIMIT, RobotMap.CLIMBER_BOTTOM_LIMIT, RobotMap.CLIMBER_LOCK);
        }
        return instance;
    }
    
    private DoubleSolenoid tipper = null;
    private Solenoid lock = null;
    private Victor dumper = null;
    private Drive drive = null;
    private DigitalInput topLimit = null;
    private DigitalInput bottomLimit = null;
    private boolean topLimitHit = false;
    private boolean bottomLimitHit = false;
    private boolean inClimbMode = false;
    
    private Climber(int tipperForward, int tipperReverse, int dumper, int topLimit, int bottomLimit, int lock) {
        drive = Drive.getInstance();
        this.tipper = new DoubleSolenoid(tipperForward, tipperReverse);
        this.dumper = new Victor(dumper);
        this.topLimit = new DigitalInput(topLimit);
        this.bottomLimit = new DigitalInput(bottomLimit);
        this.lock = new Solenoid(lock);
        
        new Thread(this).start();
    }

    public void tip() {
        tipper.set(DoubleSolenoid.Value.kForward);
    }

    public void retractTipper() {
        tipper.set(DoubleSolenoid.Value.kReverse);
    }

    public void setDumper(double x) {
        dumper.set(x);
    }

    public void enableClimbingMode() {
        inClimbMode = true;
        unlock();
    }

    public void disableClimbingMode() {
        inClimbMode = false;
    }
    
    public void runTraveller(double perc) {
        double power = 1 * perc;
        
        if(power > 0){
            if(topLimitHit){
                System.out.println("trying to go up, but the top limit switch is hit");
                drive.setPto(0);
            }
            else
                drive.setPto(power);
            
        }
        
        if(power < 0){
            if(bottomLimitHit){
                System.out.println("trying to go down, but the bottom limit switch is hit");
                drive.setPto(0);
            }
            else
                drive.setPto(power);
        }
        
    }
    
    public void lock(){
        lock.set(false);
    }
    
    public void unlock(){
        lock.set(true);
    }
    
    public boolean getTopLimit(){
        return topLimitHit;
    }
    
    public boolean getBottomLimit(){
        return bottomLimitHit;
    }

    public void run() {
        while (true) {
            if (inClimbMode) {
                if (!topLimit.get()) {
                    topLimitHit = true;
                } else if (topLimit.get()) {
                    topLimitHit = false;
                }

                if (!bottomLimit.get()) {
                    bottomLimitHit = true;
                } else if (bottomLimit.get()) {
                    bottomLimitHit = false;
                }

            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
