// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
//imported libraries

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
//import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */

//defining controllers and motors
public class Robot extends TimedRobot {

  //defines controllers
  private DifferentialDrive m_myRobot;
  private XboxController m_leftStick;
  private XboxController m_rightStick;
  //private XboxController m_rightStick;

  //pairs left and right side motors together into "groups"
  private static final int leftleadDeviceID = 1; 
  private static final int leftfollowDeviceID = 2;
  private static final int rightleadDeviceID = 3;
  private static final int rightfollowDeviceID = 4;

  //defines the driving motors
  //always referance the lead motors of each side
  private CANSparkMax m_leftleadMotor;
  private CANSparkMax m_rightleadMotor;
  private CANSparkMax m_leftfollowMotor;
  private CANSparkMax m_rightfollowMotor;

//defines the arm and intake motors
  private final WPI_TalonSRX armMotor = new WPI_TalonSRX(7);
  private final WPI_TalonSRX leftGateMotor = new WPI_TalonSRX(9);
  private final WPI_TalonSRX rightGateMotor = new WPI_TalonSRX(8);
  private final WPI_TalonSRX LowerIntake = new WPI_TalonSRX(6);

//creates options for autonomous

  private static final String kNothingAuto = "do nothing";
 // private static final String kSForwardAuto = "move forward short";
  private static final String kLForwardAuto = "move forward long";
  private static final String kDockAuto = "Dock";
  //private static final String kSDropAndGoAuto = "Lower Cube Drop and leave community short";
  private static final String kLDropAndGoAuto = "Lower Cube Drop and leave community long";
  private static final String kTopAndGoAuto = "Drop game piece at top and leave community short";
  private static final String kMiddleAndGoAuto = "Drop game piece in middle and leave community short";
  

  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
/* 
  private String m_idleSelected;
  private final SendableChooser<String> m_idlechooser = new SendableChooser<>();

private static final String kBBrake = "Brake";
private static final String kCCoast = "Coast";
*/


  /**
   * This function is run when the robot 6is first started up and should be used for any
   * initialization code.
   */

  @Override
  public void robotInit() {

    //sets up the lead and follow motors

    m_leftleadMotor = new CANSparkMax(leftleadDeviceID, MotorType.kBrushless);
    m_leftfollowMotor = new CANSparkMax(leftfollowDeviceID, MotorType.kBrushless);
    m_rightleadMotor = new CANSparkMax(rightleadDeviceID, MotorType.kBrushless);
    m_rightfollowMotor = new CANSparkMax(rightfollowDeviceID, MotorType.kBrushless);
    m_leftleadMotor.restoreFactoryDefaults();
    m_leftfollowMotor.restoreFactoryDefaults();
    m_rightleadMotor.restoreFactoryDefaults();
    m_rightfollowMotor.restoreFactoryDefaults();
    m_leftfollowMotor.follow(m_leftleadMotor);
    m_rightfollowMotor.follow(m_rightleadMotor);

    //starts differential drive

    m_myRobot = new DifferentialDrive(m_leftleadMotor, m_rightleadMotor);
    
    //defines the Xbox Controllers
       //note that each time m_leftStick is referanced, it means the Xbox Controller as a whole
   
    m_leftStick = new XboxController(0);
    m_rightStick = new XboxController(1);


    //create buttons for autonomous options
    m_chooser.setDefaultOption("do nothing", kNothingAuto);
    //m_chooser.addOption("move forward short", kSForwardAuto);
    m_chooser.addOption("move forward long", kLForwardAuto);
    //m_chooser.addOption("Lower Cube Drop and leave community short", kSDropAndGoAuto);
    m_chooser.addOption("Lower Cube Drop and leave community long", kLDropAndGoAuto);
    m_chooser.addOption("Dock", kDockAuto);
    m_chooser.addOption("Drop game piece at top and leave community short",kTopAndGoAuto);
    m_chooser.addOption("Drop game piece in middle and leave community short",kMiddleAndGoAuto);
    
    SmartDashboard.putData("Auto choices", m_chooser);
    CameraServer.startAutomaticCapture();

/* 
    m_idlechooser.setDefaultOption("Brake", kBBrake);
    m_idlechooser.addOption("Coast", kCCoast);

    SmartDashboard.putData("Drive Choices", m_idlechooser);

    m_idleSelected = m_idlechooser.getSelected();
    System.out.println("Idle selected: " + m_idleSelected);
*/
  }

  
  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */

