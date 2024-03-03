package frc.robot.SysID;

import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.MutableMeasure;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeTalonFX;

public class SysID extends SubsystemBase {
  public enum Measurement{
    ANGLE,
    DISTANCE
  }
  //Find Logged Data using FileZilla

  // Mutable holder for unit-safe voltage values, persisted to avoid reallocation.
  private final MutableMeasure<Voltage> appliedVoltage = MutableMeasure.mutable(Units.Volts.of(0));
  // Mutable holder for unit-safe linear distance values, persisted to avoid reallocation.
  private final MutableMeasure<Distance> distance = MutableMeasure.mutable(Units.Inches.of(0));
  private final MutableMeasure<Angle> angle = MutableMeasure.mutable(Units.Radians.of(0));
  // Mutable holder for unit-safe linear velocity values, persisted to avoid reallocation.
  private final MutableMeasure<Velocity<Distance>> distanceVelocity = MutableMeasure.mutable(Units.InchesPerSecond.of(0));
  private final MutableMeasure<Velocity<Angle>> angularVelocity = MutableMeasure.mutable(Units.RadiansPerSecond.of(0));
  // private final MutableMeasure<Velocity<Angle>> angularVelocity = MutableMeasure.mutable(Units.RPM.of(0));

  private SysIdRoutine routine;
  // private final SafeMotor motor;

  
  public SysID(SafeTalonFX motor, Measurement unit){
    // this.motor = motor;
    switch(unit){
      case ANGLE:
        routine = new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
          new SysIdRoutine.Config(null, null, null),
          new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
            motor.setVoltage(volts.in(Units.Volts));
          }, 
          (log)->{ 
            log.motor(motor.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))//Meters Degrees, etc?
              .angularVelocity(
                  angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));//Meters Degrees, etc?
          }, 
          this)
        );
        break;
      case DISTANCE:
        routine = new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
          new SysIdRoutine.Config(null, null, null),
          new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
            motor.setVoltage(volts.in(Units.Volts));
          }, 
          (log)->{ 
            log.motor(motor.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .linearPosition(distance.mut_replace(motor.getPosition(), Units.Inches))//Meters Degrees, etc?
              .linearVelocity(
                  distanceVelocity.mut_replace(motor.getVelocity(), Units.InchesPerSecond));//Meters Degrees, etc?
          }, 
          this)
        );
        break;   
    }
  }

  public SysID(SafeCanSparkMax motor, Measurement unit){
    // this.motor = motor;
    switch(unit){
      case ANGLE:
        routine = new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
          new SysIdRoutine.Config(null, null, null),
          new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
            motor.setVoltage(volts.in(Units.Volts));
          }, 
          (log)->{ 
            log.motor(motor.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))//Meters Degrees, etc?
              .angularVelocity(
                  angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));//Meters Degrees, etc?
          }, 
          this)
        );
        break;
      case DISTANCE:
        routine = new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
          new SysIdRoutine.Config(null, null, null),
          new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
            motor.setVoltage(volts.in(Units.Volts));
          }, 
          (log)->{ 
            log.motor(motor.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .linearPosition(distance.mut_replace(motor.getPosition(), Units.Inches))//Meters Degrees, etc?
              .linearVelocity(
                  distanceVelocity.mut_replace(motor.getVelocity(), Units.InchesPerSecond));//Meters Degrees, etc?
          }, 
          this)
        );
        break;   
    }
  }

  public SysID(SafeTalonFX motor, SafeTalonFX motor2, Measurement unit){
    // this.motor = motor;
    // this.motor2 = motor2;
    switch(unit){
      case ANGLE:
        routine = new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
          new SysIdRoutine.Config(null, null, null),
          new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
            motor.setVoltage(volts.in(Units.Volts));
          }, 
          (log)->{ 
            log.motor(motor.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))
              .angularVelocity(
                  angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));
            log.motor(motor2.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor2.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .angularPosition(angle.mut_replace(motor2.getPosition(), Units.Rotations))
              .angularVelocity(
                  angularVelocity.mut_replace(motor2.getVelocity(), Units.RotationsPerSecond));
          }, 
          this)
        );
        break;
      case DISTANCE:
        routine = new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
          new SysIdRoutine.Config(null, null, null),
          new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
            motor.setVoltage(volts.in(Units.Volts));
            motor2.setVoltage(volts.in(Units.Volts));
          }, 
          (log)->{ 
            log.motor(motor.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .linearPosition(distance.mut_replace(motor.getPosition(), Units.Inches))
              .linearVelocity(
                  distanceVelocity.mut_replace(motor.getVelocity(), Units.InchesPerSecond));
            log.motor(motor2.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor2.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .linearPosition(distance.mut_replace(motor2.getPosition(), Units.Inches))
              .linearVelocity(
                  distanceVelocity.mut_replace(motor2.getVelocity(), Units.InchesPerSecond));
          }, 
          this)
        );
        break;   
    }
  }

  public SysID(SafeCanSparkMax motor, SafeCanSparkMax motor2, Measurement unit){
    // this.motor = motor;
    switch(unit){
      case ANGLE:
        routine = new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
          new SysIdRoutine.Config(null, null, null),
          new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
            motor.setVoltage(volts.in(Units.Volts));
          }, 
          (log)->{ 
            log.motor(motor.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))
              .angularVelocity(
                  angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));
            log.motor(motor2.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor2.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .angularPosition(angle.mut_replace(motor2.getPosition(), Units.Rotations))
              .angularVelocity(
                  angularVelocity.mut_replace(motor2.getVelocity(), Units.RotationsPerSecond));
          },
          this)
        );
        break;
      case DISTANCE:
        routine = new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
          new SysIdRoutine.Config(null, null, null),
          new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
            motor.setVoltage(volts.in(Units.Volts));
          }, 
          (log)->{ 
            log.motor(motor.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .linearPosition(distance.mut_replace(motor.getPosition(), Units.Inches))//Meters Degrees, etc?
              .linearVelocity(
                  distanceVelocity.mut_replace(motor.getVelocity(), Units.InchesPerSecond));//Meters Degrees, etc?
            log.motor(motor2.toString())
              .voltage(
                  appliedVoltage.mut_replace(
                    motor2.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
              .linearPosition(distance.mut_replace(motor2.getPosition(), Units.Inches))
              .linearVelocity(
                  distanceVelocity.mut_replace(motor2.getVelocity(), Units.InchesPerSecond));
          }, 
          this)
        );
        break;   
    }
  }

  
  // public SysID(SafeMotor motor, Measurement unit){
  //   this.motor = motor;
  //   switch(unit){
  //     case ANGLE:
  //       routine = new SysIdRoutine(
  //         // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  //         // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
  //         new SysIdRoutine.Config(null, null, null),
  //         new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
  //           motor.setVoltage(volts.in(Units.Volts));
  //         }, 
  //         (log)->{ 
  //           log.motor(motor.toString())
  //             .voltage(
  //                 appliedVoltage.mut_replace(
  //                   motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  //             .angularPosition(angle.mut_replace(motor.getPosition(), Units.Rotations))//Meters Degrees, etc?
  //             .angularVelocity(
  //                 angularVelocity.mut_replace(motor.getVelocity(), Units.RotationsPerSecond));//Meters Degrees, etc?
  //         }, 
  //         this)
  //       );
  //       break;
  //     case DISTANCE:
  //       routine = new SysIdRoutine(
  //         // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
  //         // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
  //         new SysIdRoutine.Config(null, null, null),
  //         new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
  //           motor.setVoltage(volts.in(Units.Volts));
  //         }, 
  //         (log)->{ 
  //           log.motor(motor.toString())
  //             .voltage(
  //                 appliedVoltage.mut_replace(
  //                   motor.getSpeed() * RobotController.getBatteryVoltage(), Units.Volts))
  //             .linearPosition(distance.mut_replace(motor.getPosition(), Units.Inches))//Meters Degrees, etc?
  //             .linearVelocity(
  //                 distanceVelocity.mut_replace(motor.getVelocity(), Units.InchesPerSecond));//Meters Degrees, etc?
  //         }, 
  //         this)
  //       );
  //       break;   
  //   }
  // }








    // public SysID(Intake intake){//Intake Wheel
    //   routine = new SysIdRoutine(
    //       // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
    //       // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
    //       new SysIdRoutine.Config(null, null, null),
    //       new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
    //         intake.getIntakeWheel().getMotor().setVoltage(volts.in(Units.Volts));
    //       }, 
    //       (log)->{ 
    //         log.motor("Intake Wheel")
    //           .voltage(
    //               appliedVoltage.mut_replace(
    //                 intake.getIntakeWheel().getMotor().getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
    //           .angularPosition(angle.mut_replace(intake.getIntakeWheel().getMotor().getPosition(), Units.Rotations))//Meters Degrees, etc?
    //           .linearVelocity(
    //               distanceVelocity.mut_replace(intake.getIntakeWheel().getMotor().getVelocity(), Units.InchesPerSecond));//Meters Degrees, etc?
    //       }, 
    //       intake.getIntakeWheel())
    //   );
    // }
    // public SysID(Intake intake){//Intake DropDown
    //   routine = new SysIdRoutine(
    //       // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
    //       // new SysIdRoutine.Config(null, null, null, (state) -> Logger.recordOutput("Drive/SysIdState", state.toString())),
    //       new SysIdRoutine.Config(null, null, null),
    //       new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
    //         intake.getDropDown().getMotor().setVoltage(volts.in(Units.Volts));
    //       }, 
    //       (log)->{ 
    //         log.motor("intakeDropDown")
    //           .voltage(
    //               m_appliedVoltage.mut_replace(
    //                 intake.getDropDown().getMotor().getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
    //           .linearPosition(m_distance.mut_replace(intake.getDropDown().getMotor().getPosition(), Units.Inches))//Meters Degrees, etc?
    //           .linearVelocity(
    //               m_velocity.mut_replace(intake.getDropDown().getMotor().getVelocity(), Units.InchesPerSecond));//Meters Degrees, etc?
    //       }, 
    //       intake.getDropDown())
    //   );
    // }
    // public SysID(Shooter shooter){
    //   routine = new SysIdRoutine(
    //       // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
    //       new SysIdRoutine.Config(null, null, null),
    //       new SysIdRoutine.Mechanism((Measure<Voltage> volts) -> {
    //         shooter.getMotorL().setVoltage(volts.in(Units.Volts));
    //         shooter.getMotorR().setVoltage(volts.in(Units.Volts));
    //       }, 
    //       (log)->{ 
    //         log.motor("leftShooterMotor")
    //           .voltage(
    //               appliedVoltage.mut_replace(
    //                 shooter.getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
    //           .linearPosition(distance.mut_replace(shooter.getMotorL().getPosition(), Units.Meters))//Meters Degrees, etc?
    //           .linearVelocity(
    //               distanceVelocity.mut_replace(shooter.getVelocity(), Units.MetersPerSecond));//Meters Degrees, etc?
    //         log.motor("leftShooterMotor")
    //           .voltage(
    //               appliedVoltage.mut_replace(
    //                 shooter.getVelocity() * RobotController.getBatteryVoltage(), Units.Volts))
    //           .linearPosition(distance.mut_replace(shooter.getMotorR().getPosition(), Units.Meters))//Meters Degrees, etc?
    //           .linearVelocity(
    //               distanceVelocity.mut_replace(shooter.getVelocity(), Units.MetersPerSecond));//Meters Degrees, etc?
    //       }, 
    //       shooter)
    //   );
    // }

    @Override
    public void periodic(){
      // dataLog.flush();
    }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return routine.quasistatic(direction);
    }
      
    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
      return routine.dynamic(direction);
    }
    // public Command stop(){
    //   return new InstantCommand(()->motor.setPower(0));
    // }
}

