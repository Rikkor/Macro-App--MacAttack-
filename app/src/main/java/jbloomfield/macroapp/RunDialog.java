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

/**
 * Build Dialog box using the custom sign in layout
 */

public class RunDialog extends DialogFragment {

    /** dialog oncreate handler override */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // use Builder to create dialog easily
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.run_program,null))

                // create done button
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText user = getDialog().findViewById(R.id.textUser);

                        // replace the placeholder in the macro with entered program
                        String program = user.getText().toString();
                        Context context = getActivity();
                        Bundle args = getArguments();
                        String command = args.getString("command");
                        command = command.replace("%PROGRAM%",program);
                        Macro macro = new Macro ("Run Program",command);

                        // upload and report success
                        if (!Objects.equals(program, "")) {
                            MacroManager.tryUpload(context,MainActivity.msa,macro);
                        }
                    }
                });

        return builder.create();
    }
}
