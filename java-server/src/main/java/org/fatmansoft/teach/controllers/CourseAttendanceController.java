package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courseAttendance")
public class CourseAttendanceController  {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseAttendanceRepository courseAttendanceRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseChooseRepository courseChooseRepository;

    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Student s : sList) {
            itemList.add(new OptionItem( s.getStudentId(),s.getStudentId()+"", s.getPerson().getNum()+"-"+s.getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }
    @PostMapping("/getCourseItemOptionList")
    public OptionItemList getCourseItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Course> sList = courseRepository.findAll();  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Course c : sList) {
            itemList.add(new OptionItem(c.getCourseId(),c.getCourseId()+"", c.getNum()+"-"+c.getName()));
        }
        return new OptionItemList(0, itemList);
    }

    @PostMapping("/getCourseAttendanceList")
    public DataResponse getCourseAttendanceList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId = 0;
        Integer courseId = dataRequest.getInteger("courseId");
        if(courseId == null)
            courseId = 0;
        List<CourseAttendance> cList=courseAttendanceRepository.findByStudentCourse(studentId,courseId);
        List dataList=new ArrayList<>();
        Map m;
        for(CourseAttendance s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("courseAttendanceId", s.getAttendanceId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("courseId",s.getCourse().getCourseId()+"");
                m.put("courseName",s.getCourse().getName());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                m.put("className",s.getStudent().getClassName());
                m.put("time",s.getTime());
                m.put("flag",s.getFlag());
                dataList.add(m);
            }
            /*m = new HashMap();
            m.put("scoreId", s.getAttendanceId()+"");
            m.put("studentId",s.getStudent().getStudentId()+"");
            m.put("courseId",s.getCourse().getCourseId()+"");
            m.put("courseName",s.getCourse().getName());
            m.put("studentNum",s.getStudent().getPerson().getNum());
            m.put("studentName",s.getStudent().getPerson().getName());
            m.put("className",s.getStudent().getClassName());
            m.put("time",s.getTime());
            m.put("flag",s.getFlag());
            dataList.add(m);*/

        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getListByNumName")
    public DataResponse getList(@Valid @RequestBody DataRequest dataRequest){
        String num= dataRequest.getString("num");
        if(num==null){
            num="";
        }
        String name= dataRequest.getString("name");
        if(name==null){
            name="";
        }
        Integer courseId = dataRequest.getInteger("courseId");
        if(courseId == null)
            courseId = 0;
        List<CourseAttendance> attendanceList=courseAttendanceRepository.findByNumName(num,name,courseId);
        List dataList=new ArrayList<>();
        Map m;
        for(CourseAttendance s:attendanceList){
            if (s != null) {
                m = new HashMap();
                m.put("courseAttendanceId", s.getAttendanceId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("courseId",s.getCourse().getCourseId()+"");
                m.put("courseName",s.getCourse().getName());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                m.put("className",s.getStudent().getClassName());
                m.put("time",s.getTime());
                m.put("flag",s.getFlag());
                dataList.add(m);
            }
            /*m = new HashMap();
            m.put("scoreId", s.getAttendanceId()+"");
            m.put("studentId",s.getStudent().getStudentId()+"");
            m.put("courseId",s.getCourse().getCourseId()+"");
            m.put("courseName",s.getCourse().getName());
            m.put("studentNum",s.getStudent().getPerson().getNum());
            m.put("studentName",s.getStudent().getPerson().getName());
            m.put("className",s.getStudent().getClassName());
            m.put("time",s.getTime());
            m.put("flag",s.getFlag());
            dataList.add(m);*/

        }
        return CommonMethod.getReturnData(dataList);

    }
    @PostMapping("/courseAttendanceEditSave")
    public  DataResponse courseAttendanceEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        Integer courseId = dataRequest.getInteger("courseId");
        Integer attendanceId=dataRequest.getInteger("courseAttendanceId");
        String time = dataRequest.getString("time");
        String flag=dataRequest.getString("flag");
        if(flag.equals("1")){
            flag="是";
        }else if(flag.equals("2")){
            flag="否";
        }
        Optional<CourseChoose> chooseOp=courseChooseRepository.findOpByStudentCourse(studentId,courseId);
        if(!chooseOp.isPresent()){
            return CommonMethod.getReturnMessageError("该学生没有选这门课，请再仔细核对一下。");
        }
        Optional<CourseAttendance> op;
        CourseAttendance s=null;
        if(attendanceId!=null){
            op=courseAttendanceRepository.findById(attendanceId);
            if(op.isPresent()){
                s=op.get();

            }
        }
        if(s == null) {
            s = new CourseAttendance();
            s.setStudent(studentRepository.findById(studentId).get());
            s.setCourse(courseRepository.findById(courseId).get());
        }
        s.setTime(time);
        s.setFlag(flag);
        courseAttendanceRepository.save(s);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/deleteCourseAttendance")
    public DataResponse deleteCourseAttendance(@Valid@RequestBody DataRequest dataRequest){
        Integer courseAttendanceId=dataRequest.getInteger("courseAttendanceId");
        Optional<CourseAttendance> op;
        CourseAttendance c=null;
        if(courseAttendanceId!=null){

            op=courseAttendanceRepository.findById(courseAttendanceId);
            if(op.isPresent()){
                c=op.get();
                courseAttendanceRepository.delete(c);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/newCourseAttendance")
    public  DataResponse newCourseAttendance(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId=dataRequest.getInteger("studentId");
        Integer courseId= dataRequest.getInteger("courseId");
        String newTime= dataRequest.getString("newTime");
        String newFlag= dataRequest.getString("newFlag");
        newFlag=newFlag.equals("1")?"是":"否";
        Optional<CourseChoose> chooseOp=courseChooseRepository.findOpByStudentCourse(studentId,courseId);
        if(!chooseOp.isPresent()){
            return CommonMethod.getReturnMessageError("该学生没有选这门课，请再仔细核对一下。");
        }

        List<CourseAttendance> list=courseAttendanceRepository.findByCourseAndStudent(studentId,courseId);
        if(list!=null && !list.isEmpty()){
            for(CourseAttendance c:list){
                if(c.getTime().equals(newTime)){
                    return CommonMethod.getReturnMessageError("该学生对应课程记录已经存在，请选择修改而不要重复添加");
                }
            }
        }


        CourseAttendance c=new CourseAttendance();
        c.setStudent(studentRepository.findById(studentId).get());
        c.setCourse(courseRepository.findById(courseId).get());
        c.setAttendanceId(courseAttendanceRepository.getMaxId()+1);
        c.setTime(newTime);
        c.setFlag(newFlag);
        courseAttendanceRepository.save(c);
        return CommonMethod.getReturnMessageOK();
    }
    public void localDeleteCourse(Integer courseId){
        List<CourseAttendance> attendanceListList=courseAttendanceRepository.findByCourse_CourseId(courseId);
        courseAttendanceRepository.deleteAll(attendanceListList);
    }

    public void localDeleteStudent(Integer studentId){
        List<CourseAttendance> scoreList=courseAttendanceRepository.findByStudentStudentId(studentId);
        courseAttendanceRepository.deleteAll(scoreList);
    }

    public void chooseDelete(Integer studentId,Integer courseId){
        List<CourseAttendance> list=courseAttendanceRepository.findByCourseAndStudent(studentId,courseId);
        courseAttendanceRepository.deleteAll(list);
    }
}
