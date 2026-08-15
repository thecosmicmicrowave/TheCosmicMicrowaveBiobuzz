package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.config.globals.Robot;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.CommandOpMode;

import java.util.function.Function;

public class AutoRoutineOpMode extends CommandOpMode {
    private final String name;
    private final Function<Robot, Command> factory;
    private Robot robot;

    public AutoRoutineOpMode(String name, Function<Robot, Command> factory) {
        this.name = name;
        this.factory = factory;
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Alliance.BLUE);
    }

    @Override
    public void init_loop() {
        if (gamepad1.xWasPressed()) {
            robot.alliance = robot.alliance == Alliance.BLUE ? Alliance.RED : Alliance.BLUE;
        }
        telemetry.addData("Routine", name);
        telemetry.addData("Alliance", robot.alliance);
        telemetry.update();
    }

    @Override
    public void start() {
        schedule(factory.apply(robot));
    }

    @Override
    public void loop() {
        robot.periodic();
        super.loop();
    }
}
