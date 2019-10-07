package jbloomfield.macroapp;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.extensions.DriveItem;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Macro> macros = new ArrayList<>();
    ArrayAdapter<Macro> adapter;
    Context context;
    List<Macro> presets = new MacroPresets().macros;

    // the client stores all dropbox data
    static AuthManager msa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        ListView lv = findViewById(R.id.listView);

        //initialize macro presets
        MacroPresets presets = new MacroPresets();

        // initialize the listview
        // iterate through list of presets
        for (int i = 0; i < presets.macros.size(); i++){
            // dont add media macros to listView so they wont appear
            // they instead are used in the media popup
            if (!presets.macros.get(i).getName().contains("media: "))
                macros.add(presets.macros.get(i));

        }
        // create adapter and assign it to the listview
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,macros);
        lv.setAdapter(adapter);

        //create onItemClickListener for listView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, adapter.getItem(i).getMacro(), Toast.LENGTH_SHORT).show();
                Log.i(adapter.getItem(i).getName(),adapter.getItem(i).getMacro());
                // open dialog for run program
                if(adapter.getItem(i).getName() == "run program")
                {
                    DialogFragment run = new RunDialog();
                    Bundle args = new Bundle();
                    args.putString("command",adapter.getItem(i).getMacro());
                    run.setArguments(args);
                    run.show(getFragmentManager(),"run program");
                }
                // open dialog for open url
                else if(adapter.getItem(i).getName() == "open URL")
                {
                    DialogFragment openURL = new UrlPopup();
                    Bundle args = new Bundle();
                    args.putString("command",adapter.getItem(i).getMacro());
                    openURL.setArguments(args);
                    openURL.show(getFragmentManager(),"Open url");
                }
                // open media control dialog
                else if (adapter.getItem(i).getName() == "media control")
                {
                    DialogFragment mediaControl = new MediaControlPopup();
                    mediaControl.show(getFragmentManager(),"Media Control");
                }
                // for all other macros just upload it
                else
                {
                    MacroManager.tryUpload(context,msa,adapter.getItem(i));
                }

            }


        });
        msa = new AuthManager(getApplication());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // retrieve dropbox login token from shared prefs
        SharedPreferences prefs = getSharedPreferences("user-auth",MODE_PRIVATE);
        String token = prefs.getString("token",null);

        // check for dropbox login token
        if (token == null) {
            token = Auth.getOAuth2Token();
            if (token != null) {
                // store login token
                prefs.edit().putString("token",token).apply();
                loadData(token);
            }
        }
        else
            loadData(token);

     // attempt onedrive silent login
        List<User> users;
        String msToken = prefs.getString("onedrive",null);
        try{
            users = msa.msClient.getUsers();
            if (users != null && users.size() == 1 && msToken != null)
            {
                msa.getTokenSilent(this,users.get(0));
            }
        }
        catch (MsalClientException e){
            e.printStackTrace();
    }
    }

    private void loadData(String token) {
        // create client object
        DbxRequestConfig config = new DbxRequestConfig("MacAttack");
        msa.dbClient = new DbxClientV2(config, token);

        //async request for user details
        new GetCurrentAccountTask(msa.dbClient, new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                //show email and hide login button when done
                ((TextView) findViewById(R.id.textView)).setText("Signed into Dropbox\n" + result.getEmail());
                SharedPreferences prefs = getSharedPreferences("user-auth",MODE_PRIVATE);
                prefs.edit().putString("email", result.getEmail()).apply();
                findViewById(R.id.btn_dropbox).setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_onedrive).setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {
                // log errors
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }

    public void dbLogin (View view) {
        // call dropbox login via web browser
        Auth.startOAuth2Authentication(MainActivity.this,getString(R.string.APP_KEY));

    }

    public void msLogin(View view) {
        // call MS login via browser
        msa.getToken(this);
    }

    // logout of dropbox and onedrive method
    public void logoutDialog(View view){
        // delete email text
        ((TextView) findViewById(R.id.textView)).setText("");
        // set token saved in app to null aka logout
        msa.dbClient = null;
        SharedPreferences prefs = getSharedPreferences("user-auth",MODE_PRIVATE);
        prefs.edit().clear().apply();

        // make login button visible and logout invisible
        findViewById(R.id.btn_dropbox).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_onedrive).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_logout).setVisibility(View.INVISIBLE);

        Toast.makeText(context, "Succesfully Logged Out", Toast.LENGTH_SHORT).show();
    }


// move to instruction activity
public void instructions(View view)
{
    Intent intent = new Intent(this, InstructionActivity.class);
    startActivity(intent);
}
// move to custom macro activity
    public void customMacro(View view)
    {
        Intent intent = new Intent(this, CustomMacroActivity.class);
        startActivity(intent);
    }

    // launch voice recognition
    public void voiceRecognition(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        try {

            startActivityForResult(intent,3000);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),"Oops! Your device doesn’t support Speech to Text",Toast.LENGTH_SHORT).show();
        }


    }

    // voice recognition callback
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000 && resultCode == RESULT_OK && null != data) {
            // text is returned in an array of strings sorted by confidence rating
            ArrayList<String> text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            VoiceRecognitionTask vr = new VoiceRecognitionTask(text,msa, context);
            vr.execute();

        }
        // ms login callback
        else if (requestCode == 1001 && resultCode == 2003)
            msa.msClient.handleInteractiveRequestRedirect(requestCode,resultCode,data);
    }
}
