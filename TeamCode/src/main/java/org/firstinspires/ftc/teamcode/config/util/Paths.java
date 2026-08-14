/* ============================================================= *
 *                 Turtle Tracer — Auto-Generated                *
 *                                                               *
 *  Version: 2.2.1.                                              *
 *  Copyright (c) 2026 Matthew Allen                             *
 *                                                               *
 *  THIS FILE IS AUTO-GENERATED — DO NOT EDIT MANUALLY.          *
 *  Changes will be overwritten when regenerated.                *
 * ============================================================= */

public static class Paths {

  public PathChain line1;
  public PathChain line2;
  public PathChain line3;
  public PathChain line4;

  public Paths(Follower follower) {
    line1 = follower
      .pathBuilder()
      .addPath(
        new BezierCurve(
          new Pose(9.044, 8.529),
          new Pose(144.000, 3.839),
          new Pose(0.000, 32.976),
          new Pose(30.274, 70.730)
        )
      )
      .setTangentHeadingInterpolation()
      .build();

    line2 = follower
      .pathBuilder()
      .addPath(
        new BezierCurve(
          new Pose(30.274, 70.730),
          new Pose(94.450, 24.277),
          new Pose(58.429, 112.016)
        )
      )
      .setTangentHeadingInterpolation()
      .build();

    line3 = follower
      .pathBuilder()
      .addPath(
        new BezierCurve(
          new Pose(58.429, 112.016),
          new Pose(106.167, 112.016),
          new Pose(98.777, 26.972)
        )
      )
      .setTangentHeadingInterpolation()
      .build();

    line4 = follower
      .pathBuilder()
      .addPath(
        new BezierCurve(
          new Pose(98.777, 26.972),
          new Pose(26.976, 27.987),
          new Pose(44.554, 78.276)
        )
      )
      .setTangentHeadingInterpolation()
      .build();
  }
}
