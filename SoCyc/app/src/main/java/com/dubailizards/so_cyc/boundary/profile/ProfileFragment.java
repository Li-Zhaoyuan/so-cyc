package com.dubailizards.so_cyc.boundary.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.dubailizards.so_cyc.R;
import com.dubailizards.so_cyc.boundary.login.Login;
import com.dubailizards.so_cyc.databinding.FragmentProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

/**
 *  Boundary Class, Fragment of the BaseActivity UI that represents the Profile Screen
 *  Displays the user's profile details and enables logout
 */
public class ProfileFragment extends Fragment {

    /**
     *  private ProfileViewModel variable
     *  Object that holds logical code relating to this fragment
     */
    private ProfileViewModel profileViewModel;

    /**
     *  private FragmentProfileBinding variable
     *  Auto generated class type that represents the binding between XML layout file and data objects
     */
    private FragmentProfileBinding binding;

    // Muh UI elems TODO: Better comment
    Button btn_logout;
    TextView txt_displayname;
    TextView txt_email;
    ImageView img_user;

    /**
     *  protected void function, Overridden Constructor of a Fragment
     *  Initializes the Fragment, and sets up necessary parameters
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        // Get the view where UI entities are stored
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Set up my entities in the view
        SetupUIEntities(view);
        // Set up profile data
        GetProfileData(GoogleSignIn.getLastSignedInAccount(getActivity()));
        // Setup logout
        SetupLogout();
        // Return the fragment's view
        return view;
    }

    /**
     *  private void function, Setup UI Entities
     *  @param view is the view where the entities are stored in
     *  Sets up UI entities by finding them from the fragment's view
     */
    private void SetupUIEntities(View view){
        btn_logout = view.findViewById(R.id.btn_logout);
        txt_displayname = view.findViewById(R.id.txt_displayname);
        txt_email = view.findViewById(R.id.txt_email);
        img_user = view.findViewById(R.id.img_user);
    }

    /**
     *  private void function, Logout button logic
     *  Sets up a listener to handle the logic of the logout button
     */
    private void SetupLogout(){
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class); // The activity is the passed context
                startActivity(intent);
            }
        });
    }

    /**
     *  private void function, Gets User Profile Details
     *  @param account is the last signed in google account
     *  Makes use of google sign in and gets the user's details
     *  Assigns the user details to UI elements
     */
    public void GetProfileData(GoogleSignInAccount account){
        if(account != null){
            txt_displayname.setText(account.getDisplayName());
            txt_email.setText(account.getEmail());
            String url_profileimg = account.getPhotoUrl().toString();
            Glide.with(getActivity()).load(url_profileimg).into(img_user);
        }
    }

    /**
     *  protected void function, Overridden Destructor of a Fragment
     *  Terminates the Fragment, deallocating variables accordingly
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}