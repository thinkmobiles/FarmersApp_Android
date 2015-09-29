package com.farmers.underground.remote.util;

import android.support.annotation.NonNull;

/**
 * Created by tZpace
 * on 28-Sep-15.
 */
public interface ICallback<R,E> {

    void onSuccess(R result);

    void onError(@NonNull E error);

    void anyway();

}
