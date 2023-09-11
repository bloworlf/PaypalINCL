package io.drdroid.paypalincl.data.utils

import androidx.lifecycle.MutableLiveData

class DistinctUntilChangedMutableLiveData<T> : MutableLiveData<T>() {
    override fun setValue(value: T?) {
        if (value != this.value) {
            super.setValue(value)
        }
    }
}