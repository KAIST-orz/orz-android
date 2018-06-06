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

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.orz.models.Assignment;
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
    private ArrayList<Assignment> assignmentList;   // The list of assignment data.

    // The item names to show on the dialog for picking sorting criteria.
    private static final String SORT_DUE = "Due";
    private static final String SORT_COURSE = "Course";
    private static final String SORT_SIGNIFICANCE = "Significance";
    private static final String SORT_ESTIMATE = "Average time estimate";

    // Current sorting criteria. Initially sort by due date.
    private String sortingCriteria = SORT_DUE;

    ListView m_ListView;
    assignmentTabViewAdapter m_Adapter;

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
        getAssignments();
        Collections.sort(assignmentList, Assignment.DUE_COMPARATOR);
        listViewAdapter.notifyDataSetChanged();

        // Set adapter to the ListView.
        assignmentListView.setAdapter(listViewAdapter);

        //데이터를 저장하게 되는 리스트
        final List<Assignment> list = new ArrayList<>();
        //리스트뷰에 보여질 아이템을 추가
        OrzApi api = ApplicationController.getInstance().getApi();
        User user = ApplicationController.getInstance().getUser();
        Call<List<Assignment>> call = api.getStudentAssignments(user.getID());
        call.enqueue(new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                if(response.isSuccessful()) {
                    list.addAll(response.body());
                    m_Adapter.notifyDataSetChanged();
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {
            }
        });

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)view.findViewById(R.id.listview_assignment);

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        m_Adapter = new assignmentTabViewAdapter(getActivity(), list);
        //리스트뷰의 어댑터를 지정해준다.
        m_ListView.setAdapter(m_Adapter);


        //AssignmentTabFragment의 listview에 보여지는 row를 클릭 할 경우, AssignmentDetailsActivity로 넘어가는 기능
        //[Ref]http://bitsoul.tistory.com/95 [Happy Programmer~]
        m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //--For testing : 버튼 클릭 시 assignment 이름 출력--
                //Toast.makeText(getActivity() , list.get(position).getName(),Toast.LENGTH_LONG).show();

                // 상세정보 화면으로 이동하기(인텐트 날리기)
                // 1. 다음화면을 만든다 (생략)
                // 2. AndroidManifest.xml 에 화면을 등록한다 (생략)
                // 3. Intent 객체를 생성하여 날린다
                Intent intent = new Intent(
                        getActivity(), // 현재화면의 제어권자 (fragment의 경우 getActivity, activity의 경우 getApplicationContext 함수 사용)
                        AssignmentDetailsActivity.class); // 다음넘어갈 화면

                // intent 객체에 데이터를 실어서 보내기
                // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                // 데이터 주고 받는 법 : http://pds0819.tistory.com/entry/%EC%9D%B8%ED%85%90%ED%8A%B8Intent%EB%A1%9C-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%84%EB%8B%ACputExtra-getExtras-%EA%B7%B8%EB%A6%AC%EA%B3%A0-TapHost%EC%97%90%EC%84%9C-%EB%8B%A4%EB%A5%B8-%EC%97%91%ED%8B%B0%EB%B9%84%ED%8B%B0%EB%A1%9C-%EB%B3%80%EC%88%98%EA%B0%92-%EC%A0%84%EB%8B%AC
                intent.putExtra("Assignment ID", list.get(position).getID());

                startActivity(intent);
            }
        });

        return view;
    }

    private void getAssignments() {
        assignmentList.clear();
        createDummyAssignments(10);
    }

    private void createDummyAssignments(int num) {
        for (int i = 0; i < num; i++) {
            Calendar due = Calendar.getInstance();
            due.set(Calendar.HOUR_OF_DAY, i);

            Assignment ass = new Assignment(i, "Assignment " + i, "Dumb undergraduates",
                                            "Discrete mathematics ddddddddddddddddddddddddddddddddddddddddddddddddd" + (i%3), due, (float) i, i%5);
            assignmentList.add(ass);
        }
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
        ListView listView = ((AlertDialog) dialogInterface).getListView();
        Object checkedItem = listView.getAdapter().getItem(listView.getCheckedItemPosition());

        // The comparator to sort assignmentList with according to the picked one.
        Comparator<Assignment> assignmentComparator;
        // Set current sorting criteria.
        sortingCriteria = checkedItem.toString();

        switch (sortingCriteria) {
            case SORT_DUE:
                assignmentComparator = Assignment.DUE_COMPARATOR;
                break;
            case SORT_COURSE:
                assignmentComparator = Assignment.COURSE_COMPARATOR;
                break;
            case SORT_SIGNIFICANCE:
                assignmentComparator = Assignment.SIGNIFICANCE_COMPARATOR;
                break;
            case SORT_ESTIMATE:
                assignmentComparator = Assignment.ESTIMATE_COMPARATOR;
                break;
            default:
                assignmentComparator = null;
        }

        // Sort the assignmentList with picked sorting criteria, then notify assignmentListViewAdapter.
        if (assignmentComparator != null) {
            Collections.sort(assignmentList, assignmentComparator);
            listViewAdapter.notifyDataSetChanged();
        }
    }
}
