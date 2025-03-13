package com.github.pengpan.config;

import cn.hutool.core.collection.CollUtil;
import com.ejlchina.data.jackson.JacksonDataConvertor;
import com.ejlchina.json.JSONKit;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pengpan.client.DdddOcrClient;
import com.github.pengpan.client.FateadmClient;
import com.github.pengpan.client.MainClient;
import com.github.pengpan.common.constant.SystemConstant;
import com.github.pengpan.common.cookie.CookieManager;
import com.github.pengpan.common.proxy.SwitchProxySelector;
import com.github.pengpan.common.retrofit.BasicTypeConverterFactory;
import com.github.pengpan.common.retrofit.BodyCallAdapterFactory;
import com.github.pengpan.common.retrofit.ResponseCallAdapterFactory;
import com.github.pengpan.interceptor.LoggingInterceptor;
import com.github.pengpan.interceptor.MainClientInterceptor;
import com.github.pengpan.interceptor.ProxyInterceptor;
import com.github.pengpan.interceptor.RetryInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author pengpan
 */
@Configuration
public class RetrofitConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new ProxyInterceptor())
                .addInterceptor(new MainClientInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new RetryInterceptor())
                .proxySelector(new SwitchProxySelector())
                .followRedirects(false)
                .cookieJar(new CookieManager())
                .connectionPool(new ConnectionPool(200, 2, TimeUnit.MINUTES))
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .build();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl(SystemConstant.DOMAIN)
                .client(okHttpClient)
                .addCallAdapterFactory(new BodyCallAdapterFactory())
                .addCallAdapterFactory(new ResponseCallAdapterFactory())
                .addConverterFactory(new BasicTypeConverterFactory())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    @Bean
    public Void initJSONKit(ObjectMapper objectMapper) {
        JSONKit.init(new JacksonDataConvertor(objectMapper));
        return null;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public MainClient mainClient(Retrofit retrofit) {
        return retrofit.create(MainClient.class);
    }

    @Bean
    public FateadmClient fateadmClient(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl(SystemConstant.FATEADM_DOMAIN)
                .client(okHttpClient)
                .addCallAdapterFactory(new BodyCallAdapterFactory())
                .addCallAdapterFactory(new ResponseCallAdapterFactory())
                .addConverterFactory(new BasicTypeConverterFactory())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(FateadmClient.class);
    }

    @Bean
    public DdddOcrClient ddddOcrClient(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl(SystemConstant.DEFAULT_DDDD_OCR_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(new BodyCallAdapterFactory())
                .addCallAdapterFactory(new ResponseCallAdapterFactory())
                .addConverterFactory(new BasicTypeConverterFactory())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(DdddOcrClient.class);
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Cache cache = new ConcurrentMapCache("KEY_LIST");
        List<Cache> caches = CollUtil.newArrayList(cache);
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(8);
        scheduler.setThreadNamePrefix("scheduled-thread-");
        return scheduler;
    }
}
