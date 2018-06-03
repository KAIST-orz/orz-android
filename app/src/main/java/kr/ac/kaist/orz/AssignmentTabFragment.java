package kr.ac.kaist.orz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;

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

//    private OnFragmentInteractionListener mListener;

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

        /*
        //데이터를 저장하게 되는 리스트
        List<assignmentTab> list = new ArrayList<>();
        //리스트뷰에 보여질 아이템을 추가
        list.add(new assignmentTab("Logical Writing", "Writing Assignment 1"));
        list.add(new assignmentTab("Computer Architecture", "Homework 1"));
        list.add(new assignmentTab("Computer Architecture", "Homework 2"));
        list.add(new assignmentTab("Computer Architecture", "Homework 3"));
        list.add(new assignmentTab("Computer Architecture", "Homework 4"));
        list.add(new assignmentTab("Introduction to Software Engineering", "Homework 1"));

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView)view.findViewById(R.id.listview_assignment);

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        m_Adapter = new assignmentTabViewAdapter(getActivity(), list);
        //리스트뷰의 어댑터를 지정해준다.
        m_ListView.setAdapter(m_Adapter);
        */

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
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
