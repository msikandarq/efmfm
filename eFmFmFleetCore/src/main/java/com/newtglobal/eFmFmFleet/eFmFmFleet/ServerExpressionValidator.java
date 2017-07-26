package com.newtglobal.eFmFmFleet.eFmFmFleet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerExpressionValidator {

	// validates String-it allows only characters with space,(.)dot and length
	// 3-20
	public static Matcher alphaSpaceDot(CharSequence inputStr) {
		String expression = "^[\\p{L} .'-]{3,20}+$";
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

	// allows only characters and numbers without space or any other special
	// characters
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

	// validates mobile number- allows only numbers 0-9,
	public static Matcher number(CharSequence inputStr) {
		String expression = "^[0-9]$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;
	}

	// validates mobile number- allows only numbers 0-9,
	public static Matcher alphanumspecialcharacers(CharSequence inputStr) {
		String expression = "^[a-zA-Z0-9 .,;:@!#*&%()'-_]*$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher;
	}

}