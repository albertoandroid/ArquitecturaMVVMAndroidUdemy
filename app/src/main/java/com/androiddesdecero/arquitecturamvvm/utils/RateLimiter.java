package com.androiddesdecero.arquitecturamvvm.utils;

import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.TimeUtils;

import java.util.concurrent.TimeUnit;

public class RateLimiter<KEY> {
    private ArrayMap<KEY, Long> timestamps = new ArrayMap<>();
    private final long timeout;

    public RateLimiter(int timeout, TimeUnit timeUnit){
        this.timeout = timeUnit.toMillis(timeout);
    }

    public synchronized boolean shouldFetch(KEY key){
        Long lastFetched = timestamps.get(key);
        long now = now();
        if(lastFetched == null){
            timestamps.put(key, now);
            return true;
        }

        if(now - lastFetched > timeout){
            timestamps.put(key, now);
            return true;
        }

        return false;
    }

    private long now(){
        return SystemClock.uptimeMillis();
    }

    public synchronized void reset(KEY key){
        timestamps.remove(key);
    }
}
