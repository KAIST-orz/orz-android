package kr.ac.kaist.orz;

import kr.ac.kaist.orz.models.Alarms;
import kr.ac.kaist.orz.models.Course;
import kr.ac.kaist.orz.models.Assignment;
import kr.ac.kaist.orz.models.PersonalSchedule;
import kr.ac.kaist.orz.models.Schedule;
import kr.ac.kaist.orz.models.School;
import kr.ac.kaist.orz.models.StudentAssignment;
import kr.ac.kaist.orz.models.TimeForAssignment;
import kr.ac.kaist.orz.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
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

    @POST("api/v1/students/{userID}/personal-schedules")
    Call<PersonalSchedule> registerStudentPersonalSchedule(
            @Path("userID") int userID,
            @Body Map<String, Object> body
    );

    @GET("api/v1/students/{userID}/time-for-assignments")
    Call<List<TimeForAssignment>> getStudentTimeForAssignments(
            @Path("userID") int userID
    );

    @POST("api/v1/students/{userID}/assignments/{assignmentID}/time-for-assignments")
    Call<TimeForAssignment> registerStudentTimeForAssignment(
            @Path("userID") int userID,
            @Path("assignmentID") int assignmentID,
            @Body Map<String, Object> body
    );
 
    @POST("api/v1/lecturers/{userID}/courses")
    Call<Void> registerLecture(
            @Path("userID") int userID,
            @Body Map<String, String> body
    );

    @PUT("api/v1/courses/{courseID}")
    Call<Void> updateLecture(
            @Path("courseID") int courseID,
            @Body Map<String, String> body
    );

    @POST("api/v1/courses/{courseID}/assignments")
    Call<Void> registerAssignment(
            @Path("courseID") int courseID,
            @Body Map<String, String> body
    );

    @GET("api/v1/assignments/{assignmentID}")
    Call<Map<String, String>> getAssignment(
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

    @GET("api/v1/students/{userID}/alarms")
    Call<Alarms> getStudentAlarms(
            @Path("userID") int userID
    );

    @PUT("api/v1/students/{userID}/alarms")
    Call<Alarms> putStudentAlarms(
            @Path("userID") int userID,
            @Body Map<String, String> body
    );

    //해당 코스에 등록된 과제의 리스트를 가져옴
    @GET("api/v1/courses/{courseID}/assignments")
    Call<List<Assignment>> getCourseAssignment(
            @Path("courseID") int courseID
    );

    //Lecturer에 의해 course 삭제시에 쓰임
    @DELETE("api/v1/courses/{courseID}")
    Call<Void> deleteCourse(
            @Path("courseID") int courseID
    );

    //Lecturer에 의해 assignment 삭제시에 쓰임
    @DELETE("api/v1/assignments/{assignmentID}")
    Call<Void> deleteAssignment(
            @Path("assignmentID") int assignmentID
    );

    @GET("api/v1/students/{userID}/assignments/{assignmentID}/time-for-assignments")
    Call<List<TimeForAssignment>> getTimesForAssignment(
            @Path("userID") int userID,
            @Path("assignmentID") int assignmentID
    );

    @POST("api/v1/students/{userID}/assignments/{assignmentID}/time-for-assignments")
    Call<Void> addTimeForAssignment(
            @Path("userID") int userID,
            @Path("assignmentID") int assignmentID,
            @Body Map<String, Object> body
    );

    @PUT("api/v1/students/{userID}/personal-schedules/{scheduleID}")
    Call<Void> updatePersonalSchedule(
            @Path("userID") int userID,
            @Path("scheduleID") int scheduleID,
            @Body Map<String, Object> body
    );

    @PUT("api/v1/students/{userID}/assignments/{assignmentID}/time-for-assignments/{scheduleID}")
    Call<Void> updateTimeForAssignment(
            @Path("userID") int userID,
            @Path("assignmentID") int assignmentID,
            @Path("scheduleID") int scheduleID,
            @Body Map<String, Object> body
    );

    @DELETE("api/v1/students/{userID}/personal-schedules/{scheduleID}")
    Call<Void> deletePersonalSchedule(
            @Path("userID") int userID,
            @Path("scheduleID") int scheduleID
    );

    @DELETE("api/v1/students/{userID}/assignments/{assignmentID}/time-for-assignments/{scheduleID}")
    Call<Void> deleteTimeForAssignment(
            @Path("userID") int userID,
            @Path("assignmentID") int assignmentID,
            @Path("scheduleID") int scheduleID
    );
}