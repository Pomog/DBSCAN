# DBSCAN implementation for analyzing image
Learn Basic Programming Skills with Java.
This project provides an implementation of DBSCAN and Graham scan algorithms for image analysis.  
It also includes an introduction to the Robot class in Java.

<div style="display:flex;">
<img src="https://github.com/Pomog/DBSCAN/blob/master/screenshot.png?raw=true" alt="example" style="width:20%;">

<img src="https://github.com/Pomog/DBSCAN/blob/master/clusterization1.png?raw=true" style="width:20%;">

<img src="https://github.com/Pomog/DBSCAN/blob/master/clusterization2.png?raw=true" alt="example" style="width:20%;">

<img src="https://github.com/Pomog/DBSCAN/blob/master/calculatedHull.png?raw=true" alt="example" style="width:20%;">
</div>

# Example (YouTube video)
[![Video](https://img.youtube.com/vi/xTJlGgTdvog/0.jpg)](https://www.youtube.com/watch?v=xTJlGgTdvog)

# Settings:
In the Demo class, on <b>line 14</b>,  
set the repetition property to the number of cycles for the image analysis.  
In the DBScan class, on <b>line 27</b>,  
set the radius property to the radius of the circle within which all points except one will be deleted,  
reducing the number of points and speeding up the analysis.  
<b>Lines 28 and 29</b> contain the epsilon and minpts properties, which are standard settings for DBScan analysis.

# Instructions for Usage:
1. The program is optimized for defining clusters and selecting them using the mouse.
2. Run EVEonline (computer game) and choose Discovery Project.
3. Run the main method of the Demo class.
4. Use the Alt+Tab keys to switch to the EVE Online window.
5. Using the Alt+Tab keys, open a transparent JFrame window on top of the EVE Online window.
6. Click the lower left corner of the area you want to analyze.
7. Click the upper right corner of the area you want to analyze.
8. Click the "Submit" button to start the analysis.

# DBSCAN
The DBSCAN algorithm is implemented in the **DBScan** class.  
This class provides a method to cluster a set of points based on their density.  
To use the **DBScan** class, you need to provide the following parameters:  
input double[][] 2d Array of Point (x and y coordinates)  
<b>eps:</b> the radius of the neighborhood around each point
<b>minPts:</b> the minimum number of points required to form a cluster  
The DBSCAN class returns Map<String, List<Point>> a list of clusters besides the noise, where each cluster is represented as a list of points.

# Graham Scan
The Graham scan algorithm is implemented in the **ConvexHull**.  
This class provides a method to compute the convex hull of a set of points in the plane.  
To use the GrahamScan class, you need to provide a list of points as input.  
The **ConvexHull** class returns List<Point> a list of points that form the convex hull.

# Unsolved Issues
1. The implemented DBSCAN algorithm currently does not consider color when clustering data points.
2. The values for the parameters eps and minPts are currently hard-coded. In order to achieve better results, they should (probably) be dynamically adjustable based on the specific dataset being used.
3. The code was not refactored.
