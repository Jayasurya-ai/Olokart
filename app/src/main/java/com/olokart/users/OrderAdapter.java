package com.olokart.users;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.HomeViewHolder> {

    Context context;
    ArrayList<GetSet> product;
    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public OrderAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public OrderAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public OrderAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.neworders_layout,
                parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final OrderAdapter.HomeViewHolder holder, final int position) {

        holder.bind(product.get(position), listener);


        holder.bagValue.setText("₹"+product.get(position).getBagprice());
        holder.date.setText(product.get(position).getDate());
        holder.ordertxt.setText(product.get(position).getOrderid());
        holder.order = product.get(position).getOrder();
        holder.datestr = product.get(position).getDate();
        holder.optionmode = product.get(position).getOptionmode();
        holder.orderstr = product.get(position).getOrderid();
        holder.suid = product.get(position).getSuid();

        if (holder.order.equals("cancelled")){
            holder.seeall.setVisibility(View.GONE);
            holder.deliveryStatus.setVisibility(View.GONE);
            holder.orderCancel.setVisibility(View.VISIBLE);


        }
    }

    @Override
    public int getItemCount() {
        return product.size();
    }


    public interface OnItemClickListener {
        void onItemClick(GetSet GetSet);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView date, bagValue;
        String suid;
        FirebaseAuth firebaseAuth;
        TextView ordertxt, orderCancel;
        DatabaseReference databaseReference;
        String optionmode, datestr, order;
        ViewGroup viewGroup;
        Button deliveryStatus;
        TextView seeall;
        String orderstr;

        public HomeViewHolder(View itemView) {

            super(itemView);


            seeall = itemView.findViewById(R.id.order_seeall);

            orderCancel = itemView.findViewById(R.id.cancelOrdertxt);
            ordertxt = itemView.findViewById(R.id.newHomeorderid);
            bagValue = itemView.findViewById(R.id.bag_value);
            deliveryStatus = itemView.findViewById(R.id.deliveryStatus);
            date = itemView.findViewById(R.id.order_date);
            viewGroup = itemView.findViewById(android.R.id.content);


            firebaseAuth = FirebaseAuth.getInstance();

            databaseReference = FirebaseDatabase.getInstance().getReference("User Orders").child(
                    firebaseAuth.getCurrentUser().getUid());

        }



        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {

            seeall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   context.startActivity(new Intent(context,AllProducts.class).putExtra("orderid", orderstr ).putExtra("suid", suid).putExtra("optionmode", optionmode));
                   // Toast.makeText(context, ""+orderstr, Toast.LENGTH_SHORT).show();

                }
            });
            deliveryStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        View dialogView = LayoutInflater.from(context).inflate(R.layout.deliverytrack_layout, viewGroup, false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);

                        builder.setView(dialogView);

                        final AlertDialog alertDialog = builder.create();
                        Window window = alertDialog.getWindow();
                        //  window.setGravity(Gravity.BOTTOM);

                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                        alertDialog.show();

                        // final View  view2, view3, viewgray2, viewgray3;
                        final ImageView accepted, outofDelivery, delivered;
                        final TextView orderplacedtxt;
                        ImageView ok;
                        ok = dialogView.findViewById(R.id.backDelivery);

                        orderplacedtxt = dialogView.findViewById(R.id.placestext);

                        orderplacedtxt.setText("Your order " + orderstr + "\nwas placed on " + datestr + " ");
                        accepted = dialogView.findViewById(R.id.tickc);
                        outofDelivery = dialogView.findViewById(R.id.tickd);
                        delivered = dialogView.findViewById(R.id.ticke);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        databaseReference.child(orderstr).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try {

                                    String state = dataSnapshot.child("state").getValue().toString();

                                    if (state.equals("ongoing")) {
                                        accepted.setVisibility(View.VISIBLE);
                                        outofDelivery.setVisibility(View.VISIBLE);

                                    } else if (state.equals("past")) {
                                        accepted.setVisibility(View.VISIBLE);
                                        outofDelivery.setVisibility(View.VISIBLE);
                                        delivered.setVisibility(View.VISIBLE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }

                public void filterList(ArrayList<GetSet> filteredList) {
                    product = filteredList;
                    notifyDataSetChanged();
                }
            }


}

