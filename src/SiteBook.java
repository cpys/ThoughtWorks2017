import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 该类表示某场地所有预订信息
 */
class SiteBook {
    /**
     * 场地名称
     */
    private String site;
    /**
     * 某天的具体预订信息
     */
    private Map<LocalDate, DateBook> dateBookMap = new TreeMap<>();
    /**
     * 该场地的总收入
     */
    private int income = 0;

    /**
     * 仅可使用场地名称构造此类
     * @param site 场地名称
     */
    SiteBook(String site) {
        this.site = site;
    }

    /**
     * 细化到某天后尝试去预订
     * @param userId 用户名称
     * @param bookDate 预订日期
     * @param bookTimeSlot 预订时间段
     * @return true:预订成功，false:预订失败
     */
    boolean tryBook(String userId, LocalDate bookDate, TimeSlot bookTimeSlot) {
        DateBook dateBook = dateBookMap.computeIfAbsent(bookDate, v -> new DateBook(bookDate));
        // 尝试去预订
        return dateBook.tryBook(userId, bookTimeSlot);
    }

    /**
     * 细化到某天后尝试取消预订
     * @param userId 用户名称
     * @param bookDate 预订日期
     * @param bookTimeSlot 预订时间段
     * @return true:取消预订成功，false:取消预订失败
     */
    boolean tryCancelBook(String userId, LocalDate bookDate, TimeSlot bookTimeSlot) {
        if (!dateBookMap.containsKey(bookDate)) {
            return false;
        }
        DateBook dateBook = dateBookMap.get(bookDate);
        // 尝试取消预订
        return dateBook.tryCancelBook(userId, bookTimeSlot);
    }

    /**
     * 获取该场地各天所有预订收入和取消预订收入和
     * @return 收入和
     */
    int getIncome() {
        return income;
    }

    /**
     * 按格式输出该场地内各天所有收入信息
     * @return 格式输出
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("场地:" + site + "\n");

        // 遍历该场地所有日期进行累计输出并累加income
        income = 0;
        for (DateBook dateBook : dateBookMap.values()) {
            stringBuilder.append(dateBook);
            income += dateBook.getIncome();
        }

        stringBuilder.append("小计：" + income + "元\n");
        return stringBuilder.toString();
    }
}
