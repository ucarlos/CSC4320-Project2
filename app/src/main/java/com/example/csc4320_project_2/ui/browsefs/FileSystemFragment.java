package com.example.csc4320_project_2.ui.browsefs;

import android.media.audiofx.EnvironmentalReverb;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csc4320_project_2.R;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;


public class FileSystemFragment extends Fragment {

    // The File Manager will only display files with the following file extentions,
    // alongwith directories.
    // Basically, if you click a directory, it opens the contents of said directory.
    // There should be a option to go back (through .. on UNIX/Linux)
    private final String file_extention_list[] = {".mp3", ".m4a", ".aac",
            ".flac", ".ogg", ".opus"};

    private SlideshowViewModel slideshowViewModel;
    private RecyclerView recyclerView;
    private final String root_path = "/";
    private Thread filesystem_adapter_thread;

    private LinkedList<File> populate_root_directory(){
        LinkedList<File> temp = new LinkedList<File>();

        File directory = new File(root_path);
        if (!directory.isDirectory()){
            System.out.println("This is NOT a directory!");
            return null;
        }

        if (!directory.canRead()){
            System.out.println("Can't read that!");
        }

        if (!directory.exists())
            System.out.println("Also this file doesn't exist.");

        File[] list = directory.listFiles();

        if (list == null) return null;

        if (list.length < 1)
            return temp;
        else {
            for (File f: list)
                if (f.canRead() && !f.getName().contains("."))
                    temp.add(f);

        }

        Collections.sort(temp);

        return temp;

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filesystem, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);

        // Set up RecyclerView:

        LinkedList<File> list = populate_root_directory();
        File new_file = new File(root_path);
        FileSystemAdapter filesystem_adapter = new FileSystemAdapter(list, new_file);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        recyclerView = root.findViewById(R.id.FileSystemView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(filesystem_adapter);



        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


}