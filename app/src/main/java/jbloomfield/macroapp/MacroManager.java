package jbloomfield.macroapp;

import android.content.Context;
import android.util.Log;

import com.dropbox.core.v2.DbxClientV2;
import com.microsoft.graph.extensions.IGraphServiceClient;

import java.util.List;

/**
 * Functions related to macro management
 * Handles custom macros
 */

class MacroManager {
    static void tryUpload(Context context, AuthManager am, Macro macro) {
            // Upload macro
            String file = "dummy.ahk";
            UploadTask upload = new UploadTask(context, am, macro, file);
            upload.execute();

    }

    static Macro findMacro(List<Macro> macros, String name) {

        for (Macro m:macros) {
         if (m.getName().equals(name))
             return m;
        }
        return null;
    }

}
