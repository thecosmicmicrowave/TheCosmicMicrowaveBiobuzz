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

/**
 * DriveToPose — drives to a target pose and finishes when Pedro reports !isBusy().
 * <p>
 * This command uses the Ivy command system and requires the Follower, ensuring
 * that multiple drive commands do not conflict.
 * <p>
 * Always pair with .until(condition) or similar if a timeout is needed,
 * though Pedro pathing usually handles timeouts internally via path constraints.
 * <p>
 * Usage examples:
 * <p>
 *   // Straight line, linear heading, full speed
 *   new DriveToPose(follower, Poses.SCORE_CLOSE)
 * <p>
 *   // Straight line, tangential heading
 *   new DriveToPose(follower, Poses.SCORE_CLOSE, HeadingMode.TANGENTIAL)
 * <p>
 *   // Bezier curve through waypoints, tangential heading. Last pose is the target.
 *   new DriveToPose(follower, HeadingMode.TANGENTIAL, 0.8, Poses.PGP_MID, Poses.PGP_COLLECT)
 */
public class DriveToPose implements Command {

    public enum HeadingMode {
        LINEAR,         // Interpolates heading from start to end
        TANGENTIAL,     // Heading follows the curve direction
        TANGENTIAL_REV  // Tangential but reversed
    }

    private final Follower follower;
    private final Pose targetPose;
    private final Pose[] controlPoints;
    private final HeadingMode headingMode;
    private final HeadingInterpolator customInterpolator;
    private final double maxSpeed;
    private final boolean holdEnd;
    private final Set<Object> requirements;

    // ─── Constructors — Straight Line ─────────────────────────────────────────

    /** Straight line, linear heading, full speed, holds end. */
    public DriveToPose(Follower follower, Pose target) {
        this(follower, target, HeadingMode.LINEAR, 1.0, true);
    }

    /** Straight line, linear heading, custom speed. */
    public DriveToPose(Follower follower, Pose target, double maxSpeed) {
        this(follower, target, HeadingMode.LINEAR, maxSpeed, true);
    }

    /** Straight line, chosen heading mode, custom speed, holds end. */
    public DriveToPose(Follower follower, Pose target, HeadingMode headingMode, double maxSpeed) {
        this(follower, target, headingMode, maxSpeed, true);
    }

    /** Full parameter constructor for straight lines. */
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

    // ─── Constructors — Bezier Curve ──────────────────────────────────────────

    /**
     * Bezier curve through control points ending at the last waypoint.
     * The robot's current position is used as the start of the curve.
     * @param waypoints [control1, control2, ..., targetPose]
     */
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

    /**
     * Bezier curve with a custom HeadingInterpolator.
     * @param waypoints [control1, control2, ..., targetPose]
     */
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

    // ─── Ivy Lifecycle ────────────────────────────────────────────────────────

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
            // Build: [start, ...controls, target]
            Pose[] allPoses = new Pose[controlPoints.length + 2];
            allPoses[0] = from;
            System.arraycopy(controlPoints, 0, allPoses, 1, controlPoints.length);
            allPoses[allPoses.length - 1] = targetPose;
            pathObj = new Path(new BezierCurve(allPoses));
        } else {
            // Straight line
            pathObj = new Path(new BezierLine(from, targetPose));
        }

        applyHeading(pathObj, from);

        // Build a PathChain and follow it
        PathChain chain = follower.pathBuilder()
                .addPath(pathObj)
                .build();

        follower.followPath(chain, maxSpeed, holdEnd);
    }

    @Override
    public void execute() {
        // Movement is updated by the Follower (usually in Robot.periodic() or another thread)
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

    // ─── Helpers ──────────────────────────────────────────────────────────────

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
