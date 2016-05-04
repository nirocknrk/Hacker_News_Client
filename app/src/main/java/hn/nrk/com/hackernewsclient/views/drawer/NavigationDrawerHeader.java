package hn.nrk.com.hackernewsclient.views.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;

//import hn.nrk.com.hackernewsclient.R;

public class NavigationDrawerHeader extends LinearLayout {

    //private LoginSharedPreferences loginSharedPreferences;
    //private Listener listener;

    public NavigationDrawerHeader(Context context) {
        super(context);
    }

    public NavigationDrawerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationDrawerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //loginSharedPreferences = LoginSharedPreferences.newInstance();
        //listener = Classes.from(getContext());

        showProperHeader();
    }

    private void showProperHeader() {

//        if (loginSharedPreferences.isLoggedIn()) {
//            updateWithUsername();
//        } else {
            //updateWithLoginMessage();
//        }
    }


//    private void updateWithLoginMessage() {
//        LayoutInflater.from(getContext()).inflate(R.layout.view_drawer_header, this, true);
//        View loginView = Views.findById(this, R.id.view_drawer_header_login);
////        loginView.setOnClickListener(new OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                listener.onLoginClicked();
////            }
////        });
//    }

//    public interface Listener {
//        void onLoginClicked();
//    }

}
