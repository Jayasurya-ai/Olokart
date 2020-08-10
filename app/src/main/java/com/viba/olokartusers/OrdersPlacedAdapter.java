package com.viba.olokartusers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class OrdersPlacedAdapter extends RecyclerView.Adapter<OrdersPlacedAdapter.HomeViewHolder> {

    Context context;
    ArrayList<GetSet> product;
    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public OrdersPlacedAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public OrdersPlacedAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public OrdersPlacedAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.orderplacesrecyc,
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final OrdersPlacedAdapter.HomeViewHolder holder, final int position) {

        holder.bind(product.get(position), listener);

        holder.product_name.setText(product.get(position).getProduct_name());

        holder.productPrice.setText(product.get(position).getActual_price()+"₹");

        holder.noOfitems.setText(product.get(position).getTotal_quantity()+"x");

        holder.product_quan.setText(product.get(position).getProduct_quant());
//        holder.date.setText(product.get(position).getProduct_name());
//        holder.product_name1.setText(product.get(position).getProduct_name());



    }

    @Override
    public int getItemCount() {
        return product.size();
    }


    public interface OnItemClickListener {
        void onItemClick(GetSet GetSet);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, noOfitems, productPrice,product_quan;
        String productName, suid, totalPricestr, productDimen;
        FirebaseAuth firebaseAuth;
       // DatabaseReference databaseReference;
        ViewGroup viewGroup;
        ProgressBar progressBar;
        Button accept, decline;
        private RecyclerView recyclerView;
        private DatabaseReference reference;
        ArrayList<GetSet> getSets;
        OrderAdapter adapter;

        public HomeViewHolder(View itemView) {

            super(itemView);

            product_name = itemView.findViewById(R.id.i_name);

            productPrice = itemView.findViewById(R.id.prprice);
            product_quan = itemView.findViewById(R.id.pro_qua);
            viewGroup = itemView.findViewById(android.R.id.content);
            noOfitems=itemView.findViewById(R.id.no_items);



            firebaseAuth = FirebaseAuth.getInstance();

           // databaseReference = FirebaseDatabase.getInstance().getReference("Seller Orders").child(
                  //  firebaseAuth.getCurrentUser().getUid()).child("Home Delivery");

        }


        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {



        }
    }
    public void filterList(ArrayList<GetSet> filteredList) {
        product = filteredList;
        notifyDataSetChanged();
    }
}