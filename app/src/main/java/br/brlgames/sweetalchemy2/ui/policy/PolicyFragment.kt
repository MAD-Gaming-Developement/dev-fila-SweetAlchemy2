package br.brlgames.sweetalchemy2.ui.policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import br.brlgames.sweetalchemy2.databinding.FragmentPolicyBinding
import br.brlgames.sweetalchemy2.dataconfig.sac

class PolicyFragment : Fragment() {

    private var _binding: FragmentPolicyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var policyView : WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPolicyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        policyView = binding.policyView
        policyView.loadUrl(sac.policyURL)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}