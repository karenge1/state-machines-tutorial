
/**
 * Write a description of class Program1 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import lejos.nxt.*;
import lejos.util.*;

public class Program2
{
   public static void main( String[] args)
   {
       LCD.drawString("Program 1", 0,0);
       Motor.A.setSpeed(2);
       Delay.msDelay(2);
       LCD.drawString(String.valueOf(Motor.A.getTachoCount()),0,0);
       Motor.A.stop();
       LCD.drawString(String.valueOf(Motor.A.getTachoCount()),0,0);
       Motor.A.backward();
       while(Motor.A.getTachoCount() > 0) {
       LCD.drawInt(Motor.A.getTachoCount(),0,2);
       LCD.drawInt(Motor.A.getTachoCount(),0,3);
        }
       Motor.A.stop();
   }
}
