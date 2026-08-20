package org.firstinspires.ftc.teamcode.config.globals;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class PedroConstants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(5.44)
            .headingPIDFCoefficients(new PIDFCoefficients(0.75, 0, 0, 0.01))
            .predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(0.1, 0.07186138470623854, 0.0013279557640240114
            ))
            ;

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(4.37)
            .strafePodX(-3.43)
            .distanceUnit(DistanceUnit.INCH)
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("rf")
            .rightRearMotorName("rr")
            .leftRearMotorName("lr")
            .leftFrontMotorName("lf")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(76.04715424673259)
            .yVelocity(61.40360365922963)
            ;
    /*public static OctoQuadConstants localizerConstants = new OctoQuadConstants()
            .name("octoquad")
            .deadwheelPortX(0)
            .deadwheelPortY(1)
            .deadwheelXDir(OctoQuad.EncoderDirection.FORWARD)
            .deadwheelYDir(OctoQuad.EncoderDirection.REVERSE)
            .deadwheelXTicksPerMM(19.89437f)
            .deadwheelYTicksPerMM(19.89437f)
            .tcpOffsetXMM(-97.05f) // Distance from robot center to X pod
            .tcpOffsetYMM(-156.70f); // Distance from robot center to Y pod*/
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
