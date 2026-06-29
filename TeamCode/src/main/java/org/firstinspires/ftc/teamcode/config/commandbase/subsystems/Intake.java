package org.firstinspires.ftc.teamcode.config.commandbase.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Configurable
public class Intake {
    private final DcMotorEx intake;
    public static double off = 0;
    public static double in = 1;
    public static double out = -1;

    public Intake(HardwareMap hw) {
        intake = hw.get(DcMotorEx.class, "intake");
        set(0);
    }
    public void set(double power) {
        intake.setPower(power);
    }
    public void spinIn() {
        set(in);
    }
    public void spinOut() {
        set(out);
    }
    public void spinOff() {
        set(off);
    }
    public CommandBuilder off() {
        return Commands.instant(this::spinOff);
    }
    public CommandBuilder in() {
        return Commands.instant(this::spinIn);
    }
    public CommandBuilder out() {
        return Commands.instant(this::spinOut);
    }
    public CommandBuilder toggleIn() {
        return Commands.instant(() -> {
            if (intake.getPower() != 0)
                spinOff();
             else
                spinIn();
        });
    }
    public CommandBuilder toggleOut() {
        return Commands.instant(() -> {
            if (intake.getPower() != 0)
                spinOff();
            else
                spinOut();
        });
    }
}
