package com.example.animefeed.api

import com.example.animefeed.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
    this Retrofit Instance class enables us to create request from everywhere in our code

 */
class RetrofitInstance {

    companion object{
        //only init once
        // dependency to log response from retrofit
        private val retrofit by lazy {
            //lazy means we only initialize this here once
            val logging = HttpLoggingInterceptor()
            /* this HTTP LOGGING INTERCEPTOR dependency is able to log responses of retrofit
            this will be useful in debugging the code
             */
            //attaching to retrofit object
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            //pass the client to retrofit instance
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //addConverterFactory is use to determine how the response should be interpreted and converted to kotlin object
                .client(client)
                .build()
        }


        //get api instance from retrofit builder
        //api object
        // this can be used from everywhere to make network request
        val api by lazy {
            retrofit.create(AnimeAPI::class.java)
        }
    }
}