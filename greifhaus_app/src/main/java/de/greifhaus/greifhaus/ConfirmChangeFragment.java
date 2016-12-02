package de.greifhaus.greifhaus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Hauke on 24.07.2016.
 */
public class ConfirmChangeFragment  extends DialogFragment{

    public static final String TAG=ConfirmChangeFragment.class.getSimpleName();

    private DialogInterface.OnClickListener l;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof DialogInterface.OnClickListener) {
            l = (DialogInterface.OnClickListener) activity;
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.app_name);
        builder.setMessage("Diesen Besuch wirklich löschen");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",l);

        return builder.create();
    }
}
