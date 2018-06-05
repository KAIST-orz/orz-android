package kr.ac.kaist.orz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssignmentTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssignmentTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignmentTabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

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
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment_tab, container, false);

        ListView m_ListView;
        ListAdapter m_Adapter;
        int assignment_id=123; //각 assignment 마다 unique 하게 부여되며, assignment들을 구별하고 파악하는데 사용됨.
                           //DB에서 값을 받아서, AssignmentTabFragment의 버튼 클릭시, Intent를 통해 'AssignmentDetailsActivity.java'로 전달될 것임.


        //데이터를 저장하게 되는 리스트
        final List<assignmentTab> list = new ArrayList<>();
        //리스트뷰에 보여질 아이템을 추가
        list.add(new assignmentTab("Logical Writing", "Writing Assignment 1", 1));
        list.add(new assignmentTab("Computer Architecture", "Homework 1", 2));
        list.add(new assignmentTab("Computer Architecture", "Homework 2", 3));
        list.add(new assignmentTab("Computer Architecture", "Homework 3", 4));
        list.add(new assignmentTab("Computer Architecture", "Homework 4", 5));
        list.add(new assignmentTab("Introduction to Software Engineering", "Homework 1", 6));

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

    /*
    @Override
<<<<<<< .merge_file_a19328
=======
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

}
