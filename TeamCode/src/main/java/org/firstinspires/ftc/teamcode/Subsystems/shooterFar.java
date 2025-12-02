package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Dual Flywheel Shooter Far", group = "TeleOp")
public class shooterFar extends LinearOpMode {

    DcMotor flyWheel1;
    DcMotor flyWheel2;
    private boolean shooterRunning = false; // Shooter state
    private boolean previousSquareState = false; // Tracks previous trigger press


    @Override
    public void runOpMode() {

        DcMotorEx flyWheel1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        DcMotorEx flyWheel2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        telemetry.addData("Status", "Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            boolean currentSquareState = gamepad1.left_bumper; //Pressed or not
            // Toggle logic
            if (currentSquareState && !previousSquareState)
            {
                // Toggle
                shooterRunning = !shooterRunning;
                if (shooterRunning)
                {
                    flyWheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    flyWheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    flyWheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    flyWheel1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    flyWheel1.setVelocity(1400);
                    flyWheel2.setVelocity(1400);
                }
                else
                {
                    flyWheel2.setPower(0.0); // Turn off
                    flyWheel1.setPower(0.0);
                }
            }

            previousSquareState = currentSquareState; // Update state

            telemetry.addData("Intake Running", shooterRunning);
            telemetry.update();
        }
    }
}
