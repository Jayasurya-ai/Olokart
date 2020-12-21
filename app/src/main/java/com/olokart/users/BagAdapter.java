package com.olokart.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BagAdapter extends RecyclerView.Adapter<BagAdapter.HomeViewHolder> {

    Context context;
    ArrayList<GetSet> product;

    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public BagAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public BagAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public BagAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // notifyDataSetChanged();
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.bagitems_layout,
                parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull final BagAdapter.HomeViewHolder holder, int position) {
        //notifyDataSetChanged();

        holder.bind(product.get(position), listener);

        holder.product_name.setText(product.get(position).getProduct_name());
        holder.product_quant.setText(product.get(position).getProduct_quant());

        holder.product_quan = product.get(position).getProduct_quant();
        holder.uaddress = product.get(position).getUaddress();
        holder.numberButton.setNumber(product.get(position).getTotal_quantity());
        holder.product_price.setText( "₹"+product.get(position).getProduct_price());
        holder.pName = product.get(position).getProduct_name();

        holder.proPrice = product.get(position).getProduct_price();
        holder.suid = product.get(position).getSuid();
        holder.proName = product.get(position).getProduct_name();
        holder.actual_price=product.get(position).getActual_price();
        holder.numberButton.setRange(0,Integer.parseInt(product.get(position).getTotal_quant()));



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

        TextView product_name, product_price, product_quant;
        String pName, suid, product_quan;
        ElegantNumberButton numberButton;
        String actual_price,uaddress;
        RelativeLayout relativeLayout;
        DatabaseReference  databaseReference;
        ViewGroup viewGroup;
        String proPrice, proName;
        int BagPrice, subBagprice;




        FrameLayout frameLayout;

        public HomeViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.bagproname);
            product_price = itemView.findViewById(R.id.bagproprice);
            numberButton = itemView.findViewById(R.id.bagnumber_Buttonqty);
            product_quant = itemView.findViewById(R.id.bagQuantity_text);
            viewGroup = itemView.findViewById(android.R.id.content);
            frameLayout = itemView.findViewById(R.id.bagframelayout);

            databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items");

        }


        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener){

            changedata();


        }

        public void changedata() {


            numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                                      @Override
                                                      public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                    numberButton.setRange(0, Integer.valueOf(String.valueOf(total_quant)));


                                                          if (oldValue > newValue) {

                                                              subBagprice = subBagprice + Integer.parseInt(actual_price);

                                                              BagPrice = Integer.parseInt(proPrice);
//
                                                              BagPrice = BagPrice - subBagprice;


                                                              if (numberButton.getNumber().equals("0")) {
                                                                  databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid).child(proName + product_quan).removeValue();


                                                              } else {
                                                                  HashMap<String, Object> cartMap = new HashMap<>();
                                                                  cartMap.put("total_quantity", numberButton.getNumber());
                                                                  cartMap.put("product_price", String.valueOf(BagPrice));
//
                                                                  databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid).child(proName + product_quan).updateChildren(cartMap);


                                                              }
                                                              subBagprice = 0;
                                                              //
                                                          } else {


                                                              if (Integer.parseInt(numberButton.getNumber()) > 0) {

                                                                  HashMap<String, Object> cartMap = new HashMap<>();
                                                                  cartMap.put("total_quantity", numberButton.getNumber());
                                                                  cartMap.put("product_price", String.valueOf(Integer.parseInt(numberButton.getNumber()) * Integer.parseInt(actual_price)));;

                                                                  databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid).child(proName + product_quan).updateChildren(cartMap);

                                                              }


                                                          }
                                                      }
                                                  });

//
//
        }

    }

}


