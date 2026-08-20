package org.firstinspires.ftc.teamcode.opmode;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.config.globals.Robot;
import org.firstinspires.ftc.teamcode.config.util.Alliance;
import org.firstinspires.ftc.teamcode.config.util.CommandOpMode;

@Configurable
public class teleop extends CommandOpMode {
    Robot robot;
    final Alliance alliance;
    public teleop(Alliance a) {
        alliance = a;
    }
    @Override
    public void init() {
        robot = new Robot(hardwareMap, alliance);
    }
    @Override
    public void start() {
        robot.follower.startTeleopDrive();
    }
    @Override
    public void loop() {
        robot.periodic();
        robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
    }
}
