package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Roller Intake Toggle", group = "TeleOp")
public class Roller extends LinearOpMode {

    private DcMotor intakeMotor;
    private boolean intakeRunning = false; // Tracks intake state
    private boolean previousBumperState = false; // Tracks previous trigger press

    @Override
    public void runOpMode() {

        intakeMotor = hardwareMap.get(DcMotor.class, "intake");

        telemetry.addData("Status", "Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            boolean currentBumperState = gamepad1.left_bumper; //Pressed or not
            // Toggle logic
            if (currentBumperState && !previousBumperState)
            {
                // Toggle
                intakeRunning = !intakeRunning;
                if (intakeRunning)
                {
                    intakeMotor.setPower(0.75); // Turn on
                }
                else
                {
                    intakeMotor.setPower(0.0); // Turn off
                }
            }

            previousBumperState = currentBumperState; // Update state

            telemetry.addData("Intake Running", intakeRunning);
            telemetry.update();
        }
    }
}
