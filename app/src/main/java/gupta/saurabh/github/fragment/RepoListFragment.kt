package gupta.saurabh.github.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import gupta.saurabh.github.R
import gupta.saurabh.github.adapter.RepoAdapter
import gupta.saurabh.github.databinding.FragmentRepoListBinding
import gupta.saurabh.github.network.MainViewModel
import gupta.saurabh.github.network.NetworkUtils
import gupta.saurabh.github.util.DialogHelper

@AndroidEntryPoint
class RepoListFragment : Fragment() {

    private var _binding: FragmentRepoListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: RepoAdapter

    private var username: String? = null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?

    ): View {

        _binding = FragmentRepoListBinding.inflate(inflater, container, false)

        binding.btnOk.setOnClickListener { requireActivity().onBackPressed() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        username = arguments?.getString("username")

        setupRecyclerView()

        username?.let {

            binding.tvMessage.text = getString(R.string.txt_loading)

            viewModel.loadRepositories(it)
        }

        observeViewModel()
    }

    private fun setupRecyclerView() {

        adapter = RepoAdapter(requireContext(), mutableListOf()) { repo ->

            val action = RepoListFragmentDirections.actionRepoListFragmentToRepoDetailFragment(repo)

            findNavController().navigate(action)
        }

        val layoutManager = LinearLayoutManager(requireContext())

        binding.apply {

            recyclerView.layoutManager = layoutManager

            recyclerView.adapter = adapter

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    val totalItemCount = layoutManager.itemCount

                    if (lastVisibleItemPosition + 3 >= totalItemCount && viewModel.nextPage) {

                        username?.let {

                            loadData(it)
                        }
                    }
                }
            })
        }
    }

    private fun loadData(it: String) {

        if (NetworkUtils.isInternetAvailable(requireContext()))

            viewModel.loadRepositories(it)

        else DialogHelper.showDialog(

            getString(R.string.txt_no_internet_connection),

            requireActivity()
        )
    }

    private fun observeViewModel() {

        binding.apply {

            viewModel.repositories.observe(viewLifecycleOwner) { repos ->

                if (viewModel.currentPage == 1 && repos.size == 0) {

                    llNoData.visibility = View.VISIBLE

                    recyclerView.visibility = View.GONE

                    animationView.visibility = if (viewModel.isLoading) View.VISIBLE else View.GONE

                    tvMessage.text = getString(

                        if (viewModel.isLoading) R.string.txt_loading else R.string.txt_no_data_found
                    )

                } else {

                    adapter.updateRepos(repos)

                    llNoData.visibility = View.GONE

                    recyclerView.visibility = View.VISIBLE

                }
            }

            viewModel.error.observe(viewLifecycleOwner) { error ->

                llNoData.visibility = View.VISIBLE

                recyclerView.visibility = View.GONE

                animationView.visibility = View.GONE

                tvMessage.text = error

                DialogHelper.showDialog(error, requireActivity())
            }
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}