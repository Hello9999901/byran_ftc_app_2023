package org.firstinspires.ftc.teamcode.opmodes.deprecated;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.roadrunner.drive.PoseStorage;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Belt;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Consts;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.TurnTable;

/*
 * Key Map
 * Gamepad 1
 * Left joystick: Translation
 * DPad: Slow translation
 * Right joystick: Rotation
 * Bumpers: Slow rotation
 * -----------------------
 * Gamepad 2
 * A: Toggle Claw
 * B: Toggle Belt
 * DPad up: Move lift to high
 * DPad left: Move lift to mid
 * DPad right: Move lift to low
 * DPad down: Move lift to zero
 * Right joystick: Turntable rotation
 */

@TeleOp(name = "DeprecatedTeleOpProMax")
@Config()
@Disabled
public class Deprecated_TeleOpProMax extends LinearOpMode {
    public static double DPAD_SPEED = 0.35;
    public static double BUMPER_ROTATION_SPEED = 0.35;
    public static double DEFAULT_MOVE_MULTIPLIER = .7;
    public static double SLOW_MOVEMENT_MULTIPLIER = .4;
    public static double FAST_MOVEMENT_MULTIPLIER = 1;
    public static double ROTATION_MULTIPLIER = 2.05;
    public static double SLOW_ROTATION_MULTIPLIER = .4;
    public static boolean TURN_X_JOYSTICK = true;
    public static double turntableSensitivity = 2.2;
    boolean gp2AReleased = true;
    boolean gp2BReleased = true;
    boolean gp2RBumperReleased = true;
    boolean gp2LBumperReleased = true;
    boolean currentAbtn;
    boolean currentYbtn;
    boolean currentBbtn;
    boolean gp1APressed = false;
    boolean gp1BPressed = false;
    boolean currentRBumper;
    boolean currentLBumper;
    Trajectory currentATrajectory;
    Trajectory currentBTrajectory;
    Pose2d poseEstimate;
    Pose2d startingPos = new Pose2d(12, 62, Math.toRadians(-90));
    boolean followingTrajectory = false;

    private Vector2d translation;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        boolean clawOpen = false;
        boolean beltUp = true;
        double rotation = 0;
        double tableRotation = 0;

        double beltUpPos = Consts.BELT_UP_LIMIT;
        double beltDownPos = Consts.BELT_DOWN_LIMIT;

        Claw claw = new Claw();
//        OldBelt oldBelt = new OldBelt();
        Belt belt = new Belt();
        Lift lift = new Lift();
        TurnTable turntable = new TurnTable();

        drive.setPoseEstimate(startingPos);

        waitForStart();

        claw.init(hardwareMap);
        belt.init(hardwareMap);
        turntable.init(hardwareMap);
        lift.init(hardwareMap);

