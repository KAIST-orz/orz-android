package kr.ac.kaist.orz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.ac.kaist.orz.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentDetailsLecturerActivity extends AppCompatActivity {
    int assignmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details_lecturer);

        Intent intent = getIntent();
        assignmentID = intent.getExtras().getInt("assignmentID");

        OrzApi api = ApplicationController.getInstance().getApi();
        Call<Map<String, String>> call = api.getAssignment(assignmentID);
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if(response.isSuccessful() && response.code()==200) {
                    Map<String, String> body = new HashMap<>();
                    body.putAll(response.body());
                    EditText assignment_name =  findViewById(R.id.editText_assignment_name);
                    Button due_date =  findViewById(R.id.button_due_date);
                    EditText description =  findViewById(R.id.editText_description);
                    assignment_name.setText(body.get("name"));
                    SimpleDateFormat parser1 = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
                    SimpleDateFormat parser2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    try {
                        due_date.setText(parser1.format(parser2.parse(body.get("due"))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    description.setText(body.get("description"));
                }
                else {
                    Toast.makeText(AssignmentDetailsLecturerActivity.this, "Could not get assignment information", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(AssignmentDetailsLecturerActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void selectTime(final View v) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final DatePicker dp = new DatePicker(this);
        final TimePicker tp = new TimePicker(this);

        adb.setView(dp);
        adb.setPositiveButton("set",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "test1", Toast.LENGTH_LONG).show();
                        adb.setPositiveButton("set",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(getApplicationContext(), "test2", Toast.LENGTH_LONG).show();
                                        Calendar startCalendar = Calendar.getInstance();
                                        startCalendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute());
                                        Date date = startCalendar.getTime();

                                        SimpleDateFormat parser = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
                                        ((Button)v).setText(parser.format(date));
                                    }
                                });
                        adb.setNegativeButton("cancel", null);
                        adb.setView(tp);
                        adb.show();
                    }
                });
        adb.setNegativeButton("cancel", null);
        adb.show();
    }

    public void update(View v) throws ParseException {
        Intent intent = getIntent();
        int courseID = intent.getExtras().getInt("courseID");
        EditText schedule_name = findViewById(R.id.editText_assignment_name);
        TextView due_date = findViewById(R.id.button_due_date);
        EditText description = findViewById(R.id.editText_description);

        if(schedule_name.length() == 0)
            Toast.makeText(this, "assignment name can not be empty", Toast.LENGTH_LONG).show();
        else if(due_date.getText().equals("due date"))
            Toast.makeText(this, "due date can not be empty", Toast.LENGTH_LONG).show();
        else if(description.length() == 0)
            description.setText("no description");
        else {
            OrzApi api = ApplicationController.getInstance().getApi();
            Map<String, String> body = new HashMap<>();
            body.put("name", schedule_name.getText().toString());
            SimpleDateFormat parser1 = new SimpleDateFormat("HH:mm, dd MMMM yyyy");
            SimpleDateFormat parser2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            body.put("due", parser2.format(parser1.parse(due_date.getText().toString())));
            body.put("description", description.getText().toString());
            Call<Void> call = api.updateAssignment(assignmentID, body);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful() && response.code()==200) {
                        Toast.makeText(AssignmentDetailsLecturerActivity.this, "assignment updated", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else {
                        Toast.makeText(AssignmentDetailsLecturerActivity.this, "assignment update failed " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AssignmentDetailsLecturerActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void delete(View v) {
        //Toast.makeText(this, "lecture deleted", Toast.LENGTH_LONG).show();

        final OrzApi api = ApplicationController.getInstance().getApi();

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Are you sure to delete this assignment?");
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void> call = api.deleteAssignment(assignmentID);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful() && response.code()==200) {
                            Toast.makeText(AssignmentDetailsLecturerActivity.this, "Successfully deleted", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(AssignmentDetailsLecturerActivity.this, LecturerCoursesActivity.class);
                            startActivity(intent);

                            finish();
                        }
                        else {
                            Toast.makeText(AssignmentDetailsLecturerActivity.this, "Deletion failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AssignmentDetailsLecturerActivity.this, "Not connected to server", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        adb.setNegativeButton("no", null);
        adb.show();
    }
}
