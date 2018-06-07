package kr.ac.kaist.orz;

import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.Assignment;
import kr.ac.kaist.orz.models.PersonalSchedule;
import kr.ac.kaist.orz.models.School;
import kr.ac.kaist.orz.models.StudentAssignment;
import kr.ac.kaist.orz.models.TimeForAssignment;
import kr.ac.kaist.orz.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;
import java.util.Map;


public interface OrzApi {
    @POST("api/v1/signin")
    Call<User> signin(
            @Body Map<String, String> body
    );

    @POST("api/v1/signup")
    Call<Void> signup(
            @Body Map<String, Object> body
    );

    @PUT("api/v1/users/{userID}")
    Call<Void> updateUserAccount(
            @Path("userID") int userID,
            @Body Map<String, Object> body
    );

    @DELETE("api/v1/users/{userID}")
    Call<Void> deleteUserAccount(
            @Path("userID") int userID
    );

    @GET("api/v1/schools")
    Call<List<School>> getSchools(
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
    Call<List<StudentAssignment>> getStudentAssignments(
            @Path("userID") int userID
    );

    @GET("api/v1/students/{userID}/personal-schedules")
    Call<List<PersonalSchedule>> getStudentPersonalSchedules(
            @Path("userID") int userID
    );

    @GET("api/v1/students/{userID}/time-for-assignments")
    Call<List<TimeForAssignment>> getStudentTimeForAssignments(
            @Path("userID") int userID
    );
 
    @POST("api/v1/lecturers/{userID}/courses")
    Call<Void> registerLecture(
            @Path("userID") int userID,
            @Body Map<String, String> body
    );

    @POST("api/v1/courses/{courseID}/assignments")
    Call<Void> registerAssignment(
            @Path("courseID") int courseID,
            @Body Map<String, String> body
    );

    @GET("api/v1/assignments/{assignmentID}")
    Call<Assignment> getAssignment(
            @Path("assignmentID") int assignmentID
    );

    @PUT("api/v1/assignments/{assignmentID}")
    Call<Void> updateAssignment(
            @Path("assignmentID") int assignmentID,
            @Body Map<String, String> body
    );

    @PUT("api/v1/students/{userID}/assignments/{assignmentID}")
    Call<Void> updateStudentAssignment(
            @Path("userID") int userID,
            @Path("assignmentID") int assignmentID,
            @Body Map<String, Object> body
    );

    // My Courses 에 있는 삭제 버튼을 누를 시, DB에 있는 해당 코스를 삭제함
    //(Used in myCourseViewAdapter)
    @DELETE("api/v1/students/{userID}/courses/{courseID}")
    Call<Void> deleteStudentCourses(
            @Path("userID") int userID,
            @Path("courseID") int courseID
    );

    // Open Courses 에 있는 구독 버튼을 누를 시, DB에 있는 해당 코스를 유저가 구독할 수 있게 함
    //(Used in openCourseViewAdapter)
    @POST("api/v1/students/{userID}/courses/{courseID}")
    Call<List<Course>> subscribeCourses(
            @Path("userID") int userID,
            @Path("courseID") int courseID
    );

    @GET("api/v1/lecturers/{userID}/courses")
    Call<List<Course>> getLecturerCourses(
            @Path("userID") int userID
    );

    // Open Courses 에 있는 구독 버튼을 누를 시, DB에 있는 해당 코스를 유저가 구독할 수 있게 함
    //(Used in openCourseViewAdapter)
    @POST("api/v1/courses/{courseID}/assignments")
    Call<List<Assignment>> addAssignmentToCourse(
            @Path("courseID") int courseID
    );
}