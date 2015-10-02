package com.farmers.underground.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.farmers.underground.R;

/**
 * Created by tZpace
 * on 02-Oct-15.
 */
public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context);
    }

    private TextView progressMessage;

    public CharSequence getText() {
        return progressMessage != null ? progressMessage.getText() : null;
    }

    private Runnable setTextLingerTask;

    public void setText(final CharSequence text) {
        if (progressMessage == null) {
            setTextLingerTask = new Runnable() {
                @Override
                public void run() {
                    setText(text);

                    setTextLingerTask = null;
                }
            };
            return;
        }

        progressMessage.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getAttributes().dimAmount = .35f;
        getWindow().getAttributes().flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.element_dialog_progress);

        progressMessage = (TextView) findViewById(R.id.progressMessage);

        if (setTextLingerTask != null)
            setTextLingerTask.run();

    }
}