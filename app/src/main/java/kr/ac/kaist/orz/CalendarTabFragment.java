package kr.ac.kaist.orz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import java.util.Calendar;
import java.text.DateFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarTabFragment extends Fragment
                                implements DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Views for this fragment
    private ImageButton toPreviousDay;
    private ImageButton toNextDay;
    private Button pickDate;

    // Keep the date information which the user has chosen
    private Calendar calendar;

    private OnFragmentInteractionListener mListener;

    public CalendarTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalendarTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarTabFragment newInstance() {
        CalendarTabFragment fragment = new CalendarTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When this fragment is first created, instantiate a new Calendar object.
        calendar = Calendar.getInstance();
    }

    // Formats the date held by calendar to "MMM DD, YYYY" format.
    public String formatDateOfCalendar() {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return formatter.format(calendar.getTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_tab, container, false);

        // Initialize pickDate, toPreviousDay, toNextDay buttons.
        pickDate = (Button) view.findViewById(R.id.pick_date);
        toPreviousDay = (ImageButton) view.findViewById(R.id.to_previous_day);
        toNextDay = (ImageButton) view.findViewById(R.id.to_next_day);

        // Set onClick listeners as the methods in this class.
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        toPreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                pickDate.setText(formatDateOfCalendar());

                // TODO: Communicate with the server and draw schedules on the layout.

            }
        });

        toNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, 1);
                pickDate.setText(formatDateOfCalendar());

                // TODO: Communicate with the server and draw schedules on the layout.
            }
        });

        // Let the button to show picked date.
        pickDate.setText(formatDateOfCalendar());

        // TODO: Initialize the schedules of the previously chosen date.

        // Inflate the layout for this fragment
        return view;
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
<<<<<<< .merge_file_a01964


=======
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
     * onClick by button5
     */
    public void showDatePickerDialog(View view) {
        FragmentActivity parentActivity = getActivity();
        if (parentActivity == null) {
            return;
        }

        DatePickerFragment dialog = new DatePickerFragment();
        dialog.setOnDateSetListener(this);  // Setting this fragment as OnDateSetListener

        // Putting currently picked (being shown) date as arguments.
        Bundle args = new Bundle();
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt("month", calendar.get(Calendar.MONTH));
        args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setArguments(args);

        // Show to the user. fragment manager is OrzMainActivity's manager.
        dialog.show(parentActivity.getFragmentManager(), "datePicker");
    }

    // Listener for the date picker's DateSet event.
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Change where the calendar is pointing.
        calendar.set(year, month, day);
        pickDate.setText(formatDateOfCalendar());

        // Communicate with the server to acquire the schedules for the day
        // and draw appropriate views for the schedules (color, ...)
        drawSchedulesOntoLayout(getSchedules(year, month, day));
    }

    // Communicates with the server to acquire the schedules for this day.
    public int[] getSchedules(int year, int month, int day) {

        return new int[1];
    }

    // Creates and draws the UI components for schedules, assignments,
    // assignment due dates onto the fragment layout.
    public void drawSchedulesOntoLayout(int[] schedulesOfTheDay) {

    }

    public static class DatePickerFragment extends DialogFragment {
        // A OnDateSetListener that listens to this fragment's DateSet event.
        private DatePickerDialog.OnDateSetListener mOnDateSetListener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get arguments passed by CalendarTabFragment
            Bundle args = getArguments();
            int year = args.getInt("year");
            int month = args.getInt("month");
            int day = args.getInt("day");

            // Create a new instance of DatePickerDialog
            return new DatePickerDialog(getActivity(), mOnDateSetListener, year, month, day);
        }

        public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
            this.mOnDateSetListener = listener;
        }
    }
}
