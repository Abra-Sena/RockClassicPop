package com.emissa.apps.rockclassicpop.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emissa.apps.rockclassicpop.MusicApp
import com.emissa.apps.rockclassicpop.adapter.RockAdapter
import com.emissa.apps.rockclassicpop.data.MusicItemClicked
import com.emissa.apps.rockclassicpop.databinding.FragmentRockBinding
import com.emissa.apps.rockclassicpop.model.Rock
import com.emissa.apps.rockclassicpop.presenters.RockSongContract
import com.emissa.apps.rockclassicpop.presenters.RocksPresenter
import javax.inject.Inject


class RockFragment : BaseFragment(), RockSongContract, MusicItemClicked {

    @Inject
    lateinit var presenter: RocksPresenter
    private val binding by lazy {
        FragmentRockBinding.inflate(layoutInflater)
    }
    private val rockAdapter by lazy {
        RockAdapter(this)
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
        // handles swipe to refresh to load more data
        binding.swipeRefreshRock.apply {
            setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
            )
            setOnRefreshListener {
                presenter.getRockMusics()
                binding.swipeRefreshRock.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroyPresenter()
    }

    override fun loadingRockSongs(isLoading: Boolean) {
        binding.rockRecyclerView.visibility = View.GONE
        binding.progressBarRock.visibility = View.VISIBLE
    }

    override fun loadSongsOffline(rocks: List<Rock>) {
        binding.rockRecyclerView.visibility = View.VISIBLE
        binding.progressBarRock.visibility = View.GONE
        rockAdapter.updatePopSongs(rocks)
        toastMessageOffline(rockAdapter.itemCount, "Rock")
        binding.swipeRefreshRock.isRefreshing = false
        showNoInternetAlertDialog()
    }

    override fun rockSongsOnSuccess(rocks: List<Rock>) {
        binding.progressBarRock.visibility = View.GONE
        binding.rockRecyclerView.visibility = View.VISIBLE
        rockAdapter.updatePopSongs(rocks)
        toastMessageOnFetch(rocks.size)
    }

    override fun rockSongsOnError(error: Throwable) {
        binding.rockRecyclerView.visibility = View.GONE
        binding.progressBarRock.visibility = View.GONE
        showErrorAlertDialog(error)
    }

    override fun onSongClicked(musicUrl: String) {
        playSong(musicUrl)
    }

    companion object {
        fun newInstance() = RockFragment()
    }
}