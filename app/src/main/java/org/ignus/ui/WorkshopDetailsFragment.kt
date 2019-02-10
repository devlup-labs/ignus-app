package org.ignus.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.ignus.R

class WorkshopDetailsFragment : BottomSheetDialogFragment() {

    companion object {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.event_categories_card_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val bottomSheetNavFragment = WorkshopsFragment()
//        childFragmentManager.beginTransaction()
        Log.d("suthar", "onViewCrated")

    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return object : BottomSheetDialog(activity!!, theme) {
//            override fun onBackPressed() {
//                val fragment = childFragmentManager.fragments[0]
//                val navigateUp = findNavController(fragment.view!!).navigateUp()
//                if (!navigateUp) {
//                    dismiss()
//                }
//            }
//        }
//    }

}