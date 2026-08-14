# Selectable Auto with Registry Routines

## Problem

Autonomous OpModes are currently hand-written one-offs. We want a single `@Autonomous`
entry point that lets the driver pick a named auto routine during init (menu-driven, like
`Tuning.java` already does for tuner selection), then runs it. Routines are built from
`PathChain`s pasted in from the Turtle Tracer path visualizer, wrapped in `DriveToPose`
commands (see `DriveToPose.java`, which already supports following a prebuilt `PathChain`
directly via `new DriveToPose(follower, chain, maxSpeed)`).

Alliance (red/blue) must be selectable too, applied by mirroring `Pose`s at path-build time
via Pedro's `Pose.mirror()` (the only mirror primitive available — `PathChain` itself has no
mirror method).

## Architecture

```
config/globals/Paths.java        — Tracer-pasted PathChain fields, alliance-mirrored
config/globals/Routines.java     — name -> Function<Robot, Command> registry
config/commandbase/commands/DriveToPose.java  — unchanged, already supports prebuilt PathChain
opmode/AutoRoutineOpMode.java    — generic reusable OpMode, wraps one registry entry
opmode/Auto.java                 — @Autonomous entry point, SelectableOpMode menu from Routines
```

### Paths.java

Constructor becomes `Paths(Follower follower, Alliance alliance)`. Every `new Pose(x, y)`
from the raw Tracer output is wrapped through a small helper, e.g.:

```java
private static Pose p(double x, double y, Alliance alliance) {
    Pose pose = new Pose(x, y);
    return alliance == Alliance.RED ? pose.mirror() : pose;
}
```

with each call site changed from `new Pose(x, y)` to `p(x, y, alliance)`. This is the one
mechanical edit made each time a fresh Tracer file is pasted in — find/replace `new Pose(`
with `p(`, add `, alliance` before the closing paren. Everything else in the pasted class
(BezierCurve/BezierLine structure, heading interpolation calls, field names) stays exactly
as Tracer emits it.

### Routines.java

A registry mapping a display name to a factory that builds the full auto `Command` from a
`Robot`:

```java
public class Routines {
    public static final Map<String, Function<Robot, Command>> ALL = new LinkedHashMap<>();
    static {
        ALL.put("Basket 3+0", robot -> {
            Paths p = new Paths(robot.follower, robot.alliance);
            return Commands.sequence(
                new DriveToPose(robot.follower, p.line1, 1.0),
                new DriveToPose(robot.follower, p.line2, 1.0),
                new DriveToPose(robot.follower, p.line3, 1.0),
                new DriveToPose(robot.follower, p.line4, 1.0)
            );
        });
        // additional named routines go here
    }
}
```

`LinkedHashMap` preserves menu ordering as entries are added.

### AutoRoutineOpMode.java

Generic OpMode, one instance per selected menu entry:

```java
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
        super.loop(); // CommandOpMode.loop() -> Scheduler.execute()
    }

    @Override
    public void stop() {
        reset(); // CommandOpMode.reset() -> Scheduler.reset()
    }
}
```

Note: `CommandOpMode.init()` is currently a no-op in the base class — `AutoRoutineOpMode`
overrides it fully rather than calling `super.init()`.

### Auto.java

```java
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
```

Mirrors `Tuning.java`'s menu-construction pattern.

## Data flow

1. Driver Station starts `Auto` → SelectableOpMode shows the menu of routine names from
   `Routines.ALL`.
2. Driver picks a routine → `AutoRoutineOpMode(name, factory)` is instantiated and given
   control.
3. `init()` builds `Robot` with alliance defaulted to BLUE.
4. `init_loop()`: telemetry shows `"<name> | Alliance: BLUE"`; gamepad `X` flips
   `robot.alliance` live; repeats until driver presses ▶ START.
5. `start()`: calls `factory.apply(robot)` — builds a fresh `Paths(robot.follower,
   robot.alliance)` (mirrored if RED) and wraps its fields in a `Commands.sequence(new
   DriveToPose(...), ...)`; schedules it via `Scheduler`.
6. `loop()`: `robot.periodic()` (follower.update() + loop timing) then
   `Scheduler.execute()` runs the scheduled command sequence to completion.
7. `stop()`: `Scheduler.reset()`.

## Error handling

- Alliance always initialized to BLUE at `init()` — no unset state.
- If a routine's factory throws (bad `Paths` field reference, null), it surfaces as a
  normal OpMode crash visible in the Driver Station log. No defensive catch — matches
  existing codebase style of not validating internal-only data.
- An empty `Routines.ALL` produces an empty menu; not specially handled.

## Testing

No unit test harness exists for this OpMode code (hardware-dependent FTC SDK code).
Verification is on-robot:
- Run `Auto`, confirm the menu lists every `Routines` entry.
- Confirm the alliance toggle updates telemetry live during init.
- Confirm START runs the correct path, mirrored correctly for RED vs BLUE.
- New routines get a sanity run per alliance before competition, same as any hand-written
  auto today.

## Out of scope

- Automating the `Paths.java` mirror-wrap edit (still a manual find/replace after each
  Tracer regeneration).
- Folder/grouping structure in the menu (flat list for now, `SelectableOpMode` supports
  folders if needed later).
- Any change to `DriveToPose.java` — the prebuilt-`PathChain` constructor added earlier
  already covers this use case.