        while (!isStopRequested() && !lift.requestStop) {
            drive.update();
            poseEstimate = drive.getPoseEstimate();
            PoseStorage.currentPose = poseEstimate;

            // Translation
            if (gamepad1.right_trigger > .3){
                translation = new Vector2d(-1* FAST_MOVEMENT_MULTIPLIER *gamepad1.left_stick_y, -1* FAST_MOVEMENT_MULTIPLIER *gamepad1.left_stick_x);
            }
            else if (gamepad1.left_trigger > .3){
                translation = new Vector2d(-1*SLOW_MOVEMENT_MULTIPLIER*gamepad1.left_stick_y, -1*SLOW_MOVEMENT_MULTIPLIER*gamepad1.left_stick_x);
            }
            else{
                translation = new Vector2d(-1*DEFAULT_MOVE_MULTIPLIER*gamepad1.left_stick_y, -1*DEFAULT_MOVE_MULTIPLIER*gamepad1.left_stick_x);
            }


            if (TURN_X_JOYSTICK) {
                rotation = -ROTATION_MULTIPLIER * gamepad1.right_stick_x;
            }

            if (gamepad1.right_bumper){
                rotation = rotation * SLOW_ROTATION_MULTIPLIER;
            }

            // Slow translation
//            if (gamepad1.dpad_up) {
//                translation = new Vector2d(DPAD_SPEED, 0);
//            } else if (gamepad1.dpad_down) {
//                translation = new Vector2d(-DPAD_SPEED, 0);
//            } else if (gamepad1.dpad_left) {
//                translation = new Vector2d(0, DPAD_SPEED);
//            } else if (gamepad1.dpad_right) {
//                translation = new Vector2d(0, -DPAD_SPEED);
//            }

            // Slow rotation
//            if (gamepad1.left_bumper) {
//                rotation = BUMPER_ROTATION_SPEED;
//            }
//            else if (gamepad1.right_bumper) {
//                rotation = -BUMPER_ROTATION_SPEED;
//            }




            // Toggle claw
            currentAbtn = gamepad2.a;
            currentYbtn = gamepad2.y;

            if (currentYbtn) {
                claw.move(Consts.Claw.CLOSECLAW);
            }
            else if (currentAbtn) {
                claw.move(Consts.Claw.OPENCLAW);
            }


//            if (!currentAbtn) {
//                gp2AReleased = true;
//            }
//
//            if (currentAbtn && gp2AReleased) {
//                gp2AReleased = false;
//                if (clawOpen) {
//                    claw.moveClaw(Constants.ClawTargets.CLOSECLAW);
//                    clawOpen = false;
//                } else {
//                    claw.moveClaw(Constants.ClawTargets.OPENCLAW);
//                    clawOpen = true;
//                }
//            }

            // Toggle belt
            currentBbtn = gamepad2.b;
            if (!currentBbtn) {
                gp2BReleased = true;
            }

            if (currentBbtn && gp2BReleased) {
                gp2BReleased = false;
                if (beltUp) {
                    belt.move(Consts.Belt.DOWN);
                    beltUp = false;
                }
                else {
                    belt.move(Consts.Belt.UP);
                    beltUp = true;
                }
            }

            // Turntable rotation


            currentRBumper = gamepad2.right_bumper;
            if (!currentRBumper) {
                gp2RBumperReleased = true;
            }

            if (currentRBumper && gp2RBumperReleased) {
                gp2RBumperReleased = false;
                if (tableRotation < 0){
                    tableRotation = 0;
                }
                else if (tableRotation < 90){
                    tableRotation = 90;
                }
                else if (tableRotation < 180){
                    tableRotation = 180;
                }
//                else if (tableRotation < 270){
//                    tableRotation = 270;
//                }
            }

            currentLBumper = gamepad2.left_bumper;
            if (!currentLBumper) {
                gp2LBumperReleased = true;
            }

            if (currentLBumper && gp2LBumperReleased) {
                gp2LBumperReleased = false;
                if (tableRotation > 0){
                    tableRotation = 0;
                }
                else if (tableRotation > -90){
                    tableRotation = -90;
                }
                else if (tableRotation > -180){
                    tableRotation = -180;
                }
//                else if (tableRotation > -270){
//                    tableRotation = -270;
//                }
            }

            tableRotation += (turntableSensitivity * gamepad2.right_stick_x);

            // Moving lift
            if (gamepad2.dpad_up) {
                lift.move(Consts.Lift.HIGH);
            } else if (gamepad2.dpad_right) {
                lift.move(Consts.Lift.LOW);
            } else if (gamepad2.dpad_left) {
                lift.move(Consts.Lift.MEDIUM);
            } else if (gamepad2.dpad_down) {
                belt.move(Consts.Belt.UP);
                lift.move(Consts.Lift.ZERO);
            }

            // X: reset subsystems for intaking action
            // turn table turned
            // lift up
            // claw open
            // belt is down
            // belt up -> claw close -> turntable turn back -> lift down

            if (gamepad2.x) {
                belt.move(Consts.Belt.UP);
                tableRotation = 0;
                lift.move(Consts.Lift.ZERO);
            }

            if (tableRotation >= 180) {
                tableRotation = 180;
            }
            if (tableRotation <= -180) {
                tableRotation = -180;
            }
            turntable.move(tableRotation);



            
            if (gamepad1.a && !gp1APressed && !followingTrajectory){
                gp1APressed = true;
                followingTrajectory = true;
//                currentATrajectory = drive.trajectoryBuilder(poseEstimate).lineTo(new Vector2d(12, 22.5)).build();
                currentATrajectory = drive.trajectoryBuilder(poseEstimate).forward(33.5).build();
                drive.followTrajectoryAsync(currentATrajectory);
            }
            if (!gamepad1.a){
                gp1APressed = false;
            }

            if (gamepad1.b && !gp1BPressed && !followingTrajectory){
                gp1BPressed = true;
                followingTrajectory = true;
//                currentBTrajectory = drive.trajectoryBuilder(poseEstimate).lineTo(new Vector2d(12, 56)).build();
                currentBTrajectory = drive.trajectoryBuilder(poseEstimate).back(33.5).build();
                drive.followTrajectoryAsync(currentATrajectory);
            }
            if (!gamepad1.b){
                gp1BPressed = false;
            }
            if (followingTrajectory){
                drive.update();
            }
            if (!drive.isBusy()){
                followingTrajectory = false;
            }

            if (!followingTrajectory){
                drive.setWeightedDrivePower(new Pose2d(translation, rotation));
            }


            


            telemetry.addData("rTrigger ", gamepad1.right_trigger);
            telemetry.addData("lTrigger ", gamepad1.left_trigger);
            telemetry.addData("clawOpen", clawOpen);
            telemetry.addData("claw position ", claw.getPosition());
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("right stick x pos", gamepad1.right_stick_x);
            telemetry.addData("right stick y pos", gamepad1.right_stick_y);
            telemetry.addData("rotation", rotation);
            telemetry.addData("Belt Position", belt.getPosition());
            telemetry.addData("Lift Position", lift.getPosition());
            telemetry.addData("Dpad Up", gamepad2.dpad_up);
            telemetry.addData("Dpad right", gamepad2.dpad_right);
            telemetry.addData("Dpad down", gamepad2.dpad_down);
            telemetry.addData("Dpad left", gamepad2.dpad_left);
            telemetry.addData("gamepad 2 x button", gamepad2.x);
            telemetry.addData("turn table position", turntable.getPosition());
            telemetry.addData("translation ", translation);
            telemetry.update();
        }
    }
}
