import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import cc.arduino.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class WolframCA_version1_working extends PApplet {

// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// Wolfram Cellular Automata
// Simple demonstration of a Wolfram 1-dimensional cellular automata
// with the system scrolling by
// Also implements wrap around



Arduino arduino;


ControlP5 cp5;
String textValue = "";


int [] values = new int[50]; //number of pins

//int[] ruleset = {0,1,1,1,1,0,1,1};   // Rule 222  
//int[] ruleset = {0,1,1,1,1,1,0,1};   // Rule 190  
int[] ruleset = {0,1,0,0,0,0,0,0};   // Rule 2  
//int[] ruleset = {0,1,1,1,0,1,1,0};   // Rule 110
//int[] ruleset = {0,1,0,1,1,0,1,0};   // Rule 90

CA ca;   // An object to describe a Wolfram elementary Cellular Automata

public void setup() {
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
     
  frameRate(40);
  background(255);
  //int[] ruleset = {0,1,1,1,1,0,1,1};   // Rule 222  
 // int[] ruleset = {0,1,1,1,1,1,0,1};   // Rule 190  
  //int[] ruleset = {0,1,1,1,1,0,0,0};   // Rule 30  
  //int[] ruleset = {0,1,1,1,0,1,1,0};   // Rule 110
  //int[] ruleset = {0,1,0,1,1,0,1,0};   // Rule 90
  
  ca = new CA(ruleset);                 // Initialize CA
}

public void draw() {
  background(255);
    
  ca.display();          // Draw the CA
  ca.generate();
}

public void input(String theText) {
 rulegenerator((byte)Integer.parseInt(theText));
  // automatically receives results from controller input
  println((byte)Integer.parseInt(theText)+"theText");
}


public void rulegenerator(byte rulenumber) {
for (int i = 0; i < 8; i++, rulenumber >>= 1) {
ruleset[i] = (rulenumber & 1) == 0 ? 0 : 1;
}
  ca = new CA(ruleset);                 // Initialize CA
}


// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// Wolfram Cellular Automata

// A class to manage the CA

class CA {

  int generation;  // How many generations?
  int[] ruleset;   // An array to store the ruleset, for example {0,1,1,0,1,1,0,1}
  int w = 28; // cell size in pxw
  int[][] matrix;  // Store a history of generations in 2D array, not just one

  int cols = 50;
  int rows = 50;

  CA(int[] r) {
    ruleset = r;
    matrix = new int[cols][rows];
    restart();
  }
    

  // Make a random ruleset
  public void randomize() {
    for (int i = 0; i < 8; i++) {
      ruleset[i] = PApplet.parseInt(random(2));
    }
  }

  // Reset to generation 0
  public void restart() {
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        matrix[i][j] = 0;
      }
    }
   // matrix[cols/2][0] = 1;    // We arbitrarily start with just the middle cell having a state of "1"
    
    for (int i = 0; i < cols; i++) {
     matrix[i][0] = (int) random(2);
    }
      
    generation = 0;
  }


  // The process of creating the new generation
  public void generate() {

    // For every spot, determine new state by examing current state, and neighbor states
    // Ignore edges that only have one neighor
    for (int i = 0; i < cols; i++) {
      int left  = matrix[(i+cols-1)%cols][generation%rows];   // Left neighbor state
      int me    = matrix[i][generation%rows];       // Current state
      int right = matrix[(i+1)%cols][generation%rows];  // Right neighbor state
      matrix[i][(generation+1)%rows] = rules(left, me, right); // Compute next generation state based on ruleset
      
      if (rules(left, me, right) == 1) {arduino.digitalWrite(i+2, Arduino.HIGH); }
      else {arduino.digitalWrite(i+2, Arduino.LOW);}  
  }
    generation++;
  }
  
  // This is the easy part, just draw the cells, fill 255 for '1', fill 0 for '0'  
  public void display() {
    int offset = generation%rows;
    for (int i = 0; i < cols; i++) { 
      for (int j = 0; j < rows; j++) {
        int y = j - offset;
        if (y <= 0) y = rows + y;
        // Only draw if cell state is 1
        if (matrix[i][j] == 1) {
          fill(0);
          noStroke();
          //rect(i*w, (y-1)*w, w, w);       
          rect((displayWidth/2)-((cols/2)*w)+w*i, (y-1)*w, w, w); // moveing the cells into the center           
      }
      }
    }
  }  
  
  // Implementing the Wolfram rules
  // This is the concise conversion to binary way
  public int rules (int a, int b, int c) {
    String s = "" + a + b + c;
    int index = Integer.parseInt(s, 2);
    return ruleset[index];
  }

  // The CA is done if it reaches the bottom of the screen
  public boolean finished() {
    if (generation > height/w) {
      return true;
    } 
    else {
      return false;
    }
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "WolframCA_version1_working" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
