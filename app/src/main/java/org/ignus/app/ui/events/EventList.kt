package org.ignus.app.ui.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.ignus.app.R

class EventList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
    }
}
