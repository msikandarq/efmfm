package com.newtglobal.eFmFmFleet.services;

import java.io.IOException;

import com.google.zxing.WriterException;
import com.newtglobal.eFmFmFleet.eFmFmFleet.QRCodeGenarator;

public class QRCode {

	public static void main(String[] args) throws WriterException, IOException {

//		[25/04/17, 7:54:33 PM] Rajeev: DL1ZA5953
//		[25/04/17, 7:54:45 PM] Rajeev: HR55R5056
//		[25/04/17, 7:54:58 PM] Rajeev: HR55Y8571
		
		QRCodeGenarator qr=new QRCodeGenarator();
		qr.createQRCode("DL1YE1430", "/Users/sarfrazkhan/upload/");
		System.out.println("Comp");
	}

}
