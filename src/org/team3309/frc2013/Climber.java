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
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author vmagr_000
 */
public class Climber implements Runnable, PIDSource, PIDOutput {

    private static Climber instance = null;

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber(RobotMap.CLIMBER_TIPPER, RobotMap.DUMPER, RobotMap.CLIMBER_TOP_LIMIT, RobotMap.CLIMBER_BOTTOM_LIMIT);
        }
        return instance;
    }
    private Solenoid tipper = null;
    private Victor dumper = null;
    private Drive drive = null;
    private DigitalInput topLimit = null;
    private DigitalInput bottomLimit = null;
    private boolean topLimitHit = false;
    private boolean bottomLimitHit = false;
    private boolean inClimbMode = false;
    
    private Encoder encoder;
    
    private PIDController pid = null;
    
    private boolean lastSignPositive = false;

    private Climber(int tipper, int dumper, int topLimit, int bottomLimit) {
        drive = Drive.getInstance();
        this.tipper = new Solenoid(tipper);
        this.dumper = new Victor(dumper);
        this.topLimit = new DigitalInput(topLimit);
        this.bottomLimit = new DigitalInput(bottomLimit);
        this.encoder = drive.getPtoEncoder();
        this.encoder.reset();
        new Thread(this).start();
        
        pid = new PIDController(0.001,0,0, this, this);
        SmartDashboard.putData("climberPid", pid);
    }

    public void tip() {
        tipper.set(true);
    }

    public void retractTipper() {
        tipper.set(false);
    }

    public void setDumper(double x) {
        dumper.set(x);
    }

    public void enableClimbingMode() {
        inClimbMode = true;
        encoder.reset();
        encoder.start();
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
            
            if(!lastSignPositive){
                //countAtLastDirection = encoder.get();
            }
            lastSignPositive = true;
        }
        
        if(power < 0){
            if(bottomLimitHit){
                System.out.println("trying to go down, but the bottom limit switch is hit");
                drive.setPto(0);
            }
            else
                drive.setPto(power);
        }
        
        SmartDashboard.putNumber("climber count", encoder.get());
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

    public double pidGet() {
        return encoder.get();
    }

    public void pidWrite(double d) {
        runTraveller(d);
    }
}
