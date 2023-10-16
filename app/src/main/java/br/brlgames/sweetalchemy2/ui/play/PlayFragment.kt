package br.brlgames.sweetalchemy2.ui.play

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import br.brlgames.sweetalchemy2.Activity.MainActivity
import br.brlgames.sweetalchemy2.databinding.FragmentPlayBinding
import br.brlgames.sweetalchemy2.dataconfig.sac


class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var gameView : WebView

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        gameView = binding.gameView

        sharedPref = requireContext().getSharedPreferences(sac.appCode, Context.MODE_PRIVATE)

        gameView.webChromeClient = WebChromeClient()
        gameView.settings.javaScriptEnabled = true
        gameView.settings.loadsImagesAutomatically = true
        gameView.settings.domStorageEnabled = true
        gameView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        gameView.settings.allowContentAccess = true
        gameView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        gameView.settings.javaScriptCanOpenWindowsAutomatically = true
        gameView.settings.setSupportMultipleWindows(true)


        // Set the URL
        gameView.loadUrl(sac.gameURL)


        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}