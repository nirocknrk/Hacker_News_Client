package hn.nrk.com.hackernewsclient.views.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import hn.nrk.com.hackernewsclient.BuildConfig;
import hn.nrk.com.hackernewsclient.news.TopNewsFragment;
import hn.nrk.com.hackernewsclient.views.recyclerview.adapter.TaggedFragmentStatePagerAdapter;


public class StoriesPagerAdapter extends TaggedFragmentStatePagerAdapter {

    private static final String TAG_TEMPLATE = BuildConfig.APPLICATION_ID + ".STORY_FRAGMENT#";

//    private String[] categories = {"Top Stories", "Newest", "Best", "Show HN", "Ask HN", "Jobs"};
    private String[] categories = {"Top Stories", "Newest"};

    public StoriesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TopNewsFragment.from(TopNewsFragment.QUERY.top);
            case 1:
                return TopNewsFragment.from(TopNewsFragment.QUERY.newest);
//            case 2:
//                return TopStoriesFragment.from(TopStoriesFragment.QUERY.best);
//            case 3:
//                return new ShowHNFragment();
//            case 4:
//                return new AskHNFragment();
//            case 5:
//                return new JobsHNFragment();
            default:
                return TopNewsFragment.from(TopNewsFragment.QUERY.top);
        }
    }

    @Override
    public String getTag(int position) {
        return TAG_TEMPLATE + position;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories[position];
    }

}
