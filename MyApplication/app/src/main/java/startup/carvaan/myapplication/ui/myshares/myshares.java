package startup.carvaan.myapplication.ui.myshares;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import startup.carvaan.myapplication.R;
import startup.carvaan.myapplication.ui.about.AboutShare;
import startup.carvaan.myapplication.ui.user.User;


public class myshares extends Fragment {

    private RecyclerView allShareRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore ff;
    private User user=new User();
    public myshares() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_allshares, container, false);
        User user=new User();
        allShareRecyclerView=view.findViewById(R.id.allShareRecyclerView);
        ff=FirebaseFirestore.getInstance();
        Query query=ff.collection("Users").document(user.getUser().getUid()).collection("myshares");
        FirestoreRecyclerOptions<mysharemodel> options = new FirestoreRecyclerOptions.Builder<mysharemodel>().setQuery(query, mysharemodel.class).build();
        adapter= new FirestoreRecyclerAdapter<mysharemodel, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_myshare,parent,false);
                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PostViewHolder postViewHolder, int i, mysharemodel mysharemodel) {
                postViewHolder.shareName.setText(user.getEmail());
                postViewHolder.holdings.setText(mysharemodel.getHoldings());
                postViewHolder.aboutShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().startActivity(new Intent(getContext(), AboutShare.class).putExtra("shareid",mysharemodel.getId()));
                    }
                });
            }
        };
        allShareRecyclerView.setAdapter(adapter);
        allShareRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    public class PostViewHolder extends RecyclerView.ViewHolder {
        private Button aboutShare;
        private TextView shareName,holdings;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            shareName=itemView.findViewById(R.id.sharename);
            aboutShare=itemView.findViewById(R.id.trade);
            holdings=itemView.findViewById(R.id.myHoldings);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}