package gupta.saurabh.github.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gupta.saurabh.github.network.APIService
import gupta.saurabh.github.network.RepoRepository
import gupta.saurabh.github.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)

            .connectTimeout(60, TimeUnit.SECONDS).addInterceptor(loggingInterceptor)

            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()

        .addConverterFactory(GsonConverterFactory.create())

        .baseUrl(Constants.BASE_URL)

        .client(okHttpClient)

        .build()


    @Provides
    @Singleton
    fun provideGitHubApi(retrofit: Retrofit): APIService {

        return retrofit.create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepoRepository(api: APIService): RepoRepository {

        return RepoRepository(api)
    }
}
