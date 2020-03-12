/**
 * Copyright (C) 2016 Anthony C. Restaino
 * <p/>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.ofidy.ofidyshoppingbrowser.bonsai;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings("unused")
public abstract class OnSubscribe<T> {

    /**
     * Called when the observable runs into an error
     * that will cause it to abort and not finish.
     * Receiving this callback means that the observable
     * is dead and no {@link #onComplete()} or {@link #onNext(Object)}
     * callbacks will be called. Default implementation
     * throws an exception, so you will get a crash
     * if the Observable throws an exception and this
     * method is not overridden. Do not call super when
     * you override this method.
     *
     * @param throwable an optional throwable that could
     *                  be sent.
     */
    public void onError(@NonNull Throwable throwable) {
        throw new RuntimeException("Exception thrown: override onError to handle it", throwable);
    }

    /**
     * Called before the observer begins
     * to process and emit items or complete.
     */
    public void onStart() {}

    /**
     * Called when the Observer emits an
     * item. It can be called multiple times.
     * It cannot be called after onComplete
     * has been called.
     *
     * @param item the item that has been emitted,
     *             can be null.
     */
    public void onNext(@Nullable T item) {}

    /**
     * This method is called when the observer is
     * finished sending the subscriber events. It
     * is guaranteed that no other methods will be
     * called on the OnSubscribe after this method
     * has been called.
     */
    public void onComplete() {}
}
