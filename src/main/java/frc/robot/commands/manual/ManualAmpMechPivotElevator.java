package frc.robot.commands.manual;

// import java.util.function.Supplier;

// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.DroidRageConstants;
// import frc.robot.subsystems.ampMech.AmpMech;
// public class ManualAmpMechPivotElevator extends Command {
//     private final AmpMech ampMech;
//     private final Supplier<Double> armMove;
    
//     public ManualAmpMechPivotElevator(AmpMech ampMech, Supplier<Double> armMove) {
//         this.ampMech = ampMech;
//         this.armMove = armMove;
        
//         addRequirements(claw.getPivot());
//     }

//     @Override
//     public void initialize() { }

//     @Override
//     public void execute() {
//         double move = -armMove.get();
//         move = DroidRageConstants.squareInput(move);
//         move = DroidRageConstants.applyDeadBand(move);
//         ampMech.manualAmpMechPivot(ampMech.getAmpMechArmTarget() + move * 0.2);
//         ampMech.getArm().setMovingManually(!(move == 0));
//     }

//     @Override
//     public void end(boolean interrupted) {}

//     @Override
//     public boolean isFinished() {
//         return false;
//     }
// }
