package com.wang.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaLibTestClass {

    public static void main(String a[]) throws IOException {
        InputStream is = new FileInputStream("E:/image.data");

        for (int i = 0; i < 4 * 32 / 8; i++) {
            int read = is.read();
            System.out.println((i + 1) + ": " + read);
        }
        
        for(int i=0;i<28*28;i++){
            
        }

    }

}
