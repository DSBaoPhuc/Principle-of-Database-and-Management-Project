package control.lms;

public class Member {
    private int memberId;
    private int librarianID;
    private String name;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;

    public Member(int memberId, int librarianID, String name, String gender, String email, String phoneNumber, String address) {
        this.memberId = memberId;
        this.librarianID = librarianID;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getLibrarianID() {
        return librarianID;
    }

    public void setLibrarianID(int librarianID) {
        this.librarianID = librarianID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
