package jbloomfield.macroapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import static jbloomfield.macroapp.MainActivity.msa;

public class CustomMacroActivity extends AppCompatActivity {

    String token;
    String msToken;
    String email;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_custom_macro);
        Toolbar mytoolbar = findViewById(R.id.my_toolbar2);
        // force toolbar
        // text was black tried custom style still didn't  work
        mytoolbar.setTitleTextColor(Color.WHITE);
        mytoolbar.setTitle("Custom Macro");
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // set logged in text
        SharedPreferences prefs = getSharedPreferences("user-auth",MODE_PRIVATE);
        token = prefs.getString("token",null);
        msToken = prefs.getString("onedrive",null);
        email = prefs.getString("email",null);
        if (token == null && msToken == null) {
            TextView textv = findViewById(R.id.textEmail2);
            textv.setText("Please login to upload custom macros");
        }
        else if (email != null) {
            TextView textv = findViewById(R.id.textEmail2);
            textv.setText("Dropbox logged in as:\n" + email);
        }
        else if (msToken != null) {
            TextView textv = findViewById(R.id.textEmail2);
            textv.setText("OneDrive logged in as:\n" + msa.authResult.getUser().getDisplayableId());
        }
        loadMacro();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //  enable back arrow in toolbar
        onBackPressed();
        return true;
    }

    public void uploadCustom(View view){
        // upload text from textbox as custom macro
        TextView customText = findViewById(R.id.editText1);
        Macro customMacro = new Macro("Custom macro", customText.getText().toString());

        MacroManager.tryUpload(context, msa, customMacro);
        saveMacro(customMacro.getMacro());
    }

    public void saveMacro(String macro){
        // saves the user entered macro, very barebones as of now
        //TODO save and load custom macros
        try (OutputStream out = context.openFileOutput("custom.txt", Context.MODE_PRIVATE)) {
            out.write(macro.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMacro(){
        // restores the saved macro to the textbox
        String content = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("custom.txt")))){
            String line;
            while ((line = reader.readLine()) != null)
            {
                content += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            TextView customText = findViewById(R.id.editText1);
            customText.setText(content);
        }
    }
}
