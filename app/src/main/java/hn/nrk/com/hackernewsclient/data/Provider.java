package hn.nrk.com.hackernewsclient.data;

import android.content.ContentValues;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import hn.nrk.com.hackernewsclient.data.refresher.UpdateSharedPreferences;
import hn.nrk.com.hackernewsclient.data.refresher.UpdateTimeStamp;
import hn.nrk.com.hackernewsclient.model.HNStory;
import hn.nrk.com.hackernewsclient.model.Respose;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class Provider {

    private static final long MILLIS_IN_AMINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long axMillisWithoutUpgrade = 60 * MILLIS_IN_AMINUTE;

    private final HackerNewsAPI api;
    private final DataPersister dataPersister;
    private final UpdateSharedPreferences refreshPreferences;
    //private final LoginSharedPreferences loginSharedPreferences;
    private Func1<Throwable, Respose> loginExpired = new Func1<Throwable, Respose>() {
        @Override
        public Respose call(Throwable throwable) {
            if (throwable instanceof Exception) {
                return Respose.LOGIN_EXPIRED;
            }
            return Respose.FAILURE;
        }
    };

    public Provider(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
        this.api = new HackerNewsAPI();
        this.refreshPreferences = UpdateSharedPreferences.newInstance();
        //this.loginSharedPreferences = LoginSharedPreferences.newInstance();
    }

    public boolean shouldUpdateContent(HNStory.FILTER FILTER) {
        if (FILTER == HNStory.FILTER.best_story) {
            return false;
        }
        UpdateTimeStamp lastUpdate = refreshPreferences.getLastRefresh(FILTER);
        UpdateTimeStamp now = UpdateTimeStamp.now();
        long elapsedTime = now.getMillis() - lastUpdate.getMillis();
        return elapsedTime > axMillisWithoutUpgrade;
    }

    public Observable<Integer> getStories(final HNStory.FILTER FILTER) {
        return api.getStories(FILTER)
                .flatMap(new Func1<List<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final List<ContentValues> stories) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                refreshPreferences.saveRefreshTick(FILTER);
                                dataPersister.persistStories(stories);
                                subscriber.onNext(stories.size());
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

    public Observable<Respose> observeComments(final Long storyId) {
        return api.getCommentsFromStory(storyId)
                .flatMap(new Func1<Vector<ContentValues>, Observable<Respose>>() {
                    @Override
                    public Observable<Respose> call(final Vector<ContentValues> commentsJsoup) {
                        return Observable.create(new Observable.OnSubscribe<Respose>() {
                            @Override
                            public void call(Subscriber<? super Respose> subscriber) {
                                dataPersister.persistComments(commentsJsoup, storyId);
                                subscriber.onNext(Respose.SUCCESS);
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

//    public Observable<Login.Status> observeLogin(final String username, final String password) {
//        return api.login(username, password)
//                .flatMap(new Func1<Login, Observable<Login.Status>>() {
//                    @Override
//                    public Observable<Login.Status> call(final Login login) {
//                        return Observable.create(new Observable.OnSubscribe<Login.Status>() {
//                            @Override
//                            public void call(Subscriber<? super Login.Status> subscriber) {
//                                loginSharedPreferences.saveLogin(login);
//                                subscriber.onNext(login.getStatus());
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                });
//    }

    public Observable<Respose> observeVote(final HNStory story) {
        return api.vote(story)
                .flatMap(new Func1<Respose, Observable<Respose>>() {
                    @Override
                    public Observable<Respose> call(final Respose response) {
                        return Observable.create(new Observable.OnSubscribe<Respose>() {
                            @Override
                            public void call(Subscriber<? super Respose> subscriber) {
                                if (response.equals(Respose.SUCCESS)) {
                                    dataPersister.addVote(story);
                                }
                                subscriber.onNext(response);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).onErrorReturn(loginExpired);
    }

    public Observable<Respose> observeCommentOnStory(final long storyId, final String message) {
        return api.commentOnStory(storyId, message)
                .flatMap(new Func1<Respose, Observable<Respose>>() {
                    @Override
                    public Observable<Respose> call(final Respose response) {
                        return Observable.create(new Observable.OnSubscribe<Respose>() {
                            @Override
                            public void call(Subscriber<? super Respose> subscriber) {
                                subscriber.onNext(response);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).onErrorReturn(loginExpired);
    }

    public Observable<Respose> observeReplyToComment(final long storyId, final long commentId, final String message) {
        return api.replyToComment(storyId, commentId, message)
                .flatMap(new Func1<Respose, Observable<Respose>>() {
                    @Override
                    public Observable<Respose> call(final Respose response) {
                        return Observable.create(new Observable.OnSubscribe<Respose>() {
                            @Override
                            public void call(Subscriber<? super Respose> subscriber) {
                                subscriber.onNext(response);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).onErrorReturn(loginExpired);
    }

}
