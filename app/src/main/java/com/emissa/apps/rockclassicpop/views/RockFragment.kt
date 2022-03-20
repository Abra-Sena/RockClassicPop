package com.emissa.apps.rockclassicpop.views

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emissa.apps.rockclassicpop.MusicApp
import com.emissa.apps.rockclassicpop.adapter.RockAdapter
import com.emissa.apps.rockclassicpop.databinding.FragmentRockBinding
import com.emissa.apps.rockclassicpop.model.Rock
import com.emissa.apps.rockclassicpop.presenters.RockSongContract
import com.emissa.apps.rockclassicpop.presenters.RocksPresenter
import javax.inject.Inject


class RockFragment : Fragment(), RockSongContract {

    @Inject
    lateinit var presenter: RocksPresenter
    private val binding by lazy {
        FragmentRockBinding.inflate(layoutInflater)
    }
    private val rockAdapter by lazy {
        RockAdapter(rockSongClickListener = {
            //call player method here
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // injecting the component
        MusicApp.musicsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.initializePresenter(this)

        binding.rockRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = rockAdapter
        }

        presenter.checkNetworkConnection()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.getRockMusics()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroyPresenter()
    }



    companion object {
        fun newInstance() = RockFragment()
    }

    override fun loadingRockSongs(isLoading: Boolean) {
        binding.rockRecyclerView.visibility = View.GONE
        binding.progressBarRock.visibility = View.VISIBLE
    }

    override fun rockSongsOnSuccess(rocks: List<Rock>) {
        binding.progressBarRock.visibility = View.GONE
        binding.rockRecyclerView.visibility = View.VISIBLE
        rockAdapter.updatePopSongs(rocks)
    }

    override fun rockSongsOnError(error: Throwable) {
        binding.rockRecyclerView.visibility = View.GONE
        binding.progressBarRock.visibility = View.GONE

        AlertDialog.Builder(requireContext())
            .setTitle("An Error Occurred!")
            .setMessage(error.localizedMessage)
            .setPositiveButton("DISMISS") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }
}