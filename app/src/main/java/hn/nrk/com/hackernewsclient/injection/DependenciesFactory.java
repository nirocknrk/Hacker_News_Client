package hn.nrk.com.hackernewsclient.injection;



import hn.nrk.com.hackernewsclient.analy.CrashAnalytics;
import hn.nrk.com.hackernewsclient.analy.UseAnalytics;
import hn.nrk.com.hackernewsclient.data.AppInviter;
import hn.nrk.com.hackernewsclient.data.ConnectionProvider;
import hn.nrk.com.hackernewsclient.data.DataPersister;
import hn.nrk.com.hackernewsclient.data.Provider;

public interface DependenciesFactory {

    DataPersister createDatabasePersister();

    Provider createDataRepository(DataPersister dataPersister);

    CrashAnalytics createCrashAnalytics();

    ConnectionProvider createConnection();

    UseAnalytics createUsageAnalytics();

    AppInviter createAppInviter();

}
