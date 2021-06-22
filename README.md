# AndroidFlightAPP
An android application that connects to a flight simulator and provides a joystick and 2 seek bars representing the throttle and rudder.</br>
Those features invoke the airplane in the flight gear application.</br>

## Prerequisites
* FlightGear
* Android Studio

## Getting Started
1. Install the prerequisites
2. Clone the project: ```git clone https://github.com/ben6066/AndroidFlightAPP.git```
3. Open CMD/BASH and type ```ipconfig``` in order to find out your IPv4 address.
4. Open the Flightgear and go to settings, scroll to the end of the page and write this line:
```--telnet=socket,in,10,127.0.0.1,6400,tcp```

## Running the application and connecting to the FlightGear
1. Run FlightGear.exe and wait for it to load (until you see the airplane). Change the view by clicking ```V``` multiple times.
2. In the "Insert IP" edit text, insert your IP (the one you found following the ```ipconfig``` command) and the insert 6400 as the port.
3. Click the "Connect" button and move the left and bottom sliders in order to check the app is connected to the simulator.

## App Usage - Flying the plane
<img src = "https://user-images.githubusercontent.com/58342591/122798489-54544380-d2c9-11eb-8534-4f44c4f084cf.png">
As seen above, there is a joystick, a seek bar that represents the throttle (the left one), and the lower seek bar that represents the rudder.
We also added a "Start" button, instead of clicking "Auto Start" on the flightgear application.

After the application is connected to the flightgear, press the "Start" button, and the plane will move a bit.
You can start playing with the throttle and the rudder in order to start moving on the runway, and when you reached a high speed, lift the plane by dragging the inner circle of the joystick down.

### Project Structure
<p align="center">
<img src = "https://user-images.githubusercontent.com/56928005/122680158-8f397700-d1f6-11eb-9b03-c22ea3bd5467.PNG">
</p>

## [Explanation Video](https://www.youtube.com/watch?v=lTd08uuWFZ8)
