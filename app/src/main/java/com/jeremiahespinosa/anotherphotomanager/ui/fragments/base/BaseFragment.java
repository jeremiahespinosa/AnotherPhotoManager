package com.jeremiahespinosa.anotherphotomanager.ui.fragments.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jeremiahespinosa.anotherphotomanager.app.App;

import butterknife.ButterKnife;

/**
 * Created by jespinosa on 2/13/16.
 */
public class BaseFragment extends Fragment implements BaseView{

    ProgressDialog mProgressDialog;
    AlertDialog mAlertDialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mProgressDialog = App.getProgressDialog(getActivity(), "Loading");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    @Override
    public void showDialog(String title, String description, boolean cancellable) {
        mAlertDialog = App.getDialogBuilder(getActivity(), title, description, cancellable).show();
    }
}
