package com.learning.Clock_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    private final Fragment[] fragments = {
            new FragmentAlarms(),
            new FragmentStopper(),
            new FragmentTimer()
    };

    public FragmentAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    public Fragment getFragment(int position) {
        return fragments[position];
    }
}

