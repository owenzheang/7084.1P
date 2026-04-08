package com.example.a70841p;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.DatePickerDialog;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_add_event#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_add_event extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_add_event() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_add_event.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_add_event newInstance(String param1, String param2) {
        fragment_add_event fragment = new fragment_add_event();
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
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        EditText etTitle = view.findViewById(R.id.eventTitle);
        Spinner spCategory1 = view.findViewById(R.id.category1);
        Spinner spCategory2 = view.findViewById(R.id.category2);
        EditText etLocation = view.findViewById(R.id.location);
        Button btnDate = view.findViewById(R.id.selectDate);
        Button btnTime = view.findViewById(R.id.selectTime);
        Button btnSave = view.findViewById(R.id.save);

        String[] categoryList1 = {"Work", "Social", "Travel", "Personal"};
        String[] categoryList2 = {"Important", "Normal", "Family", "Friends"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryList1
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory1.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryList2
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory2.setAdapter(adapter2);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String cat1 = spCategory1.getSelectedItem().toString();
            String cat2 = spCategory2.getSelectedItem().toString();
            String location = etLocation.getText().toString().trim();
            String date = btnDate.getText().toString();
            String time = btnTime.getText().toString();

            if (title.isEmpty()) {
                Toast.makeText(getContext(), "Title is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (date.equals("Select Date")) {
                Toast.makeText(getContext(), "Date is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if(time.equals("Select Time")){
                Toast.makeText(getContext(),"Time is required", Toast.LENGTH_SHORT).show();
                return;
            }

            String category = cat1 + " - " + cat2;
            String dateTime = date + " " + time;

            if(EventRepository.selectedEvent != null){
                Event event = EventRepository.selectedEvent;

                etTitle.setText((event.getTitle()));
                etLocation.setText((event.getLocation()));
                btnSave.setText("Update");
            }

            if(EventRepository.selectedEvent == null) {
                Event newEvent = new Event(title, category, location, dateTime);
                MainActivity.database.eventDao().insert(newEvent);
                Toast.makeText(getContext(), "Event saved", Toast.LENGTH_SHORT).show();

            }else{
                Event event = EventRepository.selectedEvent;
                event.setTitle(title);
                event.setCategory(category);
                event.setLocation(location);
                event.setDateTime(dateTime);

                MainActivity.database.eventDao().update(event);
                Toast.makeText(getContext(),"Event update", Toast.LENGTH_SHORT).show();
                EventRepository.selectedEvent = null;

            }
            Toast.makeText(getContext(), "Save clicked", Toast.LENGTH_SHORT).show();

            NavHostFragment.findNavController(this).navigate(R.id.navigation_events);
        });

        btnDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String dateText = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        btnDate.setText(dateText);
                    },
                    year, month, day
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        btnTime.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(),
                    (view12, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);

                        btnTime.setText(time);
                    },
                    hour, minute, true
            );

            timePickerDialog.show();
        });

        return view;
    }
}