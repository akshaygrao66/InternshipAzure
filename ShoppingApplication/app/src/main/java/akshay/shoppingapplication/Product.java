package akshay.shoppingapplication;

public class Product {
    String pname,pdesc,pprice,pquant;
    Integer pimage;
    int customerquantity;

    public Product(Integer pimage,String pname, String pdesc, String pprice, String pquant) {
        this.pname = pname;
        this.pimage=pimage;
        this.pdesc = pdesc;
        this.pprice = pprice;
        this.pquant = pquant;
        customerquantity=0;
    }

    public void inccustomerquantity()
    {
        customerquantity=customerquantity+1;
    }
    public boolean deccustomerquantity()
    {
        if(customerquantity!=0) {
            customerquantity = customerquantity - 1;
            return  true;
        }
        return false;
    }
    public int getcustomerquantity()
    {
        return customerquantity;
    }
    public Integer getPimage() {
        return pimage;
    }

    public void setPimage(Integer pimage) {
        this.pimage = pimage;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getPquant() {
        return pquant;
    }

    public void setPquant(String pquant) {
        this.pquant = pquant;
    }
}
