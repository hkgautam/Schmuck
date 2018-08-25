package com.schmuck.www.schmuck;

public class Category {

    String pimage;
    String ptitle;
//    String pprice;

    public Category(String pimage, String ptitle) {
        this.pimage=pimage;
        this.ptitle=ptitle;
//        this.pprice=pprice;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getPimage() {
        return pimage;
    }

    //    public String getPprice() {
//        return pprice;
//    }

//    public void setPprice(String pprice) {
//        this.pprice = pprice;
//    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }
}