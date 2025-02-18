package org.fatmansoft.teach.controllers;


import org.fatmansoft.teach.models.Innovative;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.InnovativeRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/innovative")
public class InnovativeController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private InnovativeRepository innovativeRepository;

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
    @PostMapping("/getInnovativeList")
    public DataResponse getCourseAttendanceList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        Integer innovativeType = dataRequest.getInteger("innovativeType");
        if(studentId == null)
            studentId = 0;
        List<Innovative> iList = innovativeRepository.findByStudentStudentId(studentId);
        List dataList = new ArrayList<>();
        Map m;
        for(Innovative s : iList){
            if (s != null && !Objects.equals(s.getInnovativeType(), innovativeType)) continue;
            if (s != null) {
                m = new HashMap();
                m.put("innovativeId", s.getInnovativeId()+"");
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
    @PostMapping("/newInnovative")
    public DataResponse newInnovative(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId= dataRequest.getInteger("studentId");
        Optional<Student> op = studentRepository.findById(studentId);
        if(!op.isPresent() ){
            return CommonMethod.getReturnMessageError("该学生不存在");
        }
        String newHost= dataRequest.getString("newHost");
        String newLevel=dataRequest.getString("newLevel");
        String newTime=dataRequest.getString("newTime");
        String newTitle= dataRequest.getString("newTitle");
        String newType= dataRequest.getString("newType");
        Innovative h = new Innovative();
        h.setStudent(op.get());
        h.setTime(newTime);
        h.setHost(newHost);
        h.setTitle(newTitle);
        h.setLevel(newLevel);
        h.setType(newType);
        h.setInnovativeType(dataRequest.getInteger("innovativeType"));

        innovativeRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/deleteInnovative")
    public DataResponse deleteInnovative(@Valid@RequestBody DataRequest dataRequest) {
        Integer innovativeId = dataRequest.getInteger("innovativeId");
        if(innovativeId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Innovative> op = innovativeRepository.findById(innovativeId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Innovative h= op.get();
        innovativeRepository.delete(h);
        return CommonMethod.getReturnMessageOK();
    }
    public void localInnovativeDelete(Integer studentId){
        List<Innovative> list = innovativeRepository.findByStudentId(studentId);
        innovativeRepository.deleteAll(list);
    }
    @PostMapping("/innovativeEditSave")
    public DataResponse InnovativeEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer honorId=dataRequest.getInteger("innovativeId");
        if(honorId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Innovative> op = innovativeRepository.findById(honorId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Innovative h = op.get();
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
        innovativeRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
}
