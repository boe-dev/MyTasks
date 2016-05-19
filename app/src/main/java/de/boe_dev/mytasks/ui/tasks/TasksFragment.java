package de.boe_dev.mytasks.ui.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;

import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.taskDetail.TaskDetailFragment;
import model.Task;
import de.boe_dev.mytasks.ui.taskDetail.TaskDetailActivity;
import utils.Constants;

/**
 * Created by benny on 03.05.16.
 */
public class TasksFragment extends Fragment {

    private TasksAdapter mTasksAdapter;
    private ListView mListView;
    private boolean mTwoPane;

    public static TasksFragment newInstance() {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_tasks);
        if (rootView.findViewById(R.id.task_detail_container) != null) {
            mTwoPane = true;
        }

        Firebase ref = new Firebase(Constants.FIREBASE_URL_TASKS);

        mTasksAdapter = new TasksAdapter(getActivity(), Task.class, R.layout.item_tasks, ref);
        mListView.setAdapter(mTasksAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task taskList = mTasksAdapter.getItem(position);
                if (taskList != null) {
                    String listId = mTasksAdapter.getRef(position).getKey();
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(Constants.KEY_LIST_ID, listId);
                        TaskDetailFragment fragment = new TaskDetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.task_detail_container, fragment)
                                .commit();
                    } else {
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        intent.putExtra(Constants.KEY_LIST_ID, listId);
                        startActivity(intent);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTasksAdapter.cleanup();
    }
}
