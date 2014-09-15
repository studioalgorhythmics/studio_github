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

import controlP5.*;
ControlP5 cp5;
String textValue = "";


int [] values = new int[48]; //number of pins

//int[] ruleset = {0,1,1,1,1,0,1,1};   // Rule 222  
//int[] ruleset = {0,1,1,1,1,1,0,1};   // Rule 190  
int[] ruleset = {0,1,1,1,1,0,0,0};   // Rule 30  
//int[] ruleset = {0,1,1,1,0,1,1,0};   // Rule 110
//int[] ruleset = {0,1,0,1,1,0,1,0};   // Rule 90

CA ca;   // An object to describe a Wolfram elementary Cellular Automata

void setup() {
  size(displayWidth, displayHeight);

  for(int i = 0; i < values.length; i++)
  values[i] = Arduino.LOW;  
  
  println(Arduino.list());
  arduino = new Arduino(this, Arduino.list()[2], 57600);
  for (int i = 0; i <= 54; i++) //set the arduino digital pins as outputs
  arduino.pinMode(i, Arduino.OUTPUT); 
 
  PFont font = createFont("arial",40);
 
  cp5 = new ControlP5(this);
  
  cp5.addTextfield("input")
     .setPosition(20,20)
     .setSize(100,40)
     .setFont(font)
     .setFocus(true)
     .setColor(color(0))
     .setColorActive(color(0))
     .setColorBackground(color(255))
     .setColorCaptionLabel(color(5))
     ;
     
  frameRate(18);
  background(255);
  //int[] ruleset = {0,1,1,1,1,0,1,1};   // Rule 222  
 // int[] ruleset = {0,1,1,1,1,1,0,1};   // Rule 190  
  //int[] ruleset = {0,1,1,1,1,0,0,0};   // Rule 30  
  //int[] ruleset = {0,1,1,1,0,1,1,0};   // Rule 110
  //int[] ruleset = {0,1,0,1,1,0,1,0};   // Rule 90
  
  ca = new CA(ruleset);                 // Initialize CA
}

void draw() {
  background(255);
    
  ca.display();          // Draw the CA
  ca.generate();
  
  int[] ruleset = {0,1,1,1,1,1,0,1};
  delay(1000);
  int[] ruleset = {0,1,0,1,1,0,1,0}; 
}

public void input(String theText) {
 rulegenerator((byte)Integer.parseInt(theText));
  // automatically receives results from controller input
  println((byte)Integer.parseInt(theText)+"theText");
}


void rulegenerator(byte rulenumber) {
for (int i = 0; i < 8; i++, rulenumber >>= 1) {
ruleset[i] = (rulenumber & 1) == 0 ? 0 : 1;
}
  ca = new CA(ruleset);                 // Initialize CA
}


