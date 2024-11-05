package frc.robot.utility;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import frc.robot.subsystems.drive.SwerveDrive;

/**
 * Uses a profiled PID Controller to quickly turn the robot to a specified angle. Once the robot is
 * within a certain tolerance of the goal angle, a PID controller is used to hold the robot at that
 * angle.
 */
public class RotationController {
    SwerveDrive drive;
    PIDController controller;
    PIDController holdController;
    Constraints constraints;

    // @AutoLogOutput(key = "Swerve/RotationController/Output")
    double calculatedValue = 0;

    double feedbackSetpoint;
    double tolerance = (Math.PI / 720);

    public RotationController(SwerveDrive drive) {
        this.drive = drive;
        // config = swerve.config;
        // constraints = new Constraints(config.maxAngularVelocity, config.maxAngularAcceleration);
        controller =
                new PIDController(
                        0,
                        0,
                        0);

        controller.enableContinuousInput(-Math.PI, Math.PI);
        controller.setTolerance(tolerance * 2);

        // These are currently magic number and need to be put into SwerveConfig
        holdController =
                new PIDController(
                        10, 0,
                        0); // TODO: these probably have to be found again; most likely why robot
        // rotation is slightly oscillating in heading lock

        holdController.enableContinuousInput(-Math.PI, Math.PI);
        holdController.setTolerance(tolerance);

        calculatedValue = 0;
        feedbackSetpoint = 0.35;
    }

    public double calculate(double goalRadians) {
        double measurement = drive.getRotation2d().getRadians();
        calculatedValue = controller.calculate(measurement, goalRadians);
        // RobotTelemetry.print(
        //         "RotationControllerOutput: "
        //                 + calculatedValue
        //                 + " Measure: "
        //                 + measurement
        //                 + " Goal: "
        //                 + goalRadians
        //                 + " max: "
        //                 + config.maxAngularVelocity);
        if (atSetpoint()) {
            // return calculatedValue; // calculateHold(goalRadians);
            return calculatedValue = 0; // calculateHold(goalRadians);
        } else {
            return calculatedValue;
        }
    }

    public double calculateHold(double goalRadians) {
        double calculatedValue =
                holdController.calculate(drive.getRotation2d().getRadians(), goalRadians);
        return calculatedValue;
    }

    public boolean atSetpoint() {
        return controller.atSetpoint();
    }

    public boolean atFeedbackSetpoint() {
        return Math.abs(calculatedValue) <= feedbackSetpoint;
    }

    public boolean atHoldSetpoint() {
        return holdController.atSetpoint();
    }

    public void reset() {
        // controller.
        // controller.reset(drive.getRotation2d().getRadians());
        holdController.reset();
    }

    public void updatePID(double kP, double kI, double kD) {
        controller.setPID(kP, kI, kD);
    }

    // public void setLaunchPID() {
    //     controller.setPID(
    //             launchingKPRotationController,
    //             launchingKIRotationController,
    //             launchingKDRotationController);
    // }

    // public void setConfigPID() {
    //     controller.setPID(
    //             config.kPRotationController,
    //             config.kIRotationController,
    //             config.kDRotationController);
    // }
}