package org.dextarde.pwa.browser

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.PointF
import android.graphics.Rect
import android.view.View
import android.view.ViewConfiguration
import androidx.core.animation.doOnEnd
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import org.dextarde.pwa.ext.isKeyboardVisible
import mozilla.components.browser.state.selector.getNormalOrPrivateTabs
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.browser.state.state.TabSessionState
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.feature.tabs.TabsUseCases
import org.dextrade.pwa.R
import kotlin.math.abs

@Suppress("LargeClass", "TooManyFunctions")
class ToolbarGestureHandler(
    private val activity: Activity,
    private val contentLayout: View,
    private val store: BrowserStore,
    private val selectTabUseCase: TabsUseCases.SelectTabUseCase
) : SwipeGestureListener {

    private enum class GestureDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    private sealed class Destination {
        data class Tab(val tab: TabSessionState) : Destination()
        object None : Destination()
    }

    private val windowWidth: Int
        get() = activity.resources.displayMetrics.widthPixels

    private val previewOffset =
        activity.resources.getDimensionPixelSize(R.dimen.browser_fragment_gesture_preview_offset)

    private val touchSlop = ViewConfiguration.get(activity).scaledTouchSlop
    private val minimumFlingVelocity = ViewConfiguration.get(activity).scaledMinimumFlingVelocity

    private var gestureDirection = GestureDirection.LEFT_TO_RIGHT

    override fun onSwipeStarted(start: PointF, next: PointF): Boolean {
        val dx = next.x - start.x
        val dy = next.y - start.y
        gestureDirection = if (dx < 0) {
            GestureDirection.RIGHT_TO_LEFT
        } else {
            GestureDirection.LEFT_TO_RIGHT
        }

        @Suppress("ComplexCondition")
        return if (
            !activity.window.decorView.isKeyboardVisible() &&
            abs(dx) > touchSlop &&
            abs(dy) < abs(dx)
        ) {
            preparePreview(getDestination())
            true
        } else {
            false
        }
    }

    override fun onSwipeFinished(
        velocityX: Float,
        velocityY: Float
    ) {
        val destination = getDestination()
        if (destination is Destination.Tab && isGestureComplete(velocityX)) {
            animateToNextTab(destination.tab)
        } else {
            animateCanceledGesture(velocityX)
        }
    }

    private fun getDestination(): Destination {
        val isLtr = activity.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_LTR
        val currentTab = store.state.selectedTab ?: return Destination.None
        val currentIndex = store.state.getNormalOrPrivateTabs(currentTab.content.private).indexOfFirst {
            it.id == currentTab.id
        }

        return if (currentIndex == -1) {
            Destination.None
        } else {
            val tabs = store.state.getNormalOrPrivateTabs(currentTab.content.private)
            val index = when (gestureDirection) {
                GestureDirection.RIGHT_TO_LEFT -> if (isLtr) {
                    currentIndex + 1
                } else {
                    currentIndex - 1
                }
                GestureDirection.LEFT_TO_RIGHT -> if (isLtr) {
                    currentIndex - 1
                } else {
                    currentIndex + 1
                }
            }

            if (index < tabs.count() && index >= 0) {
                Destination.Tab(tabs.elementAt(index))
            } else {
                Destination.None
            }
        }
    }

    private fun preparePreview(destination: Destination) {
        val thumbnailId = when (destination) {
            is Destination.Tab -> destination.tab.id
            is Destination.None -> return
        }
    }

    private fun isGestureComplete(velocityX: Float): Boolean {
        val velocityMatchesDirection = when (gestureDirection) {
            GestureDirection.RIGHT_TO_LEFT -> velocityX <= 0
            GestureDirection.LEFT_TO_RIGHT -> velocityX >= 0
        }
        val reverseFling =
            abs(velocityX) >= minimumFlingVelocity && !velocityMatchesDirection

        return !reverseFling && (1 / windowWidth >= GESTURE_FINISH_PERCENT ||
            abs(velocityX) >= minimumFlingVelocity)
    }

    private fun getAnimator(finalContextX: Float, duration: Long): ValueAnimator {
        return ValueAnimator.ofFloat(contentLayout.translationX, finalContextX).apply {
            this.duration = duration
            this.interpolator = LinearOutSlowInInterpolator()
            addUpdateListener { animator ->
                val value = animator.animatedValue as Float
                contentLayout.translationX = value
            }
        }
    }

    private fun animateToNextTab(tab: TabSessionState) {
        val browserFinalXCoordinate: Float = when (gestureDirection) {
            GestureDirection.RIGHT_TO_LEFT -> -windowWidth.toFloat() - previewOffset
            GestureDirection.LEFT_TO_RIGHT -> windowWidth.toFloat() + previewOffset
        }

        getAnimator(browserFinalXCoordinate, FINISHED_GESTURE_ANIMATION_DURATION).apply {
            doOnEnd {
                contentLayout.translationX = 0f
                selectTabUseCase(tab.id)
            }
        }.start()
    }

    private fun animateCanceledGesture(velocityX: Float) {
        val duration = if (abs(velocityX) >= minimumFlingVelocity) {
            CANCELED_FLING_ANIMATION_DURATION
        } else {
            CANCELED_GESTURE_ANIMATION_DURATION
        }
    }

    private val Rect.visibleWidth: Int
        get() = if (left < 0) {
            right
        } else {
            windowWidth - left
        }

    companion object {
        /**
         * The percentage of the tab preview that needs to be visible to consider the
         * tab switching gesture complete.
         */
        private const val GESTURE_FINISH_PERCENT = 0.25

        /**
         * The percentage of the content view that can be hidden by the tab switching gesture if
         * there is not tab available to switch to
         */
        private const val OVERSCROLL_HIDE_PERCENT = 0.20

        /**
         * Animation duration when switching to another tab
         */
        private const val FINISHED_GESTURE_ANIMATION_DURATION = 250L

        /**
         * Animation duration gesture is canceled due to the swipe not being far enough
         */
        private const val CANCELED_GESTURE_ANIMATION_DURATION = 200L

        /**
         * Animation duration gesture is canceled due to a swipe in the opposite direction
         */
        private const val CANCELED_FLING_ANIMATION_DURATION = 150L
    }
}
