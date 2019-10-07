package jbloomfield.macroapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.List;

import static jbloomfield.macroapp.MainActivity.msa;

/**
 * Dialog Fragment for the media controls
 */

public class MediaControlPopup extends DialogFragment{
boolean isYt = false;
List<Macro> presets = new MacroPresets().macros;
Context context = getActivity();
    /** dialog oncreate handler override */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // use Builder to create dialog easily
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.mediacontrol, null));
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final Switch toggle = getDialog().findViewById(R.id.sw_media);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleYT();
                if (b)
                {
                    toggle.setText(R.string.str_youtube);
                    toggle.getThumbDrawable().setColorFilter(getResources().getColor(R.color.ytRed), PorterDuff.Mode.MULTIPLY);
                    toggle.getTrackDrawable().setColorFilter(getResources().getColor(R.color.ytRed), PorterDuff.Mode.MULTIPLY);

                    ImageButton btn = getDialog().findViewById(R.id.btn_prev);
                    btn.setColorFilter(getResources().getColor(R.color.ytRed));
                    btn = getDialog().findViewById(R.id.btn_stop);
                    btn.setColorFilter(getResources().getColor(R.color.ytRed));
                    btn = getDialog().findViewById(R.id.btn_pause);
                    btn.setColorFilter(getResources().getColor(R.color.ytRed));
                    btn = getDialog().findViewById(R.id.btn_next);
                    btn.setColorFilter(getResources().getColor(R.color.ytRed));
                    btn = getDialog().findViewById(R.id.btn_mute);
                    btn.setColorFilter(getResources().getColor(R.color.ytRed));
                    btn = getDialog().findViewById(R.id.btn_volDown);
                    btn.setColorFilter(getResources().getColor(R.color.ytRed));
                    btn = getDialog().findViewById(R.id.btn_volUp);
                    btn.setColorFilter(getResources().getColor(R.color.ytRed));
                    btn = getDialog().findViewById(R.id.btn_fullscreen);
                    btn.setColorFilter(getResources().getColor(R.color.ytRed));

            }

                else
                {
                    toggle.setText(R.string.str_mediakeys);
                    toggle.getThumbDrawable().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                    toggle.getTrackDrawable().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);

                    ImageButton btn = getDialog().findViewById(R.id.btn_prev);
                    btn.setColorFilter(getResources().getColor(R.color.darkColorAccent));
                    btn = getDialog().findViewById(R.id.btn_stop);
                    btn.setColorFilter(getResources().getColor(R.color.darkColorAccent));
                    btn = getDialog().findViewById(R.id.btn_pause);
                    btn.setColorFilter(getResources().getColor(R.color.darkColorAccent));
                    btn = getDialog().findViewById(R.id.btn_next);
                    btn.setColorFilter(getResources().getColor(R.color.darkColorAccent));
                    btn = getDialog().findViewById(R.id.btn_mute);
                    btn.setColorFilter(getResources().getColor(R.color.darkColorAccent));
                    btn = getDialog().findViewById(R.id.btn_volDown);
                    btn.setColorFilter(getResources().getColor(R.color.darkColorAccent));
                    btn = getDialog().findViewById(R.id.btn_volUp);
                    btn.setColorFilter(getResources().getColor(R.color.darkColorAccent));
                    btn = getDialog().findViewById(R.id.btn_fullscreen);
                    btn.setColorFilter(getResources().getColor(R.color.darkColorAccent));
                }


            }
        });
        // onclick listeners for each button that send the macro for youtube or media keys
        final ImageButton prevBtn =(ImageButton) getDialog().findViewById(R.id.btn_prev);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Macro prev;
                if (isYt)
                    prev = MacroManager.findMacro(presets, "media: yt back");
                else
                    prev = MacroManager.findMacro(presets, "media: previous track");
                if (prev != null) {
                    MacroManager.tryUpload(getActivity(), msa, prev);
                }
            }
        });

        final ImageButton stopBtn =(ImageButton) getDialog().findViewById(R.id.btn_stop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Macro stop;
                if (isYt)
                    stop = MacroManager.findMacro(presets, "media: yt stop");
                else
                    stop = MacroManager.findMacro(presets, "media: stop");
                if (stop != null) {
                    MacroManager.tryUpload(getActivity(), msa, stop);
                }
            }
        });

        final ImageButton playBtn =(ImageButton) getDialog().findViewById(R.id.btn_pause);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Macro m;
                if (isYt)
                    m = MacroManager.findMacro(presets, "media: yt play/pause");
                else
                    m = MacroManager.findMacro(presets, "media: play/pause");
                if (m != null) {
                    MacroManager.tryUpload(getActivity(), msa, m);
                }
            }
        });

        final ImageButton nextBtn =(ImageButton) getDialog().findViewById(R.id.btn_next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Macro m;
                if (isYt)
                    m = MacroManager.findMacro(presets, "media: yt forward");
                else
                    m = MacroManager.findMacro(presets, "media: skip track");
                if (m != null) {
                    MacroManager.tryUpload(getActivity(), msa, m);
                }
            }
        });

        final ImageButton downBtn =(ImageButton) getDialog().findViewById(R.id.btn_volDown);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Macro m;
                if (isYt)
                    m = MacroManager.findMacro(presets, "media: yt voldown");
                else
                    m = MacroManager.findMacro(presets, "media: volume down");
                if (m != null) {
                    MacroManager.tryUpload(getActivity(), msa, m);
                }
            }
        });

        final ImageButton upBtn =(ImageButton) getDialog().findViewById(R.id.btn_volUp);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Macro m;
                if (isYt)
                    m = MacroManager.findMacro(presets, "media: yt volup");
                else
                    m = MacroManager.findMacro(presets, "media: volume up");
                if (m != null) {
                    MacroManager.tryUpload(getActivity(), msa, m);
                }
            }
        });

        final ImageButton muteBtn =(ImageButton) getDialog().findViewById(R.id.btn_mute);
        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Macro m;
                if (isYt)
                    m = MacroManager.findMacro(presets, "media: yt mute");
                else
                    m = MacroManager.findMacro(presets, "media: mute");
                if (m != null) {
                    MacroManager.tryUpload(getActivity(), msa, m);
                }
            }
        });

        final ImageButton fsBtn =(ImageButton) getDialog().findViewById(R.id.btn_fullscreen);
        fsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Macro m;
                if (isYt)
                    m = MacroManager.findMacro(presets, "media: yt fullscreen");
                else
                    m = MacroManager.findMacro(presets, "media: fullscreen");
                if (m != null) {
                    MacroManager.tryUpload(getActivity(), msa, m);
                }
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences("media",Context.MODE_PRIVATE);
        boolean yt = prefs.getBoolean("isYt",false);
        if (yt)
        {
            Switch sw = getDialog().findViewById(R.id.sw_media);
            sw.setChecked(true);
        }


    }

public void toggleYT() {
        if (isYt)
        {
            isYt = false;
            SharedPreferences prefs = getActivity().getSharedPreferences("media",Context.MODE_PRIVATE);
            prefs.edit().putBoolean("isYt",false).commit();
        }

        else
        {
            isYt = true;
            SharedPreferences prefs = getActivity().getSharedPreferences("media",Context.MODE_PRIVATE);
            prefs.edit().putBoolean("isYt",true).commit();
        }

}


}
