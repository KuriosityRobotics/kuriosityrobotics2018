package org.firstinspires.ftc.teamcode.Skystone.Test;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Skystone.Odometry.Position2D;
import org.firstinspires.ftc.teamcode.Skystone.Robot;

@Autonomous(name="kinematics Test", group="Linear Opmode")
public class kinematicsTest extends LinearOpMode
{
    @Override

    public void runOpMode(){
        Robot robot = new Robot(hardwareMap,telemetry,this);
        Position2D position2D = new Position2D(robot);
        robot.resetEncoders();
        waitForStart();
        robot.intializeIMU();
        robot.changeRunModeToUsingEncoder();

        double fLPower;
        double fRPower;
        double bLPower;
        double bRPower;

        double powerScaleFactor = 0.9;

        while (opModeIsActive()){

            //position2D.getxPose();

            fLPower = (-gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x)*powerScaleFactor;
            fRPower = (-gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x)*powerScaleFactor;
            bLPower = (-gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x)*powerScaleFactor;
            bRPower = (-gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x)*powerScaleFactor;

            if(gamepad1.right_trigger!=0){
                fLPower = (gamepad1.right_trigger)*powerScaleFactor;
                fRPower = (-gamepad1.right_trigger)*powerScaleFactor;
                bLPower = (-gamepad1.right_trigger)*powerScaleFactor;
                bRPower = (gamepad1.right_trigger)*powerScaleFactor;
            }else if(gamepad1.left_trigger!=0){
                fLPower = (-gamepad1.left_trigger)*powerScaleFactor;
                fRPower = (gamepad1.left_trigger)*powerScaleFactor;
                bLPower = (gamepad1.left_trigger)*powerScaleFactor;
                bRPower = (-gamepad1.left_trigger)*powerScaleFactor;
            }
            //Straight D-Pad move
            if (gamepad1.dpad_up) {
                fLPower = (gamepad1.left_stick_y)+powerScaleFactor;
                bLPower = (gamepad1.left_stick_y)+powerScaleFactor;
                fRPower = (gamepad1.right_stick_y+powerScaleFactor);
                bRPower = (gamepad1.right_stick_y+powerScaleFactor);
            } else if (gamepad1.dpad_down) {
                fLPower = (gamepad1.left_stick_y)-powerScaleFactor;
                bLPower = (gamepad1.left_stick_y)-powerScaleFactor;
                fRPower = (gamepad1.right_stick_y-powerScaleFactor);
                bRPower = (gamepad1.right_stick_y)-powerScaleFactor;
            } else if (gamepad1.dpad_right) {
                fLPower = (gamepad1.right_stick_y)+powerScaleFactor;
                bLPower = (gamepad1.right_stick_y)+powerScaleFactor;
                fRPower = (gamepad1.left_stick_y)-powerScaleFactor;
                bRPower = (gamepad1.left_stick_y)-powerScaleFactor;
            } else if (gamepad1.dpad_left) {
                fRPower = (gamepad1.right_stick_y)+powerScaleFactor;
                bRPower = (gamepad1.right_stick_y)+powerScaleFactor;
                fLPower = (gamepad1.left_stick_y)-powerScaleFactor;
                bLPower = (gamepad1.left_stick_y)-powerScaleFactor;
            } else if (gamepad1.a){
                telemetry.addLine("x " + position2D.getxPose());
                telemetry.addLine("y " + position2D.getyPose());
                telemetry.addLine("angle: " + position2D.getAnglePose());
                telemetry.addLine("a is peressed");
                telemetry.update();
            }

            robot.fLeft.setPower(fLPower);
            robot.bLeft.setPower(bLPower);
            robot.bRight.setPower(bRPower);
            robot.fRight.setPower(fRPower);

        }
    }
}