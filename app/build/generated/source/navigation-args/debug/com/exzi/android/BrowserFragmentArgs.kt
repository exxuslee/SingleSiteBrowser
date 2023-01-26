package com.exzi.android

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class BrowserFragmentArgs(
  public val activeSessionId: String?
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("activeSessionId", this.activeSessionId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("activeSessionId", this.activeSessionId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): BrowserFragmentArgs {
      bundle.setClassLoader(BrowserFragmentArgs::class.java.classLoader)
      val __activeSessionId : String?
      if (bundle.containsKey("activeSessionId")) {
        __activeSessionId = bundle.getString("activeSessionId")
      } else {
        throw IllegalArgumentException("Required argument \"activeSessionId\" is missing and does not have an android:defaultValue")
      }
      return BrowserFragmentArgs(__activeSessionId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): BrowserFragmentArgs {
      val __activeSessionId : String?
      if (savedStateHandle.contains("activeSessionId")) {
        __activeSessionId = savedStateHandle["activeSessionId"]
      } else {
        throw IllegalArgumentException("Required argument \"activeSessionId\" is missing and does not have an android:defaultValue")
      }
      return BrowserFragmentArgs(__activeSessionId)
    }
  }
}
