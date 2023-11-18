package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Variables.VisionProcessors;
import org.firstinspires.ftc.teamcode.Variables.Detection;

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive;

import java.util.Arrays;

@Config
@Autonomous(name = "FBLeft", group = "Linear OpMode")
public class FrontBlueAutoLeft extends MeepMeepBoilerplate{
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Servo passiveServo = hardwareMap.get(Servo.class, "passiveServo");
        Servo autoServo = hardwareMap.get(Servo.class, "autoServo");
        initVision(VisionProcessors.TFOD);
        Detection detection = Detection.UNKNOWN;
        TrajectoryVelocityConstraint slowConstraint = new MinVelocityConstraint(Arrays.asList(

                new TranslationalVelocityConstraint(20),

                new AngularVelocityConstraint(1)

        ));
        while (opModeInInit()) {
            detection = getDetectionsSingleTFOD();
            telemetry.addData("Detection", detection);
            telemetry.update();
        }
        autoServo.setPosition(0.35);
        switch (detection) {
            case LEFT -> drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder(getCurrentPosition(drive))
                            .forward(2.0)
                            .waitSeconds(.25)
                            .strafeLeft(9)
                            .forward(21)
                            .waitSeconds(.25)
                            .addTemporalMarker(() -> passiveServo.setPosition(0.1))
                            .waitSeconds(.25)
                            .back(9.5)
                            .waitSeconds(.25)
                            .strafeLeft(28.5)
                            .waitSeconds(.5)
                            .addTemporalMarker(() -> autoServo.setPosition(0.68))
                            .waitSeconds(1)
                            .addTemporalMarker(() -> autoServo.setPosition(0.6))
                            .waitSeconds(.5)
                            .addTemporalMarker(() -> autoServo.setPosition(0.35))
                            .waitSeconds(.5)
                            .strafeRight(2)
                            .back(11)
                            .strafeLeft(9)
                            .build()
            );
            case CENTER -> { drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder(getCurrentPosition(drive))
                            .forward(34)
                            .waitSeconds(.25)
                            .addDisplacementMarker(() -> passiveServo.setPosition(0.1))
                            .waitSeconds(.25)
                            .back(15)
                            .waitSeconds(.25)
                            .strafeLeft(37.5)
                            .waitSeconds(.25)
                            .addTemporalMarker(() -> autoServo.setPosition(0.68))
                            .waitSeconds(1.5)
                            .addTemporalMarker(() -> autoServo.setPosition(0.6))
                            .waitSeconds(.5)
                            .addTemporalMarker(() -> autoServo.setPosition(0.35))
                            .waitSeconds(.5)
                            .strafeRight(2)
                            .back(20)
                            .strafeLeft(9)
                            .build());
            }
            case RIGHT -> drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder(getCurrentPosition(drive))
                            .forward(28.0)
                            .turn(Math.toRadians(-90))
                            .forward(7)
                            .addTemporalMarker(() -> passiveServo.setPosition(0.1))
                            .waitSeconds(.25)
                            .back(32)
                            .waitSeconds(.25)
                            .turn(Math.toRadians(90))
                            .waitSeconds(.25)
                            .back(2.5)
                            .waitSeconds(.25)
                            .strafeLeft(12)
                            .waitSeconds(.25)
                            .addTemporalMarker(() -> autoServo.setPosition(0.68))
                            .waitSeconds(1.5)
                            .addTemporalMarker(() -> autoServo.setPosition(0.6))
                            .waitSeconds(.5)
                            .addTemporalMarker(() -> autoServo.setPosition(0.35))
                            .waitSeconds(.5)
                            .strafeRight(2)
                            .back(26)
                            .strafeLeft(9)
                            .build()
            );
            default -> {
                telemetry.addLine("Warning: Cup not detected");
                telemetry.update();
                sleep(3000);
            }
        }



//        drive.followTrajectorySequence(mergeSequences(sequences));
    }
}