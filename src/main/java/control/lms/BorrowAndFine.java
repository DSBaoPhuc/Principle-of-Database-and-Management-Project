package control.lms;

public class BorrowAndFine {
    private int borrowingID;
    private int bookID;
    private int memberID;
    private String memberName;
    private String borrowDate;
    private String returnDate;
    private int returned;
    private int fineID;
    private double amount;
    private String fineDate;
    private int paid;


    public BorrowAndFine(int borrowingID, int bookID, int memberID, String memberName ,String borrowDate, String returnDate, int returned, double amount, String fineDate, int paid) {
        this.borrowingID = borrowingID;
        this.bookID = bookID;
        this.memberID = memberID;
        this.memberName = memberName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returned = returned;
        this.amount = amount;
        this.fineDate = fineDate;
        this.paid = paid;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getBorrowingID() {
        return borrowingID;
    }

    public void setBorrowingID(int borrowingID) {
        this.borrowingID = borrowingID;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getReturned() {
        return returned;
    }

    public void setReturned(int returned) {
        this.returned = returned;
    }

    public int getFineID() {
        return fineID;
    }

    public void setFineID(int fineID) {
        this.fineID = fineID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFineDate() {
        return fineDate;
    }

    public void setFineDate(String fineDate) {
        this.fineDate = fineDate;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }
}
