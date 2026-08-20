package org.firstinspires.ftc.teamcode.config.globals;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.config.util.Alliance;

import java.util.List;

public class Robot{
    //public final Arm arm;
    //public final Intake intake;
    public final Follower follower;
    public Alliance alliance;
    private final List<LynxModule> hubs;
    private final Timer loop = new Timer();
    public double loops = 0, lastLoop = 0, loopTime = 0;
    public Robot(HardwareMap hw, Alliance alliance) {
        this.alliance = alliance;
        //arm = new Arm(hw);
        //intake = new Intake(hw);
        follower = PedroConstants.createFollower(hw);
        hubs = hw.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        loop.resetTimer();

        periodic();
    }

    public void periodic() {
        loops++;

        if (loops > 10) {
            double now = loop.getElapsedTime();
            loopTime = (now - lastLoop) / loops;
            lastLoop = now;
            loops = 0;
        }

        follower.update();
    }

    public double getLoopTimeMs() {
        return loopTime;
    }

    public double getLoopTimeHz() {
        return 1000 / loopTime;
    }
}
