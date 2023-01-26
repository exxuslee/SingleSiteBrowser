package com.exzi.android

import androidx.navigation.NavDirections
import kotlin.String

public class BrowserFragmentDirections private constructor() {
  public companion object {
    public fun actionGlobalHome(activeSessionId: String?): NavDirections =
        NavGraphDirections.actionGlobalHome(activeSessionId)

    public fun actionGlobalBrowser(activeSessionId: String?): NavDirections =
        NavGraphDirections.actionGlobalBrowser(activeSessionId)
  }
}
