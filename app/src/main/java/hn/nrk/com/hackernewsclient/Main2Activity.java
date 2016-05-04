package hn.nrk.com.hackernewsclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.novoda.notils.caster.Views;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;
import com.squareup.okhttp.Response;

import hn.nrk.com.hackernewsclient.data.AppInviter;
import hn.nrk.com.hackernewsclient.data.DataPersister;
import hn.nrk.com.hackernewsclient.data.Provider;
import hn.nrk.com.hackernewsclient.injection.Inject;
import hn.nrk.com.hackernewsclient.model.HNStory;
import hn.nrk.com.hackernewsclient.model.Respose;
import hn.nrk.com.hackernewsclient.news.NewsListener;
import hn.nrk.com.hackernewsclient.news.StoryFragment;
import hn.nrk.com.hackernewsclient.views.Navigator;
import hn.nrk.com.hackernewsclient.views.SnackBarView;
import hn.nrk.com.hackernewsclient.views.activity.NewsActivity;
import hn.nrk.com.hackernewsclient.views.adapter.StoriesPagerAdapter;
import hn.nrk.com.hackernewsclient.views.drawer.ActionBarDrawerListener;
import hn.nrk.com.hackernewsclient.views.drawer.NavigationDrawerHeader;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class Main2Activity extends NewsNaviDrawerActivity implements  GoogleApiClient.OnConnectionFailedListener, ActionBarDrawerListener.Listener,NewsListener  {



        SwipeRefreshLayout swipeContainer;

    private static final int REQUEST_INVITE = 0;
    private ViewPager headersPager;
    private SnackBarView snackbarView;
    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;
    private StoriesPagerAdapter storiesPagerAdapter;
    private Subscription subscription;

    private GoogleApiClient googleApiClient;


        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

            initializeHeaders();
            initializeTabs();
            loadSnackbar();
            loadResst();




//       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    private void initializeHeaders() {
        headersPager = (ViewPager) findViewById(R.id.viewpager);
        storiesPagerAdapter = new StoriesPagerAdapter(getSupportFragmentManager());
        headersPager.setAdapter(storiesPagerAdapter);
        headersPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        Inject.usageAnalytics().trackPage(storiesPagerAdapter.getPageTitle(position).toString());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
    }

    private void initializeTabs() {
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(headersPager);
        tabLayout.setOnTabSelectedListener(new StoryTabSelectedListener());
    }

    private void setupAppBar() {
        setHighLevelActivity();
        setTitle(getString(R.string.title_app));
    }

    private void loadSnackbar() {
        snackbarView = Views.findById(this, R.id.snackbar);
        croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);
    }

    private void loadResst() {
        AppInviter appInviter = Inject.appInviter();
        if (appInviter.shouldShow()) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(AppInvite.API)
                    .enableAutoManage(this, this)
                    .build();
            showAppInviteMessage();
        }
    }

    private void showAppInviteMessage() {
        Snackbar.make(headersPager, R.string.app_invite, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.app_invite_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onInvClicked();
                    }
                }).show();
    }

    private void onInvClicked() {
        Inject.usageAnalytics().trackEvent(getString(R.string.analytics_event_app_invite_started));
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    @Override
    public void onShareClicked(Intent shareIntent) {
        Intent chooserIntent = Intent.createChooser(shareIntent, SHARE_DIALOG_DEFAULT_TITLE);
        startActivity(chooserIntent);
    }

    @Override
    public void onCommentsClicked(View v, HNStory story) {
        navigate().toComments(v, story);
    }

    @Override
    public void onCommentsClicked(HNStory story) {
        Inject.usageAnalytics().trackNavigateEvent(
                getString(R.string.analytics_event_view_comments_feed),
                story
        );
        navigate().toComments(story);
    }

    @Override
    public void onContentClicked(HNStory story) {
        Inject.usageAnalytics().trackNavigateEvent(
                getString(R.string.analytics_event_view_story_feed),
                story
        );
        DataPersister persister = Inject.dataPersister();
        persister.markStoryAsRead(story);
        navigate().toInnerBrowser(story);
    }

    @Override
    public void onNotImplementedFeatureSelected() {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_not_implemented))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .animating();
    }
    @Override
    public void onExternalLinkClicked(HNStory story) {
        if (story.isHackerNewsLocalItem()) {
            Inject.usageAnalytics().trackNavigateEvent(
                    getString(R.string.analytics_event_view_comments_feed),
                    story
            );
            navigate().toComments(story);
        } else {
            Inject.usageAnalytics().trackNavigateEvent(
                    getString(R.string.analytics_event_view_external_url_feed),
                    story
            );
            navigate().toExternalBrowser(Uri.parse(story.getUrl()));
        }
    }

    @Override
    public void onBookmarkAdded(HNStory story) {
        Inject.usageAnalytics().trackBookmarkEvent(
                getString(R.string.analytics_event_add_bookmark_feed),
                story
        );
        DataPersister persister = Inject.dataPersister();
        showAddedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onBookmarkRemoved(HNStory story) {
        Inject.usageAnalytics().trackBookmarkEvent(
                getString(R.string.analytics_event_remove_bookmark_feed),
                story
        );
        DataPersister persister = Inject.dataPersister();
        showRemovedBookmarkSnackbar(persister, story);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(headersPager, R.string.google_play_services_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onStoryVoteClicked(HNStory story) {
        Inject.usageAnalytics().trackVoteEvent(getString(R.string.analytics_event_vote), story);
        showSnackBarVoting();

        Provider provider = Inject.provider();
        subscription = provider
                .observeVote(story)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Respose>() {
                            @Override
                            public void onCompleted() {
                                if (!subscription.isUnsubscribed()) {
                                    subscription.unsubscribe();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Inject.crashAnalytics().logSomethingWentWrong("Provider - Vote: ", e);
                            }

                            @Override
                            public void onNext(Respose status) {

                                    showSnackBarVoted();

                            }
                        }
                );
    }

    private void showSnackBarVoting() {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_voting))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .animating();
    }

    private void showSnackBarVoted() {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_voted))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .animating();
    }

    public class StoryTabSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            headersPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            String tag = storiesPagerAdapter.getTag(tab.getPosition());
            StoryFragment fragment = (StoryFragment) getSupportFragmentManager().findFragmentByTag(tag);
            fragment.scrollToTop();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SimpleChromeCustomTabs.getInstance().connectTo(this);
        refreshHeader();
        trackCurrentPage();
    }

    @Override
    public void onPause() {
        SimpleChromeCustomTabs.getInstance().disconnectFrom(this);
        super.onPause();
    }

    private void trackCurrentPage() {
        Inject.usageAnalytics().trackPage(
                storiesPagerAdapter.getPageTitle(
                        headersPager.getCurrentItem()
                ).toString()
        );
    }

    private void showAddedBookmarkSnackbar(final DataPersister persister, final HNStory story) {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_added_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbarView.hideCrouton();
                                removeBookmark(persister, story);
                            }
                        }
                )
                .animating();
    }

    private void removeBookmark(DataPersister persister, HNStory story) {
        Inject.usageAnalytics().trackBookmarkEvent(
                getString(R.string.analytics_event_remove_bookmark_feed),
                story
        );
        persister.removeBookmark(story);
        showRemovedBookmarkSnackbar(persister, story);
    }

    private void showRemovedBookmarkSnackbar(final DataPersister persister, final HNStory story) {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_removed_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbarView.hideCrouton();
                                addBookmark(persister, story);
                            }
                        }
                )
                .animating();
    }

    private void addBookmark(DataPersister persister, HNStory story) {
        Inject.usageAnalytics().trackBookmarkEvent(
                getString(R.string.analytics_event_add_bookmark_feed),
                story
        );
        persister.addBookmark(story);
        showAddedBookmarkSnackbar(persister, story);
    }


    protected void refreshHeader() {
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        View header = LayoutInflater.from(this).inflate(R.layout.design_navigation_item_header, null, true);
//        navigationView.addHeaderView(header);
    }
}
