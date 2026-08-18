package org.firstinspires.ftc.teamcode.config.globals;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.config.commandbase.commands.DriveToPose;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static com.pedropathing.ivy.groups.Groups.sequential;

public class Routines {
    public static final Map<String, Function<Robot, Command>> ALL = new LinkedHashMap<>();

    static {
        ALL.put("test", robot -> {
            Paths p = new Paths(robot.follower, robot.alliance);
            return sequential(
                    new DriveToPose(robot.follower, p.line1, 1.0),
                    new DriveToPose(robot.follower, p.line2, 1.0),
                    new DriveToPose(robot.follower, p.line3, 1.0),
                    new DriveToPose(robot.follower, p.line4, 1.0),
                    new DriveToPose(robot.follower, p.line5, 1.0),
                    new DriveToPose(robot.follower, p.line6, 1.0)
            );
        });
        // additional named routines go here
    }
}
