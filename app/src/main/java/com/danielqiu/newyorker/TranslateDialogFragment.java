package com.danielqiu.newyorker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by danielqiu on 2/2/15.
 */
public class TranslateDialogFragment extends DialogFragment {


    public TextView textView;
    private String textContent;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_translate, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                });
        textView = (TextView) view.findViewById(R.id.content_view);
        textView.setText(textContent);
        textView.setMovementMethod(new ScrollingMovementMethod());
        return builder.create();
    }


    public void setTranslateText(String text) {
        textContent = text;
    }



}
