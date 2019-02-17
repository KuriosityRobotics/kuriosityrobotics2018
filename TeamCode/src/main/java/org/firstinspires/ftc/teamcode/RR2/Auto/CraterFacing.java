package org.firstinspires.ftc.teamcode.RR2.Auto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Crater Facing", group="Linear Opmode") //name of your program on the phone and defines if it is teleop or auto
public class CraterFacing extends AutoBase
{
    @Override
    public void runOpMode(){
        //Init's robot
        initLogic();
        while (opModeIsActive()){
            dropDownFromLander();
            knockOffMineral(35);
            navigateToDepotThenCrater();
            break;
        }
    }

    //private constructor.
    protected void navigateToDepotThenCrater() {
        if(tensorFlowMineralDetection.location == TensorFlowMineralDetection.Location.CENTER){
            robot.finalMove(0.5, -48);
        }else {
            robot.finalMove(0.5, -55);
        }

        //Getting to Depot
        robot.finalTurn(65,3250);
        robot.goToWall(0.75,25);
        robot.finalTurn(135);
        robot.moveRobotKillSwitch(0.6,120,-120);
        robot.goToCrater(-0.6);
        telemetry.addData("Status","done");
        telemetry.update();
    }
}



