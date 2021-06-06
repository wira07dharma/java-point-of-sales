<html>
<head>
<title>Contoh penggunaan onkeypress</title>
<script type="text/javascript">
    function saatEnter(inField, e) {
        var charCode;
        if(e && e.which){
            charCode = e.which;
        }else if(window.event){
            e = window.event;
            charCode = e.keyCode;
        }
        if(charCode == 13) {
            document.getElementById("hasil").innerHTML=document.getElementById("tuas").value+"<br>anda menekan enter, <br>keykode: "+charCode;
            alert(document.getElementById("tuas").value+"\nanda menekan enter, \nkeykode: "+charCode);
        }
    }
</script>
</head>
<body>
<hr>
<input type="text" id="tuas" onkeypress="saatEnter(this, event)" />
<hr>
<div id="hasil" ></div>
</body>
</html>