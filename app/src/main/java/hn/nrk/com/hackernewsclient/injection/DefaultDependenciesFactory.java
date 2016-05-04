package hn.nrk.com.hackernewsclient.injection;

import android.content.Context;

import hn.nrk.com.hackernewsclient.analy.CrashAnalytics;
import hn.nrk.com.hackernewsclient.analy.CrashlyticsAnalytics;
import hn.nrk.com.hackernewsclient.analy.UseAnalytics;
import hn.nrk.com.hackernewsclient.data.AppInviter;
import hn.nrk.com.hackernewsclient.data.ConnectionProvider;
import hn.nrk.com.hackernewsclient.data.DataPersister;
import hn.nrk.com.hackernewsclient.data.Provider;


public class DefaultDependenciesFactory implements DependenciesFactory {

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }

    @Override
    public DataPersister createDatabasePersister() {
        return new DataPersister(context.getContentResolver());
    }

    @Override
    public Provider createDataRepository(DataPersister dataPersister) {
        return new Provider(dataPersister);
    }

    @Override
    public CrashAnalytics createCrashAnalytics() {
        return new CrashlyticsAnalytics();
    }

    @Override
    public ConnectionProvider createConnection() {
        return new ConnectionProvider();
    }

    @Override
    public UseAnalytics createUsageAnalytics() {
        return new UseAnalytics();
    }

    @Override
    public AppInviter createAppInviter() {
        return new AppInviter();
    }

}
