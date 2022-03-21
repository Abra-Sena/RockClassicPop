package com.emissa.apps.rockclassicpop.views

import android.app.AlertDialog
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
        // implement swipe to refresh here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroyPresenter()
    }

    override fun loadingClassicSongs(isLoading: Boolean) {
        binding.classicRecyclerView.visibility = View.GONE
        binding.progressBarChar.visibility = View.VISIBLE
    }

    override fun classicSongsOnSuccess(classics: List<Classic>) {
        binding.progressBarChar.visibility = View.GONE
        binding.classicRecyclerView.visibility = View.VISIBLE
        classicAdapter.updateClassicSongs(classics)
    }

    override fun classicSongsOnError(error: Throwable) {
        binding.classicRecyclerView.visibility = View.GONE
        binding.progressBarChar.visibility = View.GONE

        AlertDialog.Builder(requireContext())
            .setTitle("An Error Occurred!")
            .setMessage(error.localizedMessage)
            .setPositiveButton("DISMISS") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    override fun onSongClicked(musicUrl: String) {
        playSong(musicUrl)
    }

    companion object {
        fun newInstance() = ClassicFragment()
    }
}