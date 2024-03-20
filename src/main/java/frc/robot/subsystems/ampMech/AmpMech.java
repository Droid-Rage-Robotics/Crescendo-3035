package frc.robot.subsystems.ampMech;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.SuppliedCommand;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class AmpMech {
    //Can use the Positions to make another Value 
    //that holds 2 Positions to join together like 
    //in Charged Up; Allows for you to different 
    //Position Based on game element
    public enum Value {
        START(0,297,0),

        INTAKE_SHOOTER(5,64,0.7),
        INTAKE_HUMAN(10,132,-.7),
       
        AUTO_AMP(0,89,0),
        AMP(0,89,-.7),
        TRAP(0,90,0),

        HOLD(0,65.5,60),
        CLIMB(16.6,0, 0),

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
    // private final AmpMechArmAbsolute arm;
    // private final PowerAmpMechIntake intake;
    private Value position = Value.START;
    private final ShuffleboardValue<String> positionWriter = ShuffleboardValue
        .create(position.name(), "Current Arm Position", "Misc")
        .withSize(1, 3)
        .build();

    public AmpMech(AmpMechElevator elevator
        // AmpMechArmAbsolute arm,
        // PowerAmpMechIntake intake
        ) {
        this.elevator = elevator;
        // this.arm = arm;
        // this.intake = intake;

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
        // intake.setTargetPosition(pos.getIntakeSpeeds());
        // arm.setTargetPosition(pos.getArmDegrees());
    }

    public Command setPositionCommand(Value targetPosition) {
        return SuppliedCommand.create(() -> Commands.sequence(
            Commands.runOnce(() -> logPosition(targetPosition)),
            switch (targetPosition) {
                // // case  AMP,TRAP ->
                // //     new SequentialCommandGroup(
                // //         new ParallelCommandGroup(
                // //             intake.runOnce(() -> intake.setTargetPosition(targetPosition.getIntakeSpeeds()))
                // //         ),
                // //         Commands.waitSeconds(0.5),
                // //         elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches()))
                // //     );
                // // case INTAKE_SHOOTER -> new SequentialCommandGroup(
                // //     new ParallelCommandGroup(
                // //         elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches()))
                // //         // arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees()))
                        
                // //     ),
                // //      new WaitCommand(1),
                // //     new WaitCommand(.7),
                // //     new ParallelCommandGroup(
                // //         // intake.runOnce(() -> intake.setTargetPosition(targetPosition.getIntakeSpeeds())))
                // // );
                // case HOLD -> new SequentialCommandGroup(
                    
                //     // new ParallelCommandGroup(
                //     //     elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches()))
                //     //     // arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees())),
                //     //     // intake.runOnce(() -> intake.setTargetPosition(targetPosition.getIntakeSpeeds()))
                //     // ),
                //      new WaitCommand(1)
                //     // new WaitCommand(.7)
                        
                // );
                // case  AMP->//,TRAP ->
                //     new SequentialCommandGroup(
                //         new ParallelCommandGroup(
                //             // arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees()))
                //         ),
                //         Commands.waitSeconds(0.5),
                //         elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches()))
                //         // intake.runOnce(()->intake.setTargetPosition(targetPosition.getIntakeSpeeds()))
                //     );
                case  START ->
                new SequentialCommandGroup(
                    // intake.runOnce(()->intake.setTargetPosition(targetPosition.getIntakeSpeeds())),
                    new WaitCommand(1),
                    new ParallelCommandGroup(
                        // arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees()))
                    ),
                    Commands.waitSeconds(0.5),
                    elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches()))
                    
                );              

                default -> 
                    new ParallelCommandGroup(
                        elevator.runOnce(() -> elevator.setTargetPosition(targetPosition.getElevatorInches()))
                        // intake.runOnce(() -> intake.setTargetPosition(targetPosition.getIntakeSpeeds())),
                        // arm.runOnce(()->arm.setTargetPosition(targetPosition.getArmDegrees()))
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
