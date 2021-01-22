package com.tiseno.poplook.webservice;

/**
 * Created by rahn on 9/14/15.
 */
public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
}
