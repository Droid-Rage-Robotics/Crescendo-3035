package frc.robot.commands.autos;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.drive.SwerveDriveConstants;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;

public class AutoChooser {
    public static final SendableChooser<Command> autoChooser = new SendableChooser<Command>();

    public AutoChooser(
        SwerveDrive drive, Intake intake, Shooter shooter, AmpMech ampMech//, Claw claw, Climb climb, Light light
    ) {
        //Put Named Commands HERE
        NamedCommands.registerCommand("shoot",
            intake.setPositionCommand(Intake.Value.AUTO_SHOOTER_TRANSFER)
        );
        NamedCommands.registerCommand("intake",
             new SequentialCommandGroup(
                intake.setPositionCommand(Intake.Value.AUTO_INTAKE_GROUND)
                // new WaitCommand(.3)
             ));
        NamedCommands.registerCommand("pickUp",
            // new SetIntakeAndShooter(intake, Intake.Value.SHOOTER_HOLD, shooter, Shooter.ShooterSpeeds.AUTO_SPEAKER_SHOOT)
            new SequentialCommandGroup(
                intake.setPositionCommand(Intake.Value.SHOOTER_HOLD)
                )
            // );
            );

        //NO Use
        // NamedCommands.registerCommand("shootPreload", shooter.setTargetVelocity(ShooterSpeeds.SPEAKER_SHOOT));//should be done before following
        // NamedCommands.registerCommand("transfer", intake.setPositionCommand(Intake.Value.START));


        createAutoBuilder(drive);
        ComplexWidgetBuilder.create(autoChooser, "Auto Chooser", "Misc")
            .withSize(1, 3);
        autoChooser.addOption("NothingAuto", new InstantCommand());
        // autoChooser.
        addAutos(drive, intake, shooter, ampMech);
        // addTuningAuto(drive);
        // autoChooser.addOption("test", BasicAutos.test(drive));
        autoChooser.addOption("shootandMiddle", Autos.onePlusMiddle(drive,intake,shooter));//WORKS - DO
        autoChooser.addOption("ShootHumanMid Pickup", Autos.onePlusHUuman(drive,intake,shooter));//Works
        autoChooser.addOption("ShootHumanPlusmIddle", Autos.onePlusHumanAndMidle(drive,intake,shooter));//work ish
        autoChooser.addOption("ShootMiddleAndHuman", Autos.onePlusMiddleAndHuman(drive,intake,shooter));//work ish
        //
        autoChooser.addOption("AMP", Autos.amp(drive,intake,shooter));//work
        autoChooser.setDefaultOption("RIGHT(non-human player)",  BasicAutos.right(drive,intake, shooter));//work
        autoChooser.addOption("Center Appreciate",  Autos.centerAppreciate(drive,intake, shooter));//maybe to tesgt
        autoChooser.addOption("Shoot Out Center",  Autos.shootOut(drive,intake, shooter));//maybe to tesgt

    }
    public static Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public static void addAutos(SwerveDrive drive, Intake intake, Shooter shooter, AmpMech ampMech){
        // autoChooser.addOption("R1+F1+ParkRed", Autos.onePlusF1PlusParkRed(drive, intake, shooter));
        // autoChooser.addOption("L1+F3+ParkBLue", 
        //     Autos.onePlusF1PlusParkBlue(drive, intake, shooter));
        // autoChooser.addOption("L1+F3ParkBlue(OnlyPickup)", 
        //     Autos.onePlusF1ParkBlue(drive, intake, shooter));
        // autoChooser.addOption("L1+F1Red(OnlyPickUp)", Autos.onePlusF1ParkRed(drive, intake, shooter));
        // autoChooser.addOption("TEST", Autos.test(drive, intake, shooter));


        // autoChooser.addOption("One Plus Three", 
        //     Autos.onePlusThree(drive, intake, shooter,ampMech));
        // autoChooser.addOption("One Plus Two", 
        //     Autos.onePlusTwo(drive, intake, shooter,ampMech));
        // autoChooser.addOption("One Plus FULL Seperate", //default??
        //     Autos.onePlusFullSeperate(drive, intake, shooter));
// autoChooser.addOption("One Plus 2 Seperate", 
//             Autos.onePlusFullSeperate(drive, intake, shooter));
        autoChooser.addOption("ShootPlusPark", 
            BasicAutos.shootPLusPark(drive,intake,shooter,0));//Works
        autoChooser.addOption("ShootPlusWait6Park", 
            BasicAutos.shootPLusPark(drive,intake,shooter,6));//Works
        autoChooser.addOption("OnlyShoot", BasicAutos.shoot(intake,shooter));//Works
        autoChooser.addOption("SHOOTPLUSTURN(Only the human player side)", 
            BasicAutos.shootPLusTurnParkHuman(drive,intake, shooter, 2));//Work
        // autoChooser.addOption("SHOOTPLUSTURN(NON human player side)", 
        //     Autos.shootPLusTurnParkNonHUMAN(drive,intake, shooter, 2));//No Work
        autoChooser.addOption("OUT(San Antonio Playoff))", 
            BasicAutos.out(drive,intake, shooter));
        
// autoChooser.addOption("LEFT(human player)", 
//             BasicAutos.left(drive,intake, shooter));
        // autoChooser.addOption("ToCenter", BasicAutos.toCenter(drive, intake, shooter));
        // autoChooser.addOption("TEST Basic autos", BasicAutos.test(drive));

        // autoChooser.addOption("test", Autos.test(drive, intake, shooter,ampMech));
        
        // autoChooser.addOption("shootandMiddle", Autos.onePlusMiddle(drive,intake,shooter));
    }
    
