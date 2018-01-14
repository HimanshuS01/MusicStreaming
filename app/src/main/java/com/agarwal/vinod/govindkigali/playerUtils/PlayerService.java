package com.agarwal.vinod.govindkigali.playerUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by darsh on 28/12/17.
 */
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    /**
     * Constants for notification fields
     */
    public static final String NOTIFY_PREVIOUS = "com.com.agarwal.vinod.govindkigali.previous";
    public static final String NOTIFY_CLOSE = "com.com.agarwal.vinod.govindkigali.close";
    public static final String NOTIFY_PLAY = "com.com.agarwal.vinod.govindkigali.play";
    public static final String NOTIFY_NEXT = "com.com.agarwal.vinod.govindkigali.next";

    private MainActivity activity;
    public MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    public NotificationManager mNotificationManager;
    private Notification notification;
    private RemoteViews simpleContentView;
    private RemoteViews expandedView;
    private ArrayList<Song> playlist = new ArrayList<>();
    private Integer value = 0;
    public static Boolean focus = true;
    public Boolean manual = true;
    private Boolean playpause = false;
    public Boolean repeat = false;
    private Boolean fav = true;
    private Integer curVolume;
    private String CHANNEL_ID = "player_goving_ki_gali";
    public Integer NOTIFICATION_ID = 50891387;
    private String client_id = "?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J";
    public static final String TAG = "PS";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference favRef = reference.child("fav");
    DatabaseReference recentRef = reference.child("recents");

    /**
     * Gaining audio focus for player for different cases
     */
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int i) {
                    if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_LOSS_TRANSIENT");
                        playpause = false;
                        manual = false;
                        playPause();
                    } else if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (curVolume * 0.5), 0);
                    } else if (i == AudioManager.AUDIOFOCUS_GAIN) {
                        Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_GAIN");
                        playpause = true;
                        manual = false;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
                        playPause();
                    } else if (i == AudioManager.AUDIOFOCUS_LOSS) {
                        Log.d(TAG, "onAudioFocusChange: AUDIOFOCUS_LOSS");
                        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        playpause = false;
                        manual = false;
                        playPause();
                    }
                }
            };

    // Binder given to clients
    private final IBinder mBinder = new PlayerBinder();

    /**
     * Empty contructor
     */
    public PlayerService() {
    }

    /**
     * Constructor to get Activity Instance
     */
    public void getActivtyContext(MainActivity activity) {
        this.activity = activity;
    }

    /**
     * Updating playlist from MainActivity
     */
    public void updateplayList(ArrayList<Song> playlist) {
        this.playlist = playlist;
    }

