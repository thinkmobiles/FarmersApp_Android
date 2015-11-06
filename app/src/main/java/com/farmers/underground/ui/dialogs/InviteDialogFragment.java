package com.farmers.underground.ui.dialogs;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.WhatsAppUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by samson
 * on 06.11.15.
 */
public class InviteDialogFragment extends BaseFragment<TransparentActivity> {

    private static final String TEXT_INVITE = "Invite you";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_invite;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.llSMS)
    protected void sendSMS(){
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("sms_body", TEXT_INVITE);
        getHostActivity().startActivity(Intent.createChooser(smsIntent, "SMS:"));
    }

    @OnClick(R.id.llEmail)
    protected void sendEmail(){
        Intent mailer = new Intent(Intent.ACTION_SEND);
        mailer.setType("message/rfc822");
        mailer.putExtra(Intent.EXTRA_TEXT, TEXT_INVITE);
        try {
            getHostActivity().startActivity(Intent.createChooser(mailer, "Send"));
        } catch (ActivityNotFoundException e) {
            getHostActivity().showToast("There are no email clients installed.", Toast.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.llWhatsapp)
    protected void sendWhatsapp(){
        WhatsAppUtil.getInstance(getHostActivity()).sendInvitation();
    }

    @OnClick(R.id.llFacebook)
    protected void sendFacebook(){

    }

    @OnClick(R.id.ivCloseDialog)
    protected void sendClose(){
        getHostActivity().finish();
    }
}
