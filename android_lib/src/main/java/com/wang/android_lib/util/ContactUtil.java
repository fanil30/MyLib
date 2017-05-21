package com.wang.android_lib.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/5/21.
 */

public class ContactUtil {

    public static class Contact {
        public String contactName;
        public String phoneNumber;

        public Contact(String contactName, String phoneNumber) {
            this.contactName = contactName;
            this.phoneNumber = phoneNumber;
        }
    }

    /**
     * 读取联系人信息，包括姓名和电话号码
     * 需要权限：android.permission.READ_CONTACTS
     */
    public static List<Contact> readContacts(Context context) {
        List<Contact> contactList = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Phone.CONTENT_URI, new String[]{
                Phone.DISPLAY_NAME,
                Phone.NUMBER,
                Phone.CONTACT_ID
        }, null, null, null);

        if (cursor == null) {
            return null;
        }

        while (cursor.moveToNext()) {
            //得到手机号码
            String phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
            //当手机号码为空的或者为空字段 跳过当前循环
            if (TextUtils.isEmpty(phoneNumber)) {
                continue;
            }
            //得到联系人名称
            String contactName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
            //得到联系人ID
//            Long contactId = cursor.getLong(cursor.getColumnIndex(Phone.CONTACT_ID));
            contactList.add(new Contact(contactName, phoneNumber));
        }

        cursor.close();
        return contactList;
    }

}
