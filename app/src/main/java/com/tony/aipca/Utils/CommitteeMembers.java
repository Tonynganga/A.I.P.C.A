package com.tony.aipca.Utils;

import android.net.Uri;

public class CommitteeMembers {
    private String membername, memberposition, memberphone, memberlocation;
    private String  memberImageUrl;

    public CommitteeMembers(){}

    public CommitteeMembers(String membername, String memberposition, String memberphone, String memberlocation, String memberImageUrl) {
        this.membername = membername;
        this.memberposition = memberposition;
        this.memberphone = memberphone;
        this.memberlocation = memberlocation;
        this.memberImageUrl = memberImageUrl;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMemberposition() {
        return memberposition;
    }

    public void setMemberposition(String memberposition) {
        this.memberposition = memberposition;
    }

    public String getMemberphone() {
        return memberphone;
    }

    public void setMemberphone(String memberphone) {
        this.memberphone = memberphone;
    }

    public String getMemberlocation() {
        return memberlocation;
    }

    public void setMemberlocation(String memberlocation) {
        this.memberlocation = memberlocation;
    }

    public String getMemberImageUrl() {
        return memberImageUrl;
    }

    public void setMemberImageUrl(String memberImageUrl) {
        this.memberImageUrl = memberImageUrl;
    }
}