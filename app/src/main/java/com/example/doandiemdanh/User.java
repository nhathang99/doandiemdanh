package com.example.doandiemdanh;

public class User {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String profession;
    private String location;

   public User(){

   }
   public User (String email, String password)
   {
       this.email =email;
       this.password = password;
   }
   public User(String email,String fullName, String phone, String profession,String location)
   {
     
       this.email= email;
       this.fullName = fullName;
       this.phone = phone;
       this.profession= profession;
       this.location = location;


   }
  

   public String getEmail()
   {

       return email;
   }

   public String getPassword()
   {
       return password;
   }
   public String getFullName()
   {
       return fullName;
   }
   public String getPhone()
   {

       return phone;
   }
   public String getProfession(){

       return profession;
    }
    public String getLocation() {
        return location;
    }



}
