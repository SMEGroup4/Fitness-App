package com.easyfitness;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.BSD3ClauseLicense;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;

public class BodyScannerFragment extends Fragment{
    private String name;
    private int id;
    private MainActivity mActivity = null;

    private final View.OnClickListener clickLicense = v -> {

        String name = null;
        String url = null;
        String copyright = null;
        License license = null;

        switch (v.getId()) {
            case R.id.LicensesDialog:
                name = "LicensesDialog";
                url = "https://github.com/PSDev/LicensesDialog";
                copyright = "Copyright 2013 Philip Schiffer";
                license = new ApacheSoftwareLicense20();
                break;
        }

        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(getMainActivity())
                .setNotices(notice)
                .build()
                .show();
    };

    public static BodyScannerFragment newInstance(String name, int id) {
        BodyScannerFragment f = new BodyScannerFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putInt("id", id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_scanner, container, false);

        return view;
    }

    public MainActivity getMainActivity() {
        return this.mActivity;
    }

}
