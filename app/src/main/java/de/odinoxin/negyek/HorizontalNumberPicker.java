package de.odinoxin.negyek;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HorizontalNumberPicker extends LinearLayout
{
    Button btn_prev;
    TextView tv_prevNum;
    EditText et_value;
    TextView tv_nextNum;
    Button btn_next;

    int min = 0;
    int value = 0;
    int max = 0;

    String latestValue;

    NumberChangedListener listener;

    float moveFromX;

    public HorizontalNumberPicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_horizontalnumberpicker, this, true);

        if(this.isInEditMode())
            return;

        this.btn_prev = ((Button) this.findViewById(R.id.btn_prev));
        this.tv_prevNum = ((TextView) this.findViewById(R.id.prev_num));
        this.et_value = ((EditText) this.findViewById(R.id.value));
        this.tv_nextNum = ((TextView) this.findViewById(R.id.next_num));
        this.btn_next = ((Button) this.findViewById(R.id.btn_next));

        this.btn_prev.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HorizontalNumberPicker.this.onPrev();
            }
        });
        this.et_value.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                HorizontalNumberPicker me = HorizontalNumberPicker.this;
                try
                {
                    me.latestValue = editable.toString();

                    if (editable.toString().isEmpty())
                        return;

                    int newValue = Integer.parseInt(editable.toString());

                    if (me.value != newValue)
                    {
                        if (newValue < me.min
                        || newValue > me.max)
                            me.et_value.setError(me.getResources().getString(R.string.err_invalid_value));
                        else
                        {
                            me.value = newValue;
                            me.updateControls();
                            me.et_value.setSelection(me.et_value.getText().length());
                            if (me.listener != null)
                                me.listener.onNumberChanged(me.value);
                        }
                    }
                }
                catch (NumberFormatException ex)
                {
                    me.et_value.setText(String.format("%s", me.value));
                    me.et_value.setSelection(me.et_value.getText().length());
                }
            }
        });
        this.btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HorizontalNumberPicker.this.onNext();
            }
        });

//        this.setOnTouchListener(new OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                Log.e("Negyek", "hnp: " + MotionEvent.actionToString(event.getActionMasked()));
//                HorizontalNumberPicker me = HorizontalNumberPicker.this;
//
//                switch (event.getActionMasked())
//                {
//                    case MotionEvent.ACTION_MOVE:
//                        Log.e("Negyek", "Action_Move");
//                        if(me.moveFromX + 10 < event.getX())
//                            me.onPrev();
//                        else if(me.moveFromX - 10 > event.getX())
//                            me.onNext();
//                        else
//                            return true;
////                    case MotionEvent.ACTION_DOWN:
////                        Log.e("Negyek", "Action_Down on hnp");
////                        me.moveFromX = event.getX();
////                        return true;
////                    default:
////                        Log.e("Negyek", "default: " + MotionEvent.actionToString(event.getActionMasked()));
////                        return v.performClick();
//                }
//                return true;
//            }
//        });

        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalNumberPicker);
        if(arr != null && !isInEditMode())
        {
            this.min = arr.getInt(R.styleable.HorizontalNumberPicker_hnp_min, 0);
            this.value = arr.getInt(R.styleable.HorizontalNumberPicker_hnp_value, 0);
            this.max = arr.getInt(R.styleable.HorizontalNumberPicker_hnp_max, 0);

            if(min > max)
                throw new IllegalArgumentException("Minimal value cannot be larger than maximal value!");

            if(this.btn_prev != null)
            {
                if(arr.getString(R.styleable.HorizontalNumberPicker_hnp_prev) != null)
                    this.btn_prev.setText(arr.getString(R.styleable.HorizontalNumberPicker_hnp_prev));
                if(arr.getString(R.styleable.HorizontalNumberPicker_hnp_next) != null)
                    this.btn_next.setText(arr.getString(R.styleable.HorizontalNumberPicker_hnp_next));

                this.btn_prev.setTextSize(arr.getDimensionPixelSize(R.styleable.HorizontalNumberPicker_hnp_textsize, 12));
                this.btn_next.setTextSize(arr.getDimensionPixelSize(R.styleable.HorizontalNumberPicker_hnp_textsize, 12));
            }

            this.updateControls();

            arr.recycle();
        }
    }

    public void onPrev()
    {
        if(this.value - 1 < min)
            return;

        this.value--;
        this.updateControls();

        if(this.listener != null)
            this.listener.onNumberChanged(this.value);
    }

    public void onNext()
    {
        if(this.value + 1 > max)
            return;

        this.value++;
        this.updateControls();

        if(this.listener != null)
            this.listener.onNumberChanged(this.value);
    }

    void updateControls()
    {
        this.et_value.setError(null);

        this.tv_prevNum.setText(String.format("%s", this.value - 1 < this.min ? "" : this.value - 1));
        this.et_value.setText(String.format("%s", this.value));
        this.tv_nextNum.setText(String.format("%s", this.value + 1 > this.max ? "" : this.value + 1));

        if(this.value + 1 > this.max)
            this.tv_nextNum.setMinWidth(this.tv_prevNum.getWidth());
        else
            this.tv_prevNum.setMinWidth(this.tv_nextNum.getWidth());

        this.btn_prev.setEnabled(this.value > min);
        this.btn_next.setEnabled(this.value < max);
    }

    public void setOnNumberChangedListener(NumberChangedListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, et_value.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if(state instanceof SavedState)
        {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            et_value.setText(ss.data);
        }
        else
            super.onRestoreInstanceState(state);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container)
    {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container)
    {
        super.dispatchThawSelfOnly(container);
    }

    public interface NumberChangedListener
    {
        public abstract void onNumberChanged(int value);
    }

    private class SavedState extends BaseSavedState
    {
        private String data;

        public SavedState(Parcel source)
        {
            super(source);
            data = source.readString();
        }

        public SavedState(Parcelable superState, String data)
        {
            super(superState);
            this.data = data;
        }

        @Override
        public void writeToParcel(Parcel out, int flags)
        {
            out.writeString(data);
        }

        public final Creator<SavedState> CREATOR = new Creator<SavedState>()
        {
            @Override
            public SavedState createFromParcel(Parcel in)
            {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size)
            {
                return new SavedState[size];
            }
        };
    }
}

