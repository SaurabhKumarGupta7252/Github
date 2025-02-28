package gupta.saurabh.github.network

import gupta.saurabh.github.model.Repo
import javax.inject.Inject

class RepoRepository @Inject constructor(private val githubApi: APIService) {

    suspend fun getRepositories(username: String, page: Int, perPage: Int): List<Repo> {

        return githubApi.getRepositories(username, page, perPage)

    }
}