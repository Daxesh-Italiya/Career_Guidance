package com.dcreation.careerguidance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.dcreation.careerguidance.Function.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private EditText MobileNumber, RgName, RgMobile, RgEmail, Otp, rgProfession, rgExperience;
    private TextView uploadtext;
    private CheckBox Tearm;
    private Uri ImageUri;
    final boolean[] fleg = {true};
    private Button Next, Back, Exit;
    private LinearLayout LoginLayout, RegisterLayout, OtpLayout,Loading;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth, mAuth;
    private String uid = "ABC", varificationId, Password;
    private ImageView photo;
    private boolean onetimefleg = true,fleg1=false;
    public  static  final int Rc_setting=123;
    private RecyclerView category_listview;
    private Category_Adupter category_adupter;
    ArrayList<String> vidiodetailsArrayList,filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Define();
        requesrpermittion();

        rgProfession.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(rgProfession.getText().toString().isEmpty())
                {
                    category_listview.setVisibility(View.GONE);
                }
                else {
                    category_listview.setVisibility(View.VISIBLE);
                    filter.removeAll(filter);
                    for (int i = 0; i < vidiodetailsArrayList.size(); i++) {
                        String p = rgProfession.getText().toString();
                        if (vidiodetailsArrayList.get(i).toLowerCase().contains(p.toLowerCase())) {
                            filter.add(vidiodetailsArrayList.get(i));
                        }
                    }
                    category_adupter = new Category_Adupter(MainActivity.this, filter);
                    category_listview.setAdapter(category_adupter);
                }

            }
        });

    }

    @AfterPermissionGranted(Rc_setting)
    private void requesrpermittion(){
        String[] per={android.Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_NETWORK_STATE };
        if(EasyPermissions.hasPermissions(this,per))
        {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                startActivity(new Intent(MainActivity.this,Dashboard.class));
                finish();
            }
            Next.setVisibility(View.GONE);
            Next.setVisibility(View.VISIBLE);
            Exit.setVisibility(View.VISIBLE);
            Back.setVisibility(View.GONE);
            GetCategory();
        }
        else
        {

            EasyPermissions.requestPermissions(this,"This app needs to access your Storage",Rc_setting,per);
        }

    }
    private void Define() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading....");
        progressDialog.setMessage("Please Wait while we are Loading...!");
        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        MobileNumber = (EditText) findViewById(R.id.usermobilenumber);
        RgName = (EditText) findViewById(R.id.rgusername);
        RgMobile = (EditText) findViewById(R.id.rgmobilenumber);
        RgEmail = (EditText) findViewById(R.id.rgEmail);
        Otp = (EditText) findViewById(R.id.otp);
        rgProfession = (EditText) findViewById(R.id.rgProfession);

        rgExperience = (EditText) findViewById(R.id.rgExperience);
        Tearm = (CheckBox) findViewById(R.id.checkBox);
        Next = (Button) findViewById(R.id.rgnext);
        Back = (Button) findViewById(R.id.rgBack);
        Exit = (Button) findViewById(R.id.rgexit);
        LoginLayout = (LinearLayout) findViewById(R.id.loginlayout);
        RegisterLayout = (LinearLayout) findViewById(R.id.userDatalayout);
        OtpLayout = (LinearLayout) findViewById(R.id.otplayout);
        Loading = (LinearLayout) findViewById(R.id.Loading);
        photo = (ImageView) findViewById(R.id.photo);
        uploadtext = (TextView) findViewById(R.id.uploadtext);
        uploadtext.setVisibility(View.VISIBLE);
        Loading.setVisibility(View.GONE);
        category_listview=(RecyclerView) findViewById(R.id.category_list);
        category_listview.setVisibility(View.GONE);
        vidiodetailsArrayList=new ArrayList<>();
        filter=new ArrayList<>();
        category_listview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    //---------------------------------------- On Next Click ---------------------------------------

    public void OnNext(View view) {
        if (OtpLayout.getVisibility() == View.VISIBLE) {
            if (Otp.getText().toString().trim().isEmpty()) {
                Toast.makeText(MainActivity.this, "Pleas Enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                verifycode(Otp.getText().toString());
            }
        } else if (RegisterLayout.getVisibility() == View.VISIBLE) {
            if (varification()) {
                sendVerificationCode("+91" + RgMobile.getText().toString());
                RegisterLayout.setVisibility(View.GONE);
                OtpLayout.setVisibility(View.VISIBLE);
                Next.setVisibility(View.VISIBLE);
                Exit.setVisibility(View.GONE);
                Back.setVisibility(View.VISIBLE);
            }
        } else {
            if (MobileNumber.getText().length() != 10) {
                Toast.makeText(MainActivity.this, "Pleas Enter Valid 10 Digite Mobile number", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();

                Query query = FirebaseDatabase.getInstance().getReference().child("Advisor");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            RegisterLayout.setVisibility(View.GONE);
                            if (snapshot.hasChild("mobileNummber")) {
                                if (snapshot.child("mobileNummber").getValue().toString().equals(MobileNumber.getText().toString().trim())) {
                                    if (onetimefleg) {
                                        sendVerificationCode("+91" + MobileNumber.getText().toString().trim());
                                        LoginLayout.setVisibility(View.GONE);
                                        OtpLayout.setVisibility(View.VISIBLE);
                                        onetimefleg = false;
                                        fleg[0] = false;
                                    }

                                }

                            }
                        }
                        progressDialog.dismiss();
                        if (fleg[0]) {
                            LoginLayout.setVisibility(View.GONE);
                            RegisterLayout.setVisibility(View.VISIBLE);
                            Next.setVisibility(View.VISIBLE);
                            Exit.setVisibility(View.GONE);
                            onetimefleg = false;
                            Back.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    //-------------------------------------------- On back click -----------------------------------
    public void OnBackClick(View view) {
        if (OtpLayout.getVisibility() == View.VISIBLE) {
            OtpLayout.setVisibility(View.GONE);
            RegisterLayout.setVisibility(View.VISIBLE);
        } else if (RegisterLayout.getVisibility() == View.VISIBLE) {
            RegisterLayout.setVisibility(View.GONE);
            LoginLayout.setVisibility(View.VISIBLE);
        }

    }


    private void GetCategory(){
        DatabaseReference Categoryreff=FirebaseDatabase.getInstance().getReference().child("Category List");
        Categoryreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vidiodetailsArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    vidiodetailsArrayList.add(snapshot.getKey());
                }
                category_adupter = new Category_Adupter(MainActivity.this, vidiodetailsArrayList);
                category_listview.setAdapter(category_adupter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private boolean varification() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if(!fleg1)
        {
            Toast.makeText(MainActivity.this, "Pleas Upload Image ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (RgName.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Pleas Enter Valid User Name ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (RgMobile.getText().toString().length() != 10) {
            Toast.makeText(MainActivity.this, "Pleas Enter Valid 10 Digite Mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!RgEmail.getText().toString().trim().matches(emailPattern)) {
            Toast.makeText(MainActivity.this, "Pleas Enter Valid Email Id Mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (rgExperience.getText().toString().isEmpty()|| Integer.parseInt(rgExperience.getText().toString()) < 0 || Integer.parseInt(rgExperience.getText().toString())>80) {
            Toast.makeText(MainActivity.this, "Pleas Enter valid Experience ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (rgProfession.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Pleas Enter Profession", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Tearm.isChecked()) {
            Toast.makeText(MainActivity.this, "Pleas Checked Term & condition box", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    public void SelectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
        {
            Uri uri = data.getData();
            try {

                if(uri!=null)
                {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    photo.setImageBitmap(bitmap);
                    uploadtext.setVisibility(View.GONE);
                    ImageUri = uri;
                    fleg1 = true;
                }

            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        }


    }
    //----------------------------------------- If no Exist --------------------------------------------


    //======================================== Registration portion ====================================

    public void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mcallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mcallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressDialog.dismiss();
            varificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                Otp.setText("");
                Otp.setText(String.valueOf(code));
                Loading.setVisibility(View.VISIBLE);
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(varificationId, code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        final boolean[] fleg1 = {true};
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    datasend();
                } else {
                    if (fleg1[0])
                        Toast.makeText(MainActivity.this, "Registration is not Complete Make sure otp is right", Toast.LENGTH_LONG).show();
                }
                fleg1[0] = false;
            }
        });
    }

    public void datasend() {

        if (fleg[0]) {
            progressDialog.setTitle("Checking your data....");
            progressDialog.show();
            AnstronCoreHelper coreHelper;
            coreHelper=new AnstronCoreHelper(this);
            progressDialog.show();
            progressDialog.setTitle("Uploading Profile Image");
//            File file=new File(SiliCompressor.with(this)
//                    .compress(FileUtils.getPath(this,ImageUri),new File(this.getCacheDir(),"temp")));
//            Uri uri1=Uri.fromFile(file);
            StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("All Images").child(rgProfession.getText().toString());
            final StorageReference imageName = imageFolder.child(RgName.getText().toString());
            imageName.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
                            uid = firebaseUser.getUid();
                            final UserData user = new UserData();
                            user.setName(RgName.getText().toString());
                            user.setEmailId(RgEmail.getText().toString().trim());
                            user.setMobileNummber(RgMobile.getText().toString().trim());
                            final String token = FirebaseInstanceId.getInstance().getToken();
                            user.setToken(token);
                            user.setImageUri(String.valueOf(uri));
                            user.setExperience(rgExperience.getText().toString());
                            user.setProfession(rgProfession.getText().toString());
                            DatabaseReference categoryreff=FirebaseDatabase.getInstance().getReference().child("Category List");
                            categoryreff.child(rgProfession.getText().toString()).setValue("fill");
                            myRef = FirebaseDatabase.getInstance().getReference();
                            myRef.child("Advisor").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        //  startActivity(new Intent(Login.this,portionList.class));
                                        Notification();
                                        String Temp=firebaseUser.getUid()+"Advisor";
                                        FirebaseMessaging.getInstance().subscribeToTopic(Temp)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        String msg = "Successful";
                                                        if (task.isSuccessful()) {
                                                            startActivity(new Intent(MainActivity.this, Dashboard.class));
                                                            finish();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(MainActivity.this,"Something Wents to wrong Please Try Again Letter",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }
                                }
                            });
                        }
                    });
                }
            });

        } else {
            Toast.makeText(MainActivity.this, "Now You are Login", Toast.LENGTH_LONG).show();
            myRef = FirebaseDatabase.getInstance().getReference();
            startActivity(new Intent(MainActivity.this, Dashboard.class));
            finish();
        }

    }
    //----------------------------------------- On Exit Click --------------------------------------

    public void OnExit(View view) {
        finish();

    }

    private void Notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setContentTitle(getResources().getString(R.string.app_name))
                .setSmallIcon(R.drawable.notificationicon)
                .setColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notificationsound))
                .setContentText("Welcome to "+getResources().getString(R.string.app_name));
        builder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(99, builder.build());
    }

    public class Category_Adupter extends RecyclerView.Adapter<Category_Adupter.MyViewHolder>{

        Context context;
        ArrayList<String > List;

        public Category_Adupter(Context c, ArrayList<String> p) {
            context = c;
            List = p;
        }

        @NonNull
        @Override
        public Category_Adupter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Category_Adupter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.suatom_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final Category_Adupter.MyViewHolder holder, final int position) {
            holder.Title.setText(List.get(position));
            holder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rgProfession.setText(holder.Title.getText().toString());
                    category_listview.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return List.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView Title;
            LinearLayout back;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                Title = (TextView) itemView.findViewById(R.id.Title);
                back = (LinearLayout) itemView.findViewById(R.id.back);
            }
        }


    }


}
