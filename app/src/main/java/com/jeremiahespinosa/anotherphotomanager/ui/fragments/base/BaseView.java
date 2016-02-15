package com.jeremiahespinosa.anotherphotomanager.ui.fragments.base;

/**
 * Created by jespinosa on 2/13/16.
 */
public interface BaseView {
    void showProgressDialog();

    void hideProgressDialog();

    void showDialog(String title, String description, boolean cancellable);
}