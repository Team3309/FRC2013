/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc2013;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author friarbots
 */
public class XboxController {

    /*
     * Xbox 360 Controller Mapping - Vincente
     */
    // Buttons
    private static final int B_D_PAD_UP = 5;
    private static final int B_D_PAD_DOWN = 6;
    private static final int B_D_PAD_LEFT = 7;
    private static final int B_D_PAD_RIGHT = 8;
    private static final int B_START = 8;
    private static final int B_BACK = 7;
    private static final int B_LEFT_STICK = 9;
    private static final int B_RIGHT_STICK = 10;
    private static final int B_LEFT_BUMPER = 5;
    private static final int B_RIGHT_BUMPER = 6;
    private static final int B_X_HOME = 15;
    private static final int B_A = 1;
    private static final int B_B = 2;
    private static final int B_X = 3;
    private static final int B_Y = 4;
    // Axis
    private static final int A_LEFT_X = 1;
    private static final int A_LEFT_Y = 2;
    private static final int A_LEFT_TRIGGER = 3;
    private static final int A_RIGHT_TRIGGER = 4;
    private static final int A_RIGHT_X = 4;//5;
    private static final int A_RIGHT_Y = 6;
    private Joystick mController;

    public XboxController(int controller) {
        mController = new Joystick(controller);
    }

    public boolean getLeftBumper() {
        return mController.getRawButton(B_LEFT_BUMPER);
    }

    public boolean getRightBumper() {
        return mController.getRawButton(B_RIGHT_BUMPER);
    }

    public boolean getDPadUp() {
        return mController.getRawButton(B_D_PAD_UP);
    }

    public boolean getDPadDown() {
        return mController.getRawButton(B_D_PAD_DOWN);
    }

    public boolean getDPadLeft() {
        return mController.getRawButton(B_D_PAD_LEFT);
    }

    public boolean getDPadRight() {
        return mController.getRawButton(B_D_PAD_RIGHT);
    }

    public boolean getStart() {
        return mController.getRawButton(B_START);
    }

    public boolean getBack() {
        return mController.getRawButton(B_BACK);
    }

    public boolean getLeftStickPressed() {
        return mController.getRawButton(B_LEFT_STICK);
    }

    public boolean getRightStickPressed() {
        return mController.getRawButton(B_RIGHT_STICK);
    }

    public boolean getA() {
        return mController.getRawButton(B_A);
    }

    public boolean getB() {
        return mController.getRawButton(B_B);
    }

    public boolean getX() {
        return mController.getRawButton(B_X);
    }

    public boolean getY() {
        return mController.getRawButton(B_Y);
    }

    public boolean getXboxButton() {
        return mController.getRawButton(B_X_HOME);
    }

    public double getLeftX() {
        double val = mController.getRawAxis(A_LEFT_X);
        if (Math.abs(val) < .05) {
            return 0;
        }
        return val;
    }

    public double getLeftY() {
        double val = mController.getRawAxis(A_LEFT_Y);
        if (Math.abs(val) < .05) {
            return 0;
        }
        return val;
    }

    public double getLeftTrigger() {
        return mController.getRawAxis(A_LEFT_TRIGGER);
    }

    public double getRightX() {
        double val = mController.getRawAxis(A_RIGHT_X);
        if (Math.abs(val) < .05) {
            return 0;
        }
        return val;
    }

    public double getRightY() {
        double val = mController.getRawAxis(A_RIGHT_Y);
        if (Math.abs(val) < .05) {
            return 0;
        }
        return val;
    }

    public double getRightTrigger() {
        return mController.getRawAxis(A_RIGHT_TRIGGER);
    }
}
