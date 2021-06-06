package com.example.poplibraries_lesson4_hw.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.poplibraries_lesson4_hw.R
import com.example.poplibraries_lesson4_hw.databinding.ActivityMainBinding
import com.example.poplibraries_lesson4_hw.mvp.presenter.MainPresenter
import com.example.poplibraries_lesson4_hw.mvp.view.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {
    private val RESULT_CODE_FILECHOOSER = 2000
    var dialog: Dialog? = null
    val presenter by moxyPresenter {
        MainPresenter(
            Converter(this),
            AndroidSchedulers.mainThread()
        )
    }

    var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.convert.setOnClickListener { presenter.startConvertion() }

    }

    override fun startConvertion() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/jpg"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        val chooser = Intent.createChooser(intent, getString(R.string.chooser_text))
        startActivityForResult(chooser, RESULT_CODE_FILECHOOSER)
    }

    override fun showDialogue() {
        dialog = AlertDialog.Builder(this).setTitle("Converting...")
            .setNegativeButton("Dismiss") { dialog, wich ->
                presenter.dismiss()
            }.create()
        dialog?.show()
    }

    override fun hideDialogue() {
        dialog?.dismiss()
    }

    override fun showResult() {
        Toast.makeText(this, "Complete!", Toast.LENGTH_LONG).show()
    }

    override fun showError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()

    }

    override fun showInterrupted() {
        Toast.makeText(this, "Interrupted!", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_CODE_FILECHOOSER) {
            if (resultCode == RESULT_OK) {
                data?.data?.let {
                    presenter.convertImage(it.toString())
                }

            }
        }

    }
}