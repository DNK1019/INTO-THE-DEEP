package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(preselectTeleOp = "Drive")
public class BasketAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d beginPose = new Pose2d(36, 64, Math.PI);
        SparkFunOTOSDrive drive = new SparkFunOTOSDrive(hardwareMap, beginPose);
        waitForStart();

        if (opModeIsActive()) {
            Actions.runBlocking(
                    drive.actionBuilder(beginPose)
                            .splineTo(new Vector2d(0, 32), -Math.PI/2)
                            .waitSeconds(.5)
                            .setTangent(Math.PI / 2)
                            .splineToConstantHeading(new Vector2d(48, 30), -Math.PI/2) // To first sample
                            .setReversed(true)
                            .splineToLinearHeading(new Pose2d(56,56,5 * Math.PI / 4), Math.PI / 4) // To basket
                            .setReversed(false)
                            .splineToLinearHeading(new Pose2d(58, 30, -Math.PI / 2), -Math.PI / 2) // To second sample
                            .setReversed(true)
                            .splineToLinearHeading(new Pose2d(56,56,5 * Math.PI / 4), Math.PI / 4) // To basket
                            .setReversed(false)
                            .splineToLinearHeading(new Pose2d(60 ,32, -Math.PI/4), -Math.PI/4) // To third sample
                            .setReversed(true)
                            .splineToLinearHeading(new Pose2d(56,56,5 * Math.PI / 4), Math.PI / 4) // To basket
                            .setReversed(false)
                            .splineTo(new Vector2d(24, 0), Math.PI) // To park
                            .build()
            );
        }
    }
}
