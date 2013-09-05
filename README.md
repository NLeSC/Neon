eSight
======

OpenGL and Java (JOGL) based Object-Oriented Visualization library for OpenGL 3.0+ with GLSL shader support and vector/matrix math library. 

Aims
----

The eSight library aims to make it easy for users to create OpenGL 3.0+ based visualizations in Java. This is especially needed in cases 
where programmable shaders can make a significant difference in performance. This ensures that the resulting visualization can be interactive, 
where it otherwise would need to be pre-rendered to achieve the same visual quality.

Demo's
------

Visualization of a simulation of the evolution of embedded star cluster.
http://www.youtube.com/watch?v=yE8LL1rE880

Visualization of Climate Modeling simulations.
http://www.youtube.com/watch?v=MCJCCGEI550

Planet Formation through solarsystem dust mass density.
http://www.youtube.com/watch?v=dNvDsFQGt5o

Limitations / System requirements
---------------------------------

The library assumes hardware that can support OpenGL 3.0 or greater. It is also written for Java 1.6+. This limits the use of this library 
for both Desktop and mobile devices, except through remote rendering (for which direct support will be implemented at a later date).

Useage
------

To create a new eSight-powered project without including all of the eSight source code (just the JAR):
1. Compile by running ant in the root directory of the eSight project.

2. Create a new java 1.6 project.

3. Copy all of the files in the dist/ folder to your lib/ folder.

4. Copy the images/ shaders/ and fonts/ directories to your new project.

5. Copy the settings.properties and log4j.properties files to the root folder of your project.

6. Include all the jar files from the lib/ folder and its jogl/ subfolder in your classpath.

7. Include the root folder of your new project in your classpath.

8. Implement an extension of ESightGLEventListener.

9. Implement a main class that creates a new ESightNewtWindow, using your new GLEventlistener as a parameter.
 
As a starting point for the implementations in step 8 and 9, you can use the HelloWorldExample, as mentioned below.

PS: If you want to create a project with all of the source code included, please make sure to include the root directory of the eSight library.

Example implementation
----------------------

Example implementations can be found in the examples directory. Typical implementations consist of at least 2 classes, namely a main class, and a class that extends the ESightGLEventListener class.
Taking the HelloWorldExample as a model implementation, the two files indicated are HelloWorldExample.java for the main class, and HelloWorldGLEventListener.java as the extension of the ESightGLEventListener class.

To run the HellowWorld example (as well as all other example implementations), the root directory of the project should be included in the classpath. This is done because the projects need some additional files, like the GLSL shaders and the font files. Since the eSight library searches for these files in a directory relative to the directories on the classpath, store these in shaders/ and fonts/ respectively. 

The Latest Version
------------------
Details of the latest version can be found on the eSight library web site at:  

<https://github.com/NLeSC/eSight>

Javadoc
-------

The javadoc of eSight library is available in "doc/index.html".

Licensing
---------

This software is licensed under the terms you may find in the file named "LICENSE" in this directory. The licence information can also be found at  
<http://www.apache.org/licenses/LICENSE-2.0>

Notice
------

If you find this software useful, please give credit to the Netherlands eScience center (www.esciencenter.nl) for developing it.
