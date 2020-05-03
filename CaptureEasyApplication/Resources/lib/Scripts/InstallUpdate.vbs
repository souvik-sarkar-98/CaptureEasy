Set filesys=CreateObject("Scripting.FileSystemObject")
Set shell=CreateObject("Wscript.shell")
WScript.Sleep(3000)
Set fopen=filesys.OpenTextFile (WScript.Arguments.Item (2),8,True)
		
If Not filesys.FileExists (WScript.Arguments.Item (0)) Then 
		MsgBox "Update Failed "&vbCrLf&"Source file : "&WScript.Arguments.Item (0)&" Not Found",ok,"File Not Found"
Else
		On Error Resume Next
		WScript.Sleep(2000)
		shell.Exec("Taskkill /f /im javaw.exe")
		shell.Exec("Taskkill /f /im javaw.exe")
		filesys.DeleteFile WScript.Arguments.Item (1)
		If Err.Description="Permission denied" Then
			shell.Exec("Taskkill /f /im javaw.exe")
			filesys.DeleteFile WScript.Arguments.Item (1)
		End If
		If filesys.FileExists (WScript.Arguments.Item (1)) Then
			fopen.WriteLine ("deleted=false")
		Else
			fopen.WriteLine ("deleted=true")
		End If
		WScript.Sleep(1000)
		filesys.CopyFile WScript.Arguments.Item (0), WScript.Arguments.Item (1)
		If filesys.FileExists (WScript.Arguments.Item (1)) Then
			fopen.WriteLine ("copied=true")
 		MsgBox "Successfully Installed. Please resatrt application now",ok,"Success"
		Else
			fopen.WriteLine ("copied=false")
			MsgBox  "Installation Failed. Unable to copy "&WScript.Arguments.Item (0)&" to "&WScript.Arguments.Item (1)& "  Please Try Again.","Failed"
		End If
End If 