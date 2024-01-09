package com.hoangquocthai.chatapp.event;

import android.view.View;

public interface ItemClickListerner {
    void onClick (View view, int pos, boolean isLongClick);
}
