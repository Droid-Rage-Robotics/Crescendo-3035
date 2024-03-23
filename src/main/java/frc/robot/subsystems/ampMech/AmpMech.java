package frc.robot.subsystems.ampMech;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.SuppliedCommand;
import frc.robot.subsystems.ampMech.ampMechArm.AmpMechArmAbsolute;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class AmpMech {
    //Can use the Positions to make another Value 
    //that holds 2 Positions to join together like 
    //in Charged Up; Allows for you to different 
    //Position Based on game element
    public enum Value {
        START(0,147,0),

        INTAKE_SHOOTER(0,140,20),
        // INTAKE_HUMAN(10,132,INTAKE_SHOOTER.getIntakeSpeeds()),
        AMP(20,220,33),
        HOLD_AMP(AMP.getElevatorInches(),220,INTAKE_SHOOTER.getIntakeSpeeds()),

        AUTO_AMP(AMP.getElevatorInches(),AMP.getArmDegrees(),AMP.getIntakeSpeeds()),
        TRAP(36.5,150,33),

        HOLD(0,180,INTAKE_SHOOTER.getIntakeSpeeds()),
        CLIMB(TRAP.getElevatorInches(),HOLD.getArmDegrees(), 0),
        SHOOT(0,216,0)

        // (HOLD.getElevatorInches(),HOLD.getIntakeSpeeds(), HOLD.getPivotDegrees()),
        ;

        private final ShuffleboardValue<Double> elevatorInches;
        private final ShuffleboardValue<Double> armAngle;
        private final ShuffleboardValue<Double> intakeSpeeds;
        

        private Value(double elevatorInches, double armAngle, double intakeSpeeds) {
            this.elevatorInches = ShuffleboardValue.create(elevatorInches, 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/ClawElevator (Inches)", "Misc")
                .withSize(1, 3)
                .build();
            this.armAngle = ShuffleboardValue.create(armAngle, 
                AmpMechElevator.class.getSimpleName()+"/"+name()+"/Arm Angle", "Misc")
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
        public double getArmDegrees() {
            return armAngle.get();
        }
    }

    private final AmpMechElevator elevator;
    private final AmpMechArmAbsolute arm;
    private final AmpMechIntake intake;
    private Value position = Value.START;
    private final ShuffleboardValue<String> positionWriter = ShuffleboardValue
        .create(position.name(), "Current Arm Position", "Misc")
        .withSize(1, 3)
        .build();

    public AmpMech(AmpMechElevator elevator,
        AmpMechArmAbsolute arm,
        AmpMechIntake intake
        ) {
        this.elevator = elevator;
        this.arm = arm;
        this.intake = intake;

        // setPositionCommand(Value.START);
        setStartPos();
    }

    private void logPosition(Value targetPosition) {
        position = targetPosition;
        positionWriter.write(position.name());
        // positionWriter.write(targetPosition.name());
    }

    public Value getPosition() {
        return position;
    }

    public void setStartPos() {
        elevator.setTargetPosition(Value.START.getElevatorInches());
        intake.setTargetPosition(Value.START.getIntakeSpeeds());
        arm.setTargetPosition(Value.START.getArmDegrees());
    }

    public Command setPositionCommand(Value targetPosition) {
        return SuppliedCommand.create(() -> Commands.sequence(
            Commands.runOnce(() -> logPosition(targetPosition)),
            switch (targetPosition) {
                case INTAKE_SHOOTER ->
                    new SequentialCommandGroup(
                        new ParallelCommandGroup(
                            elevator.runOnce(()->elevator.setTargetPosition(targetPosition.getElevatorInches())),
                            arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees()))
                        ),
                        new WaitCommand(1.1),
                        intake.runOnce(()->intake.setTargetPosition(targetPosition.getIntakeSpeeds()))
                    );
                default -> 
                    new ParallelCommandGroup(
                        elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches())),
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
    // public Command lowerPivotCommand(double lowerNum) {
    //     return pivot.runOnce(() -> 
    //         pivot.setTargetPosition(pivot.getTargetPosition() - lowerNum));
    // }
    // public Command pushChargeDownCommand() {
    //     return clawPivot.runOnce(() -> 
    //         clawPivot.setTargetPosition(Value.INTAKE_LOW_CUBE.getPivotDegrees()));
    // }
    public boolean isElementInClaw(){
        // return intake.isElementInClaw();
        return false;

    }

    public void manualAmpMechElevator(double move){
        elevator.setTargetPosition(move);
    }
    // public void manualAmpMechPivot(double move){
    //     pivot.setTargetPosition(move);
    // }
    // public void manualAmpMechIntake(double move){
    //     intake.setTargetPosition(move);
    // }
    public double getAmpMechElevatorTarget(){
        return elevator.getTargetPosition();
    }
    // public double getAmpMechPivotTarget(){
    //     return pivot.getTargetPosition();
    // }
    // public double getAmpMechIntakeTarget(){
    //     return intake.getTargetPosition();
    // }

    public AmpMechElevator getElevator(){
        return elevator;
    }
    // public AmpMechPivot getPivot(){
    //     return pivot;
    // }
    // public PowerAmpMechIntake getIntake(){
    //     return intake;
    // }
    // public AmpMechArmAbsolute getClawArmAbs(){
    //     return arm;
    // }
}
