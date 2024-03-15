package frc.robot.subsystems.ampMech;


import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.SuppliedCommand;
import frc.robot.subsystems.ampMech.ampMechArm.AmpMechArmAbsolute;
import frc.robot.subsystems.ampMech.ampMechPivot.AmpMechPivot;
import frc.robot.subsystems.ampMech.ampMechPivot.AmpMechPivotAbsolute;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class AmpMech {
    //Can use the Positions to make another Value 
    //that holds 2 Positions to join together like 
    //in Charged Up; Allows for you to different 
    //Position Based on game element
    public enum Value {
        START(0,297,305,0),

        INTAKE_SHOOTER(5,64,292,0.7),
        INTAKE_HUMAN(10,132,164,-.7),
       
        AUTO_AMP(0,89,87,0),
        AMP(0,89,87,-.7),
        TRAP(0,90,0,0),

        HOLD(0,65.5,75, 60),
        // (HOLD.getElevatorInches(),HOLD.getIntakeSpeeds(), HOLD.getPivotDegrees()),
        ;

        private final ShuffleboardValue<Double> elevatorInches;
        private final ShuffleboardValue<Double> pivotAngle;
        private final ShuffleboardValue<Double> armAngle;
        private final ShuffleboardValue<Double> intakeSpeeds;
        

        private Value(double elevatorInches, double armAngle, double pivotAngle, double intakeSpeeds) {
            this.elevatorInches = ShuffleboardValue.create(elevatorInches, 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/ClawElevator (Inches)", "Misc")
                .withSize(1, 3)
                .build();
            this.armAngle = ShuffleboardValue.create(armAngle, 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/Arm Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.pivotAngle = ShuffleboardValue.create(pivotAngle, 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/Pivot Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.intakeSpeeds = ShuffleboardValue.create(intakeSpeeds, 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/Intake Speed", "Misc")
                .withSize(1, 3)
                .build();
        }

        // This constructor lets you get it from another value
        private Value(Value copyValue) {
            // Value value = copyValue;
            this.elevatorInches = ShuffleboardValue.create(copyValue.getElevatorInches(), 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/ClawElevator (Inches)", "Misc")
                .withSize(1, 3)
                .build();
                this.armAngle = ShuffleboardValue.create(copyValue.getArmDegrees(), 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/Arm Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.pivotAngle = ShuffleboardValue.create(copyValue.getPivotDegrees(), 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/Pivot Angle", "Misc")
                .withSize(1, 3)
                .build();
            this.intakeSpeeds = ShuffleboardValue.create(copyValue.getIntakeSpeeds(), 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/Intake Speed", "Misc")
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

    private final AmpMechElevator elevator;
    private final AmpMechArmAbsolute arm;
    private final AmpMechPivotAbsolute pivot;
    private final PowerAmpMechIntake intake;
    private Value position = Value.START;
    private final ShuffleboardValue<String> positionWriter = ShuffleboardValue
        .create(position.name(), "Current Arm Position", "Misc")
        .withSize(1, 3)
        .build();

    public AmpMech(AmpMechElevator elevator,
        AmpMechArmAbsolute arm,
        AmpMechPivotAbsolute pivot,
        PowerAmpMechIntake intake) {
        this.elevator = elevator;
        this.arm = arm;
        this.pivot = pivot;
        this.intake = intake;

        // setPositionCommand(Value.START);
        setStartPos(Value.START);
    }

    private void logPosition(Value targetPosition) {
        position = targetPosition;
        positionWriter.write(position.name());
        // positionWriter.write(targetPosition.name());
    }

    public Value getPosition() {
        return position;
    }

    public void setStartPos(Value pos) {
        elevator.setTargetPosition(pos.getElevatorInches());
        pivot.setTargetPosition(pos.getPivotDegrees());
        intake.setTargetPosition(pos.getIntakeSpeeds());
        arm.setTargetPosition(pos.getArmDegrees());
    }

    public Command setPositionCommand(Value targetPosition) {
        return SuppliedCommand.create(() -> Commands.sequence(
            Commands.runOnce(() -> logPosition(targetPosition)),
            switch (targetPosition) {
                // case  AMP,TRAP ->
                //     new SequentialCommandGroup(
                //         new ParallelCommandGroup(
                //             pivot.runOnce(() -> pivot.setTargetPosition((targetPosition.getPivotDegrees()))),
                //             intake.runOnce(() -> intake.setTargetPosition(targetPosition.getIntakeSpeeds()))
                //         ),
                //         Commands.waitSeconds(0.5),
                //         elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches()))
                //     );
                case INTAKE_SHOOTER, HOLD -> new SequentialCommandGroup(
                    new ParallelCommandGroup(
                        elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches())),
                        arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees())),
                        pivot.runOnce(() -> pivot.setTargetPosition((targetPosition.getPivotDegrees())))
                    ),
                    new WaitCommand(.7),
                    new ParallelCommandGroup(
                        // pivot.runOnce(() -> pivot.setTargetPosition((targetPosition.getPivotDegrees()))),
                        intake.runOnce(() -> intake.setTargetPosition(targetPosition.getIntakeSpeeds())))
                );
                case  AMP->//,TRAP ->
                    new SequentialCommandGroup(
                        new ParallelCommandGroup(
                            pivot.runOnce(() -> pivot.setTargetPosition((targetPosition.getPivotDegrees()))),
                            arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees()))
                        ),
                        Commands.waitSeconds(0.5),
                        elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches())),
                        intake.runOnce(()->intake.setTargetPosition(targetPosition.getIntakeSpeeds()))
                    );
                default -> 
                    new ParallelCommandGroup(
                        elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches())),
                        pivot.runOnce(() -> pivot.setTargetPosition((targetPosition.getPivotDegrees()))),
                        intake.runOnce(() -> intake.setTargetPosition(targetPosition.getIntakeSpeeds())),
                        arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees()))
                    );
            }
        ));
    }

    public Command lowerElevatorCommand() {
        return elevator.runOnce(() -> 
            elevator.setTargetPosition(elevator.getTargetPosition() - 3));
    }
    public Command lowerPivotCommand(double lowerNum) {
        return pivot.runOnce(() -> 
            pivot.setTargetPosition(pivot.getTargetPosition() - lowerNum));
    }
    // public Command pushChargeDownCommand() {
    //     return clawPivot.runOnce(() -> 
    //         clawPivot.setTargetPosition(Value.INTAKE_LOW_CUBE.getPivotDegrees()));
    // }
    public boolean isElementInClaw(){
        return intake.isElementInClaw();

    }

    public void manualAmpMechElevator(double move){
        elevator.setTargetPosition(move);
    }
    public void manualAmpMechPivot(double move){
        pivot.setTargetPosition(move);
    }
    public void manualAmpMechIntake(double move){
        intake.setTargetPosition(move);
    }
    public double getAmpMechElevatorTarget(){
        return elevator.getTargetPosition();
    }
    public double getAmpMechPivotTarget(){
        return pivot.getTargetPosition();
    }
    public double getAmpMechIntakeTarget(){
        return intake.getTargetPosition();
    }

    public AmpMechElevator getElevator(){
        return elevator;
    }
    public AmpMechPivot getPivot(){
        return pivot;
    }
    public PowerAmpMechIntake getIntake(){
        return intake;
    }
    public AmpMechArmAbsolute getClawArmAbs(){
        return arm;
    }
}
