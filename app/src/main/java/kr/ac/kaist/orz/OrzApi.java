package kr.ac.kaist.orz;

import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.Assignment;
import kr.ac.kaist.orz.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;
import java.util.Map;


public interface OrzApi {
    @POST("api/v1/signin")
    Call<User> signin(
            @Body Map<String, String> body
    );
    

    @GET("api/v1/schools/{schoolID}/courses")
    Call<List<Course>> getSchoolCourses(
            @Path("schoolID") int schoolID
    );

    @GET("api/v1/students/{userID}/courses")
    Call<List<Course>> getStudentCourses(
            @Path("userID") int userID
    );

    @GET("api/v1/students/{userID}/assignments")
    Call<List<Assignment>> getStudentAssignments(
            @Path("userID") int userID
    );
}