FormatTime, CurrentDateTime,, yyyy.MM.dd.HH.mm.ss 
IfExist, dummy.ahk
{
Run, dummy.ahk
Sleep, 1000
FileMove, dummy.ahk, remoteAction_ran/%CurrentDateTime%_dummy.ahk
}
Sleep, 300000
Reload