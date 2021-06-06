var charComma = ',';
var charDot = '.';

var guiDigitGroup=charComma;
var guiDecimalSymbol=charDot;
var decPlace = 2;
/*
multiply values of two inputbox objects set to another inputbox object
*/
function multiplyInputFloat(operand, multiplier, result){
	result.value = parseFloat(operand.value) * parseFloat(multiplier.value);
	if(result.value == "NaN"){
		result.value="";
	}
	formatNumber(result);
}

/*
divide values of two inputbox objects set to another inputbox object
*/
function divideInputFloat(operand, divider, result){
	result.value = parseFloat(operand.value) / parseFloat(divider.value);
	if(result.value == "NaN"){
		result.value="";
	}
	
}
/*

*/
function calcUSDPrice(priceRp, USDRate, priceUSD){
		
		priceRp.value = getNumberFloat(priceRp,guiDigitGroup);
		USDRate.value = getNumberFloat(USDRate,guiDigitGroup);
		
		divideInputFloat(priceRp, USDRate, priceUSD);
		priceRp.value = formatFloat(priceRp.value, '', guiDigitGroup, guiDecimalSymbol, decPlace);
		priceUSD.value = formatFloat(priceUSD.value, '', guiDigitGroup, guiDecimalSymbol, decPlace);
			
}

function calcRpPrice(priceUSD,USDRate,priceRp){
	/*if(priceRp.value == 0){
		multiplyInputFloat(priceUSD,USDRate,priceRp);
	}*/
}

function formatNumber(obj){
	var str = obj.value;
	var token = str.indexOf(".");
	if(token>0)
		token = token + 3;
	else
		token = str.length;
		
	str = str.substring(0,token);
	obj.value = str;
}



/***************** common functions ***************/
function replace(szBuf, szFind, szReplace, lStart) {
 var lFind = 0;
 if (!lStart) lStart = 0;
 
 while (lFind != -1) {
 lFind = szBuf.indexOf(szFind, lStart);
 
 if (lFind != -1) {
 szBuf = szBuf.substring(0,lFind) + szReplace + szBuf.substring(lFind + szFind.length);
 lStart = lFind + szReplace.length;
 }
 }
 return szBuf;
}

function pmt(rate, nper, pv, fv) {
 var rVal;
 if (rate==0) {
 rVal=-(fv + pv)/nper;
 } else {
 ir=Math.pow(1 + rate,nper);
 rVal=-((rate * (fv + ir * pv))/(ir-1));
 }
 return rVal;
}

/******************* Integer Functions ********************/
function cleanNumberInt(strNum, digitGroup) {
 if (!strNum) return strNum;
 strNum = replace(strNum, digitGroup, '', 0);
 return strNum;
}

function formatInt(value, lead, digitGroup) {
 var strValue = new String(value);
 var len = strValue.length;
 var n;
 var strRet = '';
 var ctChar = 3 - (len%3);
 if (ctChar == 3) ctChar =0;
 for (n=0; len > n; n++) {
 if (ctChar == 3) {
 strRet += digitGroup;
 ctChar = 0;
 }
 ctChar++;
 strRet += strValue.substring(n,n+1);
 }
  
 if (lead == '%') {
 return strRet + lead;
 } else {
 return lead + strRet;
 }
}

function numCheckFormatInt(elem, minVal, maxVal, FldName, lead, digitGroup,decimalSymbol, decPlace) {
 if (elem.value == '') {
 elem.value = formatInt('0', lead, digitGroup);
 return true;
 }
 
 var value = parseInt(cleanNumberInt(elem.value, digitGroup), 10);
 
 if (value < minVal) {
 elem.focus();
 alert('You have exceeded the MINIMUM range for '+FldName+'.\nPlease check your information and try again.');
 value = minVal;
 elem.value = formatInt(value, lead, digitGroup);
 elem.focus();
 return false;
 }
 
 if (value > maxVal) {
 elem.focus();
 alert('You have exceeded the MAXIMUM range for '+FldName+'.\nPlease check your information and try again.');
 value = maxVal;
 elem.value = formatInt(value, lead, digitGroup);
 elem.focus();
 return false;
 }
 
 
 if (isNaN(value)) {
 elem.focus();
 alert('You have entered an incorrect character on '+FldName+'. \nPlease check your information and try again.');
 elem.value = formatInt('0', lead, digitGroup);
 elem.focus();
 return false;
 }
 elem.value = formatInt(value, lead, digitGroup);
 return true;
}

/************************  Float Functions *********************/
function cleanNumberFloat(strNum,digitGroup) {
 if (!strNum) return strNum; 
 
 strNum = replace(strNum, digitGroup, '', 0);
 return strNum;
}


/**
 * Clean number from digit and decimal symbol
 * Karena bilangan koma (exp : 122,90) dianggap NAN maka koma harus diganti menjadi titik
 * Edit by Edhy
 */
function cleanNumberFloat(strNum,digitGroup,decimalGroup) {
 if (!strNum) return strNum; 
 
 strNum = replace(strNum, digitGroup, '', 0);
 
 if(decimalGroup==charComma){
	 strNum = replace(strNum, decimalGroup, digitGroup, 0); 
 }
 
 return strNum;
}


