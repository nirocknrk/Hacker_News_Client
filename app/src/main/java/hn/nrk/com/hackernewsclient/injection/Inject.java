package hn.nrk.com.hackernewsclient.injection;


import com.novoda.notils.exception.DeveloperError;

import hn.nrk.com.hackernewsclient.analy.CrashAnalytics;
import hn.nrk.com.hackernewsclient.analy.UseAnalytics;
import hn.nrk.com.hackernewsclient.data.AppInviter;
import hn.nrk.com.hackernewsclient.data.ConnectionProvider;
import hn.nrk.com.hackernewsclient.data.DataPersister;
import hn.nrk.com.hackernewsclient.data.Provider;

public class Inject {

    private static Inject INSTANCE;
    private final Provider provider;
    private final CrashAnalytics crashAnalytics;
    private final DataPersister persister;
    private final ConnectionProvider connectionProvider;
    private final UseAnalytics analyticsTracker;
    private final AppInviter appInviter;

    private Inject(Provider provider, CrashAnalytics crashAnalytics, DataPersister persister, ConnectionProvider connectionProvider, UseAnalytics analyticsTracker, AppInviter appInviter) {
        this.provider = provider;
        this.crashAnalytics = crashAnalytics;
        this.persister = persister;
        this.connectionProvider = connectionProvider;
        this.analyticsTracker = analyticsTracker;
        this.appInviter = appInviter;
    }

    public static void using(DependenciesFactory factory) {
        DataPersister dataPersister = factory.createDatabasePersister();
        Provider provider = factory.createDataRepository(dataPersister);
        CrashAnalytics crashAnalytics = factory.createCrashAnalytics();
        ConnectionProvider connectionProvider = factory.createConnection();
        UseAnalytics analyticsTracker = factory.createUsageAnalytics();
        AppInviter appInviter = factory.createAppInviter();
        INSTANCE = new Inject(provider, crashAnalytics, dataPersister, connectionProvider, analyticsTracker, appInviter);
    }

    private static Inject instance() {
        if (INSTANCE == null) {
            throw new DeveloperError("You need to setup Inject to use a valid DependenciesFactory");
        }
        return INSTANCE;
    }

    public static Provider provider() {
        return instance().provider;
    }

    public static CrashAnalytics crashAnalytics() {
        return instance().crashAnalytics;
    }

    public static DataPersister dataPersister() {
        return instance().persister;
    }

    public static ConnectionProvider connectionProvider() {
        return instance().connectionProvider;
    }

    public static UseAnalytics usageAnalytics() {
        return instance().analyticsTracker;
    }

    public static AppInviter appInviter() {
        return instance().appInviter;
    }

}
