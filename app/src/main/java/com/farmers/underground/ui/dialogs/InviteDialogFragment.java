package com.farmers.underground.ui.dialogs;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.AnalyticsTrackerUtil;
import com.farmers.underground.ui.utils.FacebookInviteUtil;
import com.farmers.underground.ui.utils.WhatsAppUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by samson
 * on 06.11.15.
 */
public class InviteDialogFragment extends BaseFragment<TransparentActivity> {

    /* SMS WhatsUp FB*/
    private static final String TEXT_INVITE = "\n" +
            " הי!  אני רוצה להזמין אותך ל\"העיקר האיכר\"- אפליקציה לחקלאי שעושה סדר במחיר! תמצא שם את מחירי התקליט, השוק הסיטונאי ואפילו שיתוף של מחירי משווקים! והכל בצורה נוחה וברורה! ההתנסות בחינם! לחץ ותוריד\n";


    /** easy with this */
    private static final String TEXT_INVITE_MAIL = "הי! _(user name) רוצה להזמין אותך ל\"העיקר האיכר\"- אפליקציה לחקלאי שעושה סדר \n" +
            "\n" +
            "במחיר!\n" +
            "\n" +
            "מה באפליקציה:\n" +
            "\n" +
            "\uF0B7 עדכון מחירים יומיומי ממועצת הצמחים והשוק הסיטונאי\n" +

            "\uF0B7 שיתוף וחשיפת מחירי משווקים שונים\n" +

            "\uF0B7 סטטיסטיקות של מחירי גידולים\n" +

            "\uF0B7 והכל בצורה נוחה וברורה\n" +

            "ההתנסות בחינם!\n" +

            "לחץ ותוריד";

    private static final String TEXT_SUBJECT = "הזמנה ל\"העיקר האיכר\"";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_invite;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.llSMS)
    protected void sendSMS(){
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra("sms_body", TEXT_INVITE);
        try {
            getHostActivity().startActivity(Intent.createChooser(smsIntent, "SMS:"));
            AnalyticsTrackerUtil.getInstance().trackEvent(AnalyticsTrackerUtil.TypeEvent.InviteSms);
        } catch (ActivityNotFoundException e) {
            getHostActivity().showToast("SMS is not available.", Toast.LENGTH_SHORT);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.llEmail)
    protected void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_SUBJECT, TEXT_SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, String.format(TEXT_INVITE_MAIL, FarmersApp.getInstance().getCurrentUser().getFullName()));

        intent.setData(Uri.parse("mailto:"));
        try {
            getHostActivity().startActivity(Intent.createChooser(intent, "Send mail..."));
            AnalyticsTrackerUtil.getInstance().trackEvent(AnalyticsTrackerUtil.TypeEvent.InviteEmail);
        } catch (ActivityNotFoundException e) {
            getHostActivity().showToast("There are no email clients installed.", Toast.LENGTH_SHORT);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.llWhatsapp)
    protected void sendWhatsApp(){
        WhatsAppUtil.getInstance(getHostActivity()).sendInvitation(TEXT_INVITE);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.llFacebook)
    protected void sendFacebook(){
//        using facebook messenger for invite via intent
        FacebookInviteUtil.invitePeopleByFBMessenger(getHostActivity(), TEXT_INVITE);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.ivCloseDialog)
    protected void sendClose(){
        getHostActivity().finish();
    }
}
