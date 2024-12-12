package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class DriveSolo extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        // Declare OpMode members.
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        DcMotor armExtendA = hardwareMap.get(DcMotor.class, "armExtendA");
        DcMotor armExtendB = hardwareMap.get(DcMotor.class, "armExtendB");
        DcMotor armPitch = hardwareMap.get(DcMotor.class, "armPitch");

        Servo pivot = hardwareMap.get(Servo.class, "Pivot");
        Servo claw = hardwareMap.get(Servo.class, "Claw");


        GamepadEx armController = new GamepadEx(gamepad1);
        ToggleButtonReader armToggle = new ToggleButtonReader(armController, GamepadKeys.Button.RIGHT_BUMPER);
        ToggleButtonReader pivotToggle = new ToggleButtonReader(armController, GamepadKeys.Button.LEFT_BUMPER);


        armPitch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armPitch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armExtendA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtendA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armExtendB.setDirection(DcMotorSimple.Direction.REVERSE);

        int extensionTarget = 0;
        int pitchTarget = 0;

        double pivotTarget;

        DriveConstants.ArmStates armState = DriveConstants.ArmStates.REST;

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses START)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double x = gamepad1.left_stick_x * 1.1;
            double y = -gamepad1.left_stick_y;
            double rx  =  Math.pow(gamepad1.right_stick_x, 2) * Math.signum(gamepad1.right_stick_x);

            double denominator = Math.max(Math.abs(x) + Math.abs(y) + Math.abs(rx), 1);
            frontLeft.setPower((y + x + rx) / denominator);
            backLeft.setPower((y - x + rx) / denominator);
            frontRight.setPower((y - x - rx) / denominator);
            backRight.setPower((y + x - rx) / denominator);




            if (gamepad1.x) {
                armState = DriveConstants.ArmStates.UP;
            }
            if (gamepad1.b) {
                armState = DriveConstants.ArmStates.IN;
            }

            if (armState == DriveConstants.ArmStates.REST) {
                extensionTarget += (gamepad1.dpad_right ? 25 : 0) - (gamepad1.dpad_left ? 25 : 0);
                pitchTarget += (gamepad1.dpad_up ? 10 : 0) - (gamepad1.dpad_down ? 10 : 0);
            }

            if (armState == DriveConstants.ArmStates.UP) {
                pitchTarget = DriveConstants.PITCH_LIMIT;
                if (Math.abs(pitchTarget - armPitch.getCurrentPosition()) < 10) armState = DriveConstants.ArmStates.OUT;
            }

            if (armState == DriveConstants.ArmStates.OUT) {
                extensionTarget = DriveConstants.EXTENSION_LIMIT;
                if (Math.abs(extensionTarget - armExtendA.getCurrentPosition()) < 10) armState = DriveConstants.ArmStates.REST;
            }

            if (armState == DriveConstants.ArmStates.DOWN) {
                pitchTarget = 0;
                if (Math.abs(pitchTarget - armPitch.getCurrentPosition()) < 10) armState = DriveConstants.ArmStates.REST;
            }

            if (armState == DriveConstants.ArmStates.IN) {
                extensionTarget = 0;
                if (Math.abs(extensionTarget - armExtendA.getCurrentPosition()) < 10) armState = DriveConstants.ArmStates.DOWN;
            }

            extensionTarget = Math.max(Math.min(extensionTarget, DriveConstants.EXTENSION_LIMIT), 0);
            pitchTarget = Math.max(Math.min(pitchTarget, DriveConstants.PITCH_LIMIT), 0);

            double pow = (extensionTarget - armExtendA.getCurrentPosition()) * 0.01;
            armExtendA.setPower(pow);
            armExtendB.setPower(pow);

//            if(gamepad1.a) {
//                pivotTarget = .3;
//            } else {
                pivotTarget = pivotToggle.getState() ? 0 : .3;
//            }

            pivot.setPosition(pivotTarget);

            claw.setPosition(armToggle.getState() ? .7 : .92);

            armPitch.setPower((pitchTarget - armPitch.getCurrentPosition()) * 0.01);

            telemetry.addData("Arm Pitch", armPitch.getCurrentPosition());
            telemetry.addData("Arm Extend", armExtendA.getCurrentPosition());
            telemetry.addData("Power", pow);
            telemetry.addData("Pivot", pivot.getPosition());


            // Show the elapsed game time and wheel power.
            telemetry.update();
            armToggle.readValue();
            pivotToggle.readValue();
        }
    }
}
