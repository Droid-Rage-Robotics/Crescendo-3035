package frc.robot.subsystems.claw;


import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.SuppliedCommand;
import frc.robot.subsystems.claw.clawArm.ClawArmAbs;
import frc.robot.subsystems.claw.clawPivot.ClawPivot;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class Claw {
    //Can use the Positions to make another Value 
    //that holds 2 Positions to join together like 
    //in Charged Up; Allows for you to different 
    //Position Based on game element
    public enum Value {
        START(0.,235,0,0),

        INTAKE_SHOOTER(0,0,0,0),
        INTAKE_HUMAN(.8,160,0,0),
       
        AUTO_AMP(0,0,0,0),
        AMP(0,0,0,0),
        TRAP(0,0,0,0),

        HOLD(0,0,0, 60),
        // (HOLD.getElevatorInches(),HOLD.getIntakeSpeeds(), HOLD.getPivotDegrees()),
        ;

        private final ShuffleboardValue<Double> elevatorInches;
        private final ShuffleboardValue<Double> pivotAngle;
        private final ShuffleboardValue<Double> armAngle;
        private final ShuffleboardValue<Double> intakeSpeeds;
        

        private Value(double elevatorInches, double armAngle, double pivotAngle, double intakeSpeeds) {
            this.elevatorInches = ShuffleboardValue.create(elevatorInches, 
                ClawElevator.class.getSimpleName()+"/"+name()+"/ClawElevator (Inches)", "Misc")
                .withSize(1, 3)
                .build();
            this.armAngle = ShuffleboardValue.create(armAngle, 
                ClawElevator.class.getSimpleName()+"/"+name()+"/Arm Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.pivotAngle = ShuffleboardValue.create(pivotAngle, 
                ClawElevator.class.getSimpleName()+"/"+name()+"/Pivot Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.intakeSpeeds = ShuffleboardValue.create(intakeSpeeds, 
                ClawElevator.class.getSimpleName()+"/"+name()+"/Intake Speed", "Misc")
                .withSize(1, 3)
                .build();
        }

        // This constructor lets you get it from another value
        private Value(Value copyValue) {
            // Value value = copyValue;
            this.elevatorInches = ShuffleboardValue.create(copyValue.getElevatorInches(), 
                ClawElevator.class.getSimpleName()+"/"+name()+"/ClawElevator (Inches)", "Misc")
                .withSize(1, 3)
                .build();
                this.armAngle = ShuffleboardValue.create(copyValue.getArmDegrees(), 
                ClawElevator.class.getSimpleName()+"/"+name()+"/Arm Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.pivotAngle = ShuffleboardValue.create(copyValue.getPivotDegrees(), 
                ClawElevator.class.getSimpleName()+"/"+name()+"/Pivot Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.intakeSpeeds = ShuffleboardValue.create(copyValue.getIntakeSpeeds(), 
                ClawElevator.class.getSimpleName()+"/"+name()+"/Intake Speed", "Misc")
                .withSize(1, 3)
                .build();
        }

        public double getElevatorInches() {
            return elevatorInches.get();
        }

        public double getIntakeSpeeds() {
            return intakeSpeeds.get();
        }

        public double getPivotDegrees() {
            return pivotAngle.get();
        }
        public double getArmDegrees() {
            return armAngle.get();
        }
    }

    private final ClawElevator clawElevator;
    private final ClawArmAbs clawArm;
    private final ClawPivot clawPivot;
    private final PowerClawIntake clawIntake;
    private Value position = Value.START;
    private final ShuffleboardValue<String> positionWriter = ShuffleboardValue
        .create(position.name(), "Current Arm Position", "Misc")
        .withSize(1, 3)
        .build();

    public Claw(ClawElevator clawElevator,
        ClawArmAbs clawArm,
        ClawPivot clawPivot,
        PowerClawIntake clawIntake) {
        this.clawElevator = clawElevator;
        this.clawArm = clawArm;
        this.clawPivot = clawPivot;
        this.clawIntake = clawIntake;
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
        return SuppliedCommand.create(() -> Commands.sequence(
            Commands.runOnce(() -> logPosition(targetPosition)),
            switch (targetPosition) {
                // case  AMP,TRAP ->
                //     new SequentialCommandGroup(
                //         new ParallelCommandGroup(
                //             clawPivot.runOnce(() -> clawPivot.setTargetPosition((targetPosition.getPivotDegrees()))),
                //             clawIntake.runOnce(() -> clawIntake.setTargetPosition(targetPosition.getIntakeSpeeds()))
                //         ),
                //         Commands.waitSeconds(0.5),
                //         clawElevator.runOnce(() -> clawElevator.setTargetPosition(targetPosition.getElevatorInches()))
                //     );
                
                default -> 
                    new ParallelCommandGroup(
                        clawElevator.runOnce(() -> clawElevator.setTargetPosition(targetPosition.getElevatorInches())),
                        clawPivot.runOnce(() -> clawPivot.setTargetPosition((targetPosition.getPivotDegrees()))),
                        clawIntake.runOnce(() -> clawIntake.setTargetPosition(targetPosition.getIntakeSpeeds())),
                        clawArm.runOnce(()->clawArm.setTargetPosition(targetPosition.getArmDegrees()))
                    );
            }
        ));
    }

    public Command lowerElevatorCommand() {
        return clawElevator.runOnce(() -> 
            clawElevator.setTargetPosition(clawElevator.getTargetPosition() - 3));
    }
    public Command lowerPivotCommand(double lowerNum) {
        return clawPivot.runOnce(() -> 
            clawPivot.setTargetPosition(clawPivot.getTargetPosition() - lowerNum));
    }
    // public Command pushChargeDownCommand() {
    //     return clawPivot.runOnce(() -> 
    //         clawPivot.setTargetPosition(Value.INTAKE_LOW_CUBE.getPivotDegrees()));
    // }
    public boolean isElementInClaw(){
        return clawIntake.isElementInClaw();

    }

    public void manualClawElevator(double move){
        clawElevator.setTargetPosition(move);
    }
    public void manualClawPivot(double move){
        clawPivot.setTargetPosition(move);
    }
    public void manualClawIntake(double move){
        clawIntake.setTargetPosition(move);
    }
    public double getClawElevatorTarget(){
        return clawElevator.getTargetPosition();
    }
    public double getClawPivotTarget(){
        return clawPivot.getTargetPosition();
    }
    public double getClawIntakeTarget(){
        return clawIntake.getTargetPosition();
    }

    public ClawElevator getClawElevator(){
        return clawElevator;
    }
    public ClawPivot getClawPivot(){
        return clawPivot;
    }
    public PowerClawIntake getClawIntake(){
        return clawIntake;
    }
    public ClawArmAbs getClawArmAbs(){
        return clawArm;
    }
}
