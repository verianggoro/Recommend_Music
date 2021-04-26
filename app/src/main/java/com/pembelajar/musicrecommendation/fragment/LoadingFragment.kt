package com.pembelajar.musicrecommendation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.pembelajar.musicrecommendation.R
import com.pembelajar.musicrecommendation.databinding.FragmentLoadingBinding

class LoadingFragment: DialogFragment() {
    private lateinit var binding: FragmentLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loading, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.shape_loading_fragment)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                Toast.makeText(context,"Please Wait", Toast.LENGTH_SHORT).show()
            }
        }
    }

}