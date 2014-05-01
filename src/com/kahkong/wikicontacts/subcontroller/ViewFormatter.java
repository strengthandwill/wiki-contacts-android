package com.kahkong.wikicontacts.subcontroller;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ViewFormatter {
	public static String arrayToString(String[] array) {
		if (array==null) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=0; i<array.length; i++) {
			stringBuffer.append(array[i]);
			if (i!=array.length-1) {
				stringBuffer.append(", ");
			}
		}
		return stringBuffer.toString();
	}
	
	public static String numberArrayToString(String[] number, String countryCode) {
		if (number==null || countryCode==null) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("(+" + countryCode + ") ");
		for (int i=0; i<number.length; i++) {
			stringBuffer.append(number[i]);
			if (i!=number.length-1) {
				stringBuffer.append(", ");
			}
		}
		return stringBuffer.toString();
	}
	
	public static String combineStrings(String str1, String str2) {
		if (str1.equals("") && str2.equals("")) {
			return "";
		} else if (!str1.equals("") && str2.equals("")) {
			return str1;
		} else if (!str2.equals("") && str1.equals("")) {
			return str2;
		} else {
			return str1 + "\n" + str2;
		}
	}
	
	public static String orString(String str1, String str2) {
		return str1!=null ? str1:str2;
	}	
}
