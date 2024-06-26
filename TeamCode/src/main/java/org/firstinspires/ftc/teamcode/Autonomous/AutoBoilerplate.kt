package org.firstinspires.ftc.teamcode.Autonomous

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.hardware.rev.RevBlinkinLedDriver
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.DriveMethods
import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.RoadRunner.util.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.Variables
import org.firstinspires.ftc.teamcode.Variables.servoRestPosition
abstract class AutoBoilerplate : DriveMethods() {
    override fun runOpMode() {
        drive(startingPose!!)
    }

    lateinit var drive: SampleMecanumDrive
    var firstTime = true
    lateinit var STARTING_POSE: Pose2d
    var sequences = ArrayList<TrajectorySequence>()
    var passiveServo: Servo? = null
    var autoServo: Servo? = null
    var rotateMotor: DcMotor? = null
    abstract val startingPose: Pose2d?
    fun drive(startingPos: Pose2d) {
        firstTime = true
        initMotorsSecondBot()
        initVision(Variables.VisionProcessors.TFOD)
        telemetry.addLine("Motors and TFOD were ignited (same as inited)")
        telemetry.update()
        var detection = detect()
        STARTING_POSE = startingPos
        drive = SampleMecanumDrive(hardwareMap)
        drive.poseEstimate = startingPose!!
        initCall()
        while (opModeInInit()) {
            detection = detect()
            telemetry.addData("Detection", detection)
            telemetry.update()
        }

        drive.followTrajectorySequenceAsync(getTrajectorySequence(detection, drive))

        while (drive.isBusy && opModeIsActive()) {
            drive.update()
        }
    }

    fun detect(): Variables.Detection {
        val isFirstTime = if(firstTime) {firstTime = false; true} else false
        return getDetectionsSingleTFOD()
    }

    abstract fun getTrajectorySequence(
        detection: Variables.Detection?,
        drive: SampleMecanumDrive?
    ): TrajectorySequence?

    fun println(o: Any?) {
        kotlin.io.println(o)
    }

    /*
    public TrajectorySequence mergeSequences(ArrayList<TrajectorySequence> trajectorySequences) {
        TrajectorySequence[] trajectorySequencesArr = new TrajectorySequence[trajectorySequences.size()];
        trajectorySequencesArr = trajectorySequences.toArray(trajectorySequencesArr);

        return mergeSequences(trajectorySequencesArr);
    }

    public TrajectorySequence mergeSequences(TrajectorySequence[] trajectorySequences) {
        ArrayList<SequenceSegment> trajectorySegments = new ArrayList<SequenceSegment>();

        for (TrajectorySequence sequence : trajectorySequences) {
            for (int i = 0; i < sequence.size(); i++) {
                trajectorySegments.add(sequence.get(i));
            }
        }

        return new TrajectorySequence(trajectorySegments);
    }
    */
    fun getCurrentTrajectorySequence(drive: SampleMecanumDrive): TrajectorySequence {
        return if (sequences.size == 0) {
            drive.trajectorySequenceBuilder(STARTING_POSE)
                .forward(0.0)
                .build()
        } else {
            sequences[sequences.size - 1]
        }
    }

    fun getCurrentPosition(drive: SampleMecanumDrive): Pose2d {
        return if (sequences.size == 0) {
            STARTING_POSE
        } else {
            getCurrentTrajectorySequence(drive).end()
        }
    }

    fun followTrajectorySequence(trajectorySequence: TrajectorySequence) {
//        if (!opModeIsActive()) waitForStart();
        sequences.add(trajectorySequence)
    }

    fun initCall() {
        rotateMotor = hardwareMap.get(DcMotor::class.java, "motorSlideRotate")
        passiveServo = hardwareMap.get(Servo::class.java, "passiveServo")
        autoServo = hardwareMap.get(Servo::class.java, "autoServo")
        initVision(Variables.VisionProcessors.TFOD)
        initBlinkinSafe(defaultColour)
        autoServo!!.position =  servoRestPosition
        rotateMotor!!.power = -0.001
    }

    abstract val defaultColour: RevBlinkinLedDriver.BlinkinPattern


}
