package edu.gatech.hackathon.verve;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * TODO: document your custom view class.
 */
public class ItemView extends LinearLayout {

    TextView name;
    TextView days;
    Context context;

    public ItemView(Context context) {
        super(context);
        init(null, 0);
        this.context = context;
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        this.context = context;
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        this.context = context;
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.item_view, this);
        this.name = findViewById(R.id.name);
        this.days = findViewById(R.id.days);
    }

    public void bind(RealmItem item) {
        this.name.setText(item.name);
        this.days.setText(String.format(Locale.ENGLISH, "%d", item.days));
    }
}
