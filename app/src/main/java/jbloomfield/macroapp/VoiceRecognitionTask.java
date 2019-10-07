package jbloomfield.macroapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;

import java.util.ArrayList;
import java.util.List;

public class VoiceRecognitionTask extends AsyncTask <Void, String, Void> {

    ArrayList<String> txtVoice;

    public VoiceRecognitionTask(ArrayList<String> txtVoice, AuthManager am, Context context) {
        this.txtVoice = txtVoice;
        this.am = am;
        this.context = context;
        macros = new MacroPresets().macros;
    }

    AuthManager am;
    Context context;
    List<Macro> macros;


    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < txtVoice.size(); i++) {
            // loop through each possible phrase even though the first is probably the most likely
            switch (txtVoice.get(i)) {
                // take action for each possible voice command
                case "shutdown":
                case "shut down":
                {
                    MacroManager.tryUpload(context,am,MacroManager.findMacro(macros,"shutdown"));
                    return null;
                }
                case "reboot":
                {
                    MacroManager.tryUpload(context,am,MacroManager.findMacro(macros,"reboot"));
                    return null;
                }
                case "sleep":
                {
                    MacroManager.tryUpload(context,am,MacroManager.findMacro(macros,"sleep"));
                    return null;
                }

                case "monitor off":
                case "monitor":
                case "turn off monitor":
                {
                    MacroManager.tryUpload(context,am,MacroManager.findMacro(macros,"monitor off"));
                    return null;
                }

                case "lock pc":
                case "lock":
                {
                    MacroManager.tryUpload(context,am,MacroManager.findMacro(macros,"lock pc"));
                    return null;
                }

                case "play":
                case "play music":
                case "pause":
                case "pause music":
                case "resume":
                case "resume music":
                {
                    MacroManager.tryUpload(context,am,MacroManager.findMacro(macros, "media: play/pause"));
                    return null;
                }

                case "stop":
                case "stop music":
                {
                    MacroManager.tryUpload(context,am, MacroManager.findMacro(macros,"media: stop"));
                    return null;
                }

                case "next track":
                case "skip track":
                case "next song":
                case "skip song":
                case "next":
                case "skip":
                {
                    MacroManager.tryUpload(context,am, MacroManager.findMacro(macros,"media: skip track"));
                    return null;
                }

                case "previous track":
                case "previous song":
                case "previous":
                {
                    MacroManager.tryUpload(context,am, MacroManager.findMacro(macros,"media: previous track"));
                    return null;
                }

                case "volume up":
                case "increase volume":
                case "turn it up":
                case "louder":
                {
                    MacroManager.tryUpload(context,am, MacroManager.findMacro(macros,"media: volume up"));
                    return null;
                }

                case "volume down":
                case "lower volume":
                case "decrease volume":
                case "turn it down":
                case "quieter":
                {
                    MacroManager.tryUpload(context,am, MacroManager.findMacro(macros,"media: volume down"));
                    return null;
                }

                case "mute":
                case "mute sound":
                case "turn it off":
                case "silence":
                {
                    MacroManager.tryUpload(context,am, MacroManager.findMacro(macros,"media: mute"));
                    return null;
                }
                case "fullscreen":
                {
                    MacroManager.tryUpload(context,am, MacroManager.findMacro(macros,"media: fullscreen"));
                    return null;
                }
            }
            // no match found
            publishProgress("No matching command found");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(context, values[0],Toast.LENGTH_SHORT).show();
    }
}
