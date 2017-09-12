import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SiteBookSystem {
    /**
     * 表示各场地的预订情况，初始加入四个场地
     */
    private static Map<String, SiteBook> siteBookMap = new HashMap<String, SiteBook>(){{
        put("A", new SiteBook("A"));
        put("B", new SiteBook("B"));
        put("C", new SiteBook("C"));
        put("D", new SiteBook("D"));
    }};
    /**
     * 表示预订日期
     */
    private static LocalDate bookDate;
    /**
     * 表示预订的时间段
     */
    private static TimeSlot bookTimeSlot;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                System.out.println("收入汇总");
                System.out.println("---");
                int siteNum = siteBookMap.size();
                int income = 0;
                for (SiteBook siteBook : siteBookMap.values()) {
                    System.out.print(siteBook);
                    if (--siteNum > 0) {
                        System.out.println();
                    }
                    income += siteBook.getIncome();
                }
                System.out.println("---");
                System.out.println("总计：" + income + "元");
            }
            else {
                String[] params = line.split("\\s+");

                // 仅处理参数长度有效的指令
                BookState bookState = BookState.invalidBook;
                if (params.length == 4 || params.length == 5) {
                    String userId = params[0];
                    String bookDateStr = params[1];
                    String bookTimeSlotStr = params[2];
                    String site = params[3];

                    bookState = SiteBookSystem.checkBookDateTimeSite(bookDateStr, bookTimeSlotStr, site);
                    // 有效的预订指令
                    if (params.length == 4 && bookState == BookState.validBook) {
                        SiteBook siteBook = siteBookMap.get(site);
                        // 尝试去预订
                        if (siteBook.tryBook(userId, bookDate, bookTimeSlot)) {
                            bookState = BookState.acceptedBook;
                        }
                        else {
                            bookState = BookState.conflictsBook;
                        }
                    }
                    // 有效的取消预订指令
                    else if (params.length == 5 && bookState == BookState.validBook) {
                        // 进一步判断是否有效
                        String cancelFlag = params[4];
                        if (!cancelFlag.equals("C")) {
                            bookState = BookState.invalidBook;
                        }
                        else {
                            SiteBook siteBook = siteBookMap.get(site);
                            // 尝试去取消预订
                            if (siteBook.tryCancelBook(userId, bookDate, bookTimeSlot)) {
                                bookState = BookState.acceptedBook;
                            }
                            else {
                                bookState = BookState.notExistBook;
                            }
                        }
                    }
                }
                System.out.println(bookState);
            }
        }
    }

    /**
     * 检查输入参数的日期、时间段、地点是否初步合法
     * @param bookDateStr 日期字符串
     * @param bookTimeSlotStr 时间段字符串
     * @param site 地点
     * @return 合法:validBook,非法:invalidBook
     */
    private static BookState checkBookDateTimeSite(String bookDateStr, String bookTimeSlotStr, String site) {
        try {
            bookDate = LocalDate.parse(bookDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            bookTimeSlot = TimeSlot.parse(bookTimeSlotStr, bookDate.getDayOfWeek());
        }
        catch (Exception e) {
            return BookState.invalidBook;
        }
        if (site.equals("A") || site.equals("B") || site.equals("C") || site.equals("D")) {
            return BookState.validBook;
        }
        else {
            return BookState.invalidBook;
        }
    }
}
