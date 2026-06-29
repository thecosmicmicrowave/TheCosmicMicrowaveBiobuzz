package org.firstinspires.ftc.teamcode.config.commandbase.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Configurable
public class Arm {
    private final DcMotorEx conveyor;
    public static double off = 0;
    public static double in = 1;
    public static double out = -1;
    private final Servo elbow;
    public static double stowe = -1;
    public static double top = 1;

    public Arm(HardwareMap hw) {
        conveyor = hw.get(DcMotorEx.class, "conveyor");
        elbow = hw.get(Servo.class, "elbow");
        pos(stowe);
        power(off);
    }

    public void power(double power) {
        conveyor.setPower(power);
    }
    public void pos(double position) {
        elbow.setPosition(position);
    }
    public void spinIn() {
        power(in);
    }
    public void spinOut() {
        power(out);
    }
    public void spinOff() {
        power(off);
    }
    public void armStowe() {
        pos(stowe);
    }
    public void armTop() {
        pos(top);
    }
    public CommandBuilder off() {
        return Commands.instant(() -> power(off));
    }
    public CommandBuilder in() {
        return Commands.instant(() -> power(in));
    }
    public CommandBuilder out() {
        return Commands.instant(() -> power(out));
    }
    public CommandBuilder toggle() {
        return Commands.instant(() -> {
            if (conveyor.getPower() != 0)
                spinOff();
            else
                spinIn();
        });
    }

    public CommandBuilder stowe() {
        return Commands.instant(() -> pos(stowe));
    }
    public CommandBuilder top() {
        return Commands.instant(() -> pos(top));
    }
}
