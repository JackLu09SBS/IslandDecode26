package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "storage", group = "TeleOp")
public class storage {

    private LinearOpMode opMode;
    int positions = 0;
    DcMotorEx motor;
    private Servo pusher;

    public storage(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class,"magazine");
        pusher = hardwareMap.servo.get("servo");
        this.opMode = opMode;
    }
    public void spinAndShoot()
    {
        positions -= 250;                      // spin to next ball
        pusher.setPosition(.6);
        opMode.sleep(200);
        pusher.setPosition(0.05);
        opMode.sleep(300);
        motor.setPower(0.7);
        motor.setTargetPosition(positions);
    }
    public void spin()
    {
        positions-=250;
        motor.setPower(0.7);
        motor.setTargetPosition(positions);
    }

}