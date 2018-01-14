package com.agarwal.vinod.govindkigali;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.adapters.SongImageAdapter;
import com.agarwal.vinod.govindkigali.fragments.MainFragment;
import com.agarwal.vinod.govindkigali.fragments.MyMusicFragment;
import com.agarwal.vinod.govindkigali.fragments.SettingsFragment;
import com.agarwal.vinod.govindkigali.fragments.ThoughtFragment;
import com.agarwal.vinod.govindkigali.fragments.UpcomingFragment;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerService;
import com.agarwal.vinod.govindkigali.utils.BottomNavigationViewHelper;
import com.agarwal.vinod.govindkigali.utils.CustomDialogClass;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.agarwal.vinod.govindkigali.utils.TerminationService;
import com.agarwal.vinod.govindkigali.utils.Util;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Locale;

import static android.view.View.GONE;
import static com.agarwal.vinod.govindkigali.playerUtils.PlayerService.NOTIFY_CLOSE;
import static com.agarwal.vinod.govindkigali.playerUtils.PlayerService.NOTIFY_NEXT;
import static com.agarwal.vinod.govindkigali.playerUtils.PlayerService.NOTIFY_PLAY;
import static com.agarwal.vinod.govindkigali.playerUtils.PlayerService.NOTIFY_PREVIOUS;

public class MainActivity extends AppCompatActivity implements PlayerCommunication {

