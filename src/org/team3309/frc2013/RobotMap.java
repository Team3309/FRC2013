/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

/**
 *
 * @author friarbots
 */
public class RobotMap {
    
    private static final boolean isCompBot = false;
    
    static{
        System.out.println("Competition robot status: "+isCompBot);
        if(isCompBot){
            COMPRESSOR_RELAY = CompetitionRobotMap.COMPRESSOR_RELAY;
            PRESSURE_SWITCH = CompetitionRobotMap.PRESSURE_SWITCH;
            
            DRIVE_LEFT_1 = CompetitionRobotMap.DRIVE_LEFT_1;
            DRIVE_LEFT_2 = CompetitionRobotMap.DRIVE_LEFT_2;
            DRIVE_RIGHT_1 = CompetitionRobotMap.DRIVE_RIGHT_1;
            DRIVE_RIGHT_2 = CompetitionRobotMap.DRIVE_RIGHT_2;
            DRIVE_ENCODER_LEFT_A = CompetitionRobotMap.DRIVE_ENCODER_LEFT_A;
            DRIVE_ENCODER_LEFT_B = CompetitionRobotMap.DRIVE_ENCODER_LEFT_B;
            DRIVE_ENCODER_RIGHT_A = CompetitionRobotMap.DRIVE_ENCODER_RIGHT_A;
            DRIVE_ENCODER_RIGHT_B = CompetitionRobotMap.DRIVE_ENCODER_RIGHT_B;
            DRIVE_GYRO = CompetitionRobotMap.DRIVE_GYRO;
            
            DRIVE_SHIFTER_FORWARD = CompetitionRobotMap.DRIVE_SHIFTER_FORWARD;
            DRIVE_SHIFTER_REVERSE = CompetitionRobotMap.DRIVE_SHIFTER_REVERSE;
            DRIVE_SHIFTER_ENGAGE_PTO = CompetitionRobotMap.DRIVE_SHIFTER_ENGAGE_PTO;
            
            SHOOTER_MOTOR = CompetitionRobotMap.SHOOTER_MOTOR;
            SHOOTER_LOADER = CompetitionRobotMap.SHOOTER_LOADER;
            SHOOTER_TILTER = CompetitionRobotMap.SHOOTER_TILTER;
            SHOOTER_PHOTOSENSOR_CHANNEL = CompetitionRobotMap.SHOOTER_PHOTOSENSOR_CHANNEL;
            
            CLIMBER_TIPPER_FORWARD = CompetitionRobotMap.CLIMBER_TIPPER_FORWARD;
            CLIMBER_TIPPER_REVERSE = CompetitionRobotMap.CLIMBER_TIPPER_REVERSE;
            CLIMBER_TOP_LIMIT = CompetitionRobotMap.CLIMBER_TOP_LIMIT;
            CLIMBER_BOTTOM_LIMIT = CompetitionRobotMap.CLIMBER_BOTTOM_LIMIT;
            CLIMBER_LOCK = CompetitionRobotMap.CLIMBER_LOCK;
            
            DUMPER = CompetitionRobotMap.DUMPER;
        }
        
        else{
            COMPRESSOR_RELAY = PracticeRobotMap.COMPRESSOR_RELAY;
            PRESSURE_SWITCH = PracticeRobotMap.PRESSURE_SWITCH;
            
            DRIVE_LEFT_1 = PracticeRobotMap.DRIVE_LEFT_1;
            DRIVE_LEFT_2 = PracticeRobotMap.DRIVE_LEFT_2;
            DRIVE_RIGHT_1 = PracticeRobotMap.DRIVE_RIGHT_1;
            DRIVE_RIGHT_2 = PracticeRobotMap.DRIVE_RIGHT_2;
            DRIVE_ENCODER_LEFT_A = PracticeRobotMap.DRIVE_ENCODER_LEFT_A;
            DRIVE_ENCODER_LEFT_B = PracticeRobotMap.DRIVE_ENCODER_LEFT_B;
            DRIVE_ENCODER_RIGHT_A = PracticeRobotMap.DRIVE_ENCODER_RIGHT_A;
            DRIVE_ENCODER_RIGHT_B = PracticeRobotMap.DRIVE_ENCODER_RIGHT_B;
            DRIVE_GYRO = PracticeRobotMap.DRIVE_GYRO;
            
            DRIVE_SHIFTER_FORWARD = PracticeRobotMap.DRIVE_SHIFTER_FORWARD;
            DRIVE_SHIFTER_REVERSE = PracticeRobotMap.DRIVE_SHIFTER_REVERSE;
            DRIVE_SHIFTER_ENGAGE_PTO = PracticeRobotMap.DRIVE_SHIFTER_ENGAGE_PTO;
            
            SHOOTER_MOTOR = PracticeRobotMap.SHOOTER_MOTOR;
            SHOOTER_LOADER = PracticeRobotMap.SHOOTER_LOADER;
            SHOOTER_TILTER = PracticeRobotMap.SHOOTER_TILTER;
            SHOOTER_PHOTOSENSOR_CHANNEL = PracticeRobotMap.SHOOTER_PHOTOSENSOR_CHANNEL;
            
            CLIMBER_TIPPER_FORWARD = PracticeRobotMap.CLIMBER_TIPPER_FORWARD;
            CLIMBER_TIPPER_REVERSE = PracticeRobotMap.CLIMBER_TIPPER_REVERSE;
            CLIMBER_TOP_LIMIT = PracticeRobotMap.CLIMBER_TOP_LIMIT;
            CLIMBER_BOTTOM_LIMIT = PracticeRobotMap.CLIMBER_BOTTOM_LIMIT;
            CLIMBER_LOCK = PracticeRobotMap.CLIMBER_LOCK;
            
            DUMPER = PracticeRobotMap.DUMPER;
        }
    }
    
    public static int COMPRESSOR_RELAY;
    public static int PRESSURE_SWITCH;

    public static int DRIVE_LEFT_1;
    public static int DRIVE_LEFT_2;
    public static int DRIVE_RIGHT_1;
    public static int DRIVE_RIGHT_2;
    public static int DRIVE_ENCODER_LEFT_A;
    public static int DRIVE_ENCODER_LEFT_B;
    public static int DRIVE_ENCODER_RIGHT_A;
    public static int DRIVE_ENCODER_RIGHT_B;
    public static int DRIVE_GYRO;
    
    public static int DRIVE_SHIFTER_FORWARD;
    public static int DRIVE_SHIFTER_REVERSE;
    public static int DRIVE_SHIFTER_ENGAGE_PTO;
    
    public static int SHOOTER_MOTOR;
    public static int SHOOTER_LOADER;
    public static int SHOOTER_TILTER;
    public static int SHOOTER_PHOTOSENSOR_CHANNEL;
    
    public static int CLIMBER_TIPPER_FORWARD;
    public static int CLIMBER_TIPPER_REVERSE;
    public static int CLIMBER_TOP_LIMIT;
    public static int CLIMBER_BOTTOM_LIMIT;
    public static int CLIMBER_LOCK;
    
    public static int DUMPER;
    
}
