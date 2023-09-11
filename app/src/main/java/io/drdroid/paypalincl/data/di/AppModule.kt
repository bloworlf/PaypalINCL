package io.drdroid.paypalincl.data.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.drdroid.paypalincl.data.impl.RepositoryImpl
import io.drdroid.paypalincl.data.network.PaypalApiCall
import io.drdroid.paypalincl.data.network.TvShowCall
import io.drdroid.paypalincl.data.repository.Repository
import io.drdroid.paypalincl.utils.Common
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

//    @Provides
//    fun providesClientID(@ApplicationContext context: Context): Map<String, String> {
//        return mapOf(
//            "client_id" to context.getString(R.string.client_id),
//            "client_secret" to context.getString(R.string.client_secret),
//        )
//    }

    @Provides
    fun providesOkHttpInstance(/*@ApplicationContext context: Context*/): OkHttpClient {
        return OkHttpClient.Builder()
//            .addInterceptor(
//                BasicAuthInterceptor(
//                    context.getString(R.string.client_id),
//                    context.getString(R.string.client_secret)
//                )
//            )
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun providesRetrofitInstance(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Common.PAYPAL_API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun providesApiCall(retrofit: Retrofit): PaypalApiCall {
        return retrofit.create(PaypalApiCall::class.java)
    }

    @Provides
    fun providesTvShowApi(retrofit: Retrofit): TvShowCall {
        return retrofit.create(TvShowCall::class.java)
    }

    @Provides
    fun provideRepo(
        paypalApiCall: PaypalApiCall,
        tvShowCall: TvShowCall,
        firebaseAuth: FirebaseAuth
    ): Repository {
        return RepositoryImpl(
            firebaseAuth = firebaseAuth,
            paypalApiCall = paypalApiCall,
            tvShowCall = tvShowCall
        )
    }
}