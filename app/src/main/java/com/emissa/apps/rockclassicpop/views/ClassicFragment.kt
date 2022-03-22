package com.emissa.apps.rockclassicpop.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emissa.apps.rockclassicpop.MusicApp
import com.emissa.apps.rockclassicpop.adapter.ClassicAdapter
import com.emissa.apps.rockclassicpop.data.MusicItemClicked
import com.emissa.apps.rockclassicpop.databinding.FragmentClassicBinding
import com.emissa.apps.rockclassicpop.model.Classic
import com.emissa.apps.rockclassicpop.presenters.ClassicSongContract
import com.emissa.apps.rockclassicpop.presenters.ClassicsPresenter
import javax.inject.Inject


class ClassicFragment : BaseFragment(), ClassicSongContract, MusicItemClicked {

    /**
     * this is bugging a little while fetching data
     * when app leaves 'offline mode' to 'online mode'
     */
    @Inject
    lateinit var presenter: ClassicsPresenter
    private val binding by lazy {
        FragmentClassicBinding.inflate(layoutInflater)
    }
    private val classicAdapter by lazy {
       ClassicAdapter(this)
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

        binding.classicRecyclerView.apply {
            layoutManager =  LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = classicAdapter
        }

        presenter.checkNetworkConnection()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.getClassicMusics()
        // handles swipe to refresh to load more data
        binding.swipeRefreshClassic.apply {
            setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
            )
            setOnRefreshListener {
                presenter.getClassicMusics()
                binding.swipeRefreshClassic.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroyPresenter()
    }

    override fun loadingClassicSongs(isLoading: Boolean) {
        binding.classicRecyclerView.visibility = View.GONE
        binding.progressBarChar.visibility = View.VISIBLE
    }

    override fun loadSongsOffline(classics: List<Classic>) {
        binding.classicRecyclerView.visibility = View.VISIBLE
        binding.progressBarChar.visibility = View.GONE
        classicAdapter.updateClassicSongs(classics)
        toastMessageOffline(classicAdapter.itemCount, "Classic")
        binding.swipeRefreshClassic.isRefreshing = false
    }

    override fun classicSongsOnSuccess(classics: List<Classic>) {
        binding.progressBarChar.visibility = View.GONE
        binding.classicRecyclerView.visibility = View.VISIBLE
        classicAdapter.updateClassicSongs(classics)
        toastMessageOnFetch(classics.size)
    }

    override fun classicSongsOnError(error: Throwable) {
        binding.classicRecyclerView.visibility = View.GONE
        binding.progressBarChar.visibility = View.GONE

        showAlertDialog(error)
    }

    override fun onSongClicked(musicUrl: String) {
        playSong(musicUrl)
    }

    companion object {
        fun newInstance() = ClassicFragment()
    }
}