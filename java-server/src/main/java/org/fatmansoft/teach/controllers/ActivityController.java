package org.fatmansoft.teach.controllers;
import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.ActivityRepository;
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
@RequestMapping("/api/activity")
@RestController
public class ActivityController {
    @Autowired
    private ActivityRepository activityRepository;
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
    @PostMapping("/getActivityList")
    public DataResponse getActivityList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId = 0;

        List<Activity> cList=activityRepository.findByStudentStudentId(studentId);
        List dataList=new ArrayList<>();
        Map m;
        for(Activity s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("activityId", s.getActivityId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("student",s.getStudent());
                m.put("activityPlace",s.getActivityPlace());
                m.put("activityTime",s.getActivityTime());
                m.put("activityEvaluation",s.getActivityEvaluation());
                m.put("type",s.getType());
                m.put("content",s.getContent());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getActivityListByNumName")
    public DataResponse getActivityListByNumName(@Valid @RequestBody DataRequest dataRequest) {
            String num= dataRequest.getString("num");
            String name= dataRequest.getString("name");
            String type= dataRequest.getString("type");
            List<Activity> activityList=activityRepository.findByTypeAndNumName(num,name,type);
        List dataList=new ArrayList<>();
        Map m;
        for(Activity s:activityList){
            if (s != null) {
                m = new HashMap();
                m.put("activityId", s.getActivityId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("student",s.getStudent());
                m.put("activityPlace",s.getActivityPlace());
                m.put("activityTime",s.getActivityTime());
                m.put("activityEvaluation",s.getActivityEvaluation());
                m.put("type",s.getType());
                m.put("content",s.getContent());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/newActivity")
    public DataResponse newActivity(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId= dataRequest.getInteger("studentId");
        Optional<Student> op=studentRepository.findById(studentId);
        if(!op.isPresent() ){
            return CommonMethod.getReturnMessageError("该学生不存在");
        }
        String newActivityPlace= dataRequest.getString("newActivityPlace");
        String newActivityTime=dataRequest.getString("newActivityTime");
        String newActivityEvaluation=dataRequest.getString("newActivityEvaluation");
        String newType=dataRequest.getString("newType");
        String newContent=dataRequest.getString("newContent");
        Activity h=new Activity();
        h.setStudent(op.get());
        h.setActivityPlace(newActivityPlace);
        h.setActivityTime(newActivityTime);
        h.setActivityEvaluation(newActivityEvaluation);
        h.setContent(newContent);
        h.setType(newType);
        activityRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/deleteActivity")
    public DataResponse deleteActivity(@Valid@RequestBody DataRequest dataRequest){
        Integer activityId= dataRequest.getInteger("activityId");
        if(activityId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Activity> op=activityRepository.findById(activityId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Activity h= op.get();
        activityRepository.delete(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/activityEditSave")
    public DataResponse activityEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer activityId=dataRequest.getInteger("activityId");
        if(activityId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Activity> op=activityRepository.findById(activityId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Activity h=op.get();
        //再确定一下有没有缺少的字段
        String activityPlace= dataRequest.getString("activityPlace");
        String activityTime= dataRequest.getString("activityTime");
        String activityEvaluation= dataRequest.getString("activityEvaluation");
        String type=dataRequest.getString("type");
        String content=dataRequest.getString("content");
        h.setActivityPlace(activityPlace);
        h.setActivityTime(activityTime);
        h.setActivityEvaluation(activityEvaluation);
        h.setType(type);
        h.setContent(content);
        activityRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
}
