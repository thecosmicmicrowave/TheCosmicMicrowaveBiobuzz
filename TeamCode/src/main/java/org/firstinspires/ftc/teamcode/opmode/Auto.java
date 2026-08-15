package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.ivy.Command;
import com.pedropathing.telemetry.SelectableOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.config.globals.Robot;
import org.firstinspires.ftc.teamcode.config.globals.Routines;

import java.util.Map;
import java.util.function.Function;

@Autonomous(name = "Auto")
public class Auto extends SelectableOpMode {
    public Auto() {
        super("Select an Auto", s -> {
            for (Map.Entry<String, Function<Robot, Command>> e : Routines.ALL.entrySet()) {
                s.add(e.getKey(), () -> new AutoRoutineOpMode(e.getKey(), e.getValue()));
            }
        });
    }
}