  @Override
  public void robotPeriodic() {
    //if (m_idleSelected == kBBrake) { m_leftleadMotor.getIdleMode() == IdleMode.kBrake
    //}
    /*
    if (m_idleSelected == kBBrake) {
      if(m_leftleadMotor.setIdleMode(IdleMode.kBrake) != REVLibError.kOk){
        SmartDashboard.putString("Idle Mode", "Error");
      }
      if(m_leftleadMotor.getIdleMode() == IdleMode.kBrake) {
        SmartDashboard.putString("Idle Mode", "Brake");
      } else {
        SmartDashboard.putString("Idle Mode", "Coast");
      }
      
      if(m_leftfollowMotor.setIdleMode(IdleMode.kBrake) != REVLibError.kOk){
        SmartDashboard.putString("Idle Mode", "Error");
      }
      if(m_leftfollowMotor.getIdleMode() == IdleMode.kBrake) {
        SmartDashboard.putString("Idle Mode", "Brake");
      } else {
        SmartDashboard.putString("Idle Mode", "Coast");
      }
      
      if(m_rightleadMotor.setIdleMode(IdleMode.kBrake) != REVLibError.kOk){
        SmartDashboard.putString("Idle Mode", "Error");
      }
      if(m_rightleadMotor.getIdleMode() == IdleMode.kBrake) {
        SmartDashboard.putString("Idle Mode", "Brake");
      } else {
        SmartDashboard.putString("Idle Mode", "Coast");
      }
      
      if(m_rightfollowMotor.setIdleMode(IdleMode.kBrake) != REVLibError.kOk){
        SmartDashboard.putString("Idle Mode", "Error");
      }
      if(m_rightfollowMotor.getIdleMode() == IdleMode.kBrake) {
        SmartDashboard.putString("Idle Mode", "Brake");
      } else {
        SmartDashboard.putString("Idle Mode", "Coast");
      }
      return;
    }
    if (m_idleSelected == kCCoast) {
      if(m_leftleadMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
        SmartDashboard.putString("Idle Mode", "Error");
      }
      if(m_leftleadMotor.getIdleMode() == IdleMode.kCoast) {
        SmartDashboard.putString("Idle Mode", "Coast");
      } else {
        SmartDashboard.putString("Idle Mode", "Brake");
      }
      
      if(m_leftfollowMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
        SmartDashboard.putString("Idle Mode", "Error");
      }
      if(m_leftfollowMotor.getIdleMode() == IdleMode.kCoast) {
        SmartDashboard.putString("Idle Mode", "Coast");
      } else {
        SmartDashboard.putString("Idle Mode", "Brake");
      }
      
      if(m_rightleadMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
        SmartDashboard.putString("Idle Mode", "Error");
      }
      if(m_rightleadMotor.getIdleMode() == IdleMode.kCoast) {
        SmartDashboard.putString("Idle Mode", "Coast");
      } else {
        SmartDashboard.putString("Idle Mode", "Brake");
      }
      
      if(m_rightfollowMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
        SmartDashboard.putString("Idle Mode", "Error");
      }
      if(m_rightfollowMotor.getIdleMode() == IdleMode.kCoast) {
        SmartDashboard.putString("Idle Mode", "Coast");
      } else {
        SmartDashboard.putString("Idle Mode", "Brake");
      }
      return;
    }
    */
  }



  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */



   //define autonomous variables here
   double autonomousStartTime;

