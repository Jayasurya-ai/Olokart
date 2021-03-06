package com.olokart.users;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class userAdapter extends RecyclerView.Adapter<userAdapter.HomeViewHolder> {

    Context context;
    ArrayList<GetSet> product;

    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public userAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public userAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public userAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // notifyDataSetChanged();
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.userproducts,
                parent, false));


    }


    @Override
    public void onBindViewHolder(@NonNull final userAdapter.HomeViewHolder holder, int position) {

        holder.bind(product.get(position), listener);
        holder.shop_name.setText(product.get(position).getSname());
        holder.latsel = product.get(position).getSlat();
        holder.longsel = product.get(position).getSlong();
        holder.deliveryop=product.get(position).getDelivery();
        holder.suid = product.get(position).getSuid();
        holder.suid = product.get(position).getSuid();

        holder.sellerRef.child(holder.suid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.sname = dataSnapshot.child("sname").getValue().toString();
                holder.scity = dataSnapshot.child("scity").getValue().toString();
                holder.simage = dataSnapshot.child("simage").getValue().toString();
                holder.deliveryop=dataSnapshot.child("delivery").getValue().toString();

                holder.shop_name.setText(holder.sname);
                holder.shop_city.setText(holder.scity);
                holder.delivery.setText(holder.deliveryop);
                Picasso.get().load(holder.simage).placeholder(R.drawable.open)
                        .error(R.drawable.open).into(holder.imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try {

            holder.userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String latstr1 = dataSnapshot.child("ulat").getValue().toString();
                    String lonstr1 = dataSnapshot.child("ulong").getValue().toString();

                    final Double lat2 = Double.parseDouble(latstr1);
                    final Double lon2 = Double.parseDouble(lonstr1);

                    double latDistance = Math.toRadians(Double.parseDouble(holder.latsel) -lat2);
                    double lngDistance = Math.toRadians(Double.parseDouble(holder.longsel) - lon2);

                    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                            + Math.cos(Math.toRadians(Double.parseDouble(holder.latsel))) * Math.cos(Math.toRadians(lat2))
                            * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                    Double r= (double) (15000.00 * c);
//                    rlon1 = Math.toRadians(Double.parseDouble(holder.longsel));
//                    rlon2 = Math.toRadians(lon2);
//                    rlat1 = Math.toRadians(Double.parseDouble(holder.latsel));
//                    rlat2 = Math.toRadians(lat2);
//                    Double dlon = rlon2 - rlon1;
//                    Double dlat = rlat2 - rlat1;
//                    Double a = Math.pow(Math.sin(dlat / 2), 2)
//                            + Math.cos(rlat1) * Math.cos(rlat2)
//                            * Math.pow(Math.sin(dlon / 2), 2);
//
//                    Double c = 2 * Math.asin(Math.sqrt(a));
//                    Integer r = 800;

                    DecimalFormat df = new DecimalFormat("#.##");
                    r = Double.valueOf(df.format(r));
                    holder.skm.setText((r + " Km"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void filterList(ArrayList<GetSet> filteredList) {
        product = filteredList;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(GetSet GetSet);
    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView  shop_name, shop_city, skm,delivery;
        ImageView imageView;
        String  suid, saveCurrentDate, saveCurrentTime,deliveryop;
        Double result;
        DatabaseReference userRef, databaseReference, sellerRef;
        String latsel, longsel, sname, scity, simage;

        public HomeViewHolder(View itemView) {
            super(itemView);
            shop_name = itemView.findViewById(R.id.shop_name);
            shop_city = itemView.findViewById(R.id.shop_city);
            imageView = itemView.findViewById(R.id.storeImage);
            skm = itemView.findViewById(R.id.shop_km);
            delivery=itemView.findViewById(R.id.delivertyoption);


            try {
                userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            } catch (Exception e) {
                e.printStackTrace();
            }

            databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items").child(FirebaseAuth.getInstance()
            .getCurrentUser().getUid());

            sellerRef = FirebaseDatabase.getInstance().getReference("Sellers");


            Calendar calForDate = Calendar.getInstance();
            DateFormat currentDate = new SimpleDateFormat(" dd-mm-yyyy ");
            saveCurrentDate = currentDate.getDateInstance().format(calForDate.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat(" hh:mm:ss:ms ");
            saveCurrentTime = currentTime.format(calForDate.getTime());
        }

        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                                context.startActivity(new Intent(context, SellerProducts.class).putExtra("suid", suid).putExtra("skm", String.valueOf(result)));
                }
            });


        }
    }
}