function formatFloat(value, lead, digitGroup, decimalSymbol, decPlace) {
  var strValue = new String(value); 
  var lenStr = strValue.length;
  
  // Edit by edhy
  // karena tidak mungkin yang dianggap bilangan mengandung koma
  var idxDec = strValue.indexOf(decimalSymbol, 0);
  if(decimalSymbol==charComma)  
  {
	idxDec = strValue.indexOf(charDot, 0);	
  }  

  var lenNum = 0;
  if(idxDec<0)
  {
 	 lenNum=lenStr
  }	 
  else
  { 
     lenNum=idxDec;
  }	 
 
  var n;
  var strRet = '';
  var ctChar = 3 - (lenNum%3);
  if (ctChar == 3) ctChar =0;
  for (n=0; lenNum > n; n++) {
	 if (ctChar == 3) {
	 strRet += digitGroup;
	 ctChar = 0;
	 }
	 ctChar++;
	 strRet += strValue.substring(n,n+1);
  }
  
  var dummyStr = "0";
  if(idxDec>=0){
  	if((idxDec+1+decPlace) > (lenStr-1)){

			 
			 strRet += (decimalSymbol + strValue.substring(idxDec+1,lenStr));
			 var nzero = decPlace - (lenStr - idxDec - 1);
			 for(iz=0;iz<nzero;iz++){
				strRet = strRet + dummyStr;
			 }
			 
		}else
     	 strRet += (decimalSymbol + strValue.substring(idxDec+1,idxDec+1+decPlace));
   }else{
    strRet += decimalSymbol ;
   	for(iz=0;iz<decPlace;iz++){
		strRet = strRet + dummyStr;
	 }
   }
  

 if (lead == '%') {
	 return strRet + lead;
 } else {
	 return lead + strRet;
 }
}

function numCheckFormatFloat(elem, minVal, maxVal, FldName, lead, digitGroup, decimalSymbol, decPlace) {

	 if (elem.value == '') {
		 elem.value = formatFloat('0', lead, digitGroup, decPlace, decimalSymbol, decPlace);
		 return true;
	 }
 
 	 var value = parseFloat(cleanNumberFloat(elem.value,digitGroup), 10);
 
	 if ((value-minVal) < 0.000) {
		 elem.focus();
		 alert('You have exceeded the MINIMUM range for '+FldName+'.\nPlease check your information and try again.');
		 value = minVal;
		 elem.focus();
		 return false;
	 }
 
	 if ((value-maxVal) > 0.000) {
		 elem.focus();
		 alert('You have exceeded the MAXIMUM range for '+FldName+'.\nPlease check your information and try again.');
		 value = maxVal;
		 elem.focus();
		 return false;
	 }
 
 
	 if (isNaN(value)) {
		 elem.focus();
		 alert('You have entered an incorrect character on '+FldName+'. \nPlease check your information and try again.');
		 elem.value = formatFloat('0', lead, digitGroup,decimalSymbol, decPlace);
		 elem.focus();
		 return false;
	 }
	  
 	 elem.value = formatFloat(value, lead, digitGroup,decimalSymbol, decPlace);
 	 return true;
}


function numFormatFloat(elem,  FldName, lead, digitGroup, decimalSymbol, decPlace) {

	 if (elem.value == '') {
		 elem.value = formatFloat('0', lead, digitGroup, decPlace, decimalSymbol, decPlace);
		 return true;
	 }
 
 	 var value = parseFloat(cleanNumberFloat(elem.value,digitGroup), 10);
 
 	 //alert(value);
	 if (isNaN(value)) {
		 elem.focus();
		 alert('You have entered an incorrect character on '+FldName+'. \nPlease check your information and try again.');
		 elem.value = formatFloat('0', lead, digitGroup,decimalSymbol, decPlace);
		 elem.focus();
		 return false;
	 }
	 
     elem.value = formatFloat(value, lead, digitGroup,decimalSymbol, decPlace);	 
  	 return true;
}

function numFormatInt(elem, FldName, lead, digitGroup,decimalSymbol, decPlace) {
 if (elem.value == '') {
 elem.value = formatInt('0', lead, digitGroup);
 return true;
 }
 
 var value = parseInt(cleanNumberInt(elem.value, digitGroup), 10);
 
 if (isNaN(value)) {
 elem.focus();
 alert('You have entered an incorrect character on '+FldName+'. \nPlease check your information and try again.');
 elem.value = formatInt('0', lead, digitGroup);
 elem.focus();
 return false;
 }
 elem.value = formatInt(value, lead, digitGroup);
 return true;
}


function getNumberInt(elem,guiDigitGroup){
	var rtn = parseInt(cleanNumberInt(elem.value, guiDigitGroup), 10);
	return rtn;
}	

function getNumberFloat(elem,guiDigitGroup){
	var rtn = parseFloat(cleanNumberFloat(elem.value, guiDigitGroup), 10);
	return rtn;
}	

