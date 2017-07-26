package com.newtglobal.eFmFmFleet.eFmFmFleet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {

	// validates String-it allows only characters with space,(.)dot and length
	// 3-20
	public static Matcher alphaSpaceDot(CharSequence inputStr) {
		String expression = "^[a-zA-Z. ]+$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// allows numbers and . ,
	public static Matcher numbDotComma(CharSequence inputStr) {
		String expression = "^[0-9,.]+$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// validates mobile number- allows only numbers 0-9, min 6 and max 18 digits
	public static Matcher mobNumber(CharSequence inputStr) {
		String expression = "^[0-9]{6,18}$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// allows only characters and numbers
	public static Matcher alphaNumeric(CharSequence inputStr) {
		String expression = "^[a-zA-Z0-9]*$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}
	// ----------------

	// allows only characters and space
	public static Matcher alphaSpace(CharSequence inputStr) {
		String expression = "^[a-zA-Z ]*$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// allows only characters without space
	public static Matcher alpha(CharSequence inputStr) {
		String expression = "^[a-zA-Z]*$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// validates email ID
	public static Matcher email(CharSequence inputStr) {
		String expression = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// validates number- allows only numbers 0-9,
	public static Matcher number(CharSequence inputStr) {
		String expression = "^[0-9]$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;
	}

	public static Matcher validateNumber(CharSequence inputStr) {
		String expression = "^[0-9]+";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;
	}

	// validates alphabets numbers and special characters-
	public static Matcher alphaNumSpecialCharacers(CharSequence inputStr) {
		String expression = "^[a-zA-Z0-9 .,;:@!#*&%()'-_]*$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;
	}

	public static Matcher alphaNumericSpaceDot(CharSequence inputStr) {
		String expression = "^[a-zA-Z0-9 .]*$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;
	}

	public static Matcher dateYear(CharSequence inputStr) {
		String expression = "^(19|20|21|22|23)([0-9]{2}$)";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// allows date in dd/mm/yyyy format
	public static Matcher dateFormat(CharSequence inputStr) {
		String expression = "^(3[01]|[12][0-9]|0[1-9]|[1-9])/(1[0-2]|0[1-9]|[1-9])/(19|20)([0-9]{2}$)";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// validates field yes or no
	public static Matcher yesOrNo(CharSequence inputStr) {
		String expression = "^(yes|YES|no|NO)$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// validates field male or female
	public static Matcher maleOrFemale(CharSequence inputStr) {
		String expression = "^(male|MALE|female|FEMALE)$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// validates field pickup or drop
	public static Matcher pickUpOrDrop(CharSequence inputStr) {
		String expression = "^(pickup|PICKUP|PickUp|drop|DROP|Drop)$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;

	}

	// validates Time
	public static Matcher time(CharSequence inputStr) {
		String expression = "^([01]?[0-9]|2[0-3]):[0-5][0-9]:(00)";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;
	}

	// validates Time HH:MM
	public static Matcher timeHHMM(CharSequence inputStr) {
		String expression = "^([01]?[0-9]|2[0-3]):[0-5][0-9]";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;
	}

	// validates field pickup or drop
		public static Matcher genderPreference(CharSequence inputStr) {
			String expression = "^(y|Y|n|N)$";
			Pattern pattern = Pattern.compile(expression);
			Matcher matcher = pattern.matcher(inputStr);
			return matcher;

		}
	
	
}