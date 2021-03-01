package com.android.test.testastropay.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper

abstract class BasePresenter protected constructor() {

    @CallSuper
    open fun onCreate(savedInstanceState: Bundle?) {
    }

    @CallSuper
    open fun onResume() {
    }

    @CallSuper
    open fun onPause() {
    }

    @CallSuper
    open fun onSaveInstanceState(outState: Bundle) {
    }

    @CallSuper
    open fun onDestroy() {
    }

    @CallSuper
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    @CallSuper
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
    }
}
