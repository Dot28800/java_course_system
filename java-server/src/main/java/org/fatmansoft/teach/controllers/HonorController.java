package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.CourseAttendance;
import org.fatmansoft.teach.models.Honor;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.HonorRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/honor")
public class HonorController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private HonorRepository honorRepository;
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
    @PostMapping("/getHonorList")
    public DataResponse getCourseAttendanceList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId = 0;

        List<Honor> cList=honorRepository.findByStudentStudentId(studentId);
        List dataList=new ArrayList<>();
        Map m;
        for(Honor s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("honorId", s.getHonorId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("host",s.getHost());
                m.put("level",s.getLevel());
                m.put("student",s.getStudent());
                m.put("title",s.getTitle());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                m.put("time",s.getTime());
                m.put("type",s.getType());
                dataList.add(m);
            }


        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getHonorListByNumName")
    public DataResponse getHonorList(@Valid @RequestBody DataRequest dataRequest){
        String num= dataRequest.getString("num");
        if(num==null){
            num="";
        }
        String name= dataRequest.getString("name");
        if(name==null){
            name="";
        }
        String level= dataRequest.getString("level");
        if(level==null){
            level="";
        }
        List<Honor> honorList=honorRepository.findByNumNameLevel(num,name,level);
        List dataList=new ArrayList<>();
        Map m;
        for(Honor s:honorList){
            if (s != null) {
                m = new HashMap();
                m.put("honorId", s.getHonorId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("host",s.getHost());
                m.put("level",s.getLevel());
                m.put("student",s.getStudent());
                m.put("title",s.getTitle());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                m.put("time",s.getTime());
                m.put("type",s.getType());
                dataList.add(m);
            }


        }
        return CommonMethod.getReturnData(dataList);

    }
    @PostMapping("/newHonor")
    public DataResponse newHonor(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId= dataRequest.getInteger("studentId");
        Optional<Student> op=studentRepository.findById(studentId);
        if(!op.isPresent() ){
            return CommonMethod.getReturnMessageError("该学生不存在");

        }
        String newHost= dataRequest.getString("newHost");
        String newLevel=dataRequest.getString("newLevel");
        String newTime=dataRequest.getString("newTime");
        String newTitle= dataRequest.getString("newTitle");
        String newType= dataRequest.getString("newType");
        Honor h=new Honor();
        h.setStudent(op.get());
        h.setTime(newTime);
        h.setHost(newHost);
        h.setTitle(newTitle);
        h.setLevel(newLevel);
        h.setType(newType);
        honorRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/deleteHonor")
    public DataResponse deleteHonor(@Valid@RequestBody DataRequest dataRequest) {
        Integer honorId= dataRequest.getInteger("honorId");
        if(honorId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Honor> op=honorRepository.findById(honorId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");

        }
        Honor h= op.get();
        honorRepository.delete(h);
        return CommonMethod.getReturnMessageOK();
    }
    public void localHonorDelete(Integer studentId){
        List<Honor> list=honorRepository.findByStudentId(studentId);
        honorRepository.deleteAll(list);
    }
    @PostMapping("/honorEditSave")
    public DataResponse honorEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer honorId=dataRequest.getInteger("honorId");
        if(honorId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Honor> op=honorRepository.findById(honorId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");

        }
        Honor h=op.get();
        String title= dataRequest.getString("title");
        String time= dataRequest.getString("time");
        String host= dataRequest.getString("host");
        String type= dataRequest.getString("type");
        String level= dataRequest.getString("level");
        h.setType(type);
        h.setTime(time);
        h.setTitle(title);
        h.setHost(host);
        h.setLevel(level);
        honorRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    public void localDeleteStudent(Integer studentId){
        List<Honor> honorList=honorRepository.findByStudentId(studentId);
        honorRepository.deleteAll(honorList);
    }
}
