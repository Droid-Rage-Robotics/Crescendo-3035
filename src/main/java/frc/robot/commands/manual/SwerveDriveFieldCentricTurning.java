package frc.robot.commands.manual;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Supplier;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.DroidRageConstants;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.drive.SwerveDriveConstants;
import frc.robot.subsystems.drive.SwerveModule;
// @Deprecated
public class SwerveDriveFieldCentricTurning extends Command {
    private final SwerveDrive drive;
    private final Supplier<Double> leftX,leftY, rightX, rightY;
    private boolean FIELD_CENTRIC_DRIVING;
    private boolean FIELD_CENTRIC_TURNING;

    private volatile double xSpeed, ySpeed, turnSpeed;
    CommandXboxController driver;
    public static double STRAFE_WEIGHT = 1;//Probably 1 instead of 1.1
    public static double MIN_HEADING_P = 0.05;
    public static double MAX_HEADING_P = 1.0;
    public static double STICK_THRESHOLD = 0.05;

    // public SwerveDriveFieldCentricTurning(SwerveDrive drive, CommandXboxController controller,
    //         Supplier<Double> x, Supplier<Double> y, Supplier<Double> turn, Trigger lockTrigger) {
    //     this.drive = drive;
    //     this.x = controller::getLeftX;
    //     this.y = controller::getLeftY;
    //     this.turn = controller::getRightX;
    //     this.lockTrigger = lockTrigger;


    //     addRequirements(drive);
    // }

    // @Override
    // public void initialize() {    }

    // @Override
    // public void execute() {
    //     controller.a().onTrue(drive.setOffsetCommand(drive.getHeading())); //TODO Test Reset
    //     // if (controller.a()) {
    //     //     imu.resetYaw();
    //     // }

    //     controller.rightBumper()
    //         .whileTrue(drive.setSlowSpeed())
    //         .onFalse(drive.setNormalSpeed());
        
    //     // Square inputs
    //     if (drive.isSquaredInputs()) {
    //         xSpeed = DroidRageConstants.squareInput(xSpeed);
    //         ySpeed = DroidRageConstants.squareInput(ySpeed);
    //         turnSpeed = DroidRageConstants.squareInput(turnSpeed);
    //     }
    //     double leftY = -controller.getLeftY(); // Remember, this is reversed!
    //     double leftX = controller.getLeftX() * STRAFE_WEIGHT; // Counteract imperfect strafing
    //     double rightY = -controller.getRightY();
    //     double rightX = -controller.getRightX();


    //     double botHeading = -drive.getHeading();//TODO: Need to check if it comes in Radians or degrees - needs to be radian
    //     // double botHeading = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

    //     // Rotate the movement direction counter to the bot's rotation
    //     Vector2d move2d = new Vector2d(leftX, leftY).rotated(botHeading);

    //     double x = move2d.getX();
    //     double y = move2d.getY();

    //     // Find the angle of turn and rotate it by pi / 2 radians (90 degrees)
    //     Vector2d turn2d = new Vector2d(rightX, rightY);
    //     double stickHeading = turn2d.rotated(Math.PI / 2).angle() - Math.PI;

    //     // Find the lowest of the 3 coterminal angles
    //     double defaultHeadingError = (botHeading - stickHeading); // Default angle
    //     double lowHeadingError = defaultHeadingError - (Math.PI * 2); // Low coterminal angle (360 degrees off from original)
    //     double highHeadingError = defaultHeadingError + (Math.PI * 2); // High coterminal angle (360 degrees off from original)

    //     // Create map of the absolute value of each value to the original value
    //     Map<Double, Double> errorMap = new HashMap<>();
    //     errorMap.put(Math.abs(defaultHeadingError), defaultHeadingError);
    //     errorMap.put(Math.abs(lowHeadingError), lowHeadingError);
    //     errorMap.put(Math.abs(highHeadingError), highHeadingError);

    //     // Find the lowest absolute value key and get the value corresponding to that key
    //     double headingError = errorMap.entrySet().stream().min(Map.Entry.comparingByKey()).get().getValue();

    //     // Create proportional error correction from heading error (The P in PID)
    //     double distance = turn2d.distTo(new Vector2d(0,0));

    //     double headingP = MIN_HEADING_P + ((MAX_HEADING_P - MIN_HEADING_P) * distance);
    //     double turn = headingError * -headingP;

    //     if (distance < STICK_THRESHOLD) turn = 0;

    //     // Denominator is the largest motor power (absolute value) or 1
    //     // This ensures all the powers maintain the same ratio, but only when
    //     // at least one is out of the range [-1, 1]
    //     double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);
    //     double frontLeftPower = (y + x + turn) / denominator;
    //     double backLeftPower = (y - x + turn) / denominator;
    //     double frontRightPower = (y - x - turn) / denominator;
    //     double backRightPower = (y + x - turn) / denominator;