    public static void addTuningAuto(SwerveDrive drive){
        autoChooser.addOption(TuningAutos.backTest(drive).toString()+"BackwardTest", TuningAutos.backTest(drive));
        autoChooser.addOption("ForwardTest", TuningAutos.forwardTest(drive));
        // autoChooser.addOption("ForwardThenTurnTest", TuningAutos.forwardThenTurnTest(drive));
        autoChooser.addOption("TurnTest", TuningAutos.turnTest(drive));
        autoChooser.addOption("SplineTest", TuningAutos.splineTest(drive));
        // autoChooser.addOption("LineToLinearTest", TuningAutos.lineToLinearTest(drive));
        autoChooser.addOption("StrafeRight", TuningAutos.strafeRight(drive));
        autoChooser.addOption("StrafeLeft", TuningAutos.strafeLeft(drive));
        // autoChooser.addOption("ForwardAndBack", TuningAutos.forwardAndBackTest(drive));
    }

    public static void createAutoBuilder(SwerveDrive drive){
        // AutoBuilder
        AutoBuilder.configureHolonomic(
            drive::getPose, // Robot pose supplier
            drive::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
            drive::getSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            // drive::drive, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
            drive::setFeedforwardModuleStates,//To Use Feedforward
            new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
                new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KP.get(), 
                    SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KI.get(), 
                    SwerveDriveConstants.SwerveDriveConfig.TRANSLATIONAL_KD.get()),  // Translation PID constants
                new PIDConstants(SwerveDriveConstants.SwerveDriveConfig.THETA_KP.get(), 
                    SwerveDriveConstants.SwerveDriveConfig.THETA_KI.get(), 
                    SwerveDriveConstants.SwerveDriveConfig.THETA_KD.get()),  // Rotation PID constants
                4.5, // Max module speed, in m/s 4.5
                0.4, // Drive base radius in meters. Distance from robot center to furthest module.
                new ReplanningConfig() // Default path replanning config. See the API for the options here; Ba
            ),
            () -> {
                // Boolean supplier that controls when the path will be mirrored for the red alliance
                // This will flip the path being followed to the red side of the field.
                // THE ORIGIN WILL REMAIN ON THE BLUE SIDE
                //Is this working?
                var alliance = DriverStation.getAlliance();
                if (alliance.isPresent()) {
                    return alliance.get() == DriverStation.Alliance.Red;
                }
                return false;//To Test-true
            },
            drive // Reference to this subsystem to set requirements
        );
    }

    // public setDefaultOption
}
