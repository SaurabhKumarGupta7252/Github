package gupta.saurabh.github.network

import gupta.saurabh.github.model.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("users/{user}/repos")
    suspend fun getRepositories(

        @Path("user") user: String,

        @Query("page") page: Int,

        @Query("per_page") perPage: Int

    ): List<Repo>
}