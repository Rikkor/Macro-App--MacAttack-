package jbloomfield.macroapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import java.util.Objects;

public class UrlPopup extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.url_dialog,null))
                //create done button
                .setPositiveButton("Done",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText url = getDialog().findViewById(R.id.textURL);

                        // replace the placeholder in the macro with entered program
                        String inUrl = url.getText().toString();
                        Context context = getActivity();
                        Bundle args = getArguments();
                        String command = args.getString("command");
                        command = command.replace("%URL%", inUrl);
                        Macro macro = new Macro("Open URL", command);

                        // upload and report success
                        if (!Objects.equals(inUrl, "")) {
                            MacroManager.tryUpload(context, MainActivity.msa, macro);


                        }

                    }
                });

        return builder.create();
    }
}