    //     leftFront.setPower(frontLeftPower);
    //     leftRear.setPower(backLeftPower);
    //     rightFront.setPower(frontRightPower);
    //     rightRear.setPower(backRightPower);


    //     xSpeed = -y.get();
    //     ySpeed = -x.get();
    //     turnSpeed = -turn.get();

        

    //     // Apply Field Oriented
    //     if (drive.isFieldOriented()) {
    //         double modifiedXSpeed = xSpeed;
    //         double modifiedYSpeed = ySpeed;

            
    //         Rotation2d heading = drive.getRotation2d();

    //         // heading.rotateBy(
    //         //     Rotation2d.fromDegrees(
    //         //         switch (DriverStation.getAlliance()) {
    //         //             case Blue->00;
    //         //             case Red->180;
    //         //             case Invalid->0;
    //         //         }
    //         //     )
    //         // );

    //         modifiedXSpeed = xSpeed * heading.getCos() + ySpeed * heading.getSin();
    //         modifiedYSpeed = -xSpeed * heading.getSin() + ySpeed * heading.getCos();

    //         xSpeed = modifiedXSpeed;
    //         ySpeed = modifiedYSpeed;
    //     }

    //     // //Apply tipping
    //     // double xTilt = drive.getRoll();
    //     // double yTilt = drive.getPitch();

    //     // switch (drive.getTippingState()) {//Need to take into account on the direction of the tipp
    //     //     case ANTI_TIP:
    //     //         if (Math.abs(xTilt) > antiTipX.getPositionTolerance())
    //     //             xSpeed = antiTipX.calculate(xSpeed, xTilt);
    //     //         if (Math.abs(yTilt) > antiTipY.getPositionTolerance())
    //     //             ySpeed = antiTipY.calculate(ySpeed, yTilt);
    //     //         break;
    //     //     case AUTO_BALANCE:
    //     //     case AUTO_BALANCE_ANTI_TIP:
    //     //         if (Math.abs(xTilt) > autoBalanceX.getPositionTolerance())
    //     //             xSpeed = autoBalanceX.calculate(xSpeed, xTilt);
    //     //         // if (Math.abs(yTilt) > autoBalanceY.getPositionTolerance())
    //     //         //     ySpeed = autoBalanceY.calculate(ySpeed, yTilt);
    //     //         break;
    //     //     case NO_TIP_CORRECTION:
    //     //         break;            
    //     // }

    //     // Apply deadband
    //     if (Math.abs(xSpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) xSpeed = 0;
    //     if (Math.abs(ySpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) ySpeed = 0;
    //     if (Math.abs(turnSpeed) < DroidRageConstants.Gamepad.DRIVER_STICK_DEADZONE) turnSpeed = 0;

    //     // Smooth driving and apply speed
    //     xSpeed = 
    //             xSpeed *
    //             SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND * 
    //             drive.getTranslationalSpeed();
    //         ySpeed = 
    //             ySpeed *
    //             SwerveModuleKraken.Constants.PHYSICAL_MAX_SPEED_METERS_PER_SECOND *
    //             drive.getTranslationalSpeed();
    //         turnSpeed = 
    //             turnSpeed *
    //             SwerveDriveConstants.SwerveDriveConfig.PHYSICAL_MAX_ANGULAR_SPEED_RADIANS_PER_SECOND.get() * 
    //             drive.getAngularSpeed();

    //     ChassisSpeeds chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turnSpeed);

    //     SwerveModuleState[] states = SwerveDrive.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);

    //     // if (lockTrigger.getAsBoolean()) {
    //     //     drive.setModuleStates(new SwerveModuleState[] {
    //     //         new SwerveModuleState(0.01, new Rotation2d(Math.PI / 4)),
    //     //         new SwerveModuleState(0.01, new Rotation2d(-Math.PI / 4)),
    //     //         new SwerveModuleState(0.01, new Rotation2d(-Math.PI / 4)),
    //     //         new SwerveModuleState(0.01, new Rotation2d(Math.PI / 4))
    //     //     });
    //     // }
    //     //TODO: Test
    //     drive.setModuleStates(states);
    // }

    // @Override
    // public void end(boolean interrupted) {
    //     // autoBalanceY.close();
    //     drive.stop();
    // }

    // @Override
    // public boolean isFinished() {
    //     return false;
    // }



