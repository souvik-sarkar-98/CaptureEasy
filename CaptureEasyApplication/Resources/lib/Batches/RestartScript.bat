@echo off
echo --------------------------------------------------------------------------------------
echo           PLEASE DON'T CLOSE THIS WINDOW. IT WILL TAKE ONLY 5 SECONDS.
echo --------------------------------------------------------------------------------------
echo           		   	KILLING ALL JAVA PROCESS 
echo --------------------------------------------------------------------------------------
Taskkill /f /im javaw.exe
Taskkill /f /im javaw.exe
timeout 3
echo --------------------------------------------------------------------------------------
echo           		   	   DELETING OLD JARS
echo --------------------------------------------------------------------------------------
del C:\Users\USER\git\CaptureEasyApp\CaptureEasyApplication\Resources\lib\Jars\SRC.jar
timeout 2
echo --------------------------------------------------------------------------------------
echo           		   	    COPYING NEW JARS
echo --------------------------------------------------------------------------------------
copy C:\Users\USER\git\CaptureEasyApp\CaptureEasyApplication\Resources\Downloads\CaptureEasy-V_0.2.jar C:\Users\USER\git\CaptureEasyApp\CaptureEasyApplication\Resources\lib\Jars\SRC.jar
timeout 1
echo --------------------------------------------------------------------------------------
echo           		   	  RESTARTING APPLICATION
echo --------------------------------------------------------------------------------------
START C:\Users\USER\git\CaptureEasyApp\CaptureEasyApplication\CaptureEasy.exe
