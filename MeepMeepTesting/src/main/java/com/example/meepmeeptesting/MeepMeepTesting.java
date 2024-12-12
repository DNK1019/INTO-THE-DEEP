package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(14,14)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(36, 64, Math.PI))
//                .setTangent(Math.PI)
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
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}