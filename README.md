# Particle-Gravity-Simulation
This project simulates the movement of particles in 3D space with a given starting configuration of particles of a certain mass, size, 
velocity and position. Particles move through out a 3D space by interacting with each others gravitational force and particle collisions.
There are three programs included in this repository. The first one is GravitySimulation, which simulates, often slowly, the movement and
interaction of the particles. It is recommended to let the program run for a couple hours for better results. The second one is the
GravitySimulationPlayback, which then plays back a simulation. These two programs have a display with the user can move around in using
the The last program will generate an initial configuration into a file, which
then can later be used for simulating.

How to run:
  Gravity Simulation
    Run the the .jar file with two argument, the first being the pathname of the file which contains the initial configuration of
    particles. If there are configurations in the file in will load the latest. The second arguement is the pathname of the destination
    of the recording of the simulation. The program will record a fraction of the frames into this file, each starting with a time stamp
    and ending with an asterisk all on separate lines.
    Ex: Java -jar GravitySimulation.jar source destination
    
  Playback
    Run the .jar file with one argument, a pathname to a file which contains a prerecorded simulation. The program will then go through
    each frame and display it, creating fluid motion of the particles.
    Ex: Java -jar GravitySimulationRecorder.jar source
    
  World Generator
    Run the .jar file with one argument, a pathname to a file which will be overritten with a random starting configuration of particles.
    Ex: Java -jar GravitySimulationGenerator.jar destination
    
Config File format:
  Every file can have one or more time instances, which are the data of the particles at a given moment of time. Each instance begins
  with a number representing the time of this instances. Then on the following lines are lines are numbers, separated by commas, which
  are the actual data. Each line is one particle. The first value is the mass, followed by readius, x-component of velocity (double),
  y-component of velocity (double), z-component of velocity (double), x-component of the particle's position (double), y-component of
  the particle's position (double), and z-component of the particle's position (double). Then after the last particle of the instance,
  on a new line an asterisk is placed. Then the next instance can be placed underneath the previous. The time of each should be
  sequential. The recorder program uses this format.
  Ex:
  
  1.0
  4.0, 8.6, 4.5, -4.6, -3.0, 6.7, -4.6, -10.4
  5.0, 8.5, 9.5, -4.6, -3.0, 46.3, -4.6, -24.1
  5.0, 40.0, 4.5, -4.6, -3.0, 6.3, -34.6, -37.0
  *
  (another instance)
  
    
    
