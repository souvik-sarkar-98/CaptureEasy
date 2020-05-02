srcpath="C:\Users\USER\git\CaptureEasyApp\CaptureEasyApplication\Resources\Downloads\CaptureEasy-V_0.2.jar"
destpath="C:\Users\USER\git\CaptureEasyApp\CaptureEasyApplication\Resources\lib\Jars\SRC.jar"
apppath="C:\Users\USER\git\CaptureEasyApp\CaptureEasyApplication\Application.exe"
Set filesys=CreateObject("Scripting.FileSystemObject")
Set shell=CreateObject("Wscript.shell")
If Not filesys.FileExists (srcpath) Then 
MsgBox "Update Failed "&vbCrLf&"Source file : "&srcpath&" Not Found"&vbCrLf&"Starting previous version",ok,"File Not Found"
Else
On Error Resume Next
WScript.Sleep(3000)
If filesys.FileExists (destpath) Then
filesys.DeleteFile destpath
If Err.Description="Permission denied" Then
shell.Exec("Taskkill /f /im javaw.exe")
filesys.DeleteFile destpath
End If
End If
shell.Popup "File Deleted", 1,"Alert"
filesys.CopyFile srcpath, destpath
shell.Popup "New File Copied", 1,"Alert"
WScript.Sleep(2000)
End If 
If Not filesys.FileExists (apppath) Then 
MsgBox "Restart Failed "&vbCrLf&"Application file : "&apppath&" Not Found"&vbCrLf&"Please launch manually",ok,"File Not Found"
Else
shell.Run apppath,1,True
End If 