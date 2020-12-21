package com.olokart.users;

 class GetSet {

    String product_name,sstatus, state, product_quant,product_quanta,product_quantb, product_image, actual_price,actual_pricea,actual_priceb,name, password, phone, image, address, total_price,
           optionmode,order, total_quantity,total_quantitya,total_quantityb, actual_quant, suid, product_cat,quantity,date,time, cartuid,sname,scity,slat,slong, simage, sphone,product_subcat,product_price,total_quant,uaddress,delivery,orderid,bagprice;

    public GetSet() {
    }





     public GetSet(String actual_pricea, String actual_priceb) {
         this.actual_pricea = actual_pricea;
         this.actual_priceb = actual_priceb;

     }

     public String getBagprice() {
         return bagprice;
     }

     public void setBagprice(String bagprice) {
         this.bagprice = bagprice;
     }

     public String getOrderid() {
         return orderid;
     }

     public void setOrderid(String orderid) {
         this.orderid = orderid;
     }
     public String getActual_pricea() {
         return actual_pricea;
     }

     public void setActual_pricea(String actual_pricea) {
         this.actual_pricea = actual_pricea;
     }

     public String getActual_priceb() {
         return actual_priceb;
     }

     public void setActual_priceb(String actual_priceb) {
         this.actual_priceb = actual_priceb;
     }

     public GetSet(String order, String total_quantitya, String sstatus, String optionmode, String total_quantityb, String state, String product_quanta, String product_quantb,String product_price,String actual_price,String total_quant,String uaddress,String delivery,String orderid,String bagprice ) {
         this.total_quantitya = total_quantitya;
         this.total_quantityb = total_quantityb;
         this.state = state;
         this.product_quanta=product_quanta;
         this.optionmode = optionmode;
         this.order= order;
         this.product_quantb=product_quantb;
         this.sstatus = sstatus;
         this.product_price=product_price;
         this.actual_price=actual_price;
         this.total_quant=total_quant;
         this.uaddress=uaddress;
         this.delivery=delivery;
         this.orderid=orderid;
         this.bagprice=bagprice;

     }

     public String getOrder() {
         return order;
     }

     public void setOrder(String order) {
         this.order = order;
     }

     public String getOptionmode() {
         return optionmode;
     }

     public void setOptionmode(String optionmode) {
         this.optionmode = optionmode;
     }

     public String getSstatus() {
         return sstatus;
     }

     public void setSstatus(String sstatus) {
         this.sstatus = sstatus;
     }

     public String getState() {
         return state;
     }

     public void setState(String state) {
         this.state = state;
     }

     public String getUaddress() {
         return uaddress;
     }

     public String getDelivery() {
         return delivery;
     }

     public void setDelivery(String delivery) {
         this.delivery = delivery;
     }

     public void setUaddress(String uaddress) {
         this.uaddress = uaddress;
     }

     public String getTotal_quant() {
         return total_quant;
     }

     public void setTotal_quant(String total_quant) {
         this.total_quant = total_quant;
     }

     public String getProduct_price() {
         return product_price;
     }

     public void setProduct_price(String product_price) {
         this.product_price = product_price;
     }


     public String getProduct_quanta() {
         return product_quanta;
     }

     public void setProduct_quanta(String product_quanta) {
         this.product_quanta = product_quanta;
     }

     public String getProduct_quantb() {
         return product_quantb;
     }

     public void setProduct_quantb(String product_quantb) {
         this.product_quantb = product_quantb;
     }

     public String getTotal_quantitya() {
         return total_quantitya;
     }

     public void setTotal_quantitya(String total_quantitya) {
         this.total_quantitya = total_quantitya;
     }

     public String getTotal_quantityb() {
         return total_quantityb;
     }

     public void setTotal_quantityb(String total_quantityb) {
         this.total_quantityb = total_quantityb;
     }

     public GetSet(String quantity, String date, String time) {
        this.quantity = quantity;
        this.date = date;
        this.time = time;
    }

     public GetSet(String product_subcat) {
         this.product_subcat = product_subcat;
     }

     public String getProduct_subcat() {
         return product_subcat;
     }

     public void setProduct_subcat(String product_subcat) {
         this.product_subcat = product_subcat;
     }

     public GetSet(String product_name, String product_quant, String product_image, String actual_price, String total_price, String scity,
                   String total_quantity, String actual_quant, String suid, String product_cat, String image, String cartuid,
                   String sname, String slat, String slong, String simage, String sphone) {
        this.product_name = product_name;
        this.product_quant = product_quant;
        this.product_image = product_image;
        this.actual_price = actual_price;
        this.total_price = total_price;
        this.total_quantity = total_quantity;
        this.actual_quant=actual_quant;
        this.suid = suid;
        this.image = image;
        this.product_cat = product_cat;
        this.cartuid = cartuid;
        this.sname=sname;
        this.scity=scity;
        this.slat=slat;
        this.slong=slong;
        this.simage = simage;
        this.sphone = sphone;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSphone() {
        return sphone;
    }

    public void setPhone(String sphone) {
        this.sphone = sphone;
    }

    public String getSimage() {
        return simage;
    }

    public void setSimage(String simage) {
        this.simage = simage;
    }

    public String getSlat() {
        return slat;
    }

    public void setSlat(String slat) {
        this.slat = slat;
    }
    public String getSlong() {
        return slong;
    }

    public void
            setSlong(String slong) {
        this.slong = slong;
    }
    public String getScity() {
        return scity;
    }

    public void setScity(String scity) {
        this.scity = scity;
    }
    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCartuid() {
        return cartuid;
    }

    public void setCartuid(String cartuid) {
        this.cartuid = cartuid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_cat() {
        return product_cat;
    }

    public void setProduct_cat(String product_cat) {
        this.product_cat = product_cat;
    }

    public String getActual_quant(){
        return actual_quant;
    }

    public void setActual_quant(String actual_quant) {
        this.actual_quant= actual_quant;
    }

    public String getProduct_quant() {
        return product_quant;
    }

    public void setProduct_quant(String product_quant) {
        this.product_quant = product_quant;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
