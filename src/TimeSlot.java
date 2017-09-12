import java.time.DayOfWeek;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表示某个时间范围的类
 */
class TimeSlot implements Comparable<TimeSlot>{
    /**
     * 时间段的开始时间
     */
    private int startTime = 0;
    /**
     * 时间段的结束时间
     */
    private int endTime = 0;
    /**
     * 时间段所在的天是星期几
     */
    private DayOfWeek dayOfWeek;
    /**
     * 用于匹配时间段字符串和正则模式串和表达式
     */
    private static String pattern = "(\\d{2}):00~(\\d{2}):00";
    private static Pattern r = Pattern.compile(pattern);
    /**
     * 表示工作日和休息日各时间段价格的数组
     */
    private static int[][] prices = {
            // 工作日，周一到周五
        {
            0, 0, 0, 0, 0, 0, 0, 0, 0,  // [0, 9)
            30, 30, 30,                 // [9, 12)
            50, 50, 50, 50, 50, 50,     // [12, 18)
            80, 80,                     // [18, 20)
            60, 60                      // [20, 22)
        },
            // 休息日，周六到周日
        {
            0, 0, 0, 0, 0, 0, 0, 0, 0,  // [0, 9)
            40, 40, 40,                 // [9, 12)
            50, 50, 50, 50, 50, 50,     // [12, 18)
            60, 60, 60, 60              // [18, 22)
        }
    };

    /**
     * 实例化此类需要保存周几
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param dayOfWeek 周几
     */
    private TimeSlot(int startTime, int endTime, DayOfWeek dayOfWeek) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * 解析时间范围字符串并返回此类对象
     * @param bookTimeSlotStr 时间范围字符串
     * @param dayOfWeek 星期几
     * @return 解析成功返回此类对象
     * @throws Exception 解析失败或者时间范围不满足要求抛出异常
     */
    static TimeSlot parse(String bookTimeSlotStr, DayOfWeek dayOfWeek) throws Exception {
        Matcher matcher = r.matcher(bookTimeSlotStr);
        if (matcher.matches()) {
            int startTime = Integer.parseInt(matcher.group(1));
            int endTime = Integer.parseInt(matcher.group(2));
            if (!(startTime < endTime)
                    || !(startTime >= 9 && startTime < 22)
                    || !(endTime > 9 && endTime <= 22)) {
                throw new Exception();
            }
            else {
                return new TimeSlot(startTime, endTime, dayOfWeek);
            }
        }
        else {
            throw new Exception();
        }
    }

    /**
     * 获取该时间段预订能得到的收入
     * @return 预订收入
     */
    int getBookIncome() {
        // 先根据周几确定价格方案
        int[] price;
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            price = prices[1];
        }
        else {
            price = prices[0];
        }

        // 再计算时间段的价格和
        int bookIncome = 0;
        for (int time = startTime; time < endTime; ++time) {
            bookIncome += price[time];
        }
        return bookIncome;
    }

    /**
     * 获取该时间段取消预订能得到的收入
     * @return 取消预订收入
     */
    int getCancelBookIncome() {
        // 先根据周几确定价格方案和违约金折扣比例(1 / account)
        int[] price;
        int account = 1;
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            price = prices[1];
            account = 4;
        }
        else {
            price = prices[0];
            account = 2;
        }

        // 再计算时间段的违约金和
        int cancelBookIncome = 0;
        for (int time = startTime; time < endTime; ++time) {
            cancelBookIncome += price[time] / account;
        }
        return cancelBookIncome;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TimeSlot &&
                startTime == ((TimeSlot) obj).startTime &&
                    endTime == ((TimeSlot) obj).endTime;
    }

    @Override
    public int hashCode() {
        return startTime * endTime + startTime + endTime;
    }

    @Override
    public int compareTo(TimeSlot timeSlot) {
        if (this.startTime != timeSlot.startTime) {
            return this.startTime - timeSlot.startTime;
        }
        else {
            return this.endTime - timeSlot.endTime;
        }
    }

    @Override
    public String toString() {
        return String.format("%02d:00~%02d:00", startTime, endTime);
    }

    int getStartTime() {
        return startTime;
    }

    int getEndTime() {
        return endTime;
    }
}
