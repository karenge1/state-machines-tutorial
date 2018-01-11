# State machines tutorial (using LeJOS NXT)
(for more formal definitions and more complex ideas, look up "finite state machines" and "automata theory")

## Bigger picture

### Java concepts
states, interfaces, implementation, classes, splitting work among methods, switch statements, global variables, and more!

### Goals
- Your goal is to make a program that uses the idea (programming paradigm) of states in programming the robot.
- The robot's goal is to find and then push an object while staying within the boundaries of an obstacle course. 

### What are states?
A state is just a type of situation that the robot can be in and that the programmer would find useful to differentiate from other types of situations. In this application choosing states is pretty straight-forward, but the more complicated a problem becomes, the more important it is to choose the right states. For the robot, you can model its journey as consisting of, for example, four states: SEARCHING, SEEKING, and PUSHING.

### Some characteristics of a set of states:
- states should be mutually exclusive (i.e. it is impossible to be in state X and state Y at the same time)
- when in one state, the robot's behavior should be easy to describe (otherwise consider splitting up the state into different ones, each with simpler-to-describe required behaviors)
- each state has a single entry point
- all actions of the robot are directly tied to the state that it is in, and states cannot share local variable data (i.e. states are self-contained)

## Code!

### The challenge
Your robot will be placed in a terrain with "walls" denoted by black and ground that can be travelled on denoted by white. It is looking to find, and then push away an object that will be at its eye level. 

### Requirements
- implement the FeatureListener and SensorPortListener interfaces
- define some states for the robot and use them correctly
- Do not have everything in one or two methods!
- Add more awesome
