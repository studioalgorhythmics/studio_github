import processing.serial.*;
import cc.arduino.*;
Arduino arduino;
CA ca; // An object to describe a Wolfram elementary Cellular Automata
int delay = 0;
int iterations = 100; // working?
int [] values = new int[52]; //number of pins

void setup() {
for(int i = 0; i < values.length; i++)
values[i] = Arduino.LOW;
println(Arduino.list());
arduino = new Arduino(this, Arduino.list()[4], 57600);
for (int i = 0; i <= 51; i++)
arduino.pinMode(i, Arduino.OUTPUT);
size(1050, 1000);
background(255);
int[] ruleset = {
0, 1, 0, 1, 1, 0, 1, 0
}; // An initial rule system
ca = new CA(ruleset); // Initialize CA
frameRate(20);
}

void draw() {
for(int i = 0; i < values.length; i++)
values[i] = Arduino.LOW;
ca.rulegenerator((byte)190);
ca.display(); // Draw the CA
ca.generate();
ca.playsound();
// if (ca.finished()) {
if (false) { // If we're done, clear the screen, pick a new ruleset and restart
delay++;
if (delay > 30) {
background(255);
//ca.randomize();
ca.restart();
delay = 0;
}
}
}

