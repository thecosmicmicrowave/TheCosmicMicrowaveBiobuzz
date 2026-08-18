package org.firstinspires.ftc.teamcode.config.globals;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.config.util.Alliance;

public class Paths {

    public final PathChain line1;
    public final PathChain line2;
    public final PathChain line3;
    public final PathChain line4;
    public final PathChain line5;
    public final PathChain line6;

    public Paths(Follower follower, Alliance alliance) {
        line1 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        p(9.000, 8.800, alliance),
                        p(144.000, 22.745, alliance),
                        p(43.634, 116.527, alliance)
                ))
                .setTangentHeadingInterpolation()
                .build();

        line2 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        p(43.634, 116.527, alliance),
                        p(4.710, 115.145, alliance),
                        p(24.064, 66.254, alliance)
                ))
                .setTangentHeadingInterpolation()
                .build();

        line3 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        p(24.064, 66.254, alliance),
                        p(36.037, 23.346, alliance),
                        p(95.164, 31.373, alliance)
                ))
                .setTangentHeadingInterpolation()
                .build();

        line4 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        p(95.164, 31.373, alliance),
                        p(136.500, 58.282, alliance),
                        p(95.164, 112.872, alliance)
                ))
                .setTangentHeadingInterpolation()
                .build();

        line5 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        p(95.164, 112.872, alliance),
                        p(41.573, 112.872, alliance),
                        p(54.164, 52.119, alliance)
                ))
                .setTangentHeadingInterpolation()
                .build();

        line6 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        p(54.164, 52.119, alliance),
                        p(78.000, 55.000, alliance),
                        p(72.000, 72.000, alliance)
                ))
                .setTangentHeadingInterpolation()
                .build();
    }

    private static Pose p(double x, double y, Alliance alliance) {
        Pose pose = new Pose(x, y);
        return alliance == Alliance.RED ? pose.mirror() : pose;
    }
}
