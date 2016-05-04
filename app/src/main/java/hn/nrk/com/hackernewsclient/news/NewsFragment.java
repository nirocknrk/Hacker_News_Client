package hn.nrk.com.hackernewsclient.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import hn.nrk.com.hackernewsclient.data.data_connectivity.Network_Checker;


public class NewsFragment extends Fragment {

    private Network_Checker networkChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNetworkChecker();
    }

    private void initNetworkChecker() {
        networkChecker = new Network_Checker(getActivity());
    }

    protected boolean isOnline() {
        return networkChecker.isConnected();
    }
}
