package kr.ac.kaist.orz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link AssignmentTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignmentTabFragment extends Fragment {
    private ListView assignmentListView;    // The ListView to display items on.
    private AssignmentListViewAdapter listViewAdapter;      // The listViewAdapter that provides data to the ListView.
    private ArrayList<Assignment> assignmentList;   // The list of assignment data.

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
        /*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        */
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignmentList = new ArrayList<>();
        listViewAdapter = new AssignmentListViewAdapter(getContext(), assignmentList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_assignment_tab, container, false);
        assignmentListView = (ListView) view.findViewById(R.id.listview_assignment);

        // TODO: get the list of assignments from server.
        createDummyAssignments(10);
        listViewAdapter.notifyDataSetChanged();

        // Set adapter to the ListView.
        assignmentListView.setAdapter(listViewAdapter);

        return view;
    }

    private void createDummyAssignments(int num) {
        for (int i = 0; i < num; i++) {
            Calendar due = Calendar.getInstance();
            due.set(Calendar.HOUR_OF_DAY, i);

            Assignment ass = new Assignment(i, "Assignment " + i, "Dumb undergraduates",
                                            "Discrete mathematics" + (i%3), due, (float) i, i%5);
            assignmentList.add(ass);
        }
    }
}
