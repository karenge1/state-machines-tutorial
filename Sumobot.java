import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.objectdetection.*;

public class Sumobot implements FeatureListener, SensorPortListener
{
    enum State
    {
        SEARCHING,
        FOUND,
        SEEKING,
        PUSHING
    }

    private DifferentialPilot pilot;
    private TouchSensor leftBumper;
    private TouchSensor rightBumper;
    private UltrasonicSensor sonicSensor;
    private LightSensor lightSensor;
    private RangeFeatureDetector rangeDetector;
    private TouchFeatureDetector leftBumpDetector;
    private TouchFeatureDetector rightBumpDetector;

    private State state;
    private boolean avoidEdge;

    public Sumobot()
    {
        state = State.SEARCHING;
        avoidEdge = false;

        pilot = new DifferentialPilot(56.0 /* wheel diameter in mm */, 160.0 /* wheel base in mm */,
            Motor.B, Motor.C);

        leftBumper = new TouchSensor(SensorPort.S2);
        rightBumper = new TouchSensor(SensorPort.S4);
        sonicSensor = new UltrasonicSensor (SensorPort.S3);
        lightSensor = new LightSensor(SensorPort.S1);

        rangeDetector = new RangeFeatureDetector(sonicSensor, 80 /* max distance in mm */,
            500 /* polling interval in ms */);
        rangeDetector.enableDetection(false);
        rangeDetector.addListener(this);

        leftBumpDetector = new TouchFeatureDetector(leftBumper); 
        leftBumpDetector.enableDetection(false);
        leftBumpDetector.addListener(this);

        rightBumpDetector = new TouchFeatureDetector(rightBumper); 
        rightBumpDetector.enableDetection(false);
        rightBumpDetector.addListener(this);

        SensorPort.S1.addSensorPortListener(this);
    }

    public void calibrate()
    {
        System.out.println("Place light sensor on white and press enter");
        Button.ENTER.waitForPressAndRelease();
        lightSensor.calibrateHigh();

        LCD.clear();

        System.out.println("Place light sensor on black and press enter");
        Button.ENTER.waitForPressAndRelease();
        lightSensor.calibrateLow();
    }

    public void go()
    {
        state = State.SEARCHING;
        avoidEdge = false;
        rangeDetector.enableDetection(true);
        leftBumpDetector.enableDetection(true);
        rightBumpDetector.enableDetection(true);

        do
        {
            if(avoidEdge)
            {
                System.out.println("Avoiding edge...");
                pilot.travel(-100.0, false);
                pilot.rotate(120.0, false);
                state = State.SEARCHING;
                avoidEdge = false;
            }

            switch(state)
            {
                case SEARCHING:
                {
                    System.out.println("Searching...");
                    pilot.setRotateSpeed(60 /* degrees per second */);
                    pilot.rotateLeft();
                }
                break;

                case FOUND:
                {
                    System.out.println("Found...");
                    pilot.rotate(-8.0, false);
                    state = State.SEEKING;

                }
                break;

                case SEEKING:
                {
                    System.out.println("Seeking...");
                    pilot.setTravelSpeed(pilot.getMaxTravelSpeed());
                    pilot.forward();
                }
                break;

                case PUSHING:
                {

                    pilot.setTravelSpeed(pilot.getMaxTravelSpeed() / 2);

                    if(leftBumper.isPressed() || rightBumper.isPressed())
                    {
                        System.out.println("Pushing...");
                        pilot.forward();
                    }
                    else
                    {
                        state = State.SEARCHING;
                    }
                }
                break;
            }

        }
        while(Button.ENTER.isUp());
    }

    public void featureDetected(Feature feature, FeatureDetector detector)
    {
        if(detector == rangeDetector)
        {
            int range = (int)feature.getRangeReading().getRange();
                    Sound.playTone(1200 - (range * 10), 100);
            System.out.println("Range:" + range);
            if(state == State.SEARCHING)
            {
                state = State.FOUND;
            }
        }
        else if(detector == leftBumpDetector || detector == rightBumpDetector)
        {
            state = State.PUSHING;
        }

    }

    public void stateChanged(SensorPort sensor, int oldValue, int newValue) 
    {
        if( lightSensor.getLightValue() < 30)
        {
            avoidEdge = true;
        }
    }

    public static void main(String[] args)
    {
        Sumobot sumobot = new Sumobot();
        sumobot.calibrate();

        LCD.clear();

        System.out.println("Press enter to start");
        Button.ENTER.waitForPressAndRelease();

        sumobot.go();
    }
}