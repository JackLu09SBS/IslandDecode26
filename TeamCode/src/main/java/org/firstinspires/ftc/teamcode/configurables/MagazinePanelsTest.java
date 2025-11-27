package org.firstinspires.ftc.teamcode.configurables;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Subsystems.Magazine;

@Configurable
@TeleOp(name = "Magazine Panels Tuner")
public class MagazinePanelsTest extends LinearOpMode {

    // Visible + editable in Panels
    public static int targetPosition = 0;
    public static double power = 1.0;

    @Override
    public void runOpMode() {
        Magazine magazine = new Magazine(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
           magazine.setTargetPosition(targetPosition);

            telemetry.addData("Target", targetPosition);
            telemetry.addData("Power", power);
            telemetry.addData("Current", magazine.getCurrentPosition());
            telemetry.update();
        }
    }
}
