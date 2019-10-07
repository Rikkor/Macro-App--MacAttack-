package jbloomfield.macroapp;


import java.util.ArrayList;
import java.util.List;

class MacroPresets {
    MacroPresets(){
        macros = new ArrayList();

        //add presets to list
        macros.add(new Macro("shutdown", "Secs := 30\nSetTimer, CountDown, 1000\n" +
                "MsgBox, 1, System Shutdown,Shutting down in %Secs%, %Secs%\n" +
                "SetTimer, CountDown, Off\n" +
                "\n" +
                "IfMsgBox Ok\n" +
                "  Shutdown, 1\n" +
                "IfMsgBox timeout\n" +
                "  Shutdown, 1\n" +
                "Return\n" +
                "\n" +
                "CountDown:\n" +
                "Secs -= 1\n" +
                "ControlSetText,Static1,Shutting down in %Secs%,System Shutdown ahk_class #32770\n" +
                "Return"));
        macros.add(new Macro("reboot", "Secs := 30\n" +
                "SetTimer, CountDown, 1000\n" +
                "MsgBox, 1, System Reboot, Rebooting in %Secs%, %Secs%\n" +
                "SetTimer, CountDown, Off\n" +
                "\n" +
                "IfMsgBox Ok\n" +
                "  Shutdown, 2\n" +
                "IfMsgBox timeout\n" +
                "  Shutdown, 2\n" +
                "Return\n" +
                "\n" +
                "CountDown:\n" +
                "Secs -= 1\n" +
                "ControlSetText,Static1,Rebooting in %Secs%,System Reboot ahk_class #32770\n" +
                "Return"));

        macros.add(new Macro("sleep", "DllCall(\"PowrProf\\SetSuspendState\", \"int\", 0, \"int\", 0, \"int\", 0)"));
        macros.add(new Macro("monitor off","Sleep 1000\nSendMessage 0x112, 0xF170, 2,,Program Manager"));
        macros.add(new Macro("lock pc","Run rundll32.exe user32.dll`,LockWorkStation"));
        macros.add(new Macro("run program","sendinput, {lwin}\nsleep 1000\nsendinput, %PROGRAM%\nsleep 1000\nsendinput, {enter}"));
        macros.add(new Macro("open URL","run %URL%"));
        macros.add(new Macro("media control",""));
        macros.add(new Macro("media: play/pause","send, {Media_Play_Pause}"));
        macros.add(new Macro("media: yt play/pause","send, k"));
        macros.add(new Macro("media: stop","send, {Media_Stop}"));
        macros.add(new Macro("media: yt stop","send, k{Home}"));
        macros.add(new Macro("media: skip track","send, {Media_Next}"));
        macros.add(new Macro("media: yt forward","send, l"));
        macros.add(new Macro("media: previous track","send, {Media_Prev}"));
        macros.add(new Macro("media: yt back","send, j"));
        macros.add(new Macro("media: volume up","send, {Volume_Up}{Volume_Up}{Volume_Up}{Volume_Up}{Volume_Up}"));
        macros.add(new Macro("media: yt volup","send, {up}{up}{up}{up}{up}"));
        macros.add(new Macro("media: volume down","send, {Volume_Down}{Volume_Down}{Volume_Down}{Volume_Down}{Volume_Down}"));
        macros.add(new Macro("media: yt voldown","send, {down}{down}{down}{down}{down}"));
        macros.add(new Macro("media: mute", "send, {Volume_Mute}"));
        macros.add(new Macro("media: yt mute", "send, m"));
        macros.add(new Macro("media: fullscreen", "send, {Alt down}{Enter}{Alt up}"));
        macros.add(new Macro("media: yt fullscreen", "send, f"));


    }

    List<Macro> macros;

}
