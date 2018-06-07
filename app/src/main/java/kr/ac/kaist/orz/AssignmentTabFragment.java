package kr.ac.kaist.orz;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.orz.models.Assignment;
import kr.ac.kaist.orz.models.StudentAssignment;
import kr.ac.kaist.orz.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link AssignmentTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignmentTabFragment extends Fragment implements DialogInterface.OnClickListener {
    private ListView assignmentListView;    // The ListView to display items on.
    private AssignmentListViewAdapter listViewAdapter;      // The listViewAdapter that provides data to the ListView.
    private ArrayList<StudentAssignment> assignmentList;   // The list of assignment data.

    // The item names to show on the dialog for picking sorting criteria.
    private static final String SORT_DUE = "Due";
    private static final String SORT_COURSE = "Course";
    private static final String SORT_SIGNIFICANCE = "Significance";
    private static final String SORT_ESTIMATE = "Average time estimate";

    // Current sorting criteria. Initially sort by due date.
    private String sortingCriteria = SORT_DUE;

    public AssignmentTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AssignmentTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignmentTabFragment newInstance() {
        AssignmentTabFragment fragment = new AssignmentTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_assignment_tab, container, false);
        assignmentListView = view.findViewById(R.id.listview_assignment);

        // The list of assignments.
        assignmentList = new ArrayList<>();

        // Set adapter to the ListView.
        listViewAdapter = new AssignmentListViewAdapter(getContext(), assignmentList);
        assignmentListView.setAdapter(listViewAdapter);

        // Get the list of assignments from the orz server.
        OrzApi api = ApplicationController.getInstance().getApi();
        User user = ApplicationController.getInstance().getUser();
        Call<List<StudentAssignment>> call = api.getStudentAssignments(user.getID());
        call.enqueue(new Callback<List<StudentAssignment>>() {
            @Override
            public void onResponse(Call<List<StudentAssignment>> call, Response<List<StudentAssignment>> response) {
                if(response.isSuccessful()) {
                    // Add the assignments into the list and sort it by currently picked sorting criteria.
                    assignmentList.addAll(response.body());
                    Collections.sort(assignmentList, getComparator(sortingCriteria));
                    listViewAdapter.notifyDataSetChanged();
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<List<StudentAssignment>> call, Throwable t) {
            }
        });

        return view;
    }

    // Shows a dialog that lets the user pick a sorting criteria.
    public void showSortingCriteriaDialog(Context context) {
        final String[] criteria = new String[]{SORT_DUE, SORT_COURSE, SORT_SIGNIFICANCE, SORT_ESTIMATE};
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("Set sorting criteria");

        // Show the current sorting criteria as checked.
        int checkedItem;
        for (checkedItem = 0; checkedItem < criteria.length; checkedItem++) {
            if (sortingCriteria.equals(criteria[checkedItem])) {
                break;
            }
        }

        adb.setSingleChoiceItems(criteria, checkedItem, null);
        adb.setPositiveButton("close", this);
        adb.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        // Get which sorting criteria has been selected.
        ListView listView = ((AlertDialog) dialogInterface).getListView();
        Object checkedItem = listView.getAdapter().getItem(listView.getCheckedItemPosition());
        sortingCriteria = checkedItem.toString();

        // Get appropriate comparator for the selected criteria and sort the list of assignments.
        Collections.sort(assignmentList, getComparator(sortingCriteria));
        listViewAdapter.notifyDataSetChanged();
    }

    // Returns the comparator for the sorting criteria passed by argument.
    public static Comparator<? super StudentAssignment> getComparator(String SORT_CRITERIA) {
        switch (SORT_CRITERIA) {
            case SORT_COURSE:
                return Assignment.COURSE_COMPARATOR;
            case SORT_SIGNIFICANCE:
                return StudentAssignment.SIGNIFICANCE_COMPARATOR;
            case SORT_ESTIMATE:
                return Assignment.ESTIMATE_COMPARATOR;
            case SORT_DUE:
            default:
                // Default is to sort by due dates.
                return Assignment.DUE_COMPARATOR;
        }
    }
}
