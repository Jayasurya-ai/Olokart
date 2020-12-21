package com.olokart.users;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.HomeViewHolder>{

    Context context;
    ArrayList<GetSet> product;

    private List<GetSet> GetSetListt;
    private AdapterView.OnItemClickListener listener;


    public ProductsAdapter(List<GetSet> hList, AdapterView.OnItemClickListener listener) {
        this.GetSetListt = hList;
        this.listener = listener;
    }

    public ProductsAdapter(Context c, ArrayList<GetSet> h) {
        context = c;
        product = h;
    }

    @NonNull
    @Override
    public ProductsAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // notifyDataSetChanged();

            return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.products_layout,
                    parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull final ProductsAdapter.HomeViewHolder holder, int position) {

        holder.bind(product.get(position), listener);

        holder.productCat = product.get(position).getProduct_cat();
        holder.product_subcat = product.get(position).getProduct_subcat();

        holder.pimage = product.get(position).getProduct_image();
        holder.product_quant.setText(product.get(position).getProduct_quant());
        holder.product_quanta = product.get(position).getProduct_quanta();
        holder.product_quantb = product.get(position).getProduct_quantb();
        holder.product_quan = product.get(position).getProduct_quant();
        holder.actual_price = product.get(position).getActual_price();

        holder.product_name.setText(product.get(position).getProduct_name());
        holder.numberButton.setRange(0, Integer.parseInt(product.get(position).getTotal_quantity()));

        holder.product_price.setText("₹"+product.get(position).getActual_price());
        holder.pName = product.get(position).getProduct_name();
        holder.miniOrder = product.get(position).getProduct_name();
        holder.totalqtya = product.get(position).getTotal_quantitya();
        holder.proPrice = product.get(position).getActual_price();
        holder.totalqtyb = product.get(position).getTotal_quantityb();
      //  Glide.with(context).load(product.get(position).getProduct_image()).into(holder.imageView);
        Picasso.get().load(product.get(position).getProduct_image()).placeholder(R.drawable.shopping_cart)
                .error(R.drawable.shopping_cart).into(holder.imageView);
      // Picasso.get().load(product.get(position).getProduct_image()).placeholder(R.drawable.shopimage).into(holder.imageView);

        holder.suid = product.get(position).getSuid();
        holder.proName = product.get(position).getProduct_name();
        holder.actual_pricea = product.get(position).getActual_pricea();
        holder.actual_priceb = product.get(position).getActual_priceb();
        holder.total_quantity = product.get(position).getTotal_quantity();
        holder.total_quantitya = product.get(position).getTotal_quantitya();
        holder.total_quantityb = product.get(position).getTotal_quantityb();

        holder.qtytxta.setText(product.get(position).getProduct_quant());

        if (Integer.parseInt(holder.total_quantitya)>0) {
            holder.qtytxtb.setVisibility(View.VISIBLE);
            holder.qtytxtb.setText(product.get(position).getProduct_quanta());
        }
        if (Integer.parseInt(holder.total_quantityb)>0) {
            holder.qtytxtc.setVisibility(View.VISIBLE);
            holder.qtytxtc.setText(product.get(position).getProduct_quantb());
        }

        holder.qtyreference = FirebaseDatabase.getInstance().getReference("Seller Products").child(holder.suid).child(holder.productCat).child(holder.product_subcat)
                .child(holder.pName);


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
        ImageView imageView;
        String pName, suid, productCat, product_subcat, product_quan, product_quanta, product_quantb, actual_pricea, actual_priceb, pimage, actual_price, total_quantity, total_quantitya, total_quantityb;
        ElegantNumberButton numberButton;
        Button button;
        String totalqtya, totalqtyb;
        RelativeLayout relativeLayout;
        DatabaseReference qtyreference, databaseReference;
        ViewGroup viewGroup;
        String proPrice, proName;
        int BagPrice, subBagprice;
        String miniOrder;
        ElegantNumberButton qtynum, qtynuma, qtynumb;
        TextView qtytxta, qtytxtb, qtytxtc;
        RelativeLayout layout;

        HashMap<String, Object> cartMap = new HashMap<>();
        int Totprice, Totpricea, Totpriceb;
        String curentsuid;
        String olouid;


        FrameLayout frameLayout;

        public HomeViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.proname);
            product_price = itemView.findViewById(R.id.proprice);
            imageView = itemView.findViewById(R.id.proimage);
            numberButton = itemView.findViewById(R.id.number_Buttonqty);
            product_quant = itemView.findViewById(R.id.Quantity_text);
            viewGroup = itemView.findViewById(android.R.id.content);
            relativeLayout = itemView.findViewById(R.id.qtyrelative);
            button = itemView.findViewById(R.id.addproductsbtn);
            qtytxta = itemView.findViewById(R.id.qtytxta);
            qtytxtb = itemView.findViewById(R.id.qtytxtb);
            qtytxtc = itemView.findViewById(R.id.qtytxtc);
            frameLayout = itemView.findViewById(R.id.frameSnack);

            databaseReference = FirebaseDatabase.getInstance().getReference("Cart Items").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            // addNewBubble();


        }

        public void filterList(ArrayList<GetSet> filteredList) {
            product = filteredList;
        }

        public void bind(final GetSet item, final AdapterView.OnItemClickListener listener) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    qtyreference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                            try {

                                final String pname = dataSnapshot.child("product_name").getValue().toString();
                                final String proq = dataSnapshot.child("product_quant").getValue().toString();
                                String proqa = dataSnapshot.child("product_quanta").getValue().toString();
                                String proqb = dataSnapshot.child("product_quantb").getValue().toString();
                                final String price = dataSnapshot.child("actual_price").getValue().toString();
                                String pricea = dataSnapshot.child("actual_pricea").getValue().toString();
                                String priceb = dataSnapshot.child("actual_priceb").getValue().toString();
                                final String sellerId = dataSnapshot.child("suid").getValue().toString();
                                final String maxqty = dataSnapshot.child("total_quantity").getValue().toString();
                                final String maxqtya = dataSnapshot.child("total_quantitya").getValue().toString();
                                final String maxqtyb = dataSnapshot.child("total_quantityb").getValue().toString();


                                if (maxqtya.equals("0")) {
                                    button.setVisibility(View.GONE);
                                    numberButton.setVisibility(View.VISIBLE);
                                    numberButton.setNumber("1");
                                    BagPrice = Integer.parseInt(proPrice);

//                                    Snackbar.make(frameLayout, "Item Added to Bag !", 100000).setTextColor(Color.WHITE).setBackgroundTint(context.getResources().getColor(R.color.colorPrimary)).setActionTextColor(Color.WHITE).setAction("Open Bag", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//
//                                            context.startActivity(new Intent(context, BagAvtivity.class).putExtra("suid", suid));
//
//
//                                        }
//                                    }).show();


                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String currentsuid= dataSnapshot.child(suid).getKey();
                                            if(dataSnapshot.child(currentsuid).exists()||currentsuid==olouid) {
                                                //  databaseReference.setValue(null);
                                                cartMap.put("product_name", pname);
                                                cartMap.put("total_quantity", numberButton.getNumber());
                                                cartMap.put("product_price", String.valueOf(BagPrice));
                                                cartMap.put("product_quant", proq);
                                                cartMap.put("actual_price", proPrice);
                                                cartMap.put("total_quant", maxqty);
                                                cartMap.put("suid", suid);
                                                olouid = suid;

                                                databaseReference.child(suid).child(proName + product_quan).updateChildren(cartMap);
                                            }
                                            else if(currentsuid!=olouid){
                                                databaseReference.setValue(null);
                                                cartMap.put("product_name", pname);
                                                cartMap.put("total_quantity", numberButton.getNumber());
                                                cartMap.put("product_price", String.valueOf(BagPrice));
                                                cartMap.put("product_quant", proq);
                                                cartMap.put("actual_price", proPrice);
                                                cartMap.put("total_quant", maxqty);
                                                cartMap.put("suid", suid);
                                                olouid = suid;
                                                databaseReference.child(suid).child(proName + product_quan).updateChildren(cartMap);


                                            }

                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });






                                    numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                        @Override
                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {


                                            if (oldValue > newValue) {
                                                subBagprice = subBagprice + Integer.parseInt(proPrice);
                                                if (numberButton.getNumber().equals("0")) {
                                                   // Snackbar.make(frameLayout, "Item Removed from Bag ! ", Snackbar.LENGTH_LONG).show();
                                                   // Snackbar.make(frameLayout, "Item Removed from Bag !", Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).setBackgroundTint(context.getResources().getColor(R.color.colorPrimary)).show();
                                                } else {
//                                                    Snackbar.make(frameLayout, "Item Addes to Bag ! ", 100000).setTextColor(Color.WHITE).setBackgroundTint(context.getResources().getColor(R.color.colorPrimary)).setActionTextColor(Color.WHITE).setAction("Open Bag", new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View v) {
//
//                                                            context.startActivity(new Intent(context, BagAvtivity.class).putExtra("suid", suid));
//
//
//                                                        }
//                                                    }).show();
                                                }
//                                              Toast.makeText(context, ""+(BagPrice-subBagprice), Toast.LENGTH_SHORT).show();
                                                BagPrice = BagPrice - subBagprice;

                                                if (numberButton.getNumber().equals("0")) {
                                                    databaseReference.child(suid).child(proName +product_quan).removeValue();
                                                } else {
                                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String currentsuid= dataSnapshot.child(suid).getKey();
                                                           if(dataSnapshot.child(currentsuid).exists()|| currentsuid==olouid) {
                                                               cartMap.put("total_quantity", numberButton.getNumber());
                                                               cartMap.put("product_price", String.valueOf(BagPrice));
                                                               cartMap.put("product_quant", proq);
                                                               cartMap.put("actual_price", price);
                                                               cartMap.put("product_name", pname);
                                                               cartMap.put("total_quant", maxqty);
                                                               cartMap.put("suid", suid);
                                                               olouid = suid;


                                                               databaseReference.child(suid).child(proName + product_quan).setValue(cartMap);
                                                           }
                                                           else if(currentsuid!=olouid){
                                                               databaseReference.removeValue();
                                                               cartMap.put("total_quantity", numberButton.getNumber());
                                                               cartMap.put("product_price", String.valueOf(BagPrice));
                                                               cartMap.put("product_quant", proq);
                                                               cartMap.put("actual_price", price);
                                                               cartMap.put("product_name", pname);
                                                               cartMap.put("total_quant", maxqty);
                                                               cartMap.put("suid", suid);
                                                               olouid = suid;
                                                               databaseReference.child(suid).child(proName + product_quan).setValue(cartMap);


                                                           }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }


                                                subBagprice = 0;

                                            }
                                            else {


                                                BagPrice = Integer.parseInt(proPrice) + BagPrice;
//                                            Toast.makeText(context, ""+BagPrice, Toast.LENGTH_SHORT).show();
//                                                Snackbar.make(frameLayout, "Item Added to Bag !", 100000).setTextColor(Color.WHITE).setBackgroundTint(context.getResources().getColor(R.color.colorPrimary)).setActionTextColor(Color.WHITE).setAction("Open Bag", new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//
//                                                        context.startActivity(new Intent(context, BagAvtivity.class).putExtra("suid", suid));
//
//                                                    }
//                                                }).show();

                                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String currentsuid= dataSnapshot.child(suid).getKey();
                                                       // databaseReference.removeValue();
                                                        if(dataSnapshot.child(currentsuid).exists()|| curentsuid==olouid) {
                                                            cartMap.put("total_quantity", numberButton.getNumber());
                                                            cartMap.put("product_price", String.valueOf(BagPrice));
                                                            cartMap.put("product_quant", proq);
                                                            cartMap.put("actual_price", proPrice);
                                                            cartMap.put("total_quant", maxqty);
                                                            cartMap.put("product_name", pname);
                                                            cartMap.put("suid", suid);
                                                            olouid = suid;

                                                            databaseReference.child(suid).child(proName + product_quan).setValue(cartMap);
                                                        }
                                                        else if(currentsuid!=suid){
                                                            databaseReference.setValue(null);
                                                            cartMap.put("total_quantity", numberButton.getNumber());
                                                            cartMap.put("product_price", String.valueOf(BagPrice));
                                                            cartMap.put("product_quant", proq);
                                                            cartMap.put("actual_price", proPrice);
                                                            cartMap.put("total_quant", maxqty);
                                                            cartMap.put("product_name", pname);
                                                            cartMap.put("suid", suid);
                                                            olouid = suid;
                                                            databaseReference.child(suid).child(proName + product_quan).setValue(cartMap);
                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });



                                            }

                                            if (newValue == 0 || oldValue == 0) {
                                                subBagprice = 0;
                                                BagPrice = 0;

                                                numberButton.setVisibility(View.GONE);
                                                button.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                } else {


                                    View view = LayoutInflater.from(context).inflate(R.layout.quantity_layout, viewGroup, false);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                    builder.setView(view);

                                    final AlertDialog alertDialog = builder.create();
                                    Window window = alertDialog.getWindow();
                                    window.setGravity(Gravity.BOTTOM);

                                    window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                                    alertDialog.show();


                                    LinearLayout lin2, lin3;
                                    lin2 = view.findViewById(R.id.qtya_linear);
                                    lin3 = view.findViewById(R.id.qtyc_linear);
                                    TextView product_quantname = view.findViewById(R.id.qty_product_name);
                                    ImageView proqtyimage = view.findViewById(R.id.qty_image);
                                    TextView qty = view.findViewById(R.id.qty);
                                    TextView qtyprice = view.findViewById(R.id.qty_price);
                                    qtynum = view.findViewById(R.id.number_Button);
                                    layout = view.findViewById(R.id.relQty);
                                    qtynuma = view.findViewById(R.id.number_Buttona);

                                    TextView qtya = view.findViewById(R.id.qty_a);
                                    TextView qtypricea = view.findViewById(R.id.qty_pricea);

                                    TextView qtyb = view.findViewById(R.id.qty_b);
                                    TextView qtypriceb = view.findViewById(R.id.qty_priceb);
                                    qtynumb = view.findViewById(R.id.number_Buttonb);


                                    product_quantname.setText(proName);
                                    Picasso.get().load(pimage).placeholder(R.drawable.shopping_cart).into(proqtyimage);
                                    qty.setText(product_quan);
                                    qtya.setText(product_quanta);
                                    qtyb.setText(product_quantb);
                                    qtyprice.setText("₹"+actual_price);
                                    qtypricea.setText("₹"+actual_pricea);
                                    qtypriceb.setText("₹"+actual_priceb);


                                    qtynum.setRange(0, Integer.parseInt(total_quantity));
                                    qtynuma.setRange(0, Integer.parseInt(total_quantitya));
                                    qtynumb.setRange(0, Integer.parseInt(total_quantityb));


                                    qtynum.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                        @Override
                                        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {


                                            if (oldValue > newValue) {

                                                subBagprice = subBagprice + Integer.parseInt(actual_price);

                                                Totprice = Totprice - subBagprice;


                                                subBagprice = 0;
                                            }

                                            else if (Integer.parseInt(qtynum.getNumber())>Integer.parseInt(maxqty)) {
                                                Toast.makeText(context, "Qty limit exceeded", Toast.LENGTH_SHORT).show();

                                            }
//
                                        }
                                    });


                                    if (total_quantitya.equals("0")) {
                                        lin2.setVisibility(View.GONE);
                                    } else {

//
                                        qtynuma.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {


                                                if (oldValue > newValue) {

                                                    subBagprice = subBagprice + Integer.parseInt(actual_pricea);

                                                    Totpricea = Totpricea - subBagprice;


                                                    subBagprice = 0;
                                                }

                                            }
                                        });
                                    }

                                    if (total_quantityb.equals("0")) {
                                        lin3.setVisibility(View.GONE);
                                    } else {


                                        qtynumb.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                                            @Override
                                            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {


                                                if (oldValue > newValue) {

                                                    subBagprice = subBagprice + Integer.parseInt(actual_priceb);

                                                    Totpriceb = Totpriceb - subBagprice;


                                                    subBagprice = 0;

                                                }


                                            }
                                        });
                                    }

                                    Snackbar.make(layout, "", 1000000).setTextColor(Color.WHITE).setBackgroundTint(context.getResources().getColor(R.color.black)).setActionTextColor(Color.WHITE).setAction("Add to Bag", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
                                            if (Integer.parseInt(qtynum.getNumber()) > 0) {


                                                sadata();


                                                // addNewBubble();
                                            }
                                            else  {
                                                databaseReference.child(suid).child(proName + product_quan).removeValue();
                                            }

                                            if (Integer.parseInt(qtynuma.getNumber()) > 0) {
                                                savedataa();
                                                //addNewBubble();
                                            }
                                            else {
                                                databaseReference.child(suid).child(proName + product_quanta).removeValue();
                                            }
//
                                            if (Integer.parseInt(qtynumb.getNumber()) > 0) {
                                                savedatab();
                                                // addNewBubble();
                                            }
                                            else {
                                                databaseReference.child(suid).child(proName + product_quantb).removeValue();
                                            }

                                            alertDialog.dismiss();
//                                            Snackbar.make(frameLayout, "", 1000000000).setBackgroundTint(context.getResources().getColor(R.color.colorPrimary)).setActionTextColor(Color.WHITE).setAction("Open Bag", new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//
//                                                    context.startActivity(new Intent(context, BagAvtivity.class).putExtra("suid", suid));
//
//
//                                                }
//                                            }).show();


                                        }
                                    }).show();


                                }


                            } catch (
                                    NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


            });
        }


        public void sadata() {
            //   curentsuid = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid).getKey();



            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String currentsuid= dataSnapshot.child(suid).getKey();


                    if (dataSnapshot.child(currentsuid).exists()|| currentsuid==olouid) {
                        cartMap.put("total_quantity", qtynum.getNumber());
                        cartMap.put("product_price", String.valueOf(Integer.parseInt(qtynum.getNumber()) * Integer.parseInt(actual_price)));
                        cartMap.put("product_quant", product_quan);
                        cartMap.put("actual_price", actual_price);
                        cartMap.put("total_quant", total_quantity);
                        cartMap.put("product_name", proName);
                        cartMap.put("suid",suid);
                        olouid=suid;

                        databaseReference.child(suid).child(proName + product_quan).updateChildren(cartMap);


                    }

                    else if(currentsuid!=olouid) {

                        databaseReference.setValue(null);

                        cartMap.put("total_quantity", qtynum.getNumber());
                        cartMap.put("product_price", String.valueOf(Integer.parseInt(qtynum.getNumber()) * Integer.parseInt(actual_price)));
                        cartMap.put("product_quant", product_quan);
                        cartMap.put("actual_price", actual_price);
                        cartMap.put("total_quant", total_quantity);
                        cartMap.put("product_name", proName);
                        cartMap.put("suid",suid);
                        olouid=suid;


                        databaseReference.child(suid).child(proName + product_quan).setValue(cartMap);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void savedataa() {

            Totpricea = Integer.parseInt(actual_pricea) + Totpricea;
            //    curentsuid = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(suid).getKey();


            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String currentsuid = dataSnapshot.child(suid).getKey();
                   // Toast.makeText(context, ""+dataSnapshot.child(suid).getKey(), Toast.LENGTH_SHORT).show();
                    if (dataSnapshot.child(currentsuid).exists()|| currentsuid==olouid) {
                        cartMap.put("total_quantity", qtynuma.getNumber());
                        cartMap.put("product_price", String.valueOf(Integer.parseInt(qtynuma.getNumber()) * Integer.parseInt(actual_pricea)));
                        cartMap.put("product_quant", product_quanta);
                        cartMap.put("actual_price", actual_pricea);
                        cartMap.put("total_quant", total_quantitya);
                        cartMap.put("product_name", proName);
                        cartMap.put("suid",suid);
                        olouid=suid;


                        databaseReference.child(suid).child(proName + product_quanta).updateChildren(cartMap);
                    }
                    else if(curentsuid!=olouid){

                        databaseReference.setValue(null);

                        cartMap.put("total_quantity", qtynum.getNumber());
                        cartMap.put("product_price", String.valueOf(Integer.parseInt(qtynum.getNumber()) * Integer.parseInt(actual_price)));
                        cartMap.put("product_quant", product_quan);
                        cartMap.put("actual_price", actual_price);
                        cartMap.put("total_quant", total_quantity);
                        cartMap.put("product_name", proName);
                        cartMap.put("suid",suid);
                        olouid=suid;

                        databaseReference.child(suid).child(proName + product_quanta).setValue(cartMap);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void savedatab() {



            Totpriceb = Integer.parseInt(actual_priceb) + Totpriceb;

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentsuid = dataSnapshot.child(suid).getKey();
                 //   Toast.makeText(context, ""+dataSnapshot.child(suid).getKey(), Toast.LENGTH_SHORT).show();
                    if (dataSnapshot.child(currentsuid).exists()|| currentsuid==olouid) {
                        cartMap.put("total_quantity", qtynumb.getNumber());
                        cartMap.put("product_price", String.valueOf(Integer.parseInt(qtynumb.getNumber()) * Integer.parseInt(actual_priceb)));
                        cartMap.put("product_quant", product_quantb);
                        cartMap.put("actual_price", actual_priceb);
                        cartMap.put("total_quant", total_quantityb);
                        cartMap.put("product_name", proName);
                        cartMap.put("suid",suid);
                        olouid=suid;


                        databaseReference.child(suid).child(proName + product_quantb).updateChildren(cartMap);
                    }
                    else if(currentsuid!=olouid) {
                        databaseReference.setValue(null);


                        cartMap.put("total_quantity", qtynumb.getNumber());
                        cartMap.put("product_price", String.valueOf(Integer.parseInt(qtynumb.getNumber()) * Integer.parseInt(actual_priceb)));
                        cartMap.put("product_quant", product_quantb);
                        // cartMap.put("final_price",String.valueOf(BagPrice));
                        cartMap.put("actual_price", actual_priceb);
                        cartMap.put("total_quant", total_quantityb);
                        cartMap.put("product_name", proName);
                        cartMap.put("suid",suid);

                        databaseReference.child(suid).child(proName + product_quantb).setValue(cartMap);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }
    }
}