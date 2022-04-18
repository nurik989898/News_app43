package com.example.news_app43.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.news_app43.NewsAdaptor;
import com.example.news_app43.OnItemClickListener;
import com.example.news_app43.R;
import com.example.news_app43.databinding.FragmentHomeBinding;
import com.example.news_app43.models.Article;

public class HomeFragment extends Fragment {
    private NewsAdaptor adaptor;
    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adaptor = new NewsAdaptor();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.FAcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment();
            }
        });
        getParentFragmentManager().setFragmentResultListener("rk_news", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Article article = (Article) result.getSerializable("article");
                Log.e("Home","result = " + article.getText());
                adaptor.addItem(article);
            }
        });
        binding.reciclerView.setAdapter(adaptor);
        adaptor.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Article article = adaptor.getItem(position);
                Toast.makeText(requireContext(),article.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int position) {

            }

            @Override
            public void itemClick() {

            }
        });

    }

    private void openFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.newsFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}