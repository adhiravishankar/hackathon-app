package edu.gatech.hackathon.verve;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class CharityView extends LinearLayout {

    TextView name;
    TextView days;

    public CharityView(Context context) {
        super(context);
        init(null, 0);
    }

    public CharityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CharityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.item_view, this);
        this.name = findViewById(R.id.name);
        this.days = findViewById(R.id.days);
    }

    public void bind(Charity item) {
        this.name.setText(item.organization);
    }
}
