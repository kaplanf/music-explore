package com.kaplan.musicexplore.binding

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kaplan.musicexplore.R
import com.kaplan.musicexplore.util.RecyclerViewScrollCallback
import com.kaplan.musicexplore.util.then
import com.kaplan.musicexplore.util.ui.toNegative


@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: FloatingActionButton, isGone: Boolean?) {
    if (isGone == null || isGone) {
        view.hide()
    } else {
        view.show()
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade()).listener(object :
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (view.tag == "reverse")
                        resource?.toNegative()
                    else {
                    }
                    return false
                }
            })
            .into(view)
    }
}

interface BindableAdapter<T> {
    fun setData(data: T)
    fun updateData(data: T)
}

@BindingAdapter("update")
fun <T> recyclerviewDiff(recyclerView: RecyclerView, data: T) {
    if (recyclerView.adapter is BindableAdapter<*>) {
        if (data != null)
            (recyclerView.adapter as BindableAdapter<T>).updateData(data)
    }
}

@BindingAdapter("textOrNot")
fun bindText(view: TextView, textValue: String?) {
    var text = textValue.isNullOrEmpty().not() then textValue.toString() ?: "No Information"
    text = view.tag.toString() + ": " + text
    view.text = text
}


@BindingAdapter("renderHtml")
fun bindRenderHtml(view: TextView, description: String?) {
    if (description != null) {
        view.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
        view.movementMethod = LinkMovementMethod.getInstance()
    } else {
        view.text = ""
    }
}

@BindingAdapter("selectDrawable")
fun bindDrawable(view: ImageView, isLiked: Boolean) {
    val resource = isLiked then R.drawable.ic_like_noel ?: R.drawable.ic_like
    view.setImageResource(resource)
}

@BindingAdapter("trackTime")
fun bindMilliSeconds(view: TextView, milliseconds: Long) {
    val minutes = milliseconds / 1000 / 60
    val seconds = milliseconds / 1000 % 60
    view.text = "$minutes minutes $seconds seconds"
}

@BindingAdapter(
    value = ["visibleThreshold", "resetLoadingState", "onScrolledToBottom"],
    requireAll = false
)
fun setRecyclerViewScrollCallback(
    recyclerView: RecyclerView, visibleThreshold: Int,
    resetLoadingState: Boolean,
    onScrolledListener: RecyclerViewScrollCallback.OnScrolledListener
) {
    val callback = recyclerView.layoutManager?.let {
        RecyclerViewScrollCallback
            .Builder(it)
            .visibleThreshold(visibleThreshold)
            .onScrolledListener(onScrolledListener)
            .resetLoadingState(resetLoadingState)
            .build()
    }

    recyclerView.clearOnScrollListeners()
    callback?.let { recyclerView.addOnScrollListener(it) }
}

@BindingAdapter("animatedVisibility")
fun setVisibility(
    view: View,
    visibility: Int
) {
    val endAnimVisibility = view.getTag(view.hashCode())
    val oldVisibility = endAnimVisibility ?: view.visibility
    if (oldVisibility == visibility) {
        return
    }
    val isVisibile = oldVisibility == View.VISIBLE
    val willBeVisible = visibility == View.VISIBLE
    view.visibility = View.VISIBLE
    var startAlpha = if (isVisibile) 1f else 0f
    if (endAnimVisibility != null) {
        startAlpha = view.alpha
    }
    val endAlpha = if (willBeVisible) 1f else 0f

    val alpha = ObjectAnimator.ofFloat(
        view,
        View.ALPHA, startAlpha, endAlpha
    )
    alpha.setAutoCancel(true)
    alpha.addListener(object : AnimatorListenerAdapter() {
        private var isCanceled = false
        override fun onAnimationStart(anim: Animator?) {
            view.setTag(view.hashCode(), visibility)
        }

        override fun onAnimationCancel(anim: Animator?) {
            isCanceled = true
        }

        override fun onAnimationEnd(anim: Animator?) {
            view.setTag(view.hashCode(), null)
            if (!isCanceled) {
                view.alpha = 1f
                view.visibility = visibility
            }
        }
    })
    alpha.start()
}

