package de.boe_dev.mytasks.ui.tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.Firebase;

import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.model.Task;
import de.boe_dev.mytasks.ui.utils.Constants;

/**
 * Created by benny on 03.05.16.
 */
public class TasksFragment extends Fragment {

    private TasksAdapter mTasksAdapter;
    private ListView mListView;

    public static TasksFragment newInstance() {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_tasks);

        Firebase ref = new Firebase(Constants.FIREBASE_URL_TASKS);

        mTasksAdapter = new TasksAdapter(getActivity(), Task.class, R.layout.item_tasks, ref);
        mListView.setAdapter(mTasksAdapter);
        return rootView;
    }
}
