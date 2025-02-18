package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.CourseAttendance;
import org.fatmansoft.teach.models.CourseChoose;
import org.fatmansoft.teach.models.Score;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.JsonConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController//java bean controll可拷贝
@RequestMapping("/api/course")

public class CourseController {
    @Autowired
    private CourseRepository courseRepository;//自动注入
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CourseAttendanceRepository courseAttendanceRepository;
    @Autowired
    private CourseChooseRepository courseChooseRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private CourseAttendanceController courseAttendanceController;
    @Autowired
    private CourseChooseController courseChooseController;
    @Autowired
    private ScoreController scoreController;
    @Autowired
    private MarkController markController;
    @Autowired
    private HomeWorkController homeWorkController;

    @PostMapping("/getCourseList")//对应前端中某一个请求
    public DataResponse getCourseList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        if(numName == null)
            numName = "";
        List<Course> cList = courseRepository.findCourseListByNumName(numName);  //数据库查询操作
        List dataList = new ArrayList();
        Map m;
        Course pc;
        for (Course c : cList) {
            m = new HashMap();
            m.put("courseId", c.getCourseId()+"");
            m.put("num",c.getNum());
            m.put("name",c.getName());
            m.put("credit",c.getCredit());
            m.put("time",c.getTime());
            m.put("position",c.getPosition());
            m.put("resource",c.getResource());
            m.put("preCourse",c.getPreCourse());
            m.put("type",c.getType());
            m.put("exam",c.getExam());

            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/courseSave")
    public DataResponse courseSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        String num = dataRequest.getString("num");
        String name = dataRequest.getString("name");
        String credit = dataRequest.getString("credit");
        String time = dataRequest.getString("time");
        String position = dataRequest.getString("position");
        String type = dataRequest.getString("type");
        String preCourse = dataRequest.getString("preCourse");
        String exam = dataRequest.getString("exam");
        String resource = dataRequest.getString("resource");

        Optional<Course> op;
        Course c= null;

        if(courseId != null) {
            op = courseRepository.findById(courseId);
            if(op.isPresent())
                c= op.get();
        }

        if(num==null || name==null){
            return CommonMethod.getReturnMessageError("信息不完整，请重新输入");
        }
        Optional<Course> op1,op2;
        op1=courseRepository.findByNum(num);
        op2=courseRepository.findByName(name);
        /*if((op1.isPresent() || op2.isPresent()) && c.getCredit().equals(credit)){
            return CommonMethod.getReturnMessageError("该课程号或课程名称已经存在，请勿更改为此");
        }*/
        c.setNum(num);
        c.setName(name);
        c.setCredit(credit);
        c.setTime(time);
        c.setPosition(position);
        c.setType(type);
        c.setPreCourse(preCourse);
        c.setExam(exam);
        c.setResource(resource);

        courseRepository.save(c);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/courseDelete")
    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        Optional<Course> op;
        Optional<CourseAttendance> op1;
        Optional<CourseChoose> op2;
        Optional<Score> op3;
        Course c= null;
        if(courseId != null) {
            markController.localCourseDelete(courseId);
            homeWorkController.localCourseDelete(courseId);
            courseAttendanceController.localDeleteCourse(courseId);
            courseChooseController.localChooseDelete(courseId);
            scoreController.localDeleteCourse(courseId);

            op = courseRepository.findById(courseId);
            if(op.isPresent()) {
                c = op.get();
                courseRepository.delete(c);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/newCourse")
    public DataResponse courseNew(@Valid@RequestBody DataRequest dataRequest){
        String num= dataRequest.getString("num");
        String name=dataRequest.getString("name");
        String credit= dataRequest.getString("credit");
        String time = dataRequest.getString("time");
        String position = dataRequest.getString("position");
        String type = dataRequest.getString("type");
        String preCourse = dataRequest.getString("preCourse");
        String exam = dataRequest.getString("exam");
        String resource = dataRequest.getString("resource");

        Optional<Course> op;
        Course c= null;
        op=courseRepository.findByNumOrName(num,name);
        /*if(op.isPresent()){
            return CommonMethod.getReturnMessageError("该课程已经存在，请勿重复添加");
        }*/
        c=new Course();
        c.setNum(num);
        c.setName(name);
        c.setCredit(credit);
        c.setTime(time);
        c.setPosition(position);
        c.setType(type);
        c.setPreCourse(preCourse);
        c.setExam(exam);
        c.setResource(resource);

        //c.setCourseId(courseRepository.getMaxId()+1);
        courseRepository.save(c);
        return CommonMethod.getReturnMessageOK();
    }

}
