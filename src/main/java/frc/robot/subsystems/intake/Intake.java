package frc.robot.subsystems.intake;


import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.SuppliedCommand;
import frc.robot.subsystems.intake.dropDown.IntakeDropDown;
import frc.robot.subsystems.intake.dropDown.IntakeDropDownAbs;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Intake {
    //Can use the Positions to make another Value 
    //that holds 2 Positions to join together like 
    //in Charged Up; Allows for you to different 
    //Position Based on game element
    //170-340
    public enum Value {
        START(330,0),

        //IntakePos
        INTAKE_GROUND((300),-10000),
        INTAKE_HUMAN((280),1000),//INTAKE_GROUND

        SHOOTER_HOLD(0,0),//Ready to give Note to shooter, but not doing it
        SHOOTER_TRANSFER(0,10000),//Giving Note to Shooter
       

        HOLD(0, 60),
        OUTTAKE(0,0)
        // (HOLD.getElevatorInches(),HOLD.getIntakeSpeeds(), HOLD.getPivotDegrees()),
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

    private final IntakeDropDownAbs dropDown;
    private final IntakeWheel intakeWheel;
    
    private Value position = Value.START;
    private final ShuffleboardValue<String> positionWriter = ShuffleboardValue
        .create(position.name(), "Current Intake Position", "Misc")
        .withSize(1, 3)
        .build();

    public Intake(IntakeDropDownAbs dropDown,
        IntakeWheel intakeWheel) {
        this.dropDown = dropDown;
        this.intakeWheel = intakeWheel;
    }

    private void logPosition(Value targetPosition) {
        position = targetPosition;
        positionWriter.write(position.name());
        // positionWriter.write(targetPosition.name());
    }

    public Value getPosition() {
        return position;
    }

    public Command setPositionCommand(Value targetPosition) {
        logPosition(targetPosition);
        System.out.println("setting command."+ targetPosition.name());
        return Commands.sequence(
            switch (targetPosition) {
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
        return intakeWheel.isElementIn();
    }

    public IntakeDropDown getDropDown(){
        return dropDown;
    }
    public IntakeWheel getIntakeWheel(){
        return intakeWheel;
    }

}