    public static String FRAGMENT_TO_LAUNCH = "FRAGMENT_TO_LAUNCH";
    public static String SETTING_FRAGMENT = "SETTING_FRAGMENT";
    Toolbar toolbar;
    Spinner spinnerToolbar;
    View includeHead, includePlayer;
    public SlidingUpPanelLayout slidingUpPanelLayout;
    SearchView searchView;
    RelativeLayout rlPlayer;
    FrameLayout flPlayerOptions;
    public PlayerService service;
    TerminationService terminateService;
    public MediaPlayer mediaPlayer;
    public AudioManager audioManager;
    public ProgressBar pbLoading, pbProgress;
    //    private DownloadMusic downloadMusic;
    public ImageView ivPlayPause, ivUpArrow, ivPlay, ivNext, ivPrevious, ivRepeat, ivFav, ivMore, ivDownload;
    public TextView tvStart, tvEnd, tvName;
    public LinearLayout llProgress, llPlayerOptions;
    public String client_id = "?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J";
    public Integer value = 0;
    public Boolean f = false;
    public Boolean manual = true;
    boolean mBound = false;
    public Boolean fav = true;
    public static Boolean focus = true;
    static Boolean repeat = false;
    public static Integer fragmentCheck = 0;
    public static final String TAG = "MAIN";
    public RecyclerView recyclerView;
    public SongImageAdapter adapter;
    BottomNavigationView navigation;
    public DiscreteSeekBar discreteSeekBar;
    MainFragment mainFragment;
    public ArrayList<Song> playlist = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_play:
                    hideIt();
                    mainFragment = new MainFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, mainFragment)
                            .commit();
                    return true;
                case R.id.navigation_thought:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new ThoughtFragment())
                            .commit();
                    return true;
                case R.id.navigation_upcoming:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new UpcomingFragment())
                            .commit();
                    return true;
                case R.id.navigation_my_music:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new MyMusicFragment())
                            .commit();
                    return true;
                case R.id.navigation_settings:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new SettingsFragment())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initiateNightMode();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        //providing ids to views
        spinnerToolbar = toolbar.findViewById(R.id.spinner_toolbar);
        spinnerToolbar.setVisibility(View.INVISIBLE);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        pbLoading = findViewById(R.id.pb_loading);
        pbProgress = findViewById(R.id.pb_progress);
        ivPlayPause = findViewById(R.id.iv_play_pause);
        ivPlay = findViewById(R.id.iv_play);
        ivPrevious = findViewById(R.id.iv_previous);
        ivNext = findViewById(R.id.iv_next);
        ivUpArrow = findViewById(R.id.iv_up_arrow);
        ivUpArrow = findViewById(R.id.iv_up_arrow);
        tvName = findViewById(R.id.tv_song);
        tvStart = findViewById(R.id.tv_start);
        tvEnd = findViewById(R.id.tv_end);
        ivMore = findViewById(R.id.iv_more);
        ivFav = findViewById(R.id.iv_fav);
        ivDownload = findViewById(R.id.iv_download);
        ivRepeat = findViewById(R.id.iv_repeat);
        llPlayerOptions = findViewById(R.id.ll_player_options);
        flPlayerOptions = findViewById(R.id.fl_player_options);
        recyclerView = findViewById(R.id.rv_song_image);
        llProgress = findViewById(R.id.ll_progress);
        discreteSeekBar = findViewById(R.id.dsb_progress);
        rlPlayer = findViewById(R.id.rl_player);
        includeHead = findViewById(R.id.include_head);
        includePlayer = findViewById(R.id.include_player);
        navigation = findViewById(R.id.navigation);

        setTitle(
                Util.getLocalizedResources(MainActivity.this,
                        new Locale(new PrefManager(MainActivity.this).getUserLanguage()))
                        .getString(R.string.app_name)
        );
        initiateFirstLaunch();

        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fg, mainFragment)
                .commit();

        //setting panel to hidden when activity launches
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        //setting panel states
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    includePlayer.setVisibility(GONE);
                    includeHead.setVisibility(View.VISIBLE);
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    includePlayer.setVisibility(View.VISIBLE);
                    includeHead.setVisibility(GONE);
                    if (service.mediaPlayer != null && (service.mediaPlayer.isPlaying() || discreteSeekBar.getProgress() != 0)) {
                        ivPlayPause.setVisibility(View.VISIBLE);
                        pbLoading.setVisibility(View.GONE);
                    } else if (service.mediaPlayer != null) {
                        pbLoading.setVisibility(View.VISIBLE);
                        ivPlayPause.setVisibility(View.GONE);
                    }
                }
            }
        });

        //providing adapter to recycler views
        adapter = new SongImageAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        //snap helper to provide snaping in image scrolling
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        LocalBroadcastManager.getInstance(this).
                registerReceiver(imageReceiver, new IntentFilter("custom-image"));

        LocalBroadcastManager.getInstance(this).
                registerReceiver(terminateReciever, new IntentFilter("terminate"));

        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_PLAY));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_NEXT));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_CLOSE));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_PREVIOUS));

        //Pop up menu
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //fav goes to firebase as fav folder
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.changeFavourite();
            }
        });

        //download feature
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Starting Download", Toast.LENGTH_SHORT).show();
                service.downloadSong();
            }
        });

        //now play pause button set-up
        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.manual = true;
                service.playPause();
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.manual = true;
                service.playPause();
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.playPrevious();
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.playNext();
            }
        });

        ivRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.updatingRepeat();
            }
        });

        discreteSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, final int i, boolean b) {
                service.progressChanged(i, b);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        launchFragmentFromBundle();

    }

    private void launchFragmentFromBundle() {
        String action = getIntent().getExtras().getString(FRAGMENT_TO_LAUNCH);
        if(action!=null && action.equals(SETTING_FRAGMENT)) {
            navigation.setSelectedItemId(R.id.navigation_settings);
        }
    }

    private void initiateNightMode() {
        PrefManager prefManager = new PrefManager(this);
        if (prefManager.isNightModeEnabled2()) {
            setTheme(R.style.FeedActivityThemeDark);
            Log.d(TAG, "initiateNightMode: NIGHT MODE ENABLED");
        } else {
            setTheme(R.style.FeedActivityThemeLight);
            Log.d(TAG, "initiateNightMode: NIGHT MODE DISABLED");
        }
    }

    /**
     * Code to initiate Language with/without dialog
     */
    private void initiateFirstLaunch() {

        PrefManager prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(false);
            setLanguageWithDialog(prefManager);
        } else {
            setLanguage(prefManager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Yooo", "onQueryTextSubmit: " + query);
                searchView.clearFocus();
                /*if(mainFragment != null) {
                    mainFragment.setSongAdapterFilter(query);
                }*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Yooo", "onQueryTextSubmit: " + newText);
                if (mainFragment != null) {
                    mainFragment.setSongAdapterFilter(newText);
                }
                return true;
            }


        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (mainFragment != null) {
                    mainFragment.setSongAdapterFilter("");
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_notifications:
                break;*/
            case R.id.action_search:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragmentCheck > 0) {
            --fragmentCheck;
            getSupportFragmentManager().popBackStack();
        } else {
            moveTaskToBack(true);
//            super.onBackPressed();
        }
    }


    public void setLanguageWithDialog(final PrefManager prefManager) {
        final CharSequence[] items = {getString(R.string.english), getString(R.string.hindi)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
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
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        prefManager.setLanguage("en");
                        dialog.dismiss();
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        break;
                    }
                    case 1: {
                        // User cancelled the dialog

                        String languageToLoad = "hi"; // your language
                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        prefManager.setLanguage("hi");
                        dialog.dismiss();
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        break;

                    }
                }
            }
        });

        /*builder.setPositiveButton(R.string.english, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                prefManager.setLanguage("en");
                dialog.dismiss();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
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
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                prefManager.setLanguage("hi");
                dialog.dismiss();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });*/

        builder.create().show();
    }

    public void setLanguage(PrefManager prefManager) {
        String languageToLoad = prefManager.getUserLanguage();

        if (Locale.getDefault().getLanguage().equals(languageToLoad)) return;

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    public BroadcastReceiver imageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            service.releaseMediaPlayer();
            Integer pos = intent.getIntExtra("val", 0);
            Log.d(TAG, "onReceive: " + pos);
//            preparePlayer(pos);
        }
    };

    public BroadcastReceiver terminateReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            service.stopNotificationPlayer();
            Log.d("ClearFromRecentService", "onReceive: Notification Gone");
        }
    };

    void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: " + item);
                switch (item.getItemId()) {
                    case R.id.navigation_add_to_playlist:
                        CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, playlist.get(value), MainActivity.this);
                        cdd.show();
                        Log.d(TAG, "onMenuItemClick: jksahdkjsdhaksjhdaksjdhk");
                        break;
                }
                return true;
            }
        });
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (!focus) {
//            audioManager.abandonAudioFocus(audioFocusChangeListener);
//        } else {
//            if (mediaPlayer != null) {
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        ConnectivityManager cm =
//                                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//                        boolean isConnected = activeNetwork != null &&
//                                activeNetwork.isConnectedOrConnecting();
//                        if (isConnected) {
//                            audioManager.abandonAudioFocus(audioFocusChangeListener);
//                            if (!repeat) {
//                                if (value + 1 < playlist.size()) {
//                                    value = value + 1;
//                                    preparePlayer(value);
//                                } else {
//                                    value = 0;
//                                    preparePlayer(value);
//                                }
//                                Log.d(TAG, "onClick: OnCreateView:" + value);
//                            } else {
//                                preparePlayer(value);
//                            }
//                        } else {
//                            if (repeat) {
//                                mediaPlayer.pause();
//                                mediaPlayer.start();
//                            } else {
//                                f = true;
//                                mediaPlayer.pause();
//                                ivPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
//                                ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
//                                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
//                                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
//                                if (mNotificationManager != null) {
//                                    mNotificationManager.notify(NOTIFICATION_ID, notification);
//                                }
//                            }
//                            Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//            Log.d(TAG, "onPause: " + audioFocusChangeListener);
//        }
//    }

    void hideIt() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }


    @Override
    protected void onResume() {
        overridePendingTransition(0, 0);
        super.onResume();
    }

    public BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case NOTIFY_PLAY:
                    Toast.makeText(context, "Play/Pause", Toast.LENGTH_SHORT).show();
                    manual = true;
                    service.playPause();
                    break;

                case NOTIFY_NEXT:
                    Toast.makeText(context, "Next", Toast.LENGTH_SHORT).show();
                    service.playNext();
                    break;

                case NOTIFY_PREVIOUS:
                    Toast.makeText(context, "Previous", Toast.LENGTH_SHORT).show();
                    service.playPrevious();
                    break;

                case NOTIFY_CLOSE:
                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    service.cancelNotification();
                    break;
            }
            Toast.makeText(context, "Receiver :) ", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void uploadImage(Bitmap bitmap) {
        service.imageUpload(bitmap);
    }

    /**
     * Interface method to get playlist and song id/number to play song
     */
    @Override
    public void playSong(ArrayList<Song> playlist, Integer value) {
        Log.d(TAG, "playSong: ---------------------------------------");

        //If Service is bounded correctly
        if (mBound) {
            //setting panel to COLLAPSED STATE
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            //Providing playlist to service
            service.updateplayList(playlist);

            //Calling service method to start playing song
            service.createPlayer(value);

        } else {

            //If not bounded correctly
            Log.d(TAG, "playSong: not binding");
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder mService) {

            // We've bound to PlayerService, cast the IBinder and get PlayerService instance
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) mService;
            service = binder.getService();

            //Passing activity instance to PlayerService
            service.getActivtyContext(MainActivity.this);
            service.setViews();

            //Check value to true
            mBound = true;

            Log.d(TAG, "onServiceConnected: Binding service");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    /**
     * Creating Intent for service and calling bind service in onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();

        //Creating Intent for service
        Intent intent = new Intent(this, PlayerService.class);

        //TODO: If background possible by startService()
        //startService(intent);

        //Binding service
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //Termination Service
        startService(new Intent(this, TerminationService.class));
    }

    @Override
    public void onDestroy() {
        service.releaseMediaPlayer();
        if (service.mNotificationManager != null) service.mNotificationManager.cancel(service.NOTIFICATION_ID);
        PlayerService.focus = true;
        service.repeat = false;
        fragmentCheck = 0;
//        unregisterReceiver(playerReceiver);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        slidingUpPanelLayout = null;
        Log.d(TAG, "recreate: called :) :) :) :) :) :)");
        super.onDestroy();
    }
}

