package frc.robot.commands.manual;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.drive.SwerveDriveConstants;
import frc.robot.subsystems.drive.SwerveDriveConstants.Speed;
import frc.robot.subsystems.drive.SwerveModuleKraken;

public class SwerveDriveTeleop extends Command {
    private final SwerveDrive drive;
    private final Supplier<Double> x, y, turn;
    private final SlewRateLimiter xLimiter, yLimiter, turnLimiter;
    private volatile double xSpeed, ySpeed, turnSpeed;
    private boolean isLimiter;
    private Rotation2d heading;
    // private final Trigger rightBumper;//, aResetButton;

    public SwerveDriveTeleop(SwerveDrive drive,
            Supplier<Double> x, Supplier<Double> y, 
            Supplier<Double> turn, CommandXboxController driver, boolean isLimiter) {
        this.drive = drive;
        this.x = x;
        this.y = y;
        this.turn = turn;

        this.xLimiter = new SlewRateLimiter(SwerveDriveConstants.SwerveDriveConfig.MAX_ACCELERATION_UNITS_PER_SECOND.get());
        this.yLimiter = new SlewRateLimiter(SwerveDriveConstants.SwerveDriveConfig.MAX_ACCELERATION_UNITS_PER_SECOND.get());
        this.turnLimiter = new SlewRateLimiter(SwerveDriveConstants.SwerveDriveConfig.MAX_ANGULAR_ACCELERATION_UNITS_PER_SECOND.get());
        this.isLimiter = isLimiter;
        // drive.setSpeed(Speed.NORMAL);
        // drive.setSpeed(Speed.SLOW);
        driver.rightBumper().whileTrue(drive.setSpeed(Speed.SLOW))
            .whileFalse(drive.setSpeed(Speed.NORMAL));
                        // .onFalse(drive.setSpeed(Speed.NORMAL));

        // this.aResetButton = aResetButton;
        driver.b().onTrue(drive.setYawCommand(0));
        driver.povUp().onTrue(drive.setYawCommand(0));
        driver.povDown().onTrue(drive.setYawCommand(-180));//180
        driver.povLeft().onTrue(drive.setYawCommand(90));
        driver.povRight().onTrue(drive.setYawCommand(-90));

        
        addRequirements(drive);
    }

    @Override
    public void initialize() {    }

    @Override
    public void execute() {
        xSpeed = -y.get();
        ySpeed = -x.get();
        turnSpeed = -turn.get();


        // Square inputs
        if (drive.isSquaredInputs()) {
            xSpeed = DroidRageConstants.squareInput(xSpeed);
            ySpeed = DroidRageConstants.squareInput(ySpeed);
            turnSpeed = DroidRageConstants.squareInput(turnSpeed);
        }

        // Apply Field Oriented
        if (drive.isFieldOriented()) {
            double modifiedXSpeed = xSpeed;
            double modifiedYSpeed = ySpeed;

            
            heading = drive.getRotation2d();

            // heading.rotateBy(
            //     Rotation2d.fromDegrees(
            //         switch (DriverStation.getAlliance()) {
            //             case Blue->00;
            //             case Red->180;
            //             case Invalid->0;
            //         }
            //     )
            // );

            modifiedXSpeed = xSpeed * heading.getCos() + ySpeed * heading.getSin();
            modifiedYSpeed = -xSpeed * heading.getSin() + ySpeed * heading.getCos();

            xSpeed = modifiedXSpeed;
            ySpeed = modifiedYSpeed;
        }

        // //Apply tipping
        // double xTilt = drive.getRoll();
        // double yTilt = drive.getPitch();

        // switch (drive.getTippingState()) {//Need to take into account on the direction of the tipp
        //     case ANTI_TIP:
        //         if (Math.abs(xTilt) > antiTipX.getPositionTolerance())
        //             xSpeed = antiTipX.calculate(xSpeed, xTilt);
        //         if (Math.abs(yTilt) > antiTipY.getPositionTolerance())
        //             ySpeed = antiTipY.calculate(ySpeed, yTilt);
        //         break;
        //     case AUTO_BALANCE:
        //     case AUTO_BALANCE_ANTI_TIP:
        //         if (Math.abs(xTilt) > autoBalanceX.getPositionTolerance())
        //             xSpeed = autoBalanceX.calculate(xSpeed, xTilt);
        //         // if (Math.abs(yTilt) > autoBalanceY.getPositionTolerance())
        //         //     ySpeed = autoBalanceY.calculate(ySpeed, yTilt);
        //         break;
        //     case NO_TIP_CORRECTION:
        //         break;            
        // }

        // Apply deadband
        if (Math.abs(xSpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) xSpeed = 0;
        if (Math.abs(ySpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) ySpeed = 0;
        if (Math.abs(turnSpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) turnSpeed = 0;

        // Smooth driving and apply speed
        if(isLimiter){
            // xSpeed = 
            //     xLimiter.calculate(xSpeed) * 
            //     xSpeed *
            //     SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND * 
            //     drive.getTranslationalSpeed();
            // ySpeed = 
            //     yLimiter.calculate(ySpeed) *
            //     ySpeed *
            //     SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND *
            //     drive.getTranslationalSpeed();
            // turnSpeed = 
            //     turnLimiter.calculate(turnSpeed) * 
            //     turnSpeed *
            //     SwerveDriveConstants.SwerveDriveConfig.PHYSICAL_MAX_ANGULAR_SPEED_RADIANS_PER_SECOND.get() * 
            //     drive.getAngularSpeed();
        } else {
            xSpeed = 
                xSpeed *
                SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND * 
                drive.getTranslationalSpeed();
            ySpeed = 
                ySpeed *
                SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND *
                drive.getTranslationalSpeed();
            turnSpeed = 
                turnSpeed *
                SwerveDriveConstants.SwerveDriveConfig.PHYSICAL_MAX_ANGULAR_SPEED_RADIANS_PER_SECOND.get() * 
                drive.getAngularSpeed();
        }
        

        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turnSpeed);

        SwerveModuleState[] states = SwerveDrive.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        drive.setModuleStates(states);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
