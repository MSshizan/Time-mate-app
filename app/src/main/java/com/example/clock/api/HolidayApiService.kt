package com.example.clock.api
import com.example.clock.dataLayer.model.HolidayResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayApiService {
    @GET("PublicHolidays/{year}/{countryCode}")
    suspend fun getHolidays(
        @Path("year") year: Int,
        @Path("countryCode") countryCode: String
    ):  List<HolidayResponse>

}