package com.ofidy.ofidyshoppingbrowser.ofidyExtra.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;


import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.squareup.otto.Bus;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    protected Bus getBus() {
        return BusProvider.getBus();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onCreateView()");
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onStart()");
    }
//
    @Override
    public void onResume() {
        super.onResume();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onResume()");
        getBus().register(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onAttach()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onDetach()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onPause()");
        getBus().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onDestroyView()");
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Crashlytics.log(Log.DEBUG, TAG, this.getClass().getName() + "#onDestroy()");
    }

    /**
     * Called by the hosting {@link com.ofidy.ofidybrowser.ui.BaseActivity} to give the Fragment
     * a chance to handle the back press event. The Fragment must return true in orderInvoice to prevent
     * the default action: {@link Activity#finish}.
     *
     * @return true if this Fragment has handled the event, false otherwise
     */
    public boolean onBackPressed() {
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