  @Override
  public void autonomousInit() {

    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

    autonomousStartTime = Timer.getFPGATimestamp();

    if(m_leftleadMotor.setIdleMode(IdleMode.kBrake) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }
    if(m_leftleadMotor.getIdleMode() == IdleMode.kBrake) {
      SmartDashboard.putString("Idle Mode", "Brake");
    } else {
      SmartDashboard.putString("Idle Mode", "Coast");
    }
    
    if(m_leftfollowMotor.setIdleMode(IdleMode.kBrake) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }
    if(m_leftfollowMotor.getIdleMode() == IdleMode.kBrake) {
      SmartDashboard.putString("Idle Mode", "Brake");
    } else {
      SmartDashboard.putString("Idle Mode", "Coast");
    }
    
    if(m_rightleadMotor.setIdleMode(IdleMode.kBrake) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }
    if(m_rightleadMotor.getIdleMode() == IdleMode.kBrake) {
      SmartDashboard.putString("Idle Mode", "Brake");
    } else {
      SmartDashboard.putString("Idle Mode", "Coast");
    }
    
    if(m_rightfollowMotor.setIdleMode(IdleMode.kBrake) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }
    if(m_rightfollowMotor.getIdleMode() == IdleMode.kBrake) {
      SmartDashboard.putString("Idle Mode", "Brake");
    } else {
      SmartDashboard.putString("Idle Mode", "Coast");
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

    SmartDashboard.putNumber("time (seconds)", Timer.getFPGATimestamp());

    // Robot stays in place
    if (m_autoSelected == kNothingAuto) {
      m_leftleadMotor.set(0);
      m_rightleadMotor.set(0);
      return;
    }


    double timeElapsed = Timer.getFPGATimestamp() - autonomousStartTime;

    // Robot leaves community backwards shortest point
    /* 
    if (m_autoSelected == kSForwardAuto) {
     
      if (timeElapsed < 6) {
        m_leftleadMotor.set(-0.1);
        m_rightleadMotor.set(0.1);
      } else {
        m_leftleadMotor.set(0);
        m_rightleadMotor.set(0);
      }
    }
    */

    // Robot leaves community backwards longest point
    if (m_autoSelected == kLForwardAuto) {
     
      if (timeElapsed < 9) {
        m_leftleadMotor.set(-0.1);
        m_rightleadMotor.set(0.1);
      } else {
        m_leftleadMotor.set(0);
        m_rightleadMotor.set(0);
      }
    }

    // Drops off and leaves at shortest point
    /*if (m_autoSelected == kSDropAndGoAuto) {
     
      if (timeElapsed < 2) {
      LowerIntake.set(-0.25);
      } else if (timeElapsed > 2 && timeElapsed < 8) {
        m_leftleadMotor.set(-0.1);
        m_rightleadMotor.set(0.1);
        LowerIntake.set(0);
      } else {
        m_leftleadMotor.set(0);
        m_rightleadMotor.set(0);
        LowerIntake.set(0);
    }
  }*/

  // Drops off and leaves at longest point
  if (m_autoSelected == kLDropAndGoAuto) {
     
    if (timeElapsed < 2) {
    LowerIntake.set(-0.5);
    } else if (timeElapsed > 2 && timeElapsed < 11) {
      m_leftleadMotor.set(-0.1);
      m_rightleadMotor.set(0.1);
      LowerIntake.set(0);
    } else {
      m_leftleadMotor.set(0);
      m_rightleadMotor.set(0);
      LowerIntake.set(0);
  }
}
  if (m_autoSelected == kDockAuto) {
     
  if (timeElapsed < 4) {
    m_leftleadMotor.set(-0.15);
    m_rightleadMotor.set(0.15);
  } else if(timeElapsed > 4 && timeElapsed < 4.6) {
    m_leftleadMotor.set(0.2);
    m_rightleadMotor.set(0.2);
  }
  
  else {
    m_leftleadMotor.set(0);
    m_rightleadMotor.set(0);
  }
}
if (m_autoSelected == kTopAndGoAuto) {
     
  if (timeElapsed < 3.5) {
  armMotor.set(0.5);
  } else if (timeElapsed > 3.5 && timeElapsed < 5.5) {
    leftGateMotor.set(-1);
    rightGateMotor.set(-1);
    armMotor.set(0);
  } else if (timeElapsed > 5.5 && timeElapsed < 12 && SensorActive == false) {
    m_leftleadMotor.set(-0.14);
    m_rightleadMotor.set(0.14);
    leftGateMotor.set(0);
    rightGateMotor.set(0);
    armMotor.set(-0.28);
  }
    else {
    m_leftleadMotor.set(0);
    m_rightleadMotor.set(0);
    armMotor.set(0);
    leftGateMotor.set(0);
    rightGateMotor.set(0);
}
}
if (m_autoSelected == kMiddleAndGoAuto) {
     
  if (timeElapsed < 3) {
  armMotor.set(0.5);
  } else if (timeElapsed > 3 && timeElapsed < 5) {
    leftGateMotor.set(-0.4);
    rightGateMotor.set(-0.4);
  } else if (timeElapsed > 5 && timeElapsed < 13 && SensorActive == false) {
    m_leftleadMotor.set(-0.1);
    m_rightleadMotor.set(0.1);
    leftGateMotor.set(0);
    rightGateMotor.set(0);
    armMotor.set(-0.23);
  } else {
    m_leftleadMotor.set(0);
    m_rightleadMotor.set(0);
    armMotor.set(0);
    leftGateMotor.set(0);
    rightGateMotor.set(0);
}
}
  }

    //define Teleop variables here

  double Turbo = 0.5;
  double rightgateMotorSpeed = 0;
  double leftgateMotorSpeed = 0;
  double lowerMotorSpeed = 0;
  boolean SensorActive = false;
  double turningSpeed = .7;
  DigitalInput toplimitSwitch = new DigitalInput(0);

  double armSpeed = 1;
  
  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    if(m_leftleadMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }
    if(m_leftleadMotor.getIdleMode() == IdleMode.kCoast) {
      SmartDashboard.putString("Idle Mode", "Coast");
    } else {
      SmartDashboard.putString("Idle Mode", "Brake");
    }
    
    if(m_leftfollowMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }
    if(m_leftfollowMotor.getIdleMode() == IdleMode.kCoast) {
      SmartDashboard.putString("Idle Mode", "Coast");
    } else {
      SmartDashboard.putString("Idle Mode", "Brake");
    }
    
    if(m_rightleadMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }
    if(m_rightleadMotor.getIdleMode() == IdleMode.kCoast) {
      SmartDashboard.putString("Idle Mode", "Coast");
    } else {
      SmartDashboard.putString("Idle Mode", "Brake");
    }
    
    if(m_rightfollowMotor.setIdleMode(IdleMode.kCoast) != REVLibError.kOk){
      SmartDashboard.putString("Idle Mode", "Error");
    }
    if(m_rightfollowMotor.getIdleMode() == IdleMode.kCoast) {
      SmartDashboard.putString("Idle Mode", "Coast");
    } else {
      SmartDashboard.putString("Idle Mode", "Brake");
    }
    
    
  }
    
  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

  
    //defines driving functions
    m_myRobot.arcadeDrive(m_leftStick.getRightX() * Turbo, m_leftStick.getLeftY() * Turbo);

