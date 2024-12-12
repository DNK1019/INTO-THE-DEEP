package org.firstinspires.ftc.teamcode;

public class DriveConstants {
    // ============================
    //             ARM
    // ============================

    // Arm state machine
    public enum ArmStates {
        UP,
        OUT,
        IN,
        DOWN,
        HANG_PIVOT,
        HANG_RETRACT,
        REST,
    }

    // Arm maximums
    public static final int EXTENSION_LIMIT = 2150;
    public static final int PITCH_LIMIT = 1275;

    // Hang positions
    public static final int HANG_EXTENSION = -620;
    public static final int HANG_PITCH = 1000;

    // ============================
    //             CLAW
    // ============================

    // Claw positions
    public static final double CLAW_OPEN_POSITION = .7;
    public static final double CLAW_CLOSE_POSITION = .92;

    // "Wrist" positions
    public static final double PIVOT_FORWARD = 0;
    public static final double PIVOT_BACK = 0;
    public static final double PIVOT_OTHER = 0;
}
