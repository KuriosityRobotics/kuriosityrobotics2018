package org.firstinspires.ftc.teamcode.RR2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class TensorFlowMineralDetection {
    public enum Location{
        CENTER,LEFT,RIGHT,UNKNOWN;
    }

    public HardwareMap hardwareMap;
    public TensorFlowMineralDetection(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode linearOpMode) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.linearOpMode = linearOpMode;
    }

    public Location location;
    public Telemetry telemetry;
    public LinearOpMode linearOpMode;

    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";


    public static final String VUFORIA_KEY = "AbSCRq//////AAAAGYEdTZut2U7TuZCfZGlOu7ZgOzsOlUVdiuQjgLBC9B3dNvrPE1x/REDktOALxt5jBEJJBAX4gM9ofcwMjCzaJKoZQBBlXXxrOscekzvrWkhqs/g+AtWJLkpCOOWKDLSixgH0bF7HByYv4h3fXECqRNGUUCHELf4Uoqea6tCtiGJvee+5K+5yqNfGduJBHcA1juE3kxGMdkqkbfSjfrNgWuolkjXR5z39tRChoOUN24HethAX8LiECiLhlKrJeC4BpdRCRazgJXGLvvI74Tmih9nhCz6zyVurHAHttlrXV17nYLyt6qQB1LtVEuSCkpfLJS8lZWS9ztfC1UEfrQ8m5zA6cYGQXjDMeRumdq9ugMkS";

    public VuforiaLocalizer vuforia;

    public TFObjectDetector tfod;

    boolean isGoldInFrame = false;

    public Location runObjectDetection() {
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        }
        if (tfod != null) {
            tfod.activate();
        }

        while (this.location != Location.UNKNOWN && linearOpMode.opModeIsActive()) {
            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    if (updatedRecognitions.size() == 2) {
                        int goldXPos = -1;
                        int firstSilverXPos = -1;
                        int secondSilverXPos = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldXPos = (int) recognition.getTop();
                                isGoldInFrame = true;
                            } else if (firstSilverXPos == -1) {
                                firstSilverXPos = (int) recognition.getTop();
                            } else if(secondSilverXPos == -1){
                                secondSilverXPos = (int) recognition.getTop();
                            }
                        }
                            if (goldXPos > firstSilverXPos && isGoldInFrame) {
                                this.location = Location.CENTER;
                                return location;
                            } else if (goldXPos < firstSilverXPos && isGoldInFrame) {
                                this.location = Location.LEFT;
                                return location;
                            } else {
                                this.location = Location.RIGHT;
                                return location;
                            }


                    }
                }
            }
        }
        if (tfod != null) {
            tfod.shutdown();
        }
        return location;
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
