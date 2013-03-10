/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author vmagr_000
 */
public class Climber {
    
    private static Climber instance = null;
    public static Climber getInstance(){
        if(instance == null){
            instance = new Climber(RobotMap.CLIMBER_TIPPER_PISTON_FORWARD, RobotMap.CLIMBER_TIPPER_PISTON_REVERSE, RobotMap.DUMPER);
        }
        return instance;
    }
    
    private DoubleSolenoid tipper = null;
    private Victor dumper = null;
    private Drive mDrive = null;
    
    private Climber(int tipperForward, int tipperReverse, int dumper){
        mDrive = Drive.getInstance();
        tipper = new DoubleSolenoid(2, tipperForward, tipperReverse);
        this.dumper = new Victor(dumper);
    }
    
    public void tip(){
        tipper.set(DoubleSolenoid.Value.kForward);
    }
    
    public void retractTipper(){
        tipper.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void setDumper(double x){
        dumper.set(x);
    }
    
}