        public SwerveDriveFieldCentricTurning(SwerveDrive drive, CommandXboxController driver) {
        this.drive = drive;
        this.driver = driver;
        // this.leftY = driver;
        this.leftY = driver::getLeftY;//-
        this.leftX = driver::getLeftX;//
        this.rightY = driver::getRightY;//-
        this.rightX = driver::getRightX;//-
        
        // double leftY = -dr.left_stick_y; // Remember, this is reversed!
        // double leftX = gamepad1.left_stick_x; // Counteract imperfect strafing
        // double rightY = -gamepad1.right_stick_y;
        // double rightX = -gamepad1.right_stick_x; // TODO: Why is this negative?
        FIELD_CENTRIC_DRIVING = true;
        FIELD_CENTRIC_TURNING = true;
        driver.b().onTrue(drive.setYawCommand(0));
        
        // driver.rightBumper().whileTrue(drive.setSlowSpeed())
        //     .onFalse(drive.setNormalSpeed());


        addRequirements(drive);
    }

    @Override
    public void initialize() {    }


    @Override
    public void execute(){
        // driver.a().onTrue(drive.setYawCommand(0));
        // driver.leftStick().onTrue(new InstantCommand(()->flipDriving()));
        // driver.leftStick().onTrue(new InstantCommand(()->flipTurning()));

        

        double botHeading = drive.getHeading(); //Should be in radians, but might be degrees
        // imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double x;
        double y;
        if (FIELD_CENTRIC_DRIVING) {
            // Rotate the movement direction counter to the bot's rotation
            Pose2d rotated2d = new Pose2d(leftX.get(), leftY.get(), new Rotation2d(botHeading));//.rotated(botHeading);
            x = rotated2d.getX();
            y = rotated2d.getY();

            x *= STRAFE_WEIGHT;  // Counteract imperfect strafing
        } else {
            x = leftX.get();
            y = rightY.get();
        }

        double turn;
        if (FIELD_CENTRIC_TURNING) {
            Translation2d turn2d = new Translation2d(rightX.get(), rightY.get());
            double stickHeading = turn2d
                    .rotateBy(new Rotation2d(Math.PI / 2)).getAngle().getRadians()
                    // .rotateBy(Math.PI / 2) // Rotate by Pi / 2 Radians (90 degrees)
                    // .getAngle() 
                    - Math.PI; // Subtract Pi radians (180 degrees) from final angle

            // Find the lowest of the 3 coterminal angles
            double defaultHeadingError = (botHeading - stickHeading); // Default angle
            double lowHeadingError = defaultHeadingError - (Math.PI * 2); // Low coterminal angle (360 degrees off from original)
            double highHeadingError = defaultHeadingError + (Math.PI * 2); // High coterminal angle (360 degrees off from original)

            // Create map of the absolute value of each value to the original value
            double headingError = Arrays.stream(new Double[]{
                            defaultHeadingError,
                            lowHeadingError,
                            highHeadingError
                    })
                    .min(Comparator.comparingDouble(Math::abs)).get();

            double distance = turn2d.getDistance(new Translation2d(0,0));


            double headingP = MIN_HEADING_P + ((MAX_HEADING_P - MIN_HEADING_P) * distance);
            turn = headingError * -headingP;

            if (distance < STICK_THRESHOLD) turn = 0;

            // telemetry.addData("heading error", Math.toDegrees(botHeading - stickHeading));
            // telemetry.addData("theta", Math.toDegrees(stickHeading));

            // telemetry.addData("low heading error", lowHeadingError);
            // telemetry.addData("default heading error", defaultHeadingError);
            // telemetry.addData("high heading error", highHeadingError);
        } else {
            turn = rightX.get();

            if (rightX.get() < STICK_THRESHOLD) turn = 0;
        }

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        // double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);
        // double frontLeftPower = (y + x + turn) / denominator;
        // double backLeftPower = (y - x + turn) / denominator;
        // double frontRightPower = (y - x - turn) / denominator;
        // double backRightPower = (y + x - turn) / denominator;

        

        ChassisSpeeds chassisSpeeds = new ChassisSpeeds(x, y, turn);

        SwerveModuleState[] states = SwerveDrive.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);
        drive.setModuleStates(states);
            // drive.set

    //     // leftFront.setPower(frontLeftPower);
    //     // leftRear.setPower(backLeftPower);
    //     // rightFront.setPower(frontRightPower);
    //     // rightRear.setPower(backRightPower);
    //     // telemetry.addData("heading", Math.toDegrees(botHeading));
    // }

    // private void flipDriving(){
    //     FIELD_CENTRIC_DRIVING = !FIELD_CENTRIC_DRIVING;
    // }
    // private void flipTurning(){
    //     FIELD_CENTRIC_TURNING = !FIELD_CENTRIC_TURNING;
    // }

}
}