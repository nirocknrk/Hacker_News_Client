package hn.nrk.com.hackernewsclient.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import hn.nrk.com.hackernewsclient.R;
import hn.nrk.com.hackernewsclient.data.data_connectivity.Network_Checker;
import hn.nrk.com.hackernewsclient.views.libs.ColorTweaker;
import hn.nrk.com.hackernewsclient.views.libs.LollipopUiConfiguration;
import hn.nrk.com.hackernewsclient.views.libs.LollipopUiHelper;


public class HNActivity extends AppCompatActivity {

    public static final CharSequence SHARE_DIALOG_DEFAULT_TITLE = null;

    private ColorTweaker colorTweaker;
    private LollipopUiHelper lollipopUiHelper;
    private Navigator navigator;
    private Network_Checker networkChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNetworkChecker();

        colorTweaker = new ColorTweaker();
        lollipopUiHelper = new LollipopUiHelper(this, colorTweaker, getLollipopUiConfiguration());
        lollipopUiHelper.setTaskDescriptionOnLollipopAndLater();
        lollipopUiHelper.setSystemBarsColorOnLollipopAndLater();
        navigator = new Navigator(this);
    }

    private void initNetworkChecker() {
        networkChecker = new Network_Checker(this);
    }

    protected LollipopUiConfiguration getLollipopUiConfiguration() {
        return LollipopUiConfiguration.NEWS;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setHighLevelActivity() {
        setupToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setupSubActivity() {
        setupToolbar();
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void setupSubActivityWithTitle() {
        setupSubActivity();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public Navigator navigate() {
        if (navigator == null) {
            navigator = new Navigator(this);
        }
        return navigator;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    public boolean isOnline() {
        return networkChecker.isConnected();
    }
}
