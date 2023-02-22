# DBSCAN implementation for analyzing image

<div style="display:flex;">
<img src="https://github.com/Pomog/DBSCAN/blob/master/screenshot.png?raw=true" alt="example" style="width:25%;">

<img src="https://github.com/Pomog/DBSCAN/blob/master/clusterization1.png?raw=true" style="width:25%;">

<img src="https://github.com/Pomog/DBSCAN/blob/master/clusterization2.png?raw=true" alt="example" style="width:25%;">
</div>

# Example (YouTube video)
[![Video](https://img.youtube.com/vi/xTJlGgTdvog/0.jpg)](https://www.youtube.com/watch?v=xTJlGgTdvog)

# Settings:
In the Demo class, on <b>line 14</b>,  set the repetition property to the number of cycles for the image analysis.  
In the DBScan class, on <b>line 27</b>,  
set the radius property to the radius of the circle within which all points except one will be deleted,  
reducing the number of points and speeding up the analysis.  
Lines 28 and 29 contain the epsilon and minpts properties, which are standard settings for DBScan analysis.

# Instructions for using Java code:
1. The program is optimized for defining clusters and selecting them using the mouse.
2. Run the main method of the Demo class.
3. Use the Alt+Tab keys to switch to the EVE Online window.
4. Using the Alt+Tab keys, open a transparent JFrame window on top of the EVE Online window.
5. Click the lower left corner of the area you want to analyze.
6. Click the upper right corner of the area you want to analyze.
7. Click the "Submit" button to start the analysis.
