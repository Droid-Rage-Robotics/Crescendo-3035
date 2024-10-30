package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.intake.dropDown.IntakeDropDownAbsolute;
import frc.robot.utility.motor.old.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Intake {
    //Can use the Positions to make another Value 
    //that holds 2 Positions to join together like 
    //in Charged Up; Allows for you to different 
    //Position Based on game element
    //170-340
    public enum Value {
        START(24.4,0),//15

        //IntakePos
        INTAKE_GROUND(206.8,-4500),//205.21
        // AUTO_INTAKE_GROUND(IN)
        AUTO_INTAKE_GROUND(207,-5000),
        INTAKE_HUMAN(100,-INTAKE_GROUND.getIntakeSpeeds()),//INTAKE_GROUND
        // CLIMB(INTAKE_GROUND,-3000),
        

        SHOOTER_HOLD(24.2, 0),//Ready to give Note to shooter, but not doing it//0,-100
        SHOOTER_TRANSFER(SHOOTER_HOLD.getAngle(), 1400),//Giving Note to Shooter
        AUTO_SHOOTER_TRANSFER(SHOOTER_HOLD.getAngle(), 2000),//Giving Note to Shooter
       
        INTAKE_HOLD(SHOOTER_HOLD.getAngle(),INTAKE_GROUND.getIntakeSpeeds()),

        HOLD(90, 0),
        OUTTAKE(130,1600),
        CLIMB_DOWN(170, 0),
                // CLIMB_HOLD(55, 0),

        OUTTAKE_AMP(88,3200)//
        ;

        private final ShuffleboardValue<Double> pivotAngle;
        private final ShuffleboardValue<Double> intakeSpeeds;
        

        private Value(double pivotAngle, double intakeSpeeds) {
            this.pivotAngle = ShuffleboardValue.create(pivotAngle, 
                Intake.class.getSimpleName()+"/"+name()+"/Intake Dropdown Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.intakeSpeeds = ShuffleboardValue.create(intakeSpeeds, 
            Intake.class.getSimpleName()+"/"+name()+"/Intake Speed", "Misc")
                .withSize(1, 3)
                .build();
        }

        // This constructor lets you get it from another value
        private Value(Value copyValue) {
            // Value value = copyValue;
            this.pivotAngle = ShuffleboardValue.create(copyValue.getAngle(), 
                Intake.class.getSimpleName()+"/"+name()+"/Pivot Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.intakeSpeeds = ShuffleboardValue.create(copyValue.getIntakeSpeeds(), 
                Intake.class.getSimpleName()+"/"+name()+"/Intake Speed", "Misc")
                .withSize(1, 3)
                .build();
        }

        public double getAngle() {
            return pivotAngle.get();
        }
        public double getIntakeSpeeds() {
            return intakeSpeeds.get();
        }
    }

    private final IntakeDropDownAbsolute dropDown;
    private final IntakeWheel intakeWheel;
    
    private Value position = Value.START;
    private final ShuffleboardValue<String> positionWriter = ShuffleboardValue
        .create(position.name(), "Current Intake Position", "Misc")
        .withSize(1, 3)
        .build();

    public Intake(IntakeDropDownAbsolute dropDown,
        IntakeWheel intakeWheel) {
        this.dropDown = dropDown;
        this.intakeWheel = intakeWheel;
        setStartPos(Value.START);
    }

    // private void logPosition(Value targetPosition) {
    //     position = targetPosition;
    //     positionWriter.write(position.name());
    //     // positionWriter.write(targetPosition.name());
    // }

    public Value getPosition() {
        return position;
    }

       private void setStartPos(Value pos){
        dropDown.setTargetPosition(pos.getAngle());
        intakeWheel.setTargetPosition(pos.getIntakeSpeeds());
       }

    
    public Command setPositionCommand(Value targetPosition) {
        position = targetPosition;
        positionWriter.write(position.name());
        // System.out.println("setting command."+ targetPosition.name());
        return Commands.sequence(
            switch (targetPosition) {
                case OUTTAKE -> 
                    new SequentialCommandGroup(
                        dropDown.runOnce(() -> dropDown.setTargetPosition((targetPosition.getAngle()))),
                        new WaitCommand(1),
                        intakeWheel.runOnce(() -> intakeWheel.setTargetPosition(targetPosition.getIntakeSpeeds()))
                    );
                case OUTTAKE_AMP ->
                    new SequentialCommandGroup(
                        dropDown.runOnce(() -> dropDown.setTargetPosition((targetPosition.getAngle()))),
                        new WaitCommand(1.3),//.7
                        intakeWheel.runOnce(() -> intakeWheel.setTargetPosition(targetPosition.getIntakeSpeeds()))
                    );
                default -> 
                    new ParallelCommandGroup(
                        dropDown.runOnce(() -> dropDown.setTargetPosition((targetPosition.getAngle()))),
                        intakeWheel.runOnce(() -> intakeWheel.setTargetPosition(targetPosition.getIntakeSpeeds()))
                    );
            }
        );
        // return SuppliedCommand.create(() -> Commands.sequence(
        //     // Commands.runOnce(() -> logPosition(targetPosition)),
        //     switch (targetPosition) {
        //         // case  START ->
        //         //     new SequentialCommandGroup(
        //         //     );
                
        //         default -> 
        //             new ParallelCommandGroup(
        //                 dropDown.runOnce(() -> dropDown.setTargetPosition(Math.toRadians(targetPosition.getAngle()))),
        //                 intakeWheel.runOnce(() -> intakeWheel.setTargetPosition(targetPosition.getIntakeSpeeds()))
        //             );
        //     }
        // ));
    }
    public Command setGround(){
        return new SequentialCommandGroup(
            dropDown.runOnce(() -> dropDown.setTargetPosition(Value.INTAKE_GROUND.getAngle())),
            intakeWheel.runOnce(() -> intakeWheel.setTargetPosition(Value.INTAKE_GROUND.getIntakeSpeeds()))
        );
    }
    public Command lowerPivotCommand(double lowerNum) {
        return dropDown.runOnce(() -> 
            dropDown.setTargetPosition(dropDown.getTargetPosition() - lowerNum));
    }
    public boolean isElementInClaw(){
        return intakeWheel.isElementIn()&&positionWriter.get()==Intake.Value.INTAKE_GROUND.name();
    }

    public IntakeDropDownAbsolute getDropDown(){
        return dropDown;
    }
    public IntakeWheel getIntakeWheel(){
        return intakeWheel;
    }

    public Command setDropIdleMode(IdleMode mode){
        return new InstantCommand(()->dropDown.setDropIdleMode(mode));
    }
}
