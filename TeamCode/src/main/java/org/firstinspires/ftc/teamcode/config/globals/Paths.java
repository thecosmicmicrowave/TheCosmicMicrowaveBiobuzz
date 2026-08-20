package org.firstinspires.ftc.teamcode.config.globals;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.config.util.Alliance;

public class Paths {

        public PathChain line1;
        public PathChain line2;
        public PathChain line3;
        public PathChain line4;
        public PathChain line5;
        public PathChain line6;
        public PathChain line7;

        public Paths(Follower follower, Alliance alliance) {
            line1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(9.000, 8.800),

                                    new Pose(39.982, 34.009)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                    .build();

            line2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(39.982, 34.009),
                                    new Pose(144.000, 22.745),
                                    new Pose(43.634, 116.527)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            line3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(43.634, 116.527),
                                    new Pose(4.710, 115.145),
                                    new Pose(24.064, 66.254)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            line4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(24.064, 66.254),
                                    new Pose(36.037, 23.346),
                                    new Pose(95.164, 31.373)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            line5 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(95.164, 31.373),
                                    new Pose(136.500, 58.282),
                                    new Pose(95.164, 112.872)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            line6 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(95.164, 112.872),
                                    new Pose(41.573, 112.872),
                                    new Pose(54.164, 52.119)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            line7 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(54.164, 52.119),
                                    new Pose(78.000, 55.000),
                                    new Pose(72.000, 72.000)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();
        }
    private static Pose p(double x, double y, Alliance alliance) {
        Pose pose = new Pose(x, y);
        return alliance == Alliance.RED ? pose.mirror() : pose;
    }
}