//    TODO: Background Check if possible
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * method to download song
     */
    public void downloadSong() {
        File file = new File(activity.getDir("music", MODE_PRIVATE) + "/" + playlist.get(value).getId());
        new DownloadMusic(activity).execute(file.getPath(), playlist.get(value).getStream_url() + client_id);
    }

    /**
     * Player Notification Stop when activity gets killed
     */
    public void stopNotificationPlayer() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            // Return this instance of PlayerService so clients can call public methods
            return PlayerService.this;
        }
    }

    /**
     * Function to create Player : for media playback in service
     */
    public void createPlayer(Integer pos) {
        Log.d(TAG, "createPlayer: inside create Player");
        //Assigning value to pos
        value = pos;

        setProgressVisibilityStart();

        releaseMediaPlayer();

        //Getting Access to audio manager
        audioManager = (AudioManager) activity.getSystemService(AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            //To keep check of audio focus
            focus = true;

            //for recent songs
            recentRef.child(playlist.get(pos).getId()).setValue(playlist.get(pos));

            //for fav songs
            setFav(playlist.get(pos).getId());

            //setting if repeat or not
            setRepeat();

            //Setting Image to player
            loadImage();

            //Setting time to player
            String time = calculateTime(playlist.get(pos).getDuration() / 1000);
            activity.tvEnd.setText(time);
            Log.d(MainActivity.TAG, "preparePlayer: " + time);

            //Assigning title of sing to textview
            activity.tvName.setText(playlist.get(pos).getTitle());
            simpleContentView.setTextViewText(R.id.tv_not_name, playlist.get(pos).getTitle());
            expandedView.setTextViewText(R.id.tv_not_name, playlist.get(pos).getTitle());

            //Setting max value to seekbar
            activity.discreteSeekBar.setMax(playlist.get(pos).getDuration() / 1000);
            activity.pbProgress.setMax(playlist.get(pos).getDuration() / 1000);

            //Initializing media player object
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.reset();
            try {
                //Providing data source to media player
                provideDataSource(mediaPlayer, pos);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //when preparing completed
            mediaPlayer.setOnPreparedListener(this);

            //Set Handler to shoe progress on UI thread
            setProgressOnUI();

            //called when song playback completed
            mediaPlayer.setOnCompletionListener(this);
        }
    }

    private void setProgressOnUI() {
        final Handler mHandler = new Handler();

        //update Seekbar on UI thread
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int num = mediaPlayer.getCurrentPosition() / 1000;
                    activity.discreteSeekBar.setProgress(num);
                    activity.pbProgress.setProgress(num);

                    String time = calculateTime(num);
                    activity.tvStart.setText(time);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private String calculateTime(Integer num) {
        int hh = num / 3600;
        int mm = num / 60 - (hh * 60);
        Log.d(TAG, "preparePlayer: " + hh + " ==== " + mm);
        int ss = num - (hh * 3600) - (mm * 60);
        String HH, MM, SS;
        if (hh >= 10) {
            HH = "" + hh;
        } else {
            HH = "0" + hh;
        }
        if (mm >= 10) {
            MM = "" + mm;
        } else {
            MM = "0" + mm;
        }
        if (ss >= 10) {
            SS = "" + ss;
        } else {
            SS = "0" + ss;
        }
        String time;
        if (hh != 0) {
            time = HH + ":" + MM + ":" + SS;
        } else {
            time = MM + ":" + SS;
        }
        return time;
    }

    /**
     * Method to set progress visibility at first launch
     */
    private void setProgressVisibilityStart() {
        //setting progressbar visibility
        if (activity.slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED
                || activity.slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
            activity.pbLoading.setVisibility(View.VISIBLE);
            activity.ivPlayPause.setVisibility(GONE);
        }
    }

    private void setVisibilityWhenPrepared() {
        //Changing visibility
        //as player loaded
        if (activity.slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            activity.pbLoading.setVisibility(View.GONE);
            activity.ivPlayPause.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method to provide data source to mediaplayer
     */
    public void provideDataSource(MediaPlayer mediaPlayer, Integer pos) throws IOException {
        mediaPlayer.setDataSource(playlist.get(pos).getStream_url() + client_id);
        mediaPlayer.prepareAsync();
    }

    /**
     * Method to release all media resources before playing another song
     */
    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            Log.d(TAG, "releaseMediaPlayer: ----------------------------------------------");

            //Before playing new song  we have to release the player
            mediaPlayer.release();

            //And set player to null
            mediaPlayer = null;

            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(MainActivity.TAG, "onPrepared: prepared listener of service called");

        setVisibilityWhenPrepared();

        //Starting notification for foreground
        generateNotification();

        //Downloading Image
        new ImageLoader(activity);

        //starting media player when ready
        mp.start();
        activity.ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
        activity.ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
        simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
        expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //checking Internet connection
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            if (!repeat) {
                if (value + 1 < playlist.size()) {
                    value = value + 1;
                    createPlayer(value);
                } else {
                    value = 0;
                    createPlayer(value);
                }
                Log.d(TAG, "onClick: OnCreateView:" + value);
            } else {
                mediaPlayer.pause();
                mediaPlayer.start();
            }
        } else {
            if (repeat) {
                mediaPlayer.pause();
                mediaPlayer.start();
            } else {
                playpause = true;
                mediaPlayer.pause();
                activity.ivPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                activity.ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
            }
            Toast.makeText(activity, "Internet not available!!!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Playing pausing audio playback
     */
    public void playPause() {
        Log.d(TAG, "playPause: " + playpause);
        if (mediaPlayer != null) {
            if (playpause) {
                playpause = false;
                activity.ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                activity.ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
                if (manual) {
                    int res = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                    if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        Log.d(TAG, "playPause: granted again :)" + audioFocusChangeListener);
                        mediaPlayer.start();
                    }
                } else {
                    mediaPlayer.start();
                }
            } else {
                playpause = true;
                activity.ivPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                activity.ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
                if (manual) {
                    audioManager.abandonAudioFocus(audioFocusChangeListener);
                    Log.d(TAG, "playPause: abondoned :)" + audioFocusChangeListener);
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.pause();
                }
            }
        }
    }

    /**
     * playing next audio
     */
    public void playNext() {
        if (mediaPlayer != null) {
            if (mNotificationManager != null) {
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            }
            //Checking internet connection
            ConnectivityManager cm =
                    (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                audioManager.abandonAudioFocus(audioFocusChangeListener);

                if (value + 1 < playlist.size()) {
                    value = value + 1;
                    createPlayer(value);
                } else {
                    value = 0;
                    createPlayer(value);
                }
                Log.d(TAG, "onClick: " + value);
            } else {
                Toast.makeText(activity, "Internet not available!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * playing previous audio
     */
    public void playPrevious() {
        if (mediaPlayer != null) {
            if (mNotificationManager != null) {
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            }
            //Checking internet connection
            ConnectivityManager cm =
                    (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
//                downloadMusic.cancel(true);
                audioManager.abandonAudioFocus(audioFocusChangeListener);
                if (value - 1 > 0) {
                    value = value - 1;
                    createPlayer(value);
                } else {
                    value = playlist.size() - 1;
                    createPlayer(value);
                }
                Log.d(TAG, "onClick: " + value);
            } else {
                Toast.makeText(activity, "Internet not available!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Updating progress with seekbar
     * @param i : current progress
     * @param b : user change
     */
    public void progressChanged(Integer i, Boolean b) {
        if (mediaPlayer != null && b) {
            mediaPlayer.seekTo(i * 1000);
        }
    }

    /**
     * Updating repeat method
     */
    public void updatingRepeat() {
        if (repeat) {
            repeat = false;
            activity.ivRepeat.setImageResource(R.drawable.ic_repeat_white_24dp);
        } else {
            repeat = true;
            activity.ivRepeat.setImageResource(R.drawable.ic_repeat_one_white_24dp);
        }
    }

    void setFav(final String id) {
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists()) {
                    activity.ivFav.setImageResource(R.drawable.ic_favorite_white_24dp);
                    fav = true;
                } else {
                    fav = false;
                    activity.ivFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getDetails());
            }
        });
    }

    /**
     * method changing favourites in setOnClickListener()
     */
    public void changeFavourite() {
        //Checking internet connection
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            if (!fav) {
                fav = true;
                favRef.child(playlist.get(value).getId()).setValue(playlist.get(value));
                activity.ivFav.setImageResource(R.drawable.ic_favorite_white_24dp);
            } else {
                fav = false;
                favRef.child(playlist.get(value).getId()).removeValue();
                activity.ivFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            }
        } else {
            Toast.makeText(activity, "Internet not available!!!", Toast.LENGTH_SHORT).show();
        }
    }

    void setRepeat() {
        if (!repeat) {
            activity.ivRepeat.setImageResource(R.drawable.ic_repeat_white_24dp);
        } else {
            activity.ivRepeat.setImageResource(R.drawable.ic_repeat_one_white_24dp);
        }
    }

    /**
     * Method for generating notification
     */
    void generateNotification() {
        notification = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText("Song Name")
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        //Set Listeners
        setListeners(simpleContentView);
        setListeners(expandedView);

        notification.contentView = simpleContentView;
        if (currentVersionSupportBigNotification()) {
            notification.bigContentView = expandedView;
        }

        mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        assert mNotificationManager != null;
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        return false;
    }

    public void setListeners(RemoteViews view) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent close = new Intent(NOTIFY_CLOSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);
        Log.d(TAG, "setListeners: entered in listener");

        PendingIntent pPrevious = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pClose = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, close, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnClose, pClose);

        PendingIntent pNext = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);
    }

    void loadImage() {
        activity.adapter.updateImage(playlist);
        activity.recyclerView.scrollToPosition(value);
    }

    /**
     * Method to cancel notification
     */
    public void cancelNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    /**
     * Method to upload notification image
     * @param bitmap bitmap object of image downloaded
     */
    public void imageUpload(Bitmap bitmap) {
        simpleContentView.setImageViewBitmap(R.id.iv_not_image, bitmap);
        expandedView.setImageViewBitmap(R.id.iv_not_image, bitmap);
    }

    public void setViews(){
        simpleContentView = new RemoteViews(activity.getPackageName(), R.layout.layout_not_sm_player);
        expandedView = new RemoteViews(activity.getPackageName(), R.layout.layout_not_player);
    }

    //
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
}
