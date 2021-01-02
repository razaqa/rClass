package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.R;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.activity.DashboardActivity;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.entity.Classroom;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.ClassroomListViewModel;
import id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.viewmodel.DashboardViewModel;

@SuppressLint("NonConstantResourceId")
public class ClassroomListFragment extends Fragment  {

    public static final String CLASS_NAME = "ClassroomListFragment";

    private ClassroomListViewModel classroomListViewModel;
    private DashboardViewModel dashboardViewModel;

    private final HashMap<String, Integer> classNamesAndLastAttendanceIds = new HashMap<>();

    @BindView(R.id.textViewHeaderUsername) TextView textViewHeaderUsername;
    @BindView(R.id.textViewHeaderPercentage) TextView textViewHeaderPercentage;
    @BindView(R.id.textViewHeaderTime) TextView textViewHeaderTime;
    @BindView(R.id.textViewHeaderDate) TextView textViewHeaderDate;
    @BindView(R.id.listViewClassroomAll) ListView listViewClassroomAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_classroom_list, container, false);
        ButterKnife.bind(this, view);

        classroomListViewModel = new ViewModelProvider(requireActivity()).get(ClassroomListViewModel.class);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        setUpHeader();
        setUpList();

        return view;
    }

    protected void setUpHeader() {
        dashboardViewModel.getCurrentUser().observe(Objects.requireNonNull(getActivity()),
                user -> textViewHeaderUsername.setText(String.format(" %s", user.getName())));

        String percentage = dashboardViewModel.getHeaderPercentage();
        textViewHeaderPercentage.setText(percentage);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Date dateNow = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                String dateFormaText = dateFormat.format(dateNow);
                String timeFormatText = timeFormat.format(dateNow);

                textViewHeaderDate.setText(dateFormaText);
                textViewHeaderTime.setText(timeFormatText);

                handler.postDelayed(this, 1000);
            }
        });
    }

    protected void setUpList() {
        classroomListViewModel.getClassrooms().observe(Objects.requireNonNull(getActivity()), classrooms -> {

            listViewClassroomAll.setAdapter(
                    new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            classrooms.stream()
                                    .map(Classroom::getName)
                                    .collect(Collectors.toList())
                    )
            );

            classrooms.forEach(x -> {
                classNamesAndLastAttendanceIds.put(
                    x.getName(), x.getLastAttendanceId());
                    Log.d(x.getName(), Integer.toString(x.getLastAttendanceId()));
            });

            listViewClassroomAll.setOnItemClickListener((parent, itemView, position, id) -> {
                TextView textView = (TextView) itemView;
                String classStr = textView.getText().toString();
                dashboardViewModel.selectClassroom(classStr);
                dashboardViewModel.loadClassroomInfo(classStr);
                dashboardViewModel.loadAttendanceInfo(getLastAttendanceId(classStr));

                ((DashboardActivity) requireActivity()).showClassroomInfoFragment();
            });
        });

    }

    private int getLastAttendanceId(String className) {
        return Objects.requireNonNull(this.classNamesAndLastAttendanceIds.get(className));
    }
}
