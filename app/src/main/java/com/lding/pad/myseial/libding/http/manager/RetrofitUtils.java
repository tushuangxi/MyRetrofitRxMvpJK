package com.lding.pad.myseial.libding.http.manager;

import com.lding.pad.myseial.libding.application.PadApplication;
import com.lding.pad.myseial.libding.utils.NetworkUtils;


import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.lding.pad.myseial.libding.http.ApiService.APP_HOST;

public class RetrofitUtils {

    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;

    private RetrofitUtils() {

    }

    public static Retrofit newInstence() {
        mRetrofit = null;
        mRetrofit = new Retrofit.Builder()
                .client(getOkHttpClient())//指定OKHttpClient
                .baseUrl(APP_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit;
    }
    //连接超时时间 5s
    private static final long CONNECT_TIMEOUT_SECOND = 5;
    //缓存有效期 1天
    private static final long CACHE_STALE_SECOND = 24 * 60 * 60;
    //缓存大小 100M
    private static final long CACHE_SIZE = 1024 * 1024 * 100;

    // 配置OkHttpClient
    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitUtils.class) {
                if (mOkHttpClient == null) {
                    // OkHttpClient配置是一样的,静态创建一次即可
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(PadApplication.getContext().getCacheDir(), "HttpCache"),
                            CACHE_SIZE);

                    mOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(loggingInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(CONNECT_TIMEOUT_SECOND, TimeUnit.SECONDS).build();
                }
            }
        }
        return mOkHttpClient;
    }


    // server响应头拦截器，用来配置缓存策略
    private static Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected(PadApplication.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
//                LogUtils.e(TAG, "no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetworkUtils.isConnected(PadApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .header("Content-Type", "application/json")
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached," + CACHE_STALE_SECOND)
                        .removeHeader("Pragma").build();
            }

        }
    };

    /**

     * Function：打印请求参数和返回结果
     */

    // 打印json数据拦截器
    private static Interceptor loggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();

            long t1 = System.nanoTime();//请求发起的时间
//        com.orhanobut.logger.Logger.e(String.format("发送请求 %s %n%s",
//                request.url(),  chain.request().body().toString()));

            com.orhanobut.logger.Logger.e(String.format("发送请求 %s %n%s", request.url(),  chain.request().body()==null?"NONE":chain.request().body().toString()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间

            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            com.orhanobut.logger.Logger.e(String.format(Locale.getDefault(), "接收响应: [%s] %n返回json:【%s】 %.1fms", response.request().url(), responseBody.string(), (t2 - t1) / 1e6d));
            return response;
        }
    };

}
