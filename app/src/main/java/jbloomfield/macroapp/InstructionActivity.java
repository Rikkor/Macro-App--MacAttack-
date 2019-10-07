package jbloomfield.macroapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.microsoft.graph.extensions.DriveItem;
import com.microsoft.graph.extensions.Folder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import static jbloomfield.macroapp.MainActivity.msa;

public class InstructionActivity extends AppCompatActivity {

    String token;
    String email;
    String onedrive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        Toolbar mytoolbar = findViewById(R.id.my_toolbar);
        // force toolbar for back button
        // text was black tried custom style still didn't  work
        mytoolbar.setTitleTextColor(Color.WHITE);
        mytoolbar.setTitle("Instructions");
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        readFile();
    }

    @Override
    protected void onResume()
    {
     super.onResume();
     // check stored prefs for login info
        SharedPreferences prefs = getSharedPreferences("user-auth",MODE_PRIVATE);
        token = prefs.getString("token",null);
        email = prefs.getString("email",null);
        onedrive = prefs.getString("onedrive",null);
        // set logged in text if login found
        if (email != null)
        {
            TextView textv = findViewById(R.id.textEmail);
            textv.setText("Dropbox logged in as:\n" + email);
        }
        else if (onedrive != null)
        {
            TextView textv = findViewById(R.id.textEmail);
            textv.setText("OneDrive logged in as:\n" + msa.authResult.getUser().getDisplayableId());
        }
    }

    // read directions.text from /res/raw
    public void readFile() {
        String contents = "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.directions)))) {
            String line;
            while ((line = reader.readLine()) != null)
            {
                contents += line +"\n";
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        finally {
            // insert contents of directions into text box
            TextView textbox = findViewById(R.id.instructionBox);
            textbox.setText(contents);
            textbox.setMovementMethod(new ScrollingMovementMethod());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        // enable back arrow on toolbar
        onBackPressed();
        return true;
    }

    public void initializeDrive(View view) {
        if (token == null && onedrive == null) {
            Toast.makeText(this, "Please  Login to a could drive first", Toast.LENGTH_SHORT).show();
        } else if (msa.dbClient != null) {

            final DbxClientV2 dbClient = msa.dbClient;

            // upload remoteaction script to users dropbox
            if (dbClient != null) {
                new Thread() {
                    @Override
                    public void run() {
                        // upload remoteaction.ahk
                        try {
                            InputStream is = getResources().openRawResource(R.raw.remoteaction);
                            FileMetadata metadata = dbClient.files().uploadBuilder("/RemoteAction.ahk")
                                    .uploadAndFinish(is);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }.start();

                //upload dummy file to create remoteaction_ran folder
                new Thread(){
                    @Override
                    public void run()
                    {
                        try (OutputStream out = openFileOutput("dummy.txt", Context.MODE_PRIVATE)) {
                            out.write("This folder contains past actions that have been run\n".getBytes());
                            out.close();

                            FileInputStream in = openFileInput("dummy.txt");
                            FileMetadata metadata = dbClient.files().uploadBuilder("/remoteaction_ran/dummy.txt")
                                    .uploadAndFinish(in);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }.start();
                Toast.makeText(this, "done uploading check app/macattack folder in dropbox",Toast.LENGTH_SHORT).show();
            }
        }
        // upload remoteaction.ahk to onedrive
        else if (msa.client != null) {
            new Thread() {
                @Override
                public void run() {
                    //create _macattack folder
                    DriveItem d = new DriveItem();
                    d.folder = new Folder();
                    d.name = "_MacAttack";
                    msa.client.getMe().getDrive().getRoot().getChildren().buildRequest().post(d);
                    // create remoteaction_ran folder
                    d = new DriveItem();
                    d.folder = new Folder();
                    d.name = "remoteaction_ran";
                    msa.client.getMe().getDrive().getRoot().getItemWithPath("_macattack/").getChildren().buildRequest().post(d);
                    // upload remoteaction.ahk
                    String remoteAction = "";
                    // read contents of remoteaction.ahk to string
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.remoteaction)))) {
                        String line;
                        while ((line = reader.readLine()) != null)
                        {
                            remoteAction += line +"\n";
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    // get bytes of remoteaction string and upload it to onedrive
                    msa.client.getMe().getDrive().getRoot().getItemWithPath("_macattack/remoteaction.ahk")
                            .getContent().buildRequest().put(remoteAction.getBytes());

                }
            }.start();
            Toast.makeText(this, "done uploading, check the _Macattack folder in onedrive",Toast.LENGTH_SHORT).show();

        }


        }
}
