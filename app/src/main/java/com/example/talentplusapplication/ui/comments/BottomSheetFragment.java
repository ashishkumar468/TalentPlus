package com.example.talentplusapplication.ui.comments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentplusapplication.Constants;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.BaseResponse;
import com.example.talentplusapplication.Proxy.CommentsDtoProxy;
import com.example.talentplusapplication.Proxy.ResponseGetAllComments;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetFragment extends BottomSheetDialogFragment implements CommentsBottomSheetAdapter.OnLoadCommentListener {

    private final TextView txt_Comments_count;
    private RecyclerView mRecyclerView;
    private String userId;
    private List<CommentsDtoProxy> commentsDtoProxyList;
    private TextView txt_CommentsTitle;
    private ImageButton imgBtn_Close;
    private ImageButton imgBtn_SaveComment;
    private ProgressBar progressBar;
    private Context mContext;
    private int postId;
    private EditText edtComment;
    private SharedPreferences mSharedPreferences;

    public BottomSheetFragment(int postId, TextView txt_comment_count) {
        this.postId = postId;
        this.txt_Comments_count=txt_comment_count;
        mContext = this.getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name, Context.MODE_PRIVATE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.comments_bottom_sheet_layout, container, false);

        mRecyclerView = mView.findViewById(R.id.rceyclerView_comments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        progressBar = mView.findViewById(R.id.comment_progressbar);
        imgBtn_SaveComment = mView.findViewById(R.id.imgBtn_sent_comment);
        edtComment = mView.findViewById(R.id.edt_comment);
        txt_CommentsTitle = mView.findViewById(R.id.txt_comments_title);
        imgBtn_Close = mView.findViewById(R.id.imgBtn_comments_close);

        userId = mSharedPreferences.getString(Constants.USER_ID, "");

        imgBtn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });
        imgBtn_SaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edtComment.getText().toString();

                if (!comment.equals("")) {

                    if (userId.equals("")){
                        onShowToast("Please login");
                    }else {

                        if (new Utility(MyApplication.getAppContext()).isConnectingToInternet()) {
                            onCallSaveComment(comment);
                        } else {
                            onShowToast("Internet connection is not available");
                        }
                    }
                }


            }
        });

        setCommentList();

        return mView;
    }

    private void onCallSaveComment(String comment) {

        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);

        JsonObject mJsonObject = new JsonObject();
        mJsonObject.addProperty("postsId", "" + postId);
        mJsonObject.addProperty("userId", userId);
        mJsonObject.addProperty("commentsDescription", comment);
//                Log.e("LoginActivity","String json " +mJsonObject.toString());

        mApiInterface.saveComment(mJsonObject).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals("200")) {

                        edtComment.setText("");

                        onShowToast(response.body().getDescription());
                        setCommentList();

                    } else {

                        onShowToast("Please upload video again");
//                        Intent mIntent=new Intent(UploadActivity.this, BottomNavigationActivity.class);
//                        startActivity(mIntent);
//                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                        Toast.makeText(MyApplication.getAppContext(),"Something is wrong",Toast.LENGTH_SHORT).show();

//                button.setEnabled(true);
                onShowToast("Something is wrong");


            }
        });


    }

    private void setCommentList() {



        if (new Utility(this.getContext()).isConnectingToInternet()) {
            onCallWebServicesGetAllComments();
        } else {
            onShowToast("Internet connection is not available");
        }
    }

    private void onCallWebServicesGetAllComments() {


//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(false);
//        dialog.setMessage("Please wait");
//        dialog.show();
        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);


        mApiInterface.getAllComments("" + postId).enqueue(new Callback<ResponseGetAllComments>() {
            @Override
            public void onResponse(Call<ResponseGetAllComments> call, Response<ResponseGetAllComments> response) {
//                    dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("NCF-006")) {

                        txt_CommentsTitle.setText("No comment");

                    } else if (response.body().getCode().equals("200")) {
//                        onShowToast(response.body().getDescription());
                        ResponseGetAllComments mResponseFollowers = response.body();
                        if (mResponseFollowers.getCommentsDto() != null) {
                            commentsDtoProxyList.add(mResponseFollowers.getCommentsDto());
                        } else {
                            commentsDtoProxyList = mResponseFollowers.getCommentsDtoList();
                        }


                        onSetData();


                    } else {
                        onShowToast(response.body().getDescription());
                        commentsDtoProxyList = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAllComments> call, Throwable t) {
                onShowToast("Something is wrong");
                commentsDtoProxyList = new ArrayList<>();

            }
        });

    }

    private void onSetData() {

        if (commentsDtoProxyList!=null){
            txt_CommentsTitle.setText(commentsDtoProxyList.size() + " " + "comments");

            txt_Comments_count.setText(""+commentsDtoProxyList.size());
            CommentsBottomSheetAdapter mCommentsBottomSheetAdapter = new CommentsBottomSheetAdapter(this, commentsDtoProxyList, this::onLoadThumbnailClick);
            mRecyclerView.setAdapter(mCommentsBottomSheetAdapter);
        }

    }

    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoadThumbnailClick(int position) {

    }
}
