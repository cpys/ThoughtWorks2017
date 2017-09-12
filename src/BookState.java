/**
 * 表示所有预订中出现状态的枚举类
 */
enum BookState {
    validBook("valid"),
    invalidBook("Error: the booking is invalid!"),
    acceptedBook("Success: the booking is accepted!"),
    conflictsBook("Error: the booking conflicts with existing bookings!"),
    notExistBook("Error: the booking being cancelled does not exist!");

    private String value;
    BookState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
