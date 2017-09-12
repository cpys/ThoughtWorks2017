import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 该类表示某场地某天的所有预订信息
 */
class DateBook {
    /**
     * 预订的日期
     */
    private LocalDate bookDate;
    /**
     * 用户对应的预订时间段集合
     */
    private Map<String, Set<TimeSlot>> userBookMap = new HashMap<>();
    /**
     * 该场地该天的预订时间段开始时间集合
     */
    private Set<Integer> bookTimeSet = new HashSet<>();
    /**
     * 该场地该天的总收入
     */
    private int income = 0;
    /**
     * 记录该场地该天的明细收入, 预订为true，取消预订为false
     */
    private Map<TimeSlot, Map<Boolean, Integer>> detailIncome = new TreeMap<>();
    /**
     * 记录预订日期的格式化输出
     */
    private String bookDateStr;

    /**
     * 仅可使用预订日期来初始化此类
     * @param bookDate 预订日期
     */
    DateBook(LocalDate bookDate) {
        this.bookDate = bookDate;
        bookDateStr = bookDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 预订某场地某天的某个时间段
     * @param userId 用户名称
     * @param bookTimeSlot 预订的时间段
     * @return true:预订成功，false:预订冲突
     */
    boolean tryBook(String userId, TimeSlot bookTimeSlot) {
        // 判断每个开始整点是否可预订
        boolean couldBook = true;
        for (int startTime = bookTimeSlot.getStartTime();
             startTime < bookTimeSlot.getEndTime(); ++startTime) {
            if (bookTimeSet.contains(startTime)) {
                couldBook = false;
                break;
            }
        }
        if (!couldBook) {
            return false;
        }

        // 可预订的话就进行预订
        Set<TimeSlot> timeSlotSet = userBookMap.computeIfAbsent(userId, v -> new HashSet<>());
        timeSlotSet.add(bookTimeSlot);
        for (int startTime = bookTimeSlot.getStartTime();
                startTime < bookTimeSlot.getEndTime(); ++startTime) {
            bookTimeSet.add(startTime);
        }
        // 预订完进行明细记录
        Map<Boolean, Integer> bookIncomeMap = detailIncome.computeIfAbsent(bookTimeSlot, k -> new HashMap<>());
        bookIncomeMap.put(true, bookTimeSlot.getBookIncome());
        return true;
    }

    /**
     * 取消预订某场地某天的某个时间段
     * @param userId 用户名称
     * @param bookTimeSlot 预订时间段
     * @return true:取消预订成功，false:取消的预订不存在
     */
    boolean tryCancelBook(String userId, TimeSlot bookTimeSlot) {
        // 判断是否有该用户预订信息
        if (!userBookMap.containsKey(userId)) {
            return false;
        }
        // 判断该用户预订信息中是否有该时间段
        Set<TimeSlot> timeSlotSet = userBookMap.get(userId);
        if (!timeSlotSet.contains(bookTimeSlot)) {
            return false;
        }
        // 开始取消预订
        timeSlotSet.remove(bookTimeSlot);
        for (int startTime = bookTimeSlot.getStartTime();
                startTime < bookTimeSlot.getEndTime(); ++startTime) {
            bookTimeSet.remove(startTime);
        }
        // 取消预订完进行明细记录
        Map<Boolean, Integer> bookIncomeMap = detailIncome.computeIfAbsent(bookTimeSlot, k -> new HashMap<>());
        bookIncomeMap.remove(true);

        int newIncome = bookIncomeMap.getOrDefault(false, 0) + bookTimeSlot.getCancelBookIncome();
        bookIncomeMap.put(false, newIncome);

        return true;
    }

    /**
     * 获取该场地该天所有预订收入和取消预订收入和
     * @return 收入和
     */
    int getIncome() {
        return income;
    }

    /**
     * 按格式输出该场地内该天所有收入信息
     * @return 格式输出
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        // 遍历时间段
        income = 0;
        detailIncome.forEach((timeSlot, bookIncomeMap) -> {
            if (!bookIncomeMap.isEmpty()) {
                if (bookIncomeMap.containsKey(true)) {
                    Integer bookIncome = bookIncomeMap.get(true);
                    stringBuilder.append(bookDateStr + " " + timeSlot.toString() + " " + bookIncome + "元\n");
                    income += bookIncome;
                }
                if (bookIncomeMap.containsKey(false)) {
                    Integer cancelBookIncome = bookIncomeMap.get(false);
                    stringBuilder.append(bookDateStr + " " + timeSlot.toString() + " 违约金 " + bookIncomeMap.get(false) + "元\n");
                    income += cancelBookIncome;
                }
            }
        });
        return stringBuilder.toString();
    }
}