    //defines the limit switch for the arm
    if (toplimitSwitch.get()) {
      SensorActive = false;
    } else {
      SensorActive = true;
    }
    
    //defines arm telescoping functions
    if (m_rightStick.getRightBumper()) {
      armMotor.set(armSpeed);
    } else if (m_rightStick.getLeftBumper() && SensorActive == false) {
           armMotor.set(-armSpeed);
       } else {
           armMotor.set(0);
       }



    

    //defines the turbo button
    if (m_leftStick.getRawButton(9)){

      Turbo = 2;
    } else {
      Turbo = 0.65;
    }
    
    
    //defines the upper intake functions
    if(m_rightStick.getAButton()) {
      rightGateMotor.set(rightgateMotorSpeed = 1);
      leftGateMotor.set(leftgateMotorSpeed = 1);
  
    } else if (m_rightStick.getBButton()) {
      rightGateMotor.set(rightgateMotorSpeed = -0.25);
      leftGateMotor.set(leftgateMotorSpeed = -0.25);

    } else if (m_rightStick.getYButton()){
      rightGateMotor.set(rightgateMotorSpeed = -1);
      leftGateMotor.set(leftgateMotorSpeed = -1);

    } else {
      leftGateMotor.set(leftgateMotorSpeed = 0);
      rightGateMotor.set(rightgateMotorSpeed = 0);

    }

 

    //defines the lower intake functions
    if(m_leftStick.getXButton()) {
      LowerIntake.set(lowerMotorSpeed = 0.8);
  
    } else if(m_leftStick.getYButton()) {
      LowerIntake.set(lowerMotorSpeed = -0.8);

    } else {
      LowerIntake.set(lowerMotorSpeed = 0);
    }
 }

    
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
