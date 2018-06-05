package kr.ac.kaist.orz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

import kr.ac.kaist.orz.models.myCourseInformation;


public interface orzApi {
    @GET("api/schools/{schoolID}/courses")
    Call<List<myCourseInformation>> getSchoolCourses(
            @Path("schoolID") int schoolID
    );
}