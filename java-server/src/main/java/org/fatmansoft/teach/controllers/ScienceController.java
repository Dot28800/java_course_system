package org.fatmansoft.teach.controllers;

import org.apache.poi.ss.formula.functions.T;
import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.fatmansoft.teach.util.JsonConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/science")

public class ScienceController {
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;  //学生数据操作自动注入
    @Autowired
    private ScienceRepository scienceRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入

    @PostMapping("/getScienceList")

    public DataResponse  getScienceList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        if(numName == null)
            numName ="";
        String teacherName= dataRequest.getString("teacherName");
        if(teacherName==null){
            teacherName="";
        }
        List<Science> sList = scienceRepository.findScienceListByNumName(numName,teacherName);
        List dataList = new ArrayList();
        Map m;
        for (Science s : sList) {
            m = new HashMap();
            m.put("scienceId",s.getScienceId()+"");
            m.put("studentId",s.getStudent().getStudentId()+"");
            m.put("studentNum", s.getStudent().getPerson().getNum());
            m.put("studentName",s.getStudent().getPerson().getName());
            m.put("achievement",s.getAchievement());
            m.put("teacherName",s.getTeacherName());
            m.put("projectName",s.getProjectName());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);

    }
    @PostMapping("/getList")

    public DataResponse  getScienceListByStudent(@Valid @RequestBody DataRequest dataRequest) {
        String num = dataRequest.getString("num");
        String name= dataRequest.getString("name");
        String teacherName= dataRequest.getString("teacherName");
        if(teacherName==null){
            teacherName="";
        }
        List<Science> sList = scienceRepository.getOwn(num,name,teacherName);
        List dataList = new ArrayList();
        Map m;
        for (Science s : sList) {
            m = new HashMap();
            m.put("scienceId",s.getScienceId()+"");
            m.put("studentId",s.getStudent().getStudentId()+"");
            m.put("studentNum", s.getStudent().getPerson().getNum());
            m.put("studentName",s.getStudent().getPerson().getName());
            m.put("achievement",s.getAchievement());
            m.put("teacherName",s.getTeacherName());
            m.put("projectName",s.getProjectName());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);

    }



    @PostMapping("/deleteScience")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse scienceDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer scienceId = dataRequest.getInteger("scienceId");  //获取student_id值
        Science s = null;
        Optional<Science> op;
        if (scienceId != null) {
            op = scienceRepository.findById(scienceId);   //查询获得实体对象
            if (op.isPresent()) {
                s = op.get();
            }
        }
        if (s != null) {
            scienceRepository.delete(s);    //首先数据库永久删除科研信息
        }else{
            return CommonMethod.getReturnMessageError("该对象为空");
        }
        // 然后数据库永久删除学生信息

        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    /**
     * getStudentInfo 前端点击学生列表时前端获取学生详细信息请求服务
     *
     * @param dataRequest 从前端获取 studentId 查询学生信息的主键 student_id
     * @return 根据studentId从数据库中查出数据，存在Map对象里，并返回前端
     */

    /*@PostMapping("/getTeacherInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public String getScienceInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer scienceId = dataRequest.getInteger("scienceId");
        Science s = null;
        Optional<Science> op;
        if (scienceId != null) {
            op = scienceRepository.findById(scienceId); //根据学生主键从数据库查询学生的信息
            if (op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new Science();
            //s.setScience(new Science());
        }
        return JsonConvertUtil.getDataObjectJson(s);
    }*/

    @PostMapping("/scienceEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse scienceEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer studentId = dataRequest.getInteger("scienceId");
        if(studentId==null){
            return CommonMethod.getReturnMessageOK("缺少记录ID");
        }
        String projectName= dataRequest.getString("projectName");
        String achievement= dataRequest.getString("achievement");
        String teacherName= dataRequest.getString("teacherName");

        Optional<Science> op=scienceRepository.findById(studentId);
        Science s=null;
        if(op.isPresent()){
            s=op.get();
        }else{
            return CommonMethod.getReturnMessageError("找不到对象");
        }
        s.setProjectName(projectName);
        s.setAchievement(achievement);
        s.setTeacherName(teacherName);
        scienceRepository.save(s);
        return CommonMethod.getReturnMessageOK();  // 将studentId返回前端
    }
    @PostMapping("/newScience")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse scienceNewSave(@Valid @RequestBody DataRequest dataRequest) {
        String num= dataRequest.getString("num");
        String name= dataRequest.getString("name");
        Optional<Student> op=studentRepository.getByNumAndName(num,name);
        Student s;
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这个学生");
        }
        s=op.get();
        String projectName= dataRequest.getString("projectName");
        String achievement= dataRequest.getString("achievement");
        String teacherName= dataRequest.getString("teacherName");
        Science science=new Science();
        science.setStudent(s);
        science.setProjectName(projectName);
        science.setAchievement(achievement);
        science.setTeacherName(teacherName);
        scienceRepository.save(science);

        return CommonMethod.getReturnMessageOK();  // 将studentId返回前端
    }

    public void localStudentDelete(Integer studentId){
        List<Science> list=scienceRepository.getByStudentId(studentId);
        scienceRepository.deleteAll(list);
    }

}
