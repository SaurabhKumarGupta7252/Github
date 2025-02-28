package gupta.saurabh.github.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gupta.saurabh.github.model.Repo
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: RepoRepository) : ViewModel() {

    private val _repositories = MutableLiveData<MutableList<Repo>>(mutableListOf())

    val repositories: MutableLiveData<MutableList<Repo>> get() = _repositories

    private val _error = MutableLiveData<String>()

    val error: LiveData<String> get() = _error

    var currentPage = 1

    private val perPage = 10

    var nextPage = false

    var isLoading = false

    fun loadRepositories(username: String) {

        viewModelScope.launch {

            try {

                isLoading = true

                val repos = repository.getRepositories(username, currentPage, perPage)

                val updatedList = _repositories.value ?: mutableListOf()

                val newRepos = repos.filter { repo -> updatedList.none { it.id == repo.id } }

                updatedList.addAll(newRepos)

                nextPage = repos.size == perPage

                _repositories.postValue(updatedList)

                if (repos.isNotEmpty()) currentPage++

            } catch (e: HttpException) {

                when (e.code()) {

                    403 -> {

                        val errorBody = e.response()?.errorBody()?.string()

                        val errorMessage = try {

                            JSONObject(errorBody ?: "").getString("message")

                        } catch (jsonException: Exception) {

                            "Parsing Error"

                        }

                        _error.postValue(errorMessage)
                    }

                    401 -> _error.postValue("Unauthorized access. Please check authentication.")

                    404 -> _error.postValue("Page not found. Please check User/Organization name.")

                    else -> _error.postValue("Error ${e.code()}: ${e.message()}")
                }

            } catch (e: Exception) {

                _error.postValue(e.message ?: "Unknown error")

            } finally {

                isLoading = false
            }
        }
    }
}
