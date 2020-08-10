package com.viba.olokartusers;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

//        holder.name=product.get(position).getName();
//        holder.address=product.get(position).getAddress();
//        holder.phone=product.get(position).getPhone();
//        holder.password=product.get(position).getPassword();
        //   holder.productCat = product.get(position).getProduct_cat();
        //  holder.product_subcat = product.get(position).getProduct_subcat();
        //holder.sname=product.get(position).getSname();
        //// holder.product_name.setText(product.get(position).getProduct_name());
        holder.product_name.setText(product.get(position).getProduct_name());
        holder.product_quant.setText(product.get(position).getProduct_quant());
//        holder.product_quanta=product.get(position).getProduct_quanta();
//        holder.product_quantb=product.get(position).getProduct_quantb();
        holder.product_quan = product.get(position).getProduct_quant();
        holder.uaddress = product.get(position).getUaddress();
        //holder.shop_name.setText(product.get(position).getSname());
        // holder.latsel =product.get(position).getSlat();
        // holder.longsel =product.get(position).getSlong();

        // holder.numberButton.setRange(0, Integer.parseInt(product.get(position).getTotal_quantity()));
        holder.numberButton.setNumber(product.get(position).getTotal_quantity());
        // holder.scity.setText(product.get(position).getScity());
        // holder.skm.setText(product.get(position).getSkm);
        holder.product_price.setText(product.get(position).getProduct_price() + "₹");
        holder.pName = product.get(position).getProduct_name();
//        holder.miniOrder = product.get(position).getProduct_name();
//        holder.totalqtya = product.get(position).getTotal_quantitya();
        holder.proPrice = product.get(position).getProduct_price();
//        holder.totalqtyb = product.get(position).getTotal_quantityb();
        holder.suid = product.get(position).getSuid();
        holder.proName = product.get(position).getProduct_name();
        holder.actual_price=product.get(position).getActual_price();
   //     holder.total_quant=product.get(position).getTotal_quant();
        holder.numberButton.setRange(0,Integer.parseInt(product.get(position).getTotal_quant()));
//        holder.actual_pricea=product.get(position).getActual_pricea();
//        holder.actual_priceb=product.get(position).getActual_priceb();


//        holder.qtyreference = FirebaseDatabase.getInstance().getReference("Seller Products").child(holder.suid).child(holder.productCat).child(holder.product_subcat)
//                .child(holder.pName);


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

        TextView product_name, product_price, product_quant, actualPrice, shop_name, totalQuant, scity, skm;
        ImageView imageView;
        String pName, suid, productCat,total_quant, sphone, product_subcat,cartuid, product_quan, product_quanta, product_quantb, actual_pricea, actual_priceb, pimage;
        DatabaseReference userRef;
        ElegantNumberButton numberButton;
        Button button;
        int Input1;
        String totalqtya, totalqtyb,actual_price,uaddress;
        RelativeLayout relativeLayout;
        ProgressBar progressBar;
        DatabaseReference qtyreference, databaseReference;
        ViewGroup viewGroup;
        String proPrice, proName;
        int BagPrice, subBagprice;
        String miniOrder;
        RelativeLayout layout;
        HashMap<String, Object> cartMap = new HashMap<>();


        FrameLayout frameLayout;

        public HomeViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.bagproname);
            product_price = itemView.findViewById(R.id.bagproprice);
            numberButton = itemView.findViewById(R.id.bagnumber_Buttonqty);
            product_quant = itemView.findViewById(R.id.bagQuantity_text);
            viewGroup = itemView.findViewById(android.R.id.content);
            relativeLayout = itemView.findViewById(R.id.bagqtyrelative);
            frameLayout = itemView.findViewById(R.id.bagframelayout);

            databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items");

        }

        public void filterList(ArrayList<GetSet> filteredList) {
            product = filteredList;
            notifyDataSetChanged();
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
                                                                  cartMap.put("total_quantity", numberButton.getNumber());
                                                                  cartMap.put("product_price", String.valueOf(BagPrice));
//                            cartMap.put("product_quant",product_quan);
//                            cartMap.put("product_image",pimage);
                                                                  databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid).child(proName + product_quan).updateChildren(cartMap);
                                                              }
                                                              subBagprice = 0;
                                                              //
                                                          } else {


                                                              if (Integer.parseInt(numberButton.getNumber()) > 0) {

                                                                  cartMap.put("total_quantity", numberButton.getNumber());
                                                                  cartMap.put("product_price", String.valueOf(Integer.parseInt(numberButton.getNumber()) * Integer.parseInt(actual_price)));

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


