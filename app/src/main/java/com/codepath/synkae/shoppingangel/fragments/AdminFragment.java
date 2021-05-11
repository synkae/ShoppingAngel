package com.codepath.synkae.shoppingangel.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.synkae.shoppingangel.R;
import com.codepath.synkae.shoppingangel.models.AdminAdapter;
import com.codepath.synkae.shoppingangel.models.Item;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {
    private ArrayList<Item> allItems;
    private RecyclerView rvAdminItems;
    private AdminAdapter adminAdapter;
    EditText etAdminItem;
    EditText etAdminPrice;
    Button btnAdd;
    ImageView ivAdminPhoto;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    Bitmap itemImageBitmap;
    private static final int REQUEST_CODE = 121;
    public final static int PICK_PHOTO_CODE = 1046;

    public static final String TAG = "AdminFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etAdminItem = view.findViewById(R.id.etAdminItem);
        etAdminPrice = view.findViewById(R.id.etAdminPrice);
        btnAdd = view.findViewById(R.id.btnAdd);
        ivAdminPhoto = view.findViewById(R.id.ivAdminPhoto);
        rvAdminItems = view.findViewById(R.id.rvAdminItems);
        allItems = new ArrayList<>();

        queryItems();
        requiredUserPermission();

        adminAdapter = new AdminAdapter(getActivity(), allItems);
        rvAdminItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAdminItems.setAdapter(adminAdapter);

        ivAdminPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGallery();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = null;
                Double price = null;

                if(!etAdminItem.getText().toString().equals("") && !etAdminPrice.getText().toString().equals("") && itemImageBitmap != null) {
                    itemName = etAdminItem.getText().toString();
                    price = Double.parseDouble(etAdminPrice.getText().toString());
                    DecimalFormat df = new DecimalFormat("#.##");
                    price = Double.valueOf(df.format(price));
                    try {
                        addAdminItem(itemName, price, itemImageBitmap);
                        queryItems();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requiredUserPermission(){
        if(!permissionAlreadyGranted()){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE);
        }
    }

    private boolean permissionAlreadyGranted() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        for (String permission : perms) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void launchGallery() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.codepath.synkae.shoppingangel.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Gallery
        if (data != null && requestCode == PICK_PHOTO_CODE && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            ivAdminPhoto.setImageURI(photoUri);
            //convert URI to Bitmap
            itemImageBitmap = null;
            try {
                itemImageBitmap = getBitmapFromUri(photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addAdminItem(String itemName, double price, Bitmap itemImageBitmap) throws ParseException {
        ParseObject adminItem = ParseObject.create("Item");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        itemImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] scaledData = stream.toByteArray();

        ParseFile image = new ParseFile(itemName + ".jpg", scaledData);
        image.save();
        /*
        image.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Image has been saved as a parse file.");
                } else {
                    //Failed to save the image as parse file.
                    Log.e(TAG, "Failed to save the image as parse file. Error: " + e);
                }
            }
        });
        */
        adminItem.put("itemName", itemName);
        adminItem.put("price", price);
        adminItem.put("itemImage", image);

        adminItem.save();
        /*
        adminItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success
                    Log.d(TAG, "Image has been successfully uploaded to Parse Server.");
                    Toast.makeText(getActivity(), itemName + " was successfully added", Toast.LENGTH_SHORT).show();
                } else {
                    // Error
                    Log.e(TAG, "Failed to upload the image to parse server. Error: " + e);
                    Toast.makeText(getActivity(), "Add admin item failed", Toast.LENGTH_SHORT).show();
                }
                //queryItems();
            }
        });
        */

        //queryItems();
        adminAdapter.notifyDataSetChanged();
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    protected void queryItems() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) {
                if (e != null){
                    Log.e(TAG, "SOMETHING WENT WRONG", e);
                    return;
                }
                allItems.clear();
                allItems.addAll(items);
                adminAdapter.notifyDataSetChanged();
            }
        });
    }
}