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

Compile/export all files in the src, doc, lib, images and fonts directories into "dist/eSight.jar". Place eSight.jar in your classpath. Implementations on top of this library should extend at least the classes:

* ESightGLEventListener
* ESightNewtWindow

An annotated and as-complete-as-possible example implementation can be found in the examples directory.

Example implementation
----------------------

An example implementation can be found in the examples directory. It consists of 5 classes, of which ESightExample is the main class.

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