package com.basicneedscorporation.basicneeds.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.basicneedscorporation.basicneeds.R
import com.basicneedscorporation.basicneeds.base.BaseFragment
import com.basicneedscorporation.basicneeds.ui.login.LoginActivity
import com.growinginfotech.businesshub.base.navigateToAndFinish
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private lateinit var notificationsViewModel: ProfileViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        setActivityContext(requireActivity())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLogout.setOnClickListener {
            setSharedPreferenceloginUser(null, false)
            activity?.navigateToAndFinish<LoginActivity>()
        }
    }
}