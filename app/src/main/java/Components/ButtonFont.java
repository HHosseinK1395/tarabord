package Components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonFont extends Button {

    public ButtonFont(Context context) {
        super(context);
    }

    public ButtonFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public ButtonFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}