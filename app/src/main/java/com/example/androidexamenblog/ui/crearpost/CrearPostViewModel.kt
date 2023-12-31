package com.example.androidexamenblog.ui.crearpost

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidexamenblog.AndroidExamenBlogApp
import com.example.androidexamenblog.R
import com.example.androidexamenblog.data.entities.BlogEntryRequest
import com.example.androidexamenblog.data.remote.BlogService
import com.example.androidexamenblog.data.repository.BlogRepository
import com.example.androidexamenblog.utils.Resource
import com.example.androidexamenblog.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearPostViewModel @Inject constructor(
    private val app: Application,
    private val repository: BlogRepository
):AndroidViewModel (app){

    val crearPostResponse: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun registrarPost(
        titulo: String,
        autor: String,
        contenido: String
    ) = viewModelScope.launch(Dispatchers.IO){

        crearPostResponse.postValue(Resource.Loading())

        try{

            if(Utils.isNetworkAvailable(app)){

                val result = repository.registrarEntrada(BlogEntryRequest(autor,contenido,titulo))

                if(result.isSuccessful){

                    crearPostResponse.postValue(Resource.Success(result.code() == 200))

                }else{

                    crearPostResponse.postValue(Resource.Error(result.message(),null))

                }

            } else {

                crearPostResponse.postValue(Resource.Error(app.resources.getString(R.string.error_red)))

            }

        }catch (ex: Exception){

            crearPostResponse.postValue(Resource.Error(ex.localizedMessage?.toString() ?: "Error",null))
            Log.e("CrearPostViewModel", "Error: -> ${ex.localizedMessage}")

        }

    }

}