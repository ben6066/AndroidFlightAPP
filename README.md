# AndroidFlightAPP
An android application that provides a joystick and 2 slide bars - throttle and rudder that invokes the airplane in the flight gear application.</br>
This website allows the user to upload two .csv files and detect anomalies in different parameters of a flight.</br>
The server was written in JavaScript(Node.js) and the client was written mainly in TypeScript and a bit of HTML and CSS.

## Prerequisites
* Flightgear
* Android Studio

## Getting Started
1. Install the prerequisites
2. Clone the project: ```git clone https://github.com/ben6066/AndroidFlightAPP.git```
3. Open CMD/BASH and type ```ipconfig``` in order to find out your IPv4 address.
4. Open the Flightgear and go to settings, scroll to the end of the page and write this line : ```--telnet=socket,in,10,127.0.0.1,6400,tcp```


#### App Usage
<img src = "https://user-images.githubusercontent.com/58342591/122798489-54544380-d2c9-11eb-8534-4f44c4f084cf.png">
As we can see, you have the joystick itself , the left seek bar that represents the throttle value, and the lower seek bar that represents the rudder.

#### Connecting to the flightgear
In the "insert ip" text box, insert the ip you have found following the ipconfig command, and the port : 6400. 
Wait for the Flightgear to load, and then press the connect button. You will hear the engine start working, then you can start raising a little bit the throttle slide.
When the airplane has reached it's speed you can try to hold down the joystick's inner circle (thats the controller) and hold so it directs downwards.
You will notice the airplane starts to raise and then you can control it how ever you want !
Becareful not to crash like I did !



## Project Structure
<p align="center">
<img src = "https://user-images.githubusercontent.com/56928005/122680158-8f397700-d1f6-11eb-9b03-c22ea3bd5467.PNG">
</p>

## [Explanation Video](https://www.youtube.com/watch?v=lTd08uuWFZ8)
