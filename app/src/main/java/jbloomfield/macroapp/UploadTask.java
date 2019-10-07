package jbloomfield.macroapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Created by farcry15 on 12/9/2017.
 */

public class UploadTask extends AsyncTask<Void, String,Void> {

    Context context;
    AuthManager am;
    Macro macro;
    String file;

    public UploadTask(Context context, AuthManager am, Macro macro, String file)
    {
        this.context = context;
        this.am = am;
        this.macro = macro;
        this.file = file;
    }

    @Override
    protected Void doInBackground(Void... voids) {
    if (am.dbClient == null && am.client == null)
    {
        publishProgress("Please sign into Dropbox first");
        return null;
    }

        try{
        am.dbClient.files().getMetadata("/dummy.ahk");
        Log.d("try: ", "File exists");
        publishProgress("Please wait for Previous command to execute first");
        return null;
        }catch(Exception e){
        e.printStackTrace();
        Log.d("catch","File doesn't exist");
        }

        if (am.dbClient != null)
        {
            try (OutputStream out = context.openFileOutput(file, Context.MODE_PRIVATE)) {
                out.write(macro.getMacro().getBytes());
                out.close();

                FileInputStream in = context.openFileInput(file);
                FileMetadata metadata = am.dbClient.files().uploadBuilder("/" + file)
                        .uploadAndFinish(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress("Uploaded " + macro.getName() + " macro to dropbox");
        }
        else if(am.client != null)
        {
            am.client.getMe().getDrive().getRoot().getItemWithPath("_macattack/dummy.ahk")
                    .getContent().buildRequest().put(macro.getMacro().getBytes());
            publishProgress("Uploaded " + macro.getName() + " macro to oneDrive");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(context, values[0],Toast.LENGTH_SHORT).show();
    }
}
