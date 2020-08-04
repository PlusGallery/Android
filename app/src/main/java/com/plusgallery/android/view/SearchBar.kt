package com.plusgallery.android.view

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import com.plusgallery.android.R
import kotlinx.android.synthetic.main.view_searchbar.view.*

class SearchBar : FrameLayout, View.OnClickListener, Animation.AnimationListener,
    View.OnFocusChangeListener, TextView.OnEditorActionListener {

    interface OnSearchBarListener {
        fun onSearchStateChanged(enabled: Boolean)
        fun onSearchConfirmed(text: CharSequence?)
        fun onNavigationClicked()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var searchOpened: Boolean = false
    private var callback: OnSearchBarListener? = null

    init {
        View.inflate(context, R.layout.view_searchbar, this)
        setOnClickListener(this)
        backArrow.setOnClickListener(this)
        searchIcon.setOnClickListener(this)
        searchEdit.onFocusChangeListener = this
        searchEdit.setOnEditorActionListener(this)
        clearText.setOnClickListener(this)
        navIcon.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            this.id -> openSearch()
            R.id.backArrow -> closeSearch()
            R.id.searchIcon -> openSearch()
            R.id.clearText -> searchEdit.setText("")
            R.id.navIcon -> callback?.onNavigationClicked()
        }
    }

    override fun onAnimationEnd(animation: Animation?) {
        if (!searchOpened) {
            inputContainer.visibility = GONE
            searchEdit.setText("")
        } else {
            searchIcon.visibility = GONE
            searchEdit.requestFocus()
        }
        callback?.onSearchStateChanged(searchOpened)
    }

    override fun onAnimationRepeat(animation: Animation?) {}

    override fun onAnimationStart(animation: Animation?) {}

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (hasFocus) {
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        } else {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        callback?.onSearchConfirmed(searchEdit.text)
        return true
    }

    fun isSearchOpened(): Boolean {
        return searchOpened
    }

    fun closeSearch() {
        searchOpened = false
        navIcon.setImageResource(R.drawable.ic_animate_back)
        backArrow.visibility = View.INVISIBLE
        (navIcon.drawable as Animatable).start()
        val rightOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_right)
        val rightIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_right)
        rightOut.setAnimationListener(this)
        inputContainer.startAnimation(rightOut)
        searchIcon.visibility = View.VISIBLE
        searchIcon.startAnimation(rightIn)
        placeHolder.visibility = VISIBLE
        placeHolder.startAnimation(rightIn)
    }

    fun openSearch() {
        searchOpened = true
        navIcon.setImageResource(R.drawable.ic_animate_filter)
        val animation = navIcon.drawable as AnimatedVectorDrawable
        animation.registerAnimationCallback(object: Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                super.onAnimationEnd(drawable)
                backArrow.visibility = View.VISIBLE
            }
        })
        animation.start()
        val leftIn: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_left)
        val leftOut: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_out_left)
        leftIn.setAnimationListener(this)
        placeHolder.visibility = View.GONE
        inputContainer.visibility = View.VISIBLE
        inputContainer.startAnimation(leftIn)
        searchIcon.startAnimation(leftOut)
    }

    fun setText(str: String) {
        searchEdit.setText(str)
    }

    fun getText(): String {
        return searchEdit.text.toString()
    }

    fun setOnSearchBarListener(c: OnSearchBarListener) {
        callback = c
    }

}