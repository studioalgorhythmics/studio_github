// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// Wolfram Cellular Automata
// Simple demonstration of a Wolfram 1-dimensional cellular automata
// with the system scrolling by
// Also implements wrap around

import processing.serial.*;
import cc.arduino.*;
Arduino arduino;

int [] values = new int[48]; //number of pins

CA ca;   // An object to describe a Wolfram elementary Cellular Automata

void setup() {
  size(displayWidth, displayHeight);

  for(int i = 0; i < values.length; i++)
  values[i] = Arduino.LOW;  
  println(Arduino.list());
  arduino = new Arduino(this, Arduino.list()[2], 57600);
  for (int i = 2; i <= 50; i++)
  arduino.pinMode(i, Arduino.OUTPUT);  
  
  frameRate(10);
  background(255);
  //int[] ruleset = {0,1,1,1,1,0,1,1};   // Rule 222  
  //int[] ruleset = {0,1,1,1,1,1,0,1};   // Rule 190  
  int[] ruleset = {0,1,1,1,1,0,0,0};   // Rule 30  
  //int[] ruleset = {0,1,1,1,0,1,1,0};   // Rule 110
  //int[] ruleset = {0,1,0,1,1,0,1,0};   // Rule 90
  
  ca = new CA(ruleset);                 // Initialize CA
}

void draw() {
  background(255);
  ca.display();          // Draw the CA
  ca.generate();
}
