package org.fatmansoft.teach.controllers;
import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.PracticeRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;
import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/practice")
@RestController
public class PracticeController {
    @Autowired
    private PracticeRepository practiceRepository;
    @Autowired
    private StudentRepository studentRepository;
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
    @PostMapping("/getPracticeList")
    public DataResponse getPracticeList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId = 0;

        List<Practice> cList=practiceRepository.findByStudentStudentId(studentId);
        List dataList=new ArrayList<>();
        Map m;
        for(Practice s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("practiceId", s.getPracticeId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("student",s.getStudent());
                m.put("place",s.getPlace());
                m.put("time",s.getTime());
                m.put("evaluation",s.getEvaluation());
                m.put("type",s.getType());
                m.put("teamName",s.getTeamName());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getListByNumName")
    public DataResponse getList(@Valid @RequestBody DataRequest dataRequest) {
        String num= dataRequest.getString("num");
        String name= dataRequest.getString("name");
        List<Practice> practiceList=practiceRepository.findByStudentNumAndName(num,name);
        List dataList=new ArrayList<>();
        Map m;
        for(Practice s:practiceList){
            if (s != null) {
                m = new HashMap();
                m.put("practiceId", s.getPracticeId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("student",s.getStudent());
                m.put("place",s.getPlace());
                m.put("time",s.getTime());
                m.put("evaluation",s.getEvaluation());
                m.put("type",s.getType());
                m.put("teamName",s.getTeamName());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/newPractice")
    public DataResponse newPractice(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId= dataRequest.getInteger("studentId");
        Optional<Student> op=studentRepository.findById(studentId);
        if(!op.isPresent() ){
            return CommonMethod.getReturnMessageError("该学生不存在");
        }
        String newPlace= dataRequest.getString("newPlace");
        String newTime=dataRequest.getString("newTime");
        String newEvaluation=dataRequest.getString("newEvaluation");
        String newType=dataRequest.getString("newType");
        String newTeamName=dataRequest.getString("newTeamName");
        Practice h=new Practice();
        h.setStudent(op.get());
        h.setPlace(newPlace);
        h.setTime(newTime);
        h.setEvaluation(newEvaluation);
        h.setTeamName(newTeamName);
        h.setType(newType);
        practiceRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/deletePractice")
    public DataResponse deletePractice(@Valid@RequestBody DataRequest dataRequest){
        Integer practiceId= dataRequest.getInteger("practiceId");
        if(practiceId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Practice> op=practiceRepository.findById(practiceId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Practice h= op.get();
        practiceRepository.delete(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/practiceEditSave")
    public DataResponse practiceEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer practiceId=dataRequest.getInteger("practiceId");
        if(practiceId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Practice> op=practiceRepository.findById(practiceId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Practice h=op.get();
        //再确定一下有没有缺少的字段
        String place= dataRequest.getString("place");
        String time= dataRequest.getString("time");
        String evaluation= dataRequest.getString("evaluation");
        String type=dataRequest.getString("type");
        String teamName=dataRequest.getString("teamName");
        h.setPlace(place);
        h.setTime(time);
        h.setEvaluation(evaluation);
        h.setType(type);
        h.setTeamName(teamName);
        practiceRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }

    public void localStudentDelete(Integer studentId){
        List<Practice> studentList=practiceRepository.findByStudentId(studentId);
        practiceRepository.deleteAll(studentList);
    }


}








