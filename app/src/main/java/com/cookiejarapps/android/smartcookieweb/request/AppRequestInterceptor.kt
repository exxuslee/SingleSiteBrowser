package com.cookiejarapps.android.smartcookieweb.request

import android.content.Context
import com.cookiejarapps.android.smartcookieweb.ext.components
import mozilla.components.browser.errorpages.ErrorPages
import mozilla.components.browser.errorpages.ErrorType
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.request.RequestInterceptor
import mozilla.components.concept.engine.request.RequestInterceptor.InterceptionResponse

class AppRequestInterceptor(private val context: Context) : RequestInterceptor {

    override fun onLoadRequest(
        engineSession: EngineSession,
        uri: String,
        lastUri: String?,
        hasUserGesture: Boolean,
        isSameDomain: Boolean,
        isRedirect: Boolean,
        isDirectNavigation: Boolean,
        isSubframeRequest: Boolean,
    ): InterceptionResponse? = context.components.appLinksInterceptor.onLoadRequest(
        engineSession, uri, lastUri, hasUserGesture, isSameDomain, isRedirect,
        isDirectNavigation, isSubframeRequest,
    )

    override fun onErrorRequest(
        session: EngineSession,
        errorType: ErrorType,
        uri: String?,
    ): RequestInterceptor.ErrorResponse {
        val errorPageUri = ErrorPages.createUrlEncodedErrorPage(
            context = context,
            errorType = errorType,
            uri = uri,
            htmlResource = getErrorCategory(errorType).htmlRes,
        )
        return RequestInterceptor.ErrorResponse(errorPageUri)
    }

    private fun getErrorCategory(errorType: ErrorType): ErrorCategory = when (errorType) {
        ErrorType.ERROR_SECURITY_BAD_CERT,
        ErrorType.ERROR_SECURITY_SSL,
        ErrorType.ERROR_BAD_HSTS_CERT,
        ErrorType.ERROR_PORT_BLOCKED -> ErrorCategory.SSL

        ErrorType.ERROR_SAFEBROWSING_HARMFUL_URI,
        ErrorType.ERROR_SAFEBROWSING_PHISHING_URI,
        ErrorType.ERROR_SAFEBROWSING_MALWARE_URI,
        ErrorType.ERROR_SAFEBROWSING_UNWANTED_URI -> ErrorCategory.Malware

        else -> ErrorCategory.Network
    }

    private enum class ErrorCategory(val htmlRes: String) {
        Network(NETWORK_ERROR_PAGE),
        SSL(SSL_ERROR_PAGE),
        Malware(MALWARE_ERROR_PAGE),
    }

    companion object {
        private const val NETWORK_ERROR_PAGE = "network_error_page.html"
        private const val SSL_ERROR_PAGE = "ssl_error_page.html"
        private const val MALWARE_ERROR_PAGE = "malware_error_page.html"
    }
}
