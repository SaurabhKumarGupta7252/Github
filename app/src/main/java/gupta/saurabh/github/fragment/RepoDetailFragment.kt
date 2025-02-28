package gupta.saurabh.github.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import gupta.saurabh.github.R
import gupta.saurabh.github.databinding.FragmentRepoDetailBinding
import gupta.saurabh.github.util.Constants
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class RepoDetailFragment : Fragment() {

    private var _binding: FragmentRepoDetailBinding? = null

    private val binding get() = _binding!!

    private val args: RepoDetailFragmentArgs by navArgs()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?

    ): View {

        _binding = FragmentRepoDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        val repo = args.repo

        binding.apply {

            repoName.text = getString(R.string.txt_name, repo.name)

            repoFullName.text = getString(R.string.txt_full_name, repo.full_name)

            repoBranch.text = getString(R.string.txt_default_branch, repo.default_branch)

            repoUpdated.text =

                getString(R.string.txt_last_updated, repo.updated_at?.toReadableDate())

            repoDescription.text =
                "${getString(R.string.txt_description)}${(repo.description ?: context.getString(R.string.txt_no_description))}"

            repoLanguage.text = context.getString(

                R.string.txt_language,

                repo.language ?: context.getString(R.string.txt_unknown)
            )

            var color = Constants.defaultColor

            repo.language?.let {

                color = Constants.languageColors[it] ?: Constants.defaultColor
            }

            repoLanguage.setTextColor(Color.parseColor(color))

            repoLanguage.chipStrokeColor = ColorStateList.valueOf(Color.parseColor(color))

            repoLanguage.chipBackgroundColor = ColorStateList.valueOf(

                Constants.lightenColor(Color.parseColor(color), 0.8f)
            )

            repoStars.text = context.getString(R.string.txt_stars, repo.stargazers_count ?: "0")

            repoForks.text = context.getString(R.string.txt_forks, repo.forks_count ?: "0")

            repoVisibility.text =

                context.getString(R.string.txt_visibility, repo.visibility ?: "N/A")

            Glide
                .with(avator)
                .load(repo.owner.avatar_url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(avator)
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }

    private fun String.toReadableDate(): String {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        return try {

            val date = inputFormat.parse(this)

            date?.let { outputFormat.format(it) } ?: "Invalid Date"

        } catch (e: Exception) {

            "Invalid Date"
        }
    }
}