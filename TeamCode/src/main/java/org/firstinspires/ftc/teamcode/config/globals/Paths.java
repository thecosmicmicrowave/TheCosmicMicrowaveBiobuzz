package org.firstinspires.ftc.teamcode.config.globals;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.config.util.Alliance;

public class Paths {

    // Example routine, replace with Tracer-pasted PathChains. To paste a fresh
    // Tracer file: find/replace `new Pose(` with `p(`, add `, alliance` before
    // the closing paren on each call, leave everything else as Tracer emits it.
    public final PathChain line1;

    public Paths(Follower follower, Alliance alliance) {
        line1 = follower.pathBuilder()
                .addPath(new BezierLine(p(0, 0, alliance), p(24, 0, alliance)))
                .setLinearHeadingInterpolation(0, 0)
                .build();
    }

    private static Pose p(double x, double y, Alliance alliance) {
        Pose pose = new Pose(x, y);
        return alliance == Alliance.RED ? pose.mirror() : pose;
    }
}
