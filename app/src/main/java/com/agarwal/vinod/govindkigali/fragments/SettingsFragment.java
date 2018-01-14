package com.agarwal.vinod.govindkigali.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.activities.AboutAppDetails;
import com.agarwal.vinod.govindkigali.activities.AuthenticationScreen;
import com.agarwal.vinod.govindkigali.activities.SignInScreen;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.agarwal.vinod.govindkigali.utils.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Locale;

public class SettingsFragment extends Fragment {



    DatabaseReference feedRefrence = FirebaseDatabase.getInstance().getReference("feed");
    public static final String TAG = "SETT";


    public SettingsFragment() {
        // Required empty public constructor
    }

    Switch night_mode;
    TextView rate_app, share_app, about_app, feedback, language;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        final PrefManager prefManager = new PrefManager(getContext());
//        if (prefManager.getTheme().equals("dark"))
//            getContext().setTheme(R.style.AppThemeNormalDark);
//        else if (prefManager.getTheme().equals("black"))
//            getContext().setTheme(R.style.AppThemeNormalBlack);

        final View settingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);

        night_mode = settingsFragment.findViewById(R.id.id_NightMode);
        language = settingsFragment.findViewById(R.id.id_Language);
        feedback = settingsFragment.findViewById(R.id.id_Feedback);
        rate_app = settingsFragment.findViewById(R.id.id_RateApp);
        share_app = settingsFragment.findViewById(R.id.id_ShareApp);
        about_app = settingsFragment.findViewById(R.id.id_AboutApp);

        final PrefManager prefManager = new PrefManager(getContext());

        night_mode.setChecked(prefManager.isNightModeEnabled2());


        night_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                prefManager.setNightModeEnabled(isChecked);
                restartSettingsFrag();

            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/html");
//                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.mail_feedback_email) });
//                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));
//                intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.mail_feedback_message));
//                intent.setPackage("com.google.android.gm");
//                startActivity(Intent.createChooser(intent, getString(R.string.title_send_feedback)));
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Send Us Some Feedback!");
                final EditText input = new EditText(getContext());
                b.setView(input);
                b.setPositiveButton("Send Feedback", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String result = input.getText().toString();
                        String id = feedRefrence.push().getKey();
                        TelephonyManager manager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                        String carrierName = manager.getNetworkOperatorName();
                        File path = Environment.getDataDirectory();
                        Log.d(TAG, "onClick: " + path);
                        double usableSize = (double) path.getUsableSpace() / 1e+9;
                        double totalSize = (double) path.getTotalSpace() / 1e+9;
                        double availableBlocks = (double) path.getFreeSpace() / 1e+9;

                        String deviceOs = System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
                        Integer deviceApi = android.os.Build.VERSION.SDK_INT;
                        String deviceMan = Build.MANUFACTURER;
                        String device = Build.DEVICE;

                        String deviceModel = android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

                        feedRefrence.child(id).child("feedback: ").setValue(result);
                        feedRefrence.child(id).child("OS Version: ").setValue(deviceOs);
                        feedRefrence.child(id).child("OS API Level: ").setValue(deviceApi);
                        feedRefrence.child(id).child("Device Manufacturer: ").setValue(deviceMan);
                        feedRefrence.child(id).child("Device: ").setValue(device);
                        feedRefrence.child(id).child("Carrier: ").setValue(carrierName);
                        feedRefrence.child(id).child("Available Storage: ").setValue(availableBlocks + "GB");
                        feedRefrence.child(id).child("Usable Storage: ").setValue(usableSize + "GB");
                        feedRefrence.child(id).child("Total Storage: ").setValue(totalSize + "GB");
                        feedRefrence.child(id).child("Model (and Product): ").setValue(deviceModel);
                    }
                });
                b.setNegativeButton("CANCEL", null);
                b.show();
            }
        });

        rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("market://details?id=" +
                        "bhaktisagar.vinodagarwal.app"/*settingsFragment.getContext().getPackageName()*/);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                } else {
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                }
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" +
                                    "bhaktisagar.vinodagarwal.app"/*settingsFragment.getContext().getPackageName())*/)));
                }
            }
        });


        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String str = "\nLet me recommend you this application\n\n";
                    str = str + "https://play.google.com/store/apps/details?id=bhaktisagar.vinodagarwal.app\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, str);
                    startActivity(Intent.createChooser(i, getString(R.string.app_name)));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutAppDetails.class));
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguageWithDialog(new PrefManager(getContext()));
            }
        });

        ((TextView)settingsFragment.findViewById(R.id.id_signin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SignInScreen.class));
            }
        });

        return settingsFragment;

    }

    public void setLanguageWithDialog(final PrefManager prefManager) {
        final CharSequence[] items = {getString(R.string.english), getString(R.string.hindi)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.languages);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        String languageToLoad = "en"; // your language
                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getActivity().getBaseContext().getResources().updateConfiguration(config,
                                getActivity().getBaseContext().getResources().getDisplayMetrics());
                        prefManager.setLanguage("en");
                        dialog.dismiss();
                        //((BottomNavigationView) getActivity().findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_play);
                        restartSettingsFrag();

                        break;
                    }
                    case 1: {
                        // User cancelled the dialog

                        String languageToLoad = "hi"; // your language
                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getActivity().getBaseContext().getResources().updateConfiguration(config,
                                getActivity().getBaseContext().getResources().getDisplayMetrics());
                        prefManager.setLanguage("hi");
                        dialog.dismiss();
                       //((BottomNavigationView) getActivity().findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_play);
                        restartSettingsFrag();
                        break;

                    }
                }
            }
        });
        /*// Add the buttons
        builder.setPositiveButton(R.string.english, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config,
                        getActivity().getBaseContext().getResources().getDisplayMetrics());
                prefManager.setLanguage("en");
                dialog.dismiss();
                ((BottomNavigationView) getActivity().findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_play);

                getActivity().recreate();
                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
        builder.setNegativeButton(R.string.hindi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

                String languageToLoad = "hi"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config,
                        getActivity().getBaseContext().getResources().getDisplayMetrics());
                prefManager.setLanguage("hi");
                dialog.dismiss();
                ((BottomNavigationView) getActivity().findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_play);
                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });*/

        builder.create().show();
    }

    void restartSettingsFrag(){

        Intent i = getActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra(MainActivity.FRAGMENT_TO_LAUNCH, MainActivity.SETTING_FRAGMENT);
        startActivity(i);
        getActivity().overridePendingTransition(0, 0);
    }
}

