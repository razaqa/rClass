package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity.ClassroomRegistrationActivity;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity.DashboardActivity;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity.LoginActivity;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.application.BasicApp;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.ClassroomListViewModel;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.DashboardViewModel;

public class ClassroomListFragment extends Fragment  {

    public static final String CLASS_NAME = "ClassroomListFragment";

    private ClassroomListViewModel classroomListViewModel;
    private DashboardViewModel dashboardViewModel;

    @BindView(R.id.textViewHeaderUsername)
    TextView textViewHeaderUsername;

    @BindView(R.id.textViewHeaderPercentage)
    TextView textViewHeaderPercentage;

    @BindView(R.id.listViewClassroomAll)
    ListView listViewClassroomAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classroom_list, container, false);
        ButterKnife.bind(this, view);

        classroomListViewModel = new ViewModelProvider(requireActivity()).get(ClassroomListViewModel.class);
        dashboardViewModel = DashboardViewModel.getInstance(BasicApp.getInstance());

        setUpHeader();
        setUpList();

        return view;
    }

    protected void setUpHeader() {
        classroomListViewModel.getCurrentUser().observe(getActivity(), user -> {
            textViewHeaderUsername.setText(user.getName());
        });
        String percentage = String.valueOf(classroomListViewModel.getHeaderPercentage());
        percentage = String.format("%s%%", percentage);
        percentage = percentage.equalsIgnoreCase("nan%") ? "0%" : percentage;
        textViewHeaderPercentage.setText(percentage);
    }

    protected void setUpList() {
        classroomListViewModel.getClassrooms().observe(getActivity(), classrooms -> {

            listViewClassroomAll.setAdapter(
                    new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            classrooms.stream()
                                    .map(x -> x.getLastAttendanceId() + "," + x.getName())
                                    .collect(Collectors.toList())
                    )
            );

            listViewClassroomAll.setOnItemClickListener((parent, itemView, position, id) -> {
                TextView textView = (TextView) itemView;
                int lastAttendanceId = Integer.parseInt(textView.getText().toString().split(",")[0]);
                String classStr = textView.getText().toString().split(",")[1];
                textView.setText(classStr);
                dashboardViewModel.selectClassroom(classStr);
                dashboardViewModel.loadClassroomInfo(classStr);
                dashboardViewModel.loadAttendanceInfo(lastAttendanceId);
                ((DashboardActivity) requireActivity()).showClassroomInfoFragment();
            });
        });

    }
}
