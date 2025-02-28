package gupta.saurabh.github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repo(

    val id: Int,

    val name: String,

    val full_name: String,

    val description: String?,

    val language: String?,

    val updated_at: String?,

    val default_branch: String?,

    val visibility: String?,

    val stargazers_count: Int,

    val forks_count: Int,

    val owner: Owner,

    ) : Parcelable {

    @Parcelize
    data class Owner(

        val avatar_url: String

    ) : Parcelable
}