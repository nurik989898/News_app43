package com.example.news_app43;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.news_app43.databinding.FragmentNewsBinding;
import com.example.news_app43.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menuu, menu);
        menu.removeItem(R.id.Cleanone);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Cleanone) {
            MainActivity.prefs.clearPreferences();
            binding.editTextProfile.setText(MainActivity.prefs.getText());
            if (MainActivity.prefs.getPic() != null) {
                uri = Uri.parse(MainActivity.prefs.getPic());
                Glide.with(binding.profileImage).load(uri).circleCrop().into(binding.profileImage);
            }
            return true;
        }
        return false;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.prefs = new Prefs(requireContext());
        binding.editTextProfile.setText(MainActivity.prefs.getText());
        if (MainActivity.prefs.getPic() != null){
            Glide.with(binding.profileImage).load(MainActivity.prefs.getPic()).circleCrop().into(binding.profileImage);
        }
        SaveText();
        ImageView imageView = view.findViewById(R.id.profileImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_secondProfileFragment);
            }
        });
    }


    private void SaveText(){
        binding.editTextProfile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            MainActivity.prefs.saveEditText(editable.toString());
            }
        });
    }
}