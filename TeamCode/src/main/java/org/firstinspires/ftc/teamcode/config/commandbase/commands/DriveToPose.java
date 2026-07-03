package org.firstinspires.ftc.teamcode.config.commandbase.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DriveToPose implements Command {

    public enum HeadingMode {
        LINEAR,
        TANGENTIAL,
        TANGENTIAL_REV
    }

    private final Follower follower;
    private final Pose targetPose;
    private final Pose[] controlPoints;
    private final HeadingMode headingMode;
    private final HeadingInterpolator customInterpolator;
    private final double maxSpeed;
    private final boolean holdEnd;
    private final Set<Object> requirements;

    public DriveToPose(Follower follower, Pose target) {
        this(follower, target, HeadingMode.LINEAR, 1.0, true);
    }
    public DriveToPose(Follower follower, Pose target, double maxSpeed) {
        this(follower, target, HeadingMode.LINEAR, maxSpeed, true);
    }
    public DriveToPose(Follower follower, Pose target, HeadingMode headingMode, double maxSpeed) {
        this(follower, target, headingMode, maxSpeed, true);
    }
    public DriveToPose(Follower follower, Pose target, HeadingMode headingMode, double maxSpeed, boolean holdEnd) {
        this.follower = follower;
        this.targetPose = target;
        this.controlPoints = null;
        this.headingMode = headingMode;
        this.customInterpolator = null;
        this.maxSpeed = maxSpeed;
        this.holdEnd = holdEnd;
        this.requirements = new HashSet<>(Collections.singletonList(follower));
    }

    public DriveToPose(Follower follower, HeadingMode headingMode, double maxSpeed, Pose... waypoints) {
        if (waypoints == null || waypoints.length == 0) throw new IllegalArgumentException("Waypoints cannot be empty");
        this.follower = follower;
        this.targetPose = waypoints[waypoints.length - 1];
        this.controlPoints = Arrays.copyOfRange(waypoints, 0, waypoints.length - 1);
        this.headingMode = headingMode;
        this.customInterpolator = null;
        this.maxSpeed = maxSpeed;
        this.holdEnd = true;
        this.requirements = new HashSet<>(Collections.singletonList(follower));
    }
    public DriveToPose(Follower follower, HeadingInterpolator interpolator, double maxSpeed, Pose... waypoints) {
        if (waypoints == null || waypoints.length == 0) throw new IllegalArgumentException("Waypoints cannot be empty");
        this.follower = follower;
        this.targetPose = waypoints[waypoints.length - 1];
        this.controlPoints = Arrays.copyOfRange(waypoints, 0, waypoints.length - 1);
        this.headingMode = null;
        this.customInterpolator = interpolator;
        this.maxSpeed = maxSpeed;
        this.holdEnd = true;
        this.requirements = new HashSet<>(Collections.singletonList(follower));
    }

    @Override
    public Set<Object> requirements() {
        return requirements;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public InterruptedBehavior interruptedBehavior() {
        return InterruptedBehavior.END;
    }

    @Override
    public ConflictBehavior conflictBehavior() {
        return ConflictBehavior.OVERRIDE;
    }

    @Override
    public BlockedBehavior blockedBehavior() {
        return BlockedBehavior.CANCEL;
    }

    @Override
    public void start() {
        Pose from = follower.getPose();
        Path pathObj;

        if (controlPoints != null && controlPoints.length > 0) {
            Pose[] allPoses = new Pose[controlPoints.length + 2];
            allPoses[0] = from;
            System.arraycopy(controlPoints, 0, allPoses, 1, controlPoints.length);
            allPoses[allPoses.length - 1] = targetPose;
            pathObj = new Path(new BezierCurve(allPoses));
        } else {
            pathObj = new Path(new BezierLine(from, targetPose));
        }

        applyHeading(pathObj, from);

        PathChain chain = follower.pathBuilder()
                .addPath(pathObj)
                .build();

        follower.followPath(chain, maxSpeed, holdEnd);
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean done() {
        return !follower.isBusy();
    }

    @Override
    public void end(EndCondition condition) {
        if (condition == EndCondition.INTERRUPTED) {
            follower.breakFollowing();
        }
    }


    private void applyHeading(Path path, Pose from) {
        if (customInterpolator != null) {
            path.setHeadingInterpolation(customInterpolator);
            return;
        }

        switch (headingMode) {
            case TANGENTIAL:
                path.setTangentHeadingInterpolation();
                break;
            case TANGENTIAL_REV:
                path.setTangentHeadingInterpolation();
                path.reverseHeadingInterpolation();
                break;
            case LINEAR:
            default:
                path.setLinearHeadingInterpolation(from.getHeading(), targetPose.getHeading());
                break;
        }
    }
}
