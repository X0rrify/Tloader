package com.tloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


public class ImageFragment extends Fragment implements ImageDownloaderThread.OnResponseListener {

    public static final String TAG = ImageFragment.class.getName();
    private static final String ARG_AVATAR_URLS = "AVATAR_URLS";

    private ArrayList<String> chosenAvatarUrls;

    private GridView imagesGrid;

    public ImageFragment() {
        // Required empty public constructor.
    }

    public static ImageFragment newInstance(ArrayList chosenAvatarUrls) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ARG_AVATAR_URLS, chosenAvatarUrls);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chosenAvatarUrls = getArguments().getStringArrayList(ARG_AVATAR_URLS);
            ImageDownloaderThread imageDownloaderThread = new ImageDownloaderThread(chosenAvatarUrls);
            imageDownloaderThread.onResponseListener = this;
            imageDownloaderThread.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);
        imagesGrid = (GridView) view.findViewById(R.id.gridview_images);
        return view;
    }

    @Override
    public void onImagesInteractionComplete(String[] imagePaths) {
        imagesGrid.setAdapter(new ImageAdapter(getContext(), imagePaths));
    }
}
