package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TestBenchColor {
    NormalizedColorSensor colorSensor;
    public enum DetectedColor
    {
        TAN,
        UNKNOWN

    }
    public void init(HardwareMap hwMap)
    {
        colorSensor=hwMap.get(NormalizedColorSensor.class,"colorSensor");
        colorSensor.setGain(3);
    }
    public DetectedColor getDetectedColor(Telemetry telemetry) {
        NormalizedRGBA colors = colorSensor.getNormalizedColors(); // 4 values
        float normRed, normGreen, normBlue;
        normRed = colors.red / colors.alpha;
        normGreen = colors.green / colors.alpha;
        normBlue = colors.blue / colors.alpha;
        // Red: OUT: 0.048, 0.0492 IN: 0.044, 0.045
        // Green: OUT: 0.0812, 0.0842 IN: 0.0724, 0.0751
        //  Blue: OUT: 0.0566, 0.0573 IN:0.0601, 0.0594

        telemetry.addData("red", normRed);
        telemetry.addData("green", normGreen);
        telemetry.addData("blue", normBlue);
        if (normRed > 0.04 && normRed < 0.055 && normGreen > 0.07 && normGreen < 0.095 && normBlue < 0.052 && normBlue < 0.06) {
            return DetectedColor.TAN;
        }
        return DetectedColor.UNKNOWN;

    }
}
