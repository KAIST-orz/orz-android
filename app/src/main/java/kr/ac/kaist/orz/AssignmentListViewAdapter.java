package kr.ac.kaist.orz;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AssignmentListViewAdapter extends ArrayAdapter {
    // View lookup cache.
    private static class ViewHolder {
        TextView courseNameText;
        TextView assignmentNameText;
        LinearLayout textLayout;
        ImageView arrowHead;
    }

    public AssignmentListViewAdapter(Context context, List<Assignment> assignments) {
        super(context, R.layout.assignment_view, assignments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data at the position.
        final Assignment assignment = (Assignment) getItem(position);

        // Check if an existing view is being reused.
        ViewHolder viewHolder;
        if (convertView == null) {
            // No view to reuse. Inflate a new view.
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.assignment_view, parent, false);

            // Finding necessary components in the view.
            viewHolder = new ViewHolder();
            viewHolder.arrowHead = convertView.findViewById(R.id.arrow_head);
            viewHolder.textLayout = convertView.findViewById(R.id.description);
            viewHolder.courseNameText = convertView.findViewById(R.id.course_name);
            viewHolder.assignmentNameText = convertView.findViewById(R.id.assignment_name);

            convertView.setTag(viewHolder);
        } else {
            // View is being recycled. Retrieve the cached resources.
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the view using viewHolder object.
        // TODO: set appropriate color.
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        viewHolder.arrowHead.getDrawable().setTint(color);
        viewHolder.textLayout.setBackgroundColor(color);
        viewHolder.courseNameText.setText(assignment.getCourseName());
        viewHolder.assignmentNameText.setText(assignment.getAssignmentName());

        // Set OnClickListner to change to assignment details screen.
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AssignmentDetailsActivity.class);
                intent.putExtra("assignment_id", assignment.getId());
                view.getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
