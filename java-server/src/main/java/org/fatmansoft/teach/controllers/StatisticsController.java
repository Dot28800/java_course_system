package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatisticsDayRepository statisticsDayRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FeeRepository feeRepository;
    @Autowired
    private CourseChooseRepository courseChooseRepository;
    @Autowired
    private MarkRepository markRepository;
    @Autowired
    private StatisticsRepository statisticsRepository;
    @Autowired
    private ConsumptionRepository consumptionRepository;
    @Autowired
    private RankRepository rankRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private HonorRepository honorRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private PracticeRepository practiceRepository;
    public void getAllStudentStatistics()
    {
        List<Student> allStudent = studentRepository.findAll();
        //List<Statistics> statisticsList = statisticsRepository.findAll();

        /*
         *
         * 对每一个学生，计算他的GPA（总学分绩点），未合格课程数，总花费，选课总数
         * */
        List<Statistics> statisticsList=statisticsRepository.findAll();
        List numList=new ArrayList<>();
        for(Student s:allStudent){
            numList.add(s.getPerson().getNum());
        }
        for(Statistics s:statisticsList){
            if(!numList.contains(s.getStudentNum())){
                statisticsRepository.delete(s);

            }
        }
        for (Student s: allStudent) {
            Statistics statistics;
            String onlyNum = s.getNumName();
            onlyNum = onlyNum.substring(0,onlyNum.indexOf("-"));

            Optional<Statistics> tempStatistics = statisticsRepository.findByStudentNum(onlyNum);
            if (tempStatistics.isPresent()){
                statistics = tempStatistics.get();
            }else {
                statistics = new Statistics();
            }
            //新建一个

            //List<CourseChoose> courseChooses = courseChooseRepository.findByStudentStudentId(s.getStudentId());
            double GPA = 0.0;
            //Object tempFee = feeRepository.getMoneyByStudentId(s.getStudentId());
            List<Consumption> consumptionList= consumptionRepository.findByStudentId(s.getStudentId());
            double tempFee=0.0;
            if(!consumptionList.isEmpty()){
                for(Consumption c : consumptionList){
                    tempFee+=c.getMoney();
                }
            }

            double totalFee;
            if (tempFee>0){
                totalFee = (double)tempFee;
            }else {
                totalFee = 0.0;
            }

            List<CourseChoose> courseChooseList = courseChooseRepository.findByStudentStudentId(s.getStudentId());
            List<Mark> sMark = markRepository.getByStudentId(s.getStudentId());
            int markNum = 0;
            for (Mark m: sMark) {
                GPA = GPA + m.getGPA()*Double.parseDouble((m.getCourseChoose().getCourse().getCredit()));
                if(m.getMark()<60){
                    markNum++;
                }
            }
            if (!sMark.isEmpty())
                GPA = 0.0 + GPA * 1.0 / sMark.size();
            else
                GPA = 0.0;


            BigDecimal bd_gpa = new BigDecimal(String.valueOf(GPA));
            BigDecimal rounded_gpa = bd_gpa.setScale(2, RoundingMode.FLOOR);

            BigDecimal bd_money = new BigDecimal(String.valueOf(totalFee));
            BigDecimal rounded_money = bd_money.setScale(2, RoundingMode.FLOOR);
            Integer studentId=s.getStudentId();
            Optional<Rank> op=rankRepository.getByStudentId(studentId);
            if(op.isPresent()){
                Rank r=op.get();
                statistics.setName(r.getStudent().getPerson().getName());
                statistics.setStudentNum(r.getStudent().getPerson().getNum());
                double gpa=r.getAvgGPA();
                statistics.setGpa(String.format("%.2f",gpa));
                statistics.setRank(Integer.valueOf(r.getTotalRank()));

            }

            List<Activity> activityList=activityRepository.findByStudentStudentId(studentId);
            List<Competition> competitionList=competitionRepository.getByStudentStudentId(studentId);
            List<Honor> honorList=honorRepository.findByStudentId(studentId);
            List<Practice> practiceList=practiceRepository.findByStudentId(studentId);
            /*statistics.setName(s.getPerson().getName());*/
            statistics.setSumMoney(rounded_money.toString());
            statistics.setPracticeTimes(String.valueOf(practiceList.size()));
            statistics.setDailyTimes(String.valueOf(activityList.size()));
            statistics.setCompetitionTimes(String.valueOf(competitionList.size()));
            statistics.setHonorTimes(String.valueOf(honorList.size()));
           /* statistics.setGpa(rounded_gpa.toString());
            statistics.setStudentNum(onlyNum);*/
            statistics.setSumOfCourse(courseChooseList.size());
            statistics.setSumOfDisPassCourse(markNum);
            statisticsRepository.save(statistics);
        }


    }
    //普通初始化
    @PostMapping("/getAllStatistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or hasRole('TEACHER')")
    public DataResponse getAllStatistics(@Valid @RequestBody DataRequest dataRequest){
        getAllStudentStatistics();
        List<Statistics> statisticsList = statisticsRepository.findAll();
        List dataList = new ArrayList();
        for (Statistics statistics: statisticsList)
        {
            Map map = new HashMap();
            map.put("name",statistics.getName());
            map.put("studentNum",statistics.getStudentNum());
            map.put("sumMoney",statistics.getSumMoney());
            map.put("practiceTimes",statistics.getPracticeTimes());
            map.put("honorTimes",statistics.getHonorTimes());
            map.put("competitionTimes",statistics.getCompetitionTimes());
            map.put("dailyTimes",statistics.getDailyTimes());
            /*Double courseSum=statistics.getSumOfCourse().doubleValue();
            Double unSum=statistics.getSumOfDisPassCourse().doubleValue();*/
            map.put("sumOfCourse",statistics.getSumOfCourse());
            map.put("sumOfDisPassCourse",statistics.getSumOfDisPassCourse());
            map.put("gpa", statistics.getGpa());
            dataList.add(map);
        }
        return CommonMethod.getReturnData(dataList,"ok");
    }

    //更新之后初始化
    @PostMapping("/getNewData")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or hasRole('TEACHER')")
    public DataResponse getNewData(@Valid @RequestBody DataRequest dataRequest) {
        getAllStudentStatistics();
        List<Statistics> statisticsList = statisticsRepository.findAll();
        List dataList = new ArrayList();
        for (Statistics statistics: statisticsList)
        {
            Map map = new HashMap();
            map.put("name",statistics.getName());
            map.put("studentNum",statistics.getStudentNum());
            map.put("sumMoney",statistics.getSumMoney());
            map.put("sumOfCourse",statistics.getSumOfCourse());
            map.put("sumOfDisPassCourse",statistics.getSumOfDisPassCourse());
            map.put("gpa",statistics.getGpa());
            map.put("practiceTimes",statistics.getPracticeTimes());
            map.put("honorTimes",statistics.getHonorTimes());
            map.put("competitionTimes",statistics.getCompetitionTimes());
            map.put("dailyTimes",statistics.getDailyTimes());
            dataList.add(map);
        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/queryByNumName")
    public DataResponse queryByNumName(@Valid@RequestBody DataRequest dataRequest){
        String numName= dataRequest.getString("numName");
        if(numName==null){
            numName="";
        }
        Map map;
        List<Statistics> statisticsList=statisticsRepository.findByNumName(numName);
        List dataList=new ArrayList<>();
        for(Statistics s:statisticsList){
            if(s!=null){
                map = new HashMap<>();
                map.put("name",s.getName());
                map.put("studentNum",s.getStudentNum());
                map.put("sumMoney",s.getSumMoney());
                map.put("sumOfCourse",s.getSumOfCourse());
                map.put("sumOfDisPassCourse",s.getSumOfDisPassCourse());
                map.put("gpa",s.getGpa());
                map.put("practiceTimes",s.getPracticeTimes());
                map.put("honorTimes",s.getHonorTimes());
                map.put("competitionTimes",s.getCompetitionTimes());
                map.put("dailyTimes",s.getDailyTimes());
                dataList.add(map);
            }
        }
        return CommonMethod.getReturnData(dataList);
    }
    public void localStudentDelete(String num){
        Optional<Statistics> op =statisticsRepository.findByStudentNum(num);
        op.ifPresent(statistics -> statisticsRepository.delete(statistics));
    }






    //---------------------5.4改动


    @PostMapping("/getMainPageData")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or hasRole('TEACHER')")
    public DataResponse getMainPageData(@Valid @RequestBody DataRequest dataRequest) {
        Date day = new Date();
        Date monthDay = DateTimeTool.prevMonth(day);
        List nList;
        int i;
        Integer id;
        Object a[];
        Long l;
        String name;
        Long total = userRepository.count();
        Integer monthCount = userRepository.countLastLoginTime(DateTimeTool.parseDateTime(monthDay,"yyyy-MM-dd")+" 00:00:00");
        Integer dayCount = userRepository.countLastLoginTime(DateTimeTool.parseDateTime(day,"yyyy-MM-dd")+" 00:00:00");
        Map data = new HashMap();
        Map m = new HashMap();
        m.put("total",total.intValue());
        m.put("monthCount",monthCount);
        m.put("dayCount",dayCount);
        data.put("onlineUser", m);
        nList = userRepository.getCountList();
        List userTypeList = new ArrayList();
        for(i= 0;i < nList.size();i++) {
            m = new HashMap();
            a = (Object[])nList.get(i);
            id = (Integer)a[0];
            l = (Long)a[1];
            if(id == 1)
                name = "管理员";
            else if(id == 2)
                name = "学生";
            else if(id == 3)
                name = "教师";
            else
                name = "";
            m.put("name", name);
            m.put("value",l.intValue());
            userTypeList.add(m);
        }
        data.put("userTypeList", userTypeList);
        List requestList= new ArrayList();
        List operateList = new ArrayList();
        List<StatisticsDay>sList = statisticsDayRepository.findListByDay(DateTimeTool.parseDateTime(monthDay,"yyyyMMdd"),DateTimeTool.parseDateTime(day,"yyyyMMdd"));
        List<String> dayList = new ArrayList();
        List<String> lList = new ArrayList();
        List<String> rList = new ArrayList();
        List<String> cList = new ArrayList();
        List<String> mList = new ArrayList();
        for(StatisticsDay s:sList) {
            dayList.add(s.getDay());
            lList.add(""+s.getLoginCount());
            rList.add(""+s.getRequestCount());
            cList.add(""+s.getCreateCount());
            mList.add(""+s.getLoginCount());
        }
        m = new HashMap();
        m.put("value",dayList);
        m.put("label1",lList);
        m.put("label2",rList);
        data.put("requestData", m);
        m = new HashMap();
        m.put("value",dayList);
        m.put("label1",cList);
        m.put("label2",mList);
        data.put("operateData", m);



        return CommonMethod.getReturnData(data);
    }

   /* @PostMapping("/getMainPageData")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or hasRole('TEACHER')")
    public DataResponse getMainPageData(@Valid @RequestBody DataRequest dataRequest) {
        Date day = new Date();
        Date monthDay = DateTimeTool.prevMonth(day);
        List nList;
        int i;
        Integer id;
        Object a[];
        Long l;
        String name;
        Long total = userRepository.count();
        Integer monthCount = userRepository.countLastLoginTime(DateTimeTool.parseDateTime(monthDay,"yyyy-MM-dd")+" 00:00:00");
        Integer dayCount = userRepository.countLastLoginTime(DateTimeTool.parseDateTime(day,"yyyy-MM-dd")+" 00:00:00");
        Map data = new HashMap();
        Map m = new HashMap();
        m.put("total",total.intValue());
        m.put("monthCount",monthCount);
        m.put("dayCount",dayCount);
        data.put("onlineUser", m);
        nList = userRepository.getCountList();
        List userTypeList = new ArrayList();
        for(i= 0;i < nList.size();i++) {
            m = new HashMap();
            a = (Object[])nList.get(i);
            id = (Integer)a[0];
            l = (Long)a[1];
            if(id == 1)
                name = "管理员";
            else if(id == 2)
                name = "学生";
            else if(id == 3)
                name = "教师";
            else
                name = "";
            m.put("name", name);
            m.put("value",l.intValue());
            userTypeList.add(m);
        }
        data.put("userTypeList", userTypeList);
        List requestList= new ArrayList();
        List operateList = new ArrayList();
        List<StatisticsDay>sList = statisticsDayRepository.findListByDay(DateTimeTool.parseDateTime(monthDay,"yyyyMMdd"),DateTimeTool.parseDateTime(day,"yyyyMMdd"));
        List<String> dayList = new ArrayList();
        List<String> lList = new ArrayList();
        List<String> rList = new ArrayList();
        List<String> cList = new ArrayList();
        List<String> mList = new ArrayList();
        for(StatisticsDay s:sList) {
            dayList.add(s.getDay());
            lList.add(""+s.getLoginCount());
            rList.add(""+s.getRequestCount());
            cList.add(""+s.getCreateCount());
            mList.add(""+s.getLoginCount());
        }
        m = new HashMap();
        m.put("value",dayList);
        m.put("label1",lList);
        m.put("label2",rList);
        data.put("requestData", m);
        m = new HashMap();
        m.put("value",dayList);
        m.put("label1",cList);
        m.put("label2",mList);
        data.put("operateData", m);



        return CommonMethod.getReturnData(data);
    }*/
}
