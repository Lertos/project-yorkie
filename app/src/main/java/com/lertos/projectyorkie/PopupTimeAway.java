package com.lertos.projectyorkie;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.data.DataManager;

public class PopupTimeAway {

    public PopupTimeAway(AppCompatActivity activity, int anyView) {
        prepareToLoadPopup(activity, anyView);
    }

    private void prepareToLoadPopup(AppCompatActivity activity, int anyView) {
        RelativeLayout layout = activity.findViewById(R.id.relScreen);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                View view = loadTimeAwayPopup(activity, anyView);

                String timeAwayTotalTime = DataManager.getInstance().getTimeAwayTotalTime();
                double timeAwayHeartsGained = DataManager.getInstance().getTimeAwayHeartsGained();
                double timeAwayTokensGained = DataManager.getInstance().getTimeAwayTokensGained();

                ((TextView) view.findViewById(R.id.tvPopupTimeAway)).setText(timeAwayTotalTime);
                ((TextView) view.findViewById(R.id.tvPopupHeartsGained)).setText(IdleNumber.getStrNumber(timeAwayHeartsGained) + " Hearts");
                ((TextView) view.findViewById(R.id.tvPopupTokensGained)).setText(IdleNumber.getStrNumber(timeAwayTokensGained) + " Tokens");

                Animation anim = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.alpha_on_off);
                view.findViewById(R.id.tvCloseMessage).startAnimation(anim);
            }
        });
    }

    private View loadTimeAwayPopup(AppCompatActivity activity, int anyView) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_time_away, null);

        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;

        //4th argument ignores clicks/taps outside the popup
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //The view passed doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(activity.findViewById(anyView), Gravity.CENTER, 0, 0);

        //Dismiss the popup when ignore clicks to anything else
        popupView.setOnClickListener(v -> {
            v.findViewById(R.id.tvCloseMessage).animate().cancel();

            popupWindow.dismiss();
        });

        return popupWindow.getContentView();
    }

}
