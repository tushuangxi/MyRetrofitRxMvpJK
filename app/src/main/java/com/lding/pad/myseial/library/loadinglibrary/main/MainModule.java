package com.lding.pad.myseial.library.loadinglibrary.main;

import android.support.annotation.NonNull;
import java.util.Random;
import dagger.Module;
import dagger.Provides;
import rx.Observable;


/**
 * 主要的Module
 */
@Module
public class MainModule {
    @Provides @NonNull
    @MainScope
    public Observable<MainLibrary> mainLibrary(final Random random) {
        return Observable.fromCallable(() -> new MainLibrary(random));
    }
}
