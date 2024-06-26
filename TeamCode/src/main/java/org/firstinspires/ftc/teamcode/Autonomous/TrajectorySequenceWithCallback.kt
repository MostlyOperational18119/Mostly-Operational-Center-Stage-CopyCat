package org.firstinspires.ftc.teamcode.Autonomous

import org.firstinspires.ftc.teamcode.RoadRunner.drive.SampleMecanumDrive

data class TrajectorySequenceWithCallback(var trajectorySequence: OptionalTrajectorySequence, val hasCallback: Boolean, val callback: (() -> Unit)?) {
    fun followSync(drive: SampleMecanumDrive) {
        trajectorySequence.followSync(drive)
        if(hasCallback) callback!!()
    }
}