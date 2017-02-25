package org.usfirst.frc.team4765.robot;
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
    Joystick stick;
    Joystick buttons;

 	public CANTalon motorfr = new CANTalon(3); // front right, follower
 	public CANTalon motorbl = new CANTalon(4); // rear left, follower
 	public CANTalon motorfl = new CANTalon(5); // front left, leader
 	public CANTalon motorbr = new CANTalon(8); // rear right, leader
 	public CANTalon motorwl = new CANTalon(2); // left winch motor
    public CANTalon motorwr = new CANTalon(9); // right winch motor
    public CANTalon motorgp = new CANTalon(1); // gear pusher motor
    public DigitalInput backLim = new DigitalInput(0); //back limit switch of the gearpusher
    public DigitalInput frontLim = new DigitalInput(1); //front limit switch of the gearpusher
 	
    // The channel on the driver station that the joystick is connected to
    final int joystickChannel	= 0;
    // The channel on the driver station that the operator buttons are connected to
    final int operatorChannel = 1;
    
    public long startTime = 0;
	public boolean restrictThrottlePush = false;
	public boolean restrictThrottleWinch = false;
	public double maxCurrent = 0;

    public void robotInit() {
    	System.out.println("ROBOT INITIALIZING");
    	motorfr.changeControlMode(TalonControlMode.PercentVbus);
	    motorbl.changeControlMode(TalonControlMode.PercentVbus);
	    motorfl.changeControlMode(TalonControlMode.PercentVbus);
	    motorbr.changeControlMode(TalonControlMode.PercentVbus);
	    motorwl.changeControlMode(TalonControlMode.PercentVbus);
	    motorwr.changeControlMode(TalonControlMode.PercentVbus);
	    motorgp.changeControlMode(TalonControlMode.PercentVbus);
	    motorfr.set(0.0);
	    motorbl.set(0.0);
	    motorfl.set(0.0);
	    motorbr.set(0.0);
	    motorwl.set(0.0);
	    motorwr.set(0.0);
	    motorgp.set(0.0);

        stick = new Joystick(joystickChannel);
        buttons = new Joystick (operatorChannel);
        System.out.println("ROBOT INITIALIZED");
    }
    public void robotPeriodic() {
        //System.out.println("robotPeriodic");
    }
    public void autonomousInit(){
	    System.out.println("AUTONOMOUS STARTED");
	    startTime = System.currentTimeMillis();
    }
    public void autonomousPeriodic(){
	    //System.out.println("autonomousPeriodic");
    	double x = 0;
    	double y = 0;
    	double z = 0;
    	long currentTime = System.currentTimeMillis() - startTime;
    	if (buttons.getRawButton(1)){
	    	System.out.println("STAY");
	    	//STAY
	    	//x, y, and z all stay zero
	    }else
	    if (buttons.getRawButton(2)){
	    	System.out.println("TOUCH Forward 0.25 for one second");
	    	//TOUCH - track time or rotations or 'wait until hit something'
	    	//Drive for a set amount of time or rotations or a wait loop
	    	//Stop at base line
	    	if (currentTime < 1000){
	    		//SET THE X, Y, and Z values to reflect moving forward.
	    		y = -0.25;
	    	}
	    }else
	    if (buttons.getRawButton(3 )){
	    	System.out.println("PLACE Forward 0.25 for two seconds");
	    	//PLACE - Drive to precise gear-dropping location, possibly to be removed by the pilots
	    	//possibly to be left in close position so it can be deposited quickly and easily during teleop
	    	if (currentTime < 2000){
	    		y = -0.31;
	    	}
	    	if (currentTime > 2000 && currentTime < 3000){
	    		if (buttons.getY() < -0.33){
	    			z = -0.25;
	    		}else
	    		if (buttons.getY() > 0.33){
	    			z = 0.25;
	    		}else{
	    			y = -0.1;
	    		} 
	    	}
	    	if (currentTime > 3000){
	    		y = -0.15;
	    	}
	    }else
		if (buttons.getRawButton(4)){
		    System.out.println("DUMP");
		    //DUMP - Drive to a feeder-hopper pressure plate and dump the fuel
		    //May require precision if we end up with a hopper
		    if (currentTime < 3000){
		    	//y = 0.25;
		    }
		}else
	    if (buttons.getRawButton(5)){
	    	System.out.println("DIALFIVE");
	    	//SHOOT - Drive into a shooting position for the low goal
	    	//Activate shooting motors - shoot all ten fuel
	    }
    	double maximum = Math.abs(x) + Math.abs(y) + Math.abs(z);
   		if (maximum > 1.0) {
   			x = x / maximum;
       		y = y / maximum;
       		z = z / maximum;
   		}
        double frontLeft = -x+y+z;
        double rearLeft	= x+y+z;
        double frontRight = -x-y+z;
        double rearRight = x-y+z;
        
        //System.out.println("x: " + x);
        //System.out.println("y: " + y);
        //System.out.println("z: " + z);
       
        motorfl.set(frontLeft);
        motorbl.set(rearLeft);
        motorfr.set(frontRight);
        motorbr.set(rearRight);
    }
    public void disabledInit(){
	    System.out.println("ROBOT DISABLED");
    }
    public void disabledPeriodic(){
	    //System.out.println("disabledPeriodic"); CONFIRMED TO WORK
    }
    public void teleopInit(){
	    System.out.println("TELEOP STARTED");
    }
    public void teleopPeriodic(){
	    // Use the Joystick X axis for lateral movement, Y axis for forward movement, and Z axis for rotation.
        //System.out.println("teleopPeriodic"); Confirmed to work!
    	double throttle = (stick.getThrottle() + 1.0) / 2.0;
        double x = stick.getX();
   		double y = stick.getY();
   		double z = stick.getZ();
   		double current1 = motorwl.getOutputCurrent();
   		double current2 = motorwr.getOutputCurrent();
   		//System.out.println("current1 " + current1);
   		//System.out.println("current2 " + current2);
   		double current = current1 + current2;

   		if (stick.getRawButton(1) || buttons.getRawButton(8)){
   			restrictThrottlePush = true;
   			if (frontLim.get()){ //NEVER DRIVE FORWARDS IF THE LIMIT IS PRESSED
   				//System.out.println("RELEASE GEAR");
   				motorgp.set(1.0);
   			}else{
   				motorgp.set(0.0);
   			}
   		}else{
   			if (backLim.get()){ //NEVER DRIVE BACKWARDS IF THE LIMIT IS PRESSED
				motorgp.set(-1.0);
				restrictThrottlePush = false;
   			}else{
   				motorgp.set(0.0);
   				restrictThrottlePush = false;
   			}
   		}
   		if (stick.getRawButton(2)){
   			System.out.println("BUTTON2");
   		}
   		
   		if (stick.getRawButton(4) || buttons.getRawButton(10)){
   			motorwr.set(0.0);
   			motorwl.set(0.0);
   			restrictThrottleWinch = false;
   		}else
   		if (current > 60.0){
   			motorwr.set(0.0);
   			motorwl.set(0.0);
   			restrictThrottleWinch = false;
   			System.out.println("TOO HIGH current " + current);
   			//35 AMPS STARTUP CURRENT
   			//50.625 AMPS Climbing
   			//56 AMPS trying to start
   		}else
   		if (stick.getRawButton(3) || buttons.getRawButton(11)){
   			restrictThrottleWinch = true;
   			throttle = throttle / 2; //This might really not work because this is a toggle
   			motorwr.set(1.0);
   			motorwl.set(1.0);
   		}
		if (current > maxCurrent){
			maxCurrent = current;
		}
		if (restrictThrottleWinch){
			System.out.println("current " + current);
			System.out.println("MAX current " + maxCurrent);
		}
   		//START OPERATOR BUTTON TESTS
	    if (buttons.getRawButton(9)){
	    	System.out.println("BUTTONRESET");
	    	//RESET
	    }else
	    if (buttons.getRawButton(12)){
	    	System.out.println("BACKFLIP");
	    }
   		
    	//END
   		
   		//System.out.println("x: " + x);
        //System.out.println("y: " + y);
        //System.out.println("z: " + z);
   		x = x * 1.2;
   		double maximum = Math.abs(x) + Math.abs(y) + Math.abs(z);
   		if (maximum > 1.0) {
   			x = x / maximum;
       		y = y / maximum;
       		z = z / maximum;
   		}
        double frontLeft = -x+y+z;
        double rearLeft	= x+y+z;
        double frontRight = -x-y+z;
        double rearRight = x-y+z;
        
        //System.out.println("x: " + x);
        //System.out.println("y: " + y);
        //System.out.println("z: " + z);
        if (buttons.getRawButton(6)){
        	if (restrictThrottlePush || restrictThrottleWinch){
        		if (throttle > 0.25){
        			throttle = 0.25;
        		}
        	}
        }
        motorfl.set(frontLeft * throttle);
        motorbl.set(rearLeft * throttle);
        motorfr.set(frontRight * throttle);
        motorbr.set(rearRight * throttle);
       
        Timer.delay(0.005);	// wait 5ms to avoid hogging CPU cycles
    }
    public void testInit(){
	    System.out.println("testInit");
    }
    public void testPeriodic(){
    	
    }
 }

