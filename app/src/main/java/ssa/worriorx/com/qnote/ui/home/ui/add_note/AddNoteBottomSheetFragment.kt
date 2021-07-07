package ssa.worriorx.com.qnote.ui.home.ui.add_note

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ssa.worriorx.com.qnote.R

class AddNoteBottomSheetFragment:BottomSheetDialogFragment() {

    private var clickcamera: LinearLayout? = null
    private var clickGallery: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =  inflater.inflate(R.layout.add_notes_bottom_sheet, container, false)
        clickcamera = view.findViewById(R.id.add_note_bottom_sheet_click_camera)
        clickGallery = view.findViewById(R.id.add_note_bottom_sheet_click_galley)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        // We can have cross button on the top right corner for providing elemnet to dismiss the bottom sheet
        //iv_close.setOnClickListener { dismissAllowingStateLoss() }
        clickcamera?.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("Camera")
        }

        clickGallery?.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("Gallery")
        }
    }

    private var mListener: ItemClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            mListener = context as ItemClickListener
        } else {
            throw RuntimeException(
                context.toString()
                    .toString() + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    interface ItemClickListener {
        fun onItemClick(item: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): AddNoteBottomSheetFragment {
            val fragment = AddNoteBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}