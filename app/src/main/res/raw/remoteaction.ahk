
#Persistent
SetTimer, CheckScript, 400 ; Edit this value to adjust time between checks
Return

CheckScript:
	if FileExist("dummy.ahk")
	{
	FormatTime, CurrentDateTime,, yyyy.MM.dd.HH.mm.ss
	Run, dummy.ahk
	sleep 1000
	FileMove, dummy.ahk, remoteAction_ran/%CurrentDateTime%_dummy.ahk
	Sleep, 1000
	}
	Return