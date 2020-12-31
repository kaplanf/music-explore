package com.kaplan.musicexplore.util

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData

infix fun <T : Any> Boolean.then(param: T): T? = if (this) param else null

infix fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

inline fun <A, B, R> ifNotNull(a: A?, b: B?, code: (A, B) -> R) {
    if (a != null && b != null) {
        code(a, b)
    }
}

infix fun <T: Observable> T.addOnPropertyChanged(callback: (T) -> Unit) =
    object: Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable?, i: Int) =
            callback(observable as T)
    }.also { addOnPropertyChangedCallback(it) }

infix fun <T>Boolean.thenDo(action : () -> T): T? {
    return if (this)
        action.invoke()
    else null
}

infix fun <T>T?.elseDo(action: () -> T): T {
    return if (this == null)
        action.invoke()
    else this
}

fun doNothing(): Unit {}