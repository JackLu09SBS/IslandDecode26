package org.firstinspires.ftc.teamcode.configurables;
import com.bylazar.configurables.annotations.Configurable;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Subsystems.Magazine;
@Configurable
@TeleOp(name = "Magazine Tuner")
public class MagazineTest extends LinearOpMode {

    public static int targetPosition = 0;
    public static double power = 1.0;
   // public static int tolerance = 10;

    @Override
    public void runOpMode() {
        Magazine magazine = new Magazine(hardwareMap);
        targetPosition=0;
        waitForStart();

        while (opModeIsActive()) {
            if(gamepad1.triangleWasPressed())
            {
                targetPosition=targetPosition-100;
                magazine.setTargetPosition(targetPosition);

            }
            if(gamepad1.squareWasPressed())
            {
              targetPosition=targetPosition+10;
                magazine.setTargetPosition(targetPosition);

            }
            if(gamepad1.circleWasPressed())
            {
                targetPosition=targetPosition+100;
                magazine.setTargetPosition(targetPosition);

            }
            if(gamepad1.crossWasPressed())
            {
                targetPosition=targetPosition-10;
                magazine.setTargetPosition(targetPosition);

            }
            telemetry.addData("Target Position", targetPosition);
            telemetry.addData("Current Position", magazine.getCurrentPosition());
            telemetry.update();
        }
    }
}
