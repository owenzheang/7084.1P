package com.example.a70841p;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_event_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_event_list extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_event_list() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_event_list.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_event_list newInstance(String param1, String param2) {
        fragment_event_list fragment = new fragment_event_list();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadEvents();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadEvents();
    }

    private void loadEvents() {
        eventList = MainActivity.database.eventDao().getAllEvents();
        adapter = new EventAdapter(
                eventList,
                position -> {
                    Event event = eventList.get(position);
                    MainActivity.database.eventDao().delete(event);
                    eventList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                },
                position -> {
                    EventRepository.selectedEvent = eventList.get(position);
                    NavHostFragment.findNavController(this).navigate(R.id.navigation_add);
                }
        );
        recyclerView.setAdapter(adapter);
    }
}