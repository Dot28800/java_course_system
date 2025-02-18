package org.fatmansoft.teach.controllers;


import org.fatmansoft.teach.init.MarkInit;
import org.fatmansoft.teach.init.RankInit;
import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.CourseChoose;
import org.fatmansoft.teach.models.Mark;
import org.fatmansoft.teach.models.Student;
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
@RequestMapping("/api/mark")
public class MarkController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private MarkRepository markRepository;
    @Autowired
    private CourseChooseRepository courseChooseRepository;
    @Autowired
    private MarkInit markInit;
    @Autowired
    private RankInit rankInit;
    @Autowired
    private RankRepository rankRepository;

    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Student s : sList) {
            itemList.add(new OptionItem(s.getStudentId(), s.getStudentId() + "", s.getPerson().getNum() + "-" + s.getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }

    @PostMapping("/getCourseItemOptionList")
    public OptionItemList getCourseItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Course> sList = courseRepository.findAll();  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Course c : sList) {
            itemList.add(new OptionItem(c.getCourseId(), c.getCourseId() + "", c.getNum() + "-" + c.getName()));
        }
        return new OptionItemList(0, itemList);
    }

    @PostMapping("/getMarkList")
    public DataResponse getMarkList(@Valid @RequestBody DataRequest dataRequest) {
        markInit.initialize();
        Integer studentId = dataRequest.getInteger("studentId");
        if (studentId == null)
            studentId = 0;
        Integer courseId = dataRequest.getInteger("courseId");
        if (courseId == null)
            courseId = 0;

        List<Mark> markList = markRepository.getByCourseOrStudent(studentId, courseId);
        List dataList = new ArrayList<>();
        Map m;
        for (Mark s : markList) {
            if (s != null) {
                m = new HashMap();
                m.put("markId", s.getMarkId() + "");
                m.put("studentId", s.getCourseChoose().getStudent().getStudentId() + "");
                m.put("courseId", s.getCourseChoose().getCourse().getCourseId() + "");
                m.put("courseName", s.getCourseChoose().getCourse().getName());
                m.put("credit", s.getCourseChoose().getCourse().getCredit());
                m.put("studentNum", s.getCourseChoose().getStudent().getPerson().getNum());
                m.put("studentName", s.getCourseChoose().getStudent().getPerson().getName());
                if(s.getMark().toString().length()>4){
                    m.put("mark", s.getMark().toString().substring(0,4));
                }else {
                    m.put("mark", s.getMark().toString());
                }
                if(s.getGPA().toString().length()>4){
                    m.put("GPA",s.getGPA().toString().substring(0,4));
                }else{
                    m.put("GPA",s.getGPA().toString());
                }
                m.put("rank", s.getRank());

                m.put("teacher", s.getCourseChoose().getTeacher());
               /* m.put("time",s.getTime());
                m.put("flag",s.getFlag());*/
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getMarkListByNumName")
    public DataResponse getMarkListByNumName(@Valid@RequestBody DataRequest dataRequest){
        String num= dataRequest.getString("num");
        if(num==null){
            num="";
        }
        String name= dataRequest.getString("name");
        if(name==null){
            name="";
        }
        Integer courseId=dataRequest.getInteger("courseId");
        if(courseId==null){
            courseId=0;
        }
        List<Mark > markList=markRepository.getByStudentNumName(num,name,courseId);
        List dataList = new ArrayList<>();
        Map m;
        for (Mark s : markList) {
            if (s != null) {
                m = new HashMap();
                m.put("markId", s.getMarkId() + "");
                m.put("studentId", s.getCourseChoose().getStudent().getStudentId() + "");
                m.put("courseId", s.getCourseChoose().getCourse().getCourseId() + "");
                m.put("courseName", s.getCourseChoose().getCourse().getName());
                m.put("credit", s.getCourseChoose().getCourse().getCredit());
                m.put("studentNum", s.getCourseChoose().getStudent().getPerson().getNum());
                m.put("studentName", s.getCourseChoose().getStudent().getPerson().getName());
                if(s.getMark().toString().length()>4){
                    m.put("mark", s.getMark().toString().substring(0,4));
                }else {
                    m.put("mark", s.getMark().toString());
                }
                if(s.getGPA().toString().length()>4){
                    m.put("GPA",s.getGPA().toString().substring(0,4));
                }else{
                    m.put("GPA",s.getGPA().toString());
                }
                m.put("rank", s.getRank());

                m.put("teacher", s.getCourseChoose().getTeacher());
               /* m.put("time",s.getTime());
                m.put("flag",s.getFlag());*/
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);
     }
    @PostMapping("/refresh")
    public DataResponse refresh(@Valid@RequestBody DataRequest dataRequest){
        try{
            markInit.initialize();
        }catch (Exception e){
            return CommonMethod.getReturnMessageError("刷新失败");
        }
        return CommonMethod.getReturnMessageOK();

    }
    public void localCourseDelete(Integer courseId){
        List<Mark> markList=markRepository.getByCourseId(courseId);
        markRepository.deleteAll(markList);
        rankInit.initialize();
    }
    public void localStudentDelete(Integer studentId){
        List<Mark> studentList=markRepository.getByStudentId(studentId);
        markRepository.deleteAll(studentList);
        rankInit.initialize();
    }

    public void localChooseDelete(Integer courseChooseId){
        Optional<Mark> list=markRepository.getByCourseChooseId(courseChooseId);
        list.ifPresent(mark -> markRepository.delete(mark));
    }
}