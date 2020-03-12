package com.ofidy.ofidyshoppingbrowser.bonsai;

import android.support.annotation.NonNull;

/**
 * Copyright 8/17/2016 Anthony Restaino
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class OnErrorRunnable<T> implements Runnable {
    private final OnSubscribe<T> onSubscribe;
    private final Throwable throwable;

    public OnErrorRunnable(@NonNull OnSubscribe<T> onSubscribe, @NonNull Throwable throwable) {
        this.onSubscribe = onSubscribe;
        this.throwable = throwable;
    }

    @Override
    public void run() {
        onSubscribe.onError(throwable);
    }
}
