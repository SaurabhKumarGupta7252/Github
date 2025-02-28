package gupta.saurabh.github.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gupta.saurabh.github.R
import gupta.saurabh.github.databinding.ItemRepoBinding
import gupta.saurabh.github.model.Repo
import gupta.saurabh.github.util.Constants

class RepoAdapter(
    val context: Context,

    private val repositories: MutableList<Repo>,

    private val onItemClick: (Repo) -> Unit

) : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    inner class RepoViewHolder(private val binding: ItemRepoBinding) :

        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(repo: Repo, onItemClick: (Repo) -> Unit) {

            binding.apply {

                repoName.text = "${adapterPosition + 1}. ${repo.name}"

                repoFullName.text = "/${repo.full_name}"

                repoDescription.text = repo.description ?: context.getString(R.string.txt_no_description)

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

                root.setOnClickListener { onItemClick(repo) }

                Glide
                    .with(avator)
                    .load(repo.owner.avatar_url)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(avator)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {

        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {

        holder.bind(repositories[position], onItemClick)
    }

    override fun getItemCount(): Int = repositories.size

    fun updateRepos(newRepos: List<Repo>) {

        val startPosition = repositories.size

        repositories.addAll(newRepos)

        notifyItemRangeInserted(startPosition, newRepos.size)
    }
}