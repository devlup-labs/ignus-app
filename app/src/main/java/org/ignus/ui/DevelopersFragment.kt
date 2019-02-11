package org.ignus.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_developers.*
import org.ignus.R
import org.ignus.db.models.Developer
import org.ignus.ui.adapter.DeveloperAdapter
import java.io.BufferedReader
import java.io.InputStreamReader


class DevelopersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_developers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDevelopers()
    }

    private fun getDevelopers() {
        val raw = resources.openRawResource(R.raw.developers)
        val rd = BufferedReader(InputStreamReader(raw))
        val objectType = object : TypeToken<List<Developer>>() {}.type
        val list = Gson().fromJson<List<Developer>>(rd, objectType)

        setUpRecyclerView(list)
    }

    private fun setUpRecyclerView(list: List<Developer>) {
        developersRecyclerView.adapter = DeveloperAdapter(list)
        developersRecyclerView.layoutManager = GridLayoutManager(context, 1)
    }

}
