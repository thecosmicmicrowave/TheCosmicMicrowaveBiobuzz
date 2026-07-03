package org.firstinspires.ftc.teamcode.config.globals;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.localization.constants.OctoQuadConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class PedroConstants {
    public static FollowerConstants followerConstants = new FollowerConstants();

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static OctoQuadConstants localizerConstants = new OctoQuadConstants()
            .name("octoquad")
            .deadwheelPortX(0)
            .deadwheelPortY(1)
            .deadwheelXDir(OctoQuad.EncoderDirection.FORWARD)
            .deadwheelYDir(OctoQuad.EncoderDirection.REVERSE)
            .deadwheelXTicksPerMM(19.89437f)
            .deadwheelYTicksPerMM(19.89437f)
            .tcpOffsetXMM(-97.05f) // Distance from robot center to X pod
            .tcpOffsetYMM(-156.70f); // Distance from robot center to Y pod
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .build();
    }
}
