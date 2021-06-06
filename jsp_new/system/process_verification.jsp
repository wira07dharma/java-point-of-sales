<%
    
    String regTemp="";
    String whereClause ="";
    regTemp = request.getParameter("VerPas");
    
     if (!regTemp.equals("")){
        String [] tempData = regTemp.split("\\;");
        try{
            if(tempData.length>0){
                 System.out.print("masuk");
                String user_id	= tempData[0];
                                 //System.out.print("masuk2 "+user_id);
            }
            
        }catch(Exception es){
            System.out.print("sss "+es);
        }
        

        whereClause=" A";
        //Vector listAppUser = PstAppUser.listPartObj(0, 0, whereClause, "");
        //AppUser appUser = (AppUser)listAppUser.get(0);
        
        String test ="";
       
     }
    
%>
