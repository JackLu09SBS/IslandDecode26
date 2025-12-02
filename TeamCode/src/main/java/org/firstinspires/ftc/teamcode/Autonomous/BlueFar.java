package org.firstinspires.ftc.teamcode.Autonomous;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable
public class BlueFar extends LinearOpMode {

    // PedroPathing follower + paths
    private Follower follower;
    private Paths paths;

    // Shooter + feeder hardware
    private DcMotorEx shooterMotor;
    private DcMotorEx magazine;
    private Servo feederServo;

    // Roller / intake
    private DcMotor roller;

    // Magazine encoder position (your original "positions" var)
    private int positions = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        // --- PedroPathing setup ---
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(56, 7.5, Math.toRadians(90)));
        paths = new Paths(follower);

        // --- Map hardware (names must match your config) ---
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        magazine     = hardwareMap.get(DcMotorEx.class, "magazine");
        roller       = hardwareMap.get(DcMotor.class,   "roller");
        feederServo  = hardwareMap.get(Servo.class,     "feeder");

        // Magazine setup
        magazine.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        magazine.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        magazine.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initial mechanism states
        shooterMotor.setPower(0);
        feederServo.setPosition(0.05);
        roller.setPower(0.0);

        // Shooter runs fast the whole autonomous
        startShooterFast();

        telemetry.addLine("BlueFar Initialized");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // ================== SEQUENCE ==================

        // 1) Path1
        followAndWait(paths.Path1);

        // After Path1: turn on roller
        roller();

        // 2) Path2
        followAndWait(paths.Path2);

        // After Path2: pause 0.5s and "spin up" magazine once
        sleep(500);
        spinUp();

        // 3) Path3
        followAndWait(paths.Path3);

        // After Path3: pause 0.5s and spin up again
        sleep(500);
        spinUp();

        // 4) Path4
        followAndWait(paths.Path4);

        // After Path4: pause 0.5s and spin up again
        sleep(500);
        spinUp();

        // 5) Path5
        followAndWait(paths.Path5);

        // After Path5: pause for 5 seconds total,
        // and during those 5 seconds feedOneBall 3 times with 0.5s between them.
        long windowStart = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            feedOneBall();          // uses your helper, includes servo + spinUp
            if (i < 2) {
                sleep(500);         // 0.5s between balls
            }
        }
        long elapsed = System.currentTimeMillis() - windowStart;
        long remaining = 5000 - elapsed;
        if (remaining > 0) {
            sleep(remaining);       // make sure total pause ~5 seconds
        }

        // Optional: clean up at end
        stopRoller();
        // stopShooter();           // comment out if you truly want it spinning until the OpMode ends

        telemetry.addLine("BlueFar Auto Complete");
        telemetry.update();
    }

    // ----------------- PATH DEFINITIONS -----------------

    public static class Paths {

        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain Path6;

        public Paths(Follower follower) {
            Path1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(56.000, 7.500),
                                    new Pose(56.000, 7.500)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(108))
                    .build();

            Path2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(56.000, 7.500),
                                    new Pose(51.900, 37.037),
                                    new Pose(40.000, 35.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(108), Math.toRadians(180))
                    .build();

            Path3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(40.000, 35.000),
                                    new Pose(36.000, 35.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(36.000, 35.000),
                                    new Pose(31.000, 35.000)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            Path5 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(31.000, 35.000),
                                    new Pose(25.000, 35.000)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            Path6 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(25.000, 35.000),
                                    new Pose(59.000, 10.600)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(115))
                    .build();
        }
    }

    // ----------------- YOUR HARDWARE HELPERS -----------------

    // Roller on
    private void roller() {
        roller.setPower(0.80);
    }

    // Roller off
    private void stopRoller() {
        roller.setPower(0);
    }

    // Shooter fast (launch speed)
    private void startShooterFast() {
        shooterMotor.setVelocity(1400);   // tune as needed
    }

    // Shooter slow (kept from your original, even if unused for now)
    private void startShooterSlow() {
        shooterMotor.setVelocity(990);    // tune as needed
    }

    private void stopShooter() {
        shooterMotor.setPower(0.0);
    }

    // "Spin up" magazine: move to next ball
    private void spinUp() {
        positions -= 250;                 // spin to next ball (tune if needed)
        magazine.setPower(0.7);
        magazine.setTargetPosition(positions);
    }

    // Feed one ball into shooter and advance mag
    private void feedOneBall() throws InterruptedException {
        positions -= 250;                 // next ball position
        feederServo.setPosition(0.6);
        sleep(200);
        feederServo.setPosition(0.05);
        sleep(300);
        magazine.setPower(0.7);
        magazine.setTargetPosition(positions);
    }

    // ----------------- FOLLOW HELPER -----------------

    private void followAndWait(PathChain path) {
        follower.followPath(path);
        while (opModeIsActive() && follower.isBusy()) {
            follower.update();
            telemetry.addData("X", follower.getPose().getX());
            telemetry.addData("Y", follower.getPose().getY());
            telemetry.addData("Heading", follower.getPose().getHeading());
            telemetry.update();
        }
    }
}
