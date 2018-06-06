package kr.ac.kaist.orz;

import kr.ac.kaist.orz.models.Course;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;


public interface OrzApi {
    @GET("api/v1/schools/{schoolID}/courses")
    Call<List<Course>> getSchoolCourses(
            @Path("schoolID") int schoolID
    );
}