package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Test", group = "Autos")
public class Test extends OpMode {

    private Follower follower;
    private Timer pathTimer;
    private int pathState = 0;

    // Points from your GeneratedPath
    private final Pose a1 = new Pose(56.000, 8.000,Math.toRadians(90));
    private final Pose a2 = new Pose(61.854, 47.805);
    private final Pose a3 = new Pose(50.000, 75.000);

    private final Pose b1 = new Pose(50.000, 75.000);
    private final Pose b2 = new Pose(53.073, 50.732);
    private final Pose b3 = new Pose(42.927, 35.707);

    private final Pose c1 = new Pose    (42.927, 35.707);
    private final Pose c2 = new Pose(27, 35.122);

    private final Pose d1 = new Pose(27, 35.122);
    private final Pose d2 = new Pose(73.366, 68.878);
    private final Pose d3 = new Pose(54.000, 91.000);

    // Path segments
    private PathChain path1, path2, path3, path4;

    @Override
    public void init() {
        pathTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        // Path 1: BezierCurve A
        path1 = follower.pathBuilder()
                .addPath(new BezierCurve(a1, a2, a3))

                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(120))
                .build();

        // Path 2: BezierCurve B
        path2 = follower.pathBuilder()
                .addPath(new BezierCurve(b1, b2, b3))
                .setLinearHeadingInterpolation(Math.toRadians(120), Math.toRadians(180))
                .build();

        // Path 3: BezierLine C
        path3 = follower.pathBuilder()
                .addPath(new BezierLine(c1, c2))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();

        // Path 4: BezierCurve D
        path4 = follower.pathBuilder()
                .addPath(new BezierCurve(d1, d2, d3))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                .build();

        follower.setStartingPose(a1);
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }

    private void autonomousPathUpdate() {
        switch (pathState) {

            case 0:
                follower.followPath(path1);
                setPathState(1);
                break;

            case 1:  // WAIT AFTER PATH 1
                if (pathTimer.getElapsedTimeSeconds() >= 3) {   // 3 second pause
                    follower.followPath(path2);
                    setPathState(2);
                }
                break;

            case 2:
                if (!follower.isBusy()) {
                    setPathState(3);  // go to wait state
                }
                break;

            case 3:  // WAIT AFTER PATH 2
                if (pathTimer.getElapsedTimeSeconds() >= 3) {   // 3 second pause
                    follower.followPath(path3);
                    setPathState(4);
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(path4);
                    setPathState(5);
                }
                break;

            case 5:
                if (!follower.isBusy()) {
                    setPathState(-1); // done
                }
                break;
        }
    }

    private void setPathState(int newState) {
        pathState = newState;
        pathTimer.resetTimer();
    }
}
