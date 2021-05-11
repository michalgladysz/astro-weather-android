package com.example.astro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(
            R.layout.main_fragment,
            container, false
        )

      //  populateSpinner(view)
        return view
    }
//    fun populateSpinner(view : View) {
//        val spinner = view.findViewById(R.id.spinner) as Spinner
//// Create an ArrayAdapter using the string array and a default spinner layout
//        this.context?.let {
//            ArrayAdapter.createFromResource(
//                it,
//                R.array.refresh_array,
//                android.R.layout.simple_spinner_item
//            ).also { adapter ->
//                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                // Apply the adapter to the spinner
//                spinner.adapter = adapter
//            }
//        }
//    }
}