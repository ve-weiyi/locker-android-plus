package com.ve.lib.common.widget.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceViewHolder
import com.ve.lib.common.R
import com.ve.lib.common.databinding.ItemIconListpreferenceBinding
import com.ve.lib.common.databinding.ItemIconListpreferencePreviewBinding


/**
 * Created by chenxz on 2018/6/13.
 */
class IconListPreference(context: Context, attrs: AttributeSet) : ListPreference(context, attrs),
    DialogInterface.OnClickListener {

    private val drawableList = ArrayList<Drawable>()

    init {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.IconListPreference, 0, 0)
        val drawables: Array<CharSequence>
        try {
            drawables = ta.getTextArray(R.styleable.IconListPreference_iconsDrawables)
        } finally {
            ta.recycle()
        }
        for (drawable in drawables) {
            val resId =
                context.resources.getIdentifier(drawable.toString(), "mipmap", context.packageName)
            val d = context.resources.getDrawable(resId)
            drawableList.add(d)
        }

        widgetLayoutResource = R.layout.item_icon_listpreference_preview
    }

    private fun createListAdapter(): ListAdapter {
        val selectedValue = value
        val selectedIndex = findIndexOfValue(selectedValue)
        return IconArrayAdapter(
            context, R.layout.item_icon_listpreference,
            entries, drawableList, selectedIndex
        )
    }

    private lateinit var mBinIconPreviewBinding: ItemIconListpreferencePreviewBinding
    private lateinit var mBinIconBinding: ItemIconListpreferenceBinding
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val selectedValue = value
        val selectedIndex = findIndexOfValue(selectedValue)

        val drawable = drawableList[selectedIndex]
        holder.run {
            mBinIconPreviewBinding.ivPreview.setImageDrawable(drawable)
        }

    }

    override fun onClick() {
//        mEntries = entries
//        mEntryValues = entryValues
//        mSummary = summary.toString()
//        mValue = value
//        mClickedDialogEntryIndex = findIndexOfValue(mValue)
        val mDialog = AlertDialog.Builder(context)
            .setAdapter(createListAdapter(), this)
            .setNegativeButton(
                "取消"
            ) { dialog, which -> dialog.dismiss() }
            .create()

        mDialog.show()
    }


    private inner class IconArrayAdapter(
        context: Context, textViewResourceId: Int,
        objects: Array<CharSequence>, imageDrawables: List<Drawable>,
        selectedIndex: Int
    ) : ArrayAdapter<CharSequence>(context, textViewResourceId, objects) {

        private var list: List<Drawable>? = null
        private var selectedIndex = 0

        init {
            this.selectedIndex = selectedIndex
            this.list = imageDrawables
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val inflater = (context as AppCompatActivity).layoutInflater

            val view = inflater.inflate(R.layout.item_icon_listpreference, parent, false)

            view.run {
                mBinIconBinding.label.text = getItem(position)
                mBinIconBinding.label.isChecked = position == selectedIndex

                mBinIconBinding.icon.setImageDrawable(list!![position])
            }
            return view
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

    }
}