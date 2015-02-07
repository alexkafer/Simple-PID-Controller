package org.usfirst.frc.team2526.robot;


import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This sample program shows how to control a motor using a joystick. In the operator
 * control part of the program, the joystick is read and the value is written to the motor.
 *
 * Joystick analog values range from -1 to 1 and speed controller inputs also range from -1
 * to 1 making it easy to work together. The program also delays a short time in the loop
 * to allow other threads to run. This is generally a good idea, especially since the joystick
 * values are only transmitted from the Driver Station once every 20ms.
 */
public class Robot extends SampleRobot {
	
    private CANTalon motor;	
    private Joystick stick = new Joystick(0);
    
    private boolean powerLocked;
    private double power;

	private final double k_updatePeriod = 0.005; // update every 0.005 seconds/5 milliseconds (200Hz)

    public Robot() {
        motor = new CANTalon(1);
        
        motor.changeControlMode(CANTalon.ControlMode.PercentVbus);
        
        motor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        
        double p = 0.1; 
        double i = 0.001; 
        double d = 1; 
        double f = 0.0001; 
        int izone = 250; //Encoder Ticks
        double ramprate = 36; //Volts per second
        int profile = 0; //0 or 1
        
        motor.setPID(p, i, d, f, izone, ramprate, profile);
        motor.enableControl();
    }

    /**
     * Runs the motor from a joystick.
     */
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
        	// Set the motor's output.
        	// This takes a number from -1 (100% speed in reverse) to +1 (100% speed going forward)
        	if (stick.getTop()){
        		powerLocked = false;
        		power = 0;
        	} else if (stick.getTrigger()) {
        		powerLocked = true;
        		power = stick.getY();
        	}
        	
        	motor.set(powerLocked ? power : stick.getY());
        	
        	SmartDashboard.putNumber("Motor Get", motor.get());
        	
            Timer.delay(k_updatePeriod);	// wait 5ms to the next update
        }
        
    }
}
