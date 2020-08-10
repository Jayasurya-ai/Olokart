package com.viba.olokartusers;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

        holder.bagValue.setText(product.get(position).getBagprice()+"₹");
        holder.date.setText(product.get(position).getDate());
        holder.ordertxt.setText(product.get(position).getOrderid());

        holder.optionmode = product.get(position).getOptionmode();
        holder.orderstr = product.get(position).getOrderid();
        holder.suid = product.get(position).getSuid();

    }

    @Override
    public int getItemCount() {
        return product.size();
    }


    public interface OnItemClickListener {
        void onItemClick(GetSet GetSet);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView product_name1, date, bagValue, noOfitems1, productPrice1, product_name2, noOfitems2, productPrice2,product_qua1,product_qua2,textuid,Homedelivery,Homedeliver1;
        String productName, suid, totalPricestr,productDimen;
        FirebaseAuth firebaseAuth;
        TextView ordertxt;
        DatabaseReference databaseReference;
        String optionmode;

        ViewGroup viewGroup;
        ProgressBar progressBar;
        Button deliveryStatus, seeall;
        String orderstr;
        private RecyclerView recyclerView;
        private DatabaseReference reference;
        ArrayList<GetSet> getSets;

        public HomeViewHolder(View itemView) {

            super(itemView);


            seeall = itemView.findViewById(R.id.order_seeall);

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
                    Toast.makeText(context, ""+orderstr, Toast.LENGTH_SHORT).show();

                }
            });
            deliveryStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.delivery_statuslayout, viewGroup, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setView(dialogView);

                    final AlertDialog alertDialog = builder.create();
                    Window window = alertDialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);

                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                    alertDialog.show();

                    final View  view2, view3, viewgray2, viewgray3;
                    final ImageView deliverying, delivered, deliveringImage, deliveredImage;
                    final TextView deliveryingtxt, deliveredtxt;
                    Button ok;

                    view2 = dialogView.findViewById(R.id.view2);
                    view3 = dialogView.findViewById(R.id.view3);
                    viewgray2 = dialogView.findViewById(R.id.viewgray2);
                    viewgray3 = dialogView.findViewById(R.id.viewgray3);
                    deliveredImage = dialogView.findViewById(R.id.manicongray);
                    deliveringImage = dialogView.findViewById(R.id.bikeicongray);
                    deliveryingtxt = dialogView.findViewById(R.id.startedDeltxt);
                    deliveredtxt = dialogView.findViewById(R.id.deliveredtxt);
                    ok = dialogView.findViewById(R.id.okStatusdel);

                    deliverying = dialogView.findViewById(R.id.bikeicon);
                    delivered = dialogView.findViewById(R.id.manicon);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    databaseReference.child(orderstr).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String state = dataSnapshot.child("state").getValue().toString();

                            if (state.equals("ongoing")) {
                                viewgray2.setVisibility(View.GONE);
                                deliveringImage.setVisibility(View.GONE);
                                view2.setVisibility(View.VISIBLE);
                                deliverying.setVisibility(View.VISIBLE);
                                deliveryingtxt.setTextColor(Color.GREEN);

                            }
                            else if (state.equals("past")) {
                                viewgray2.setVisibility(View.GONE);
                                deliveringImage.setVisibility(View.GONE);
                                viewgray3.setVisibility(View.GONE);
                                deliveredImage.setVisibility(View.GONE);
                                view2.setVisibility(View.VISIBLE);
                                deliverying.setVisibility(View.VISIBLE);
                                view3.setVisibility(View.VISIBLE);
                                delivered.setVisibility(View.VISIBLE);
                                deliveredtxt.setTextColor(Color.GREEN);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });


        }

                public void filterList(ArrayList<GetSet> filteredList) {
                    product = filteredList;
                    notifyDataSetChanged();
                }
            }


}

