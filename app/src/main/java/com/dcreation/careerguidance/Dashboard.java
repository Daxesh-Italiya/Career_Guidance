package com.dcreation.careerguidance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dcreation.careerguidance.Function.AnswerData;
import com.dcreation.careerguidance.Function.New_Post_Data;
import com.dcreation.careerguidance.Function.Post_Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.PublicKey;
import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView Query, Post, PostNumber, AnswerNumber, LkeNumber, unlikenumber;
    private RecyclerView Recycler;
    private ArrayList<String> Query_List, Query_Id;
    private ArrayList<String> Post_List,Rev_Post_List;
    private ArrayList<String> Question_List,Rev_Question_List;
    private FirebaseAuth firebaseAuth;
    private Query_Adupter query_adupter;
    private Post_Adupter post_Adupter;
    private ProgressBar progressBar, progressBar3, progressBar4;
    private String Profession, UserId,UserName,UserPost;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button AddButton;
    private boolean query_flag = true, post_flag = false;
    private RelativeLayout NewPostPage;
    private int Like_count = 0, Unlike_count = 0, Post_count = 0, Answer_count = 0;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LinearLayout Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        navigationsetup();
        Define();
        GetQuery();
        GetData();
        GetPost();
        QueryClick(null);
    }

    private void GetData() {
        DatabaseReference UserDataReff = FirebaseDatabase.getInstance().getReference().child("Advisor").child(UserId);
        UserDataReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserName=snapshot.child("name").getValue().toString();
                UserPost=snapshot.child("profession").getValue().toString();
                if (snapshot.hasChild("like")) {
                    progressBar3.setVisibility(View.GONE);
                    int templike = Integer.parseInt(snapshot.child("like").getValue().toString());
                    Like_count = templike;
                    if (templike > 9000)
                        LkeNumber.setText("9K+");
                    else if (templike > 8000)
                        LkeNumber.setText("8K+");
                    else if (templike > 7000)
                        LkeNumber.setText("7K+");
                    else if (templike > 6000)
                        LkeNumber.setText("6K+");
                    else if (templike > 5000)
                        LkeNumber.setText("5K+");
                    else if (templike > 4000)
                        LkeNumber.setText("4K+");
                    else if (templike > 3000)
                        LkeNumber.setText("3K+");
                    else if (templike > 2000)
                        LkeNumber.setText("2K+");
                    else if (templike > 1000)
                        LkeNumber.setText("1K+");
                    else
                        LkeNumber.setText(String.valueOf(templike));
                } else {
                    progressBar3.setVisibility(View.GONE);
                    LkeNumber.setText("00");
                }

                if (snapshot.hasChild("unlike")) {
                    int templike = Integer.parseInt(snapshot.child("unlike").getValue().toString());
                    Unlike_count = templike;
                    if (templike > 9000)
                        unlikenumber.setText("9K+");
                    else if (templike > 8000)
                        unlikenumber.setText("8K+");
                    else if (templike > 7000)
                        unlikenumber.setText("7K+");
                    else if (templike > 6000)
                        unlikenumber.setText("6K+");
                    else if (templike > 5000)
                        unlikenumber.setText("5K+");
                    else if (templike > 4000)
                        unlikenumber.setText("4K+");
                    else if (templike > 3000)
                        unlikenumber.setText("3K+");
                    else if (templike > 2000)
                        unlikenumber.setText("2K+");
                    else if (templike > 1000)
                        unlikenumber.setText("1K+");
                    else
                        unlikenumber.setText(String.valueOf(templike));
                } else {
                    unlikenumber.setText("00");
                }

                if (snapshot.hasChild("post")) {
                    int templike = Integer.parseInt(snapshot.child("post").getValue().toString());
                    Post_count = templike;
                    if (templike > 9000)
                        PostNumber.setText("9K+");
                    else if (templike > 8000)
                        PostNumber.setText("8K+");
                    else if (templike > 7000)
                        PostNumber.setText("7K+");
                    else if (templike > 6000)
                        PostNumber.setText("6K+");
                    else if (templike > 5000)
                        PostNumber.setText("5K+");
                    else if (templike > 4000)
                        PostNumber.setText("4K+");
                    else if (templike > 3000)
                        PostNumber.setText("3K+");
                    else if (templike > 2000)
                        PostNumber.setText("2K+");
                    else if (templike > 1000)
                        PostNumber.setText("1K+");
                    else
                        PostNumber.setText(String.valueOf(templike));
                } else {
                    PostNumber.setText("00");
                }

                if (snapshot.hasChild("answer")) {
                    int templike = Integer.parseInt(snapshot.child("answer").getValue().toString());
                    Answer_count = templike;
                    if (templike > 9000)
                        AnswerNumber.setText("9K+");
                    else if (templike > 8000)
                        AnswerNumber.setText("8K+");
                    else if (templike > 7000)
                        AnswerNumber.setText("7K+");
                    else if (templike > 6000)
                        AnswerNumber.setText("6K+");
                    else if (templike > 5000)
                        AnswerNumber.setText("5K+");
                    else if (templike > 4000)
                        AnswerNumber.setText("4K+");
                    else if (templike > 3000)
                        AnswerNumber.setText("3K+");
                    else if (templike > 2000)
                        AnswerNumber.setText("2K+");
                    else if (templike > 1000)
                        AnswerNumber.setText("1K+");
                    else
                        AnswerNumber.setText(String.valueOf(templike));
                } else {
                    AnswerNumber.setText("00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void GetQuery() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        UserId = user.getUid();
        DatabaseReference PostCheckreff = FirebaseDatabase.getInstance().getReference().child("Query");
        PostCheckreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Question_List.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(!snapshot.hasChild(UserId)&&snapshot.hasChild("category")&&snapshot.hasChild("person_Id")&&snapshot.hasChild("query"))
                        Question_List.add(snapshot.getKey());
                }
                if (Question_List.size() == 0) {
                    Question_List.add("No Data");
                }
                Rev_Question_List.clear();
                for (int i =0 ;i<Question_List.size();i++)
                {
                    Rev_Question_List.add(Question_List.get(Question_List.size()-i-1));
                }
                if(query_flag)
                {
                    query_adupter = new Query_Adupter(Dashboard.this, Rev_Question_List);
                    Recycler.setAdapter(query_adupter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void GetPost() {

        DatabaseReference PostCheckreff = FirebaseDatabase.getInstance().getReference().child("Post");
        PostCheckreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post_List.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.hasChild("advisor_Id")&&snapshot.child("advisor_Id").getValue().equals(UserId))
                        Post_List.add(snapshot.getKey());
                }
                if (Post_List.size() == 0) {
                    Post_List.add("No Data");
                }
                Rev_Post_List.clear();
                for (int i =0 ;i<Post_List.size();i++)
                {
                    Rev_Post_List.add(Post_List.get(Post_List.size()-i-1));
                }
                if (post_flag) {
                    post_Adupter = new Post_Adupter(Dashboard.this, Rev_Post_List);
                    Recycler.setAdapter(post_Adupter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //    {
//        DatabaseReference Postreff = FirebaseDatabase.getInstance().getReference().child("Post").child(Profession).child(UserId);
//        Postreff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Post_List.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (snapshot.hasChild("topic")) {
//                        Post_Data data = new Post_Data();
//                        data.setTopic(snapshot.child("topic").getValue().toString());
//                        data.setOpinion(snapshot.child("opinion").getValue().toString());
//                        data.setLike(snapshot.child("like").getValue().toString());
//                        data.setDislike(snapshot.child("dislike").getValue().toString());
//                        data.setId(snapshot.getKey());
//                        Post_List.add(data);
//                    }
//                }
//                if (Post_List.size() == 0) {
//                    Post_Data data = new Post_Data();
//                    data.setLike("No Data");
//                    data.setDislike("No Data");
//                    data.setOpinion("No Data");
//                    data.setId("No Data");
//                    data.setTopic("No Data");
//                    Post_List.add(data);
//                }
//                if (post_flag) {
//                    post_Adupter = new Post_Adupter(Dashboard.this, Post_List);
//                    Recycler.setAdapter(post_Adupter);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    private void Define() {
        Rev_Post_List = new ArrayList<String>();
        Loading = (LinearLayout) findViewById(R.id.Loading);
        Loading.setVisibility(View.VISIBLE);

        Recycler = (RecyclerView) findViewById(R.id.Recycler);
        Recycler.setLayoutManager(new LinearLayoutManager(Dashboard.this));

        Query = (TextView) findViewById(R.id.Query_menu);
        Post = (TextView) findViewById(R.id.Post_menu);
        PostNumber = (TextView) findViewById(R.id.PostNumber);
        AnswerNumber = (TextView) findViewById(R.id.AnswerNumber);
        LkeNumber = (TextView) findViewById(R.id.LkeNumber);
        unlikenumber = (TextView) findViewById(R.id.unlikenumber);

        AddButton = (Button) findViewById(R.id.AddButton);

        NewPostPage = (RelativeLayout) findViewById(R.id.NewPostPage);
        NewPostPage.setVisibility(View.GONE);

        Query_List = new ArrayList<String>();
        Post_List = new ArrayList<String>();
        Query_Id = new ArrayList<String>();
        Question_List = new ArrayList<String>();
        Rev_Question_List = new ArrayList<String>();

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
        progressBar4.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));


        final FirebaseUser user = firebaseAuth.getCurrentUser();
        UserId = user.getUid();
    }

    public void QueryClick(View view) {
        query_flag = true;
        post_flag = false;
        Query.setBackgroundResource(R.color.colorPrimary);
        Query.setTextColor(Color.WHITE);
        Query.setTypeface(null, Typeface.BOLD);

        Post.setBackgroundColor(Color.WHITE);
        Post.setTextColor(Color.BLACK);
        Post.setTypeface(null, Typeface.NORMAL);

        AddButton.setVisibility(View.GONE);
        GetQuery();

        query_adupter = new Query_Adupter(Dashboard.this, Query_List);
        Recycler.setAdapter(query_adupter);

    }

    public void PostClick(View view) {
        query_flag = false;
        post_flag = true;
        Post.setBackgroundResource(R.color.colorPrimary);
        Post.setTextColor(Color.WHITE);
        Post.setTypeface(null, Typeface.BOLD);

        Query.setBackgroundColor(Color.WHITE);
        Query.setTextColor(Color.BLACK);
        Query.setTypeface(null, Typeface.NORMAL);

        AddButton.setVisibility(View.VISIBLE);

        post_Adupter = new Post_Adupter(Dashboard.this, Rev_Post_List);
        Recycler.setAdapter(post_Adupter);
    }

    public void DoNothing(View view) {

    }

    public void NewPostClick(View view) {
        NewPostPage.setVisibility(View.VISIBLE);
    }

    public void NewPostBackClick(View view) {
        NewPostPage.setVisibility(View.GONE);
    }

    public void newPostUpdate(View view) {
        if (progressBar4.getVisibility() == View.GONE) {
            final EditText NewPostTopic = (EditText) findViewById(R.id.NewPostTopic);
            final EditText NewpostDescription = (EditText) findViewById(R.id.NewpostDescription);
            if (NewPostTopic.getText().toString().isEmpty()) {
                Toast.makeText(Dashboard.this, "Please Enter Topic Name", Toast.LENGTH_LONG).show();
            } else if (NewpostDescription.getText().toString().isEmpty()) {
                Toast.makeText(Dashboard.this, "Please Enter Description", Toast.LENGTH_LONG).show();
            } else {
                progressBar4.setVisibility(View.VISIBLE);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                New_Post_Data data = new New_Post_Data();
                                data.setTopic(NewPostTopic.getText().toString());
                                data.setOpinion(NewpostDescription.getText().toString());
                                data.setLike("0");
                                data.setDislike("0");
                                data.setAdvisor_Id(UserId);
                                data.setProfession(Profession);
                                DatabaseReference Newpostreff = FirebaseDatabase.getInstance().getReference().child("Post");
                                Newpostreff.push().setValue(data);
                                DatabaseReference UserDataReff = FirebaseDatabase.getInstance().getReference().child("Advisor").child(UserId);
                                UserDataReff.child("post").setValue(String.valueOf(Post_count + 1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressBar4.setVisibility(View.GONE);
                                            NewPostPage.setVisibility(View.GONE);
                                            Toast.makeText(Dashboard.this, "New Post Added", Toast.LENGTH_LONG).show();
                                            NewPostTopic.setText("");
                                            NewpostDescription.setText("");
                                        } else {
                                            progressBar4.setVisibility(View.GONE);
                                            Toast.makeText(Dashboard.this, "Something Went Wrong, Please Try again later", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                progressBar4.setVisibility(View.GONE);
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setMessage("Are you sure you want to add this new post?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        }


    }

    public class Query_Adupter extends RecyclerView.Adapter<Query_Adupter.MyViewHolder> {
        Context context;
        ArrayList<String> List;

        public Query_Adupter(Context c, ArrayList<String> p) {
            context = c;
            List = p;
        }

        @NonNull
        @Override
        public Query_Adupter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Query_Adupter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.querycard, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final Query_Adupter.MyViewHolder holder, final int position) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
                Loading.setVisibility(View.GONE);
            }
            if (List.get(position).equals("No Data")) {
                holder.send.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                holder.answer.setVisibility(View.GONE);
                holder.close.setVisibility(View.GONE);
                holder.Query_item.setText("No Any Query Found");
            } else {
                holder.Postreff.child(List.get(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("category")&&snapshot.hasChild("person_Id")&&snapshot.hasChild("query"))
                        {
                            holder.Query_item.setText("Query " + (position + 1) + ": " + snapshot.child("query").getValue().toString());
                            if (snapshot.hasChild("answer"))
                            {
                                holder.Ans_fleg=true;
                                holder.Answer=snapshot.child("answer").getValue().toString();
                            }

                        }
                        else {
                            holder.send.setVisibility(View.GONE);
                            holder.cancel.setVisibility(View.GONE);
                            holder.answer.setVisibility(View.GONE);
                            holder.close.setVisibility(View.GONE);
                            holder.Query_item.setText("No Any Query Found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.btnback.setVisibility(View.GONE);
                holder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        DatabaseReference Colsereff = FirebaseDatabase.getInstance().getReference().child("Query").child(List.get(position));
                                        Colsereff.child(UserId).child("status").setValue("close");
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure ? ").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });

                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.btnback.setVisibility(View.GONE);
                        holder.answer.setText("");
                    }
                });


                holder.send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        DatabaseReference Colsereff = FirebaseDatabase.getInstance().getReference().child("Query").child(List.get(position));

                                        if(holder.Ans_fleg)
                                        {
                                            Colsereff.child("answer").setValue(holder.Answer+"\n\n"+UserName+"("+UserPost+") : "+holder.answer.getText().toString());
                                        }
                                        else
                                        {
                                            Colsereff.child("answer").setValue(UserName+"("+UserPost+") : "+holder.answer.getText().toString());
                                        }
                                        Colsereff.child(UserId).child("status").setValue("Answered");
                                        DatabaseReference answercounter = FirebaseDatabase.getInstance().getReference().child("Advisor").child(UserId).child("answer");
                                        answercounter.setValue(String.valueOf(Answer_count + 1));
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure ? ").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });

                holder.answer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (holder.answer.getText().toString().isEmpty()) {
                            holder.btnback.setVisibility(View.GONE);
                        } else {
                            holder.btnback.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return List.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView Query_item, send, cancel;
            EditText answer;
            ImageView close;
            String Answer;
            LinearLayout btnback;
            boolean Ans_fleg=false;
            DatabaseReference Postreff=FirebaseDatabase.getInstance().getReference().child("Query");
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                Query_item = (TextView) itemView.findViewById(R.id.Query_item);
                send = (TextView) itemView.findViewById(R.id.send);
                cancel = (TextView) itemView.findViewById(R.id.cancel);
                answer = (EditText) itemView.findViewById(R.id.answer);
                close = (ImageView) itemView.findViewById(R.id.close);
                btnback = (LinearLayout) itemView.findViewById(R.id.btnback);
            }
        }

    }

    public class Post_Adupter extends RecyclerView.Adapter<Post_Adupter.MyViewHolder> {
        Context context;
        ArrayList<String> arrayList;

        public Post_Adupter(Context c, ArrayList<String> p) {
            context = c;
            arrayList = p;
        }

        @NonNull
        @Override
        public Post_Adupter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Post_Adupter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.posr_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final Post_Adupter.MyViewHolder holder, final int position) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
                Loading.setVisibility(View.GONE);
            }

            if (arrayList.get(position).equals("No Data")) {
                holder.TopicText.setVisibility(View.GONE);
                holder.Topic.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                holder.Edit.setVisibility(View.GONE);
                holder.Remove.setVisibility(View.GONE);
                holder.update.setVisibility(View.GONE);
                holder.Newpost.setVisibility(View.GONE);
                holder.Like.setVisibility(View.GONE);
                holder.dislike.setVisibility(View.GONE);
                holder.likeback.setVisibility(View.GONE);
                holder.text.setText("No Any Post Found");
            } else {
                holder.Postreff.child(arrayList.get(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("topic")&&dataSnapshot.hasChild("opinion")&&dataSnapshot.hasChild("like")&&dataSnapshot.hasChild("dislike"))
                        {
                            holder.Topic.setText(dataSnapshot.child("topic").getValue().toString());
                            holder.text.setText(dataSnapshot.child("opinion").getValue().toString());
                            holder.Like.setText(dataSnapshot.child("like").getValue().toString());
                            holder.dislike.setText(dataSnapshot.child("dislike").getValue().toString());
                            holder.Edit.setVisibility(View.GONE);
                            holder.update.setVisibility(View.GONE);
                            holder.Remove.setVisibility(View.VISIBLE);
                            holder.Edit.setVisibility(View.VISIBLE);
                            holder.likeback.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            holder.TopicText.setVisibility(View.GONE);
                            holder.Topic.setVisibility(View.GONE);
                            holder.text.setVisibility(View.GONE);
                            holder.Edit.setVisibility(View.GONE);
                            holder.Remove.setVisibility(View.GONE);
                            holder.update.setVisibility(View.GONE);
                            holder.Newpost.setVisibility(View.GONE);
                            holder.Like.setVisibility(View.GONE);
                            holder.dislike.setVisibility(View.GONE);
                            holder.likeback.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




                holder.Remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        holder.Postreff.child(arrayList.get(position)).removeValue();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to delete this post?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });

                holder.Edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.Remove.setVisibility(View.GONE);
                        holder.Edit.setVisibility(View.GONE);
                        holder.Topic.setVisibility(View.GONE);
                        holder.text.setVisibility(View.GONE);
                        holder.Newpost.setText(holder.text.getText().toString());
                        holder.UpdateTopic.setText(holder.Topic.getText().toString());
                        holder.update.setVisibility(View.VISIBLE);
                        holder.Newpost.setVisibility(View.VISIBLE);
                        holder.UpdateTopic.setVisibility(View.VISIBLE);
                    }
                });

                holder.update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        if (holder.UpdateTopic.getText().toString().isEmpty()) {
                                            Toast.makeText(Dashboard.this, "Please Enter Topic Name", Toast.LENGTH_LONG).show();
                                        } else if (holder.Newpost.getText().toString().isEmpty()) {
                                            Toast.makeText(Dashboard.this, "Please Enter Description", Toast.LENGTH_LONG).show();
                                        } else {
                                            holder.progressBar2.setVisibility(View.VISIBLE);
                                            holder.update.setVisibility(View.GONE);
                                            holder.Postreff.child(arrayList.get(position)).child("opinion").setValue(holder.Newpost.getText().toString());
                                            holder.Postreff.child(arrayList.get(position)).child("topic").setValue(holder.UpdateTopic.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        holder.progressBar2.setVisibility(View.GONE);
                                                        holder.UpdateTopic.setVisibility(View.GONE);
                                                        holder.update.setVisibility(View.GONE);
                                                        holder.Newpost.setVisibility(View.GONE);
                                                        holder.text.setText(holder.Newpost.getText().toString());
                                                        holder.Topic.setText(holder.UpdateTopic.getText().toString());
                                                        holder.text.setVisibility(View.VISIBLE);
                                                        holder.Topic.setVisibility(View.VISIBLE);
                                                        holder.Remove.setVisibility(View.VISIBLE);
                                                        holder.Edit.setVisibility(View.VISIBLE);
                                                    } else {
                                                        holder.update.setVisibility(View.VISIBLE);
                                                        holder.progressBar2.setVisibility(View.GONE);
                                                        Toast.makeText(Dashboard.this, "Something Went Wrong, Please Try again later", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        }

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to Update this post?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });

            }

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView Topic, text, TopicText, Like, dislike;
            TextView Edit, Remove, update;
            EditText Newpost, UpdateTopic;
            ProgressBar progressBar2;
            LinearLayout likeback;
            DatabaseReference Postreff = FirebaseDatabase.getInstance().getReference().child("Post");

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                Topic = (TextView) itemView.findViewById(R.id.Topic);
                text = (TextView) itemView.findViewById(R.id.text);
                Edit = (TextView) itemView.findViewById(R.id.Edit);
                Like = (TextView) itemView.findViewById(R.id.Like);
                dislike = (TextView) itemView.findViewById(R.id.dislike);
                TopicText = (TextView) itemView.findViewById(R.id.TopicText);
                Remove = (TextView) itemView.findViewById(R.id.remove);
                update = (TextView) itemView.findViewById(R.id.update);
                Newpost = (EditText) itemView.findViewById(R.id.Newpost);
                UpdateTopic = (EditText) itemView.findViewById(R.id.UpdateTopic);
                progressBar2 = (ProgressBar) itemView.findViewById(R.id.progressBar2);
                likeback = (LinearLayout) itemView.findViewById(R.id.likeback);

            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setCheckable(true);
        String item_name = (String) item.getTitle();
        switch (item.getItemId()) {
            case R.id.home:
                break;
            case R.id.logOut:
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(user.getUid())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "Successful";
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(Dashboard.this, Dashboard.class));
                                    finish();
                                } else {
                                    Toast.makeText(Dashboard.this, "Something Wents to wrong Please Try Again Letter", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                firebaseAuth.signOut();
                startActivity(new Intent(Dashboard.this, MainActivity.class));
                finish();
                break;
            case R.id.feedback:

                Intent send = new Intent((Intent.ACTION_SEND));
                String[] Emailid = {"xabc3539@gmail.com"};
                send.putExtra(Intent.EXTRA_EMAIL, Emailid);
                send.putExtra((Intent.EXTRA_SUBJECT), "Feed back on " + getResources().getString(R.string.app_name) + " App");
                send.putExtra(Intent.EXTRA_TEXT, " ");
                send.setType("application/vnd.android.package-archive");
                startActivity(Intent.createChooser(send, "choose an email client"));
                break;

        }
        closeDrawer();
        return false;
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    //------------------------------------------------------------------------------------
    public void showDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void navigationsetup() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }
}
