package com.emissa.apps.rockclassicpop.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emissa.apps.rockclassicpop.MusicApp
import com.emissa.apps.rockclassicpop.adapter.PopAdapter
import com.emissa.apps.rockclassicpop.data.MusicItemClicked
import com.emissa.apps.rockclassicpop.databinding.FragmentPopBinding
import com.emissa.apps.rockclassicpop.model.Pop
import com.emissa.apps.rockclassicpop.presenters.PopSongContract
import com.emissa.apps.rockclassicpop.presenters.PopsPresenter
import javax.inject.Inject

class PopFragment : BaseFragment(), PopSongContract, MusicItemClicked {

    @Inject
    lateinit var presenter: PopsPresenter
    private val binding by lazy {
        FragmentPopBinding.inflate(layoutInflater)
    }
    private val popAdapter by lazy {
        PopAdapter(this)
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

        binding.popRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = popAdapter
        }

        presenter.checkNetworkConnection()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.getPopMusics()
        // handles swipe to refresh to load more data
        binding.swipeRefreshPop.apply {
            setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
            )
            setOnRefreshListener {
                presenter.getPopMusics()
                binding.swipeRefreshPop.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroyPresenter()
    }

    override fun loadingPopSongs(isLoading: Boolean) {
        binding.popRecyclerView.visibility = View.GONE
        binding.progressBarPop.visibility = View.VISIBLE
    }

    override fun loadSongsOffline(pops: List<Pop>) {
        binding.popRecyclerView.visibility = View.VISIBLE
        binding.progressBarPop.visibility = View.GONE
        popAdapter.updatePopSongs(pops)
        toastMessageOffline(popAdapter.itemCount, "Pop")
        binding.swipeRefreshPop.isRefreshing = false
    }

    override fun popSongsOnSuccess(pops: List<Pop>) {
        binding.progressBarPop.visibility = View.GONE
        binding.popRecyclerView.visibility = View.VISIBLE
        popAdapter.updatePopSongs(pops)
        toastMessageOnFetch(pops.size)
    }

    override fun popSongsOnError(error: Throwable) {
        binding.popRecyclerView.visibility = View.GONE
        binding.progressBarPop.visibility = View.GONE

        showAlertDialog(error)
    }

    companion object {
        fun newInstance() = PopFragment()
    }

    override fun onSongClicked(musicUrl: String) {
        playSong(musicUrl)
    }
}