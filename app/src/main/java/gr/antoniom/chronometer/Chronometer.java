package gr.antoniom.chronometer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import java.text.DecimalFormat;

public class Chronometer extends AppCompatTextView {
    @SuppressWarnings("unused")
    private static final String TAG = "Chronometer";
    private static final int TICK_WHAT = 2;

    // How many 0's to put for milliseconds
    private static final int MILLIS_DIGIT_COUNT = 2;

    // How long to wait before update timer text
    private static final int DELAY_MILLIS = 1000 / (int) Math.pow(10, MILLIS_DIGIT_COUNT);

    private long mBase;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private boolean mPreciseClock = true;
    private OnChronometerTickListener mOnChronometerTickListener;
    private long timeElapsed;

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this, TICK_WHAT),
                        DELAY_MILLIS);
            }
        }
    };

    public Chronometer(Context context) {
        this(context, null, 0);
    }

    public Chronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        mBase = SystemClock.elapsedRealtime();
        updateText(mBase);
    }

    public long getBase() {
        return mBase;
    }

    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public void setPrecision(boolean prec) {
        mPreciseClock = prec;
    }

    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }

    public void setOnChronometerTickListener(
            OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    public void start() {
        //mBase = SystemClock.elapsedRealtime();
        mStarted = true;
        updateRunning();
    }

    public void stop() {
        mStarted = false;
        updateRunning();
    }


    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    public String getFormattedText(long timeElapsed) {
        DecimalFormat df = new DecimalFormat("00");

        int hours = (int) (timeElapsed / (3600 * 1000));
        int remaining = (int) (timeElapsed % (3600 * 1000));

        int minutes = remaining / (60 * 1000);
        remaining = remaining % (60 * 1000);

        int seconds = remaining / 1000;
        remaining = remaining % (1000);

        int milliseconds = remaining / DELAY_MILLIS;

        StringBuilder millisFormatPatter = new StringBuilder();
        for (int i = 0; i < MILLIS_DIGIT_COUNT; i++) {
            millisFormatPatter.append("0");
        }
        DecimalFormat millisFormat = new DecimalFormat(millisFormatPatter.toString());

        String text = "";

        if (hours > 0) {
            text += df.format(Math.abs(hours)) + ":";
        }

        text += df.format(Math.abs(minutes)) + ":";
        text += df.format(Math.abs(seconds)) + ":";

        if (mPreciseClock)
            text += millisFormat.format(milliseconds);

        return text;
    }

    private synchronized void updateText(long now) {
        setText(getFormattedText(now - mBase));
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                if (mPreciseClock) {
                    mHandler.sendMessageDelayed(Message.obtain(mHandler,
                            TICK_WHAT), DELAY_MILLIS);
                } else {
                    mHandler.sendMessageDelayed(Message.obtain(mHandler,
                            TICK_WHAT), 1000);
                }
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public interface OnChronometerTickListener {

        void onChronometerTick(Chronometer chronometer);
    }

}
