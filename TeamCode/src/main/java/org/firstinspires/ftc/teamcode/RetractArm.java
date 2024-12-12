package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class RetractArm extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        // Declare OpMode members.

        DcMotor armExtendA = hardwareMap.get(DcMotor.class, "armExtendA");
        DcMotor armExtendB = hardwareMap.get(DcMotor.class, "armExtendB");
        DcMotor armPitch = hardwareMap.get(DcMotor.class, "armPitch");



        armPitch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armPitch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armExtendA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtendA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armExtendB.setDirection(DcMotorSimple.Direction.REVERSE);

        int extensionTarget = 0;
        int pitchTarget = 0;

        // Wait for the game to start (driver presses START)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            pitchTarget += (gamepad2.dpad_up || gamepad1.dpad_up ? 10 : 0) - (gamepad2.dpad_down || gamepad1.dpad_down ? 10 : 0);

            extensionTarget += (gamepad2.dpad_right || gamepad1.dpad_right ? 10 : 0) - (gamepad2.dpad_left || gamepad1.dpad_left ? 10 : 0);

            double pow = (extensionTarget - armExtendA.getCurrentPosition()) * 0.01;
            armExtendA.setPower(pow);
            armExtendB.setPower(pow);


            armPitch.setPower((pitchTarget - armPitch.getCurrentPosition()) * 0.01);

            telemetry.addLine("Warning: No limits, this program is potentially dangerous.");
            telemetry.addData("Arm Pitch", armPitch.getCurrentPosition());
            telemetry.addData("Arm Extend", armExtendA.getCurrentPosition());


            // Show the elapsed game time and wheel power.
            telemetry.update();
        }
    }
}
