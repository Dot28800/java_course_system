package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.CompetitionRepository;
import org.fatmansoft.teach.repository.HonorRepository;
import org.fatmansoft.teach.repository.LeaveRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/leave")
public class LeaveController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LeaveRepository leaveRepository;
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
    @PostMapping("/getLeaveList")
    public DataResponse getLeaveList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId = 0;

        List<Leave> cList= leaveRepository.findByStudentStudentId(studentId);
        List dataList=new ArrayList<>();
        Map m;
        for(Leave s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("leaveId", s.getLeaveId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("leaveTime",s.getLeaveTime());
                m.put("returnTime",s.getReturnTime());
                m.put("flag",s.getFlag());
                m.put("leaveReason",s.getLeaveReason());
                m.put("destination",s.getDestination());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/newLeave")
    public DataResponse newLeave(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId= dataRequest.getInteger("studentId");
        Optional<Student> op=studentRepository.findById(studentId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("该学生不存在");
        }
        String newLeaveTime = dataRequest.getString("leaveTime");
        String newReturnTime = dataRequest.getString("returnTime");
        String newFlag = dataRequest.getString("flag");
        String newLeaveReason = dataRequest.getString("leaveReason");
        String newDestination = dataRequest.getString("destination");

        Leave h=new Leave();
        h.setStudent(op.get());
        h.setLeaveTime(newLeaveTime);
        h.setReturnTime(newReturnTime);
        h.setFlag(newFlag);
        h.setLeaveReason(newLeaveReason);
        h.setDestination(newDestination);
        leaveRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/studentNew")
    public DataResponse studentNew(@Valid@RequestBody DataRequest dataRequest){
        String num= dataRequest.getString("num");

        Optional<Student> op=studentRepository.findByPersonNum(num);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("该学生不存在");
        }
        String newLeaveTime = dataRequest.getString("leaveTime");
        String newReturnTime = dataRequest.getString("returnTime");
        String newFlag = dataRequest.getString("flag");
        String newLeaveReason = dataRequest.getString("leaveReason");
        String newDestination = dataRequest.getString("destination");

        Leave h=new Leave();
        h.setStudent(op.get());
        h.setLeaveTime(newLeaveTime);
        h.setReturnTime(newReturnTime);
        h.setFlag(newFlag);
        h.setLeaveReason(newLeaveReason);
        h.setDestination(newDestination);
        leaveRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/deleteLeave")
    public DataResponse deleteLeave(@Valid@RequestBody DataRequest dataRequest){
        Integer leaveId= dataRequest.getInteger("leaveId");
        if(leaveId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Leave> op=leaveRepository.findById(leaveId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Leave h= op.get();
        leaveRepository.delete(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/leaveEditSave")
    public DataResponse leaveEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer leaveId=dataRequest.getInteger("leaveId");
        if(leaveId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Leave> op=leaveRepository.findById(leaveId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Leave h=op.get();
        //再确定一下有没有缺少的字段
        String returnTime= dataRequest.getString("returnTime");
        String leaveTime = dataRequest.getString("leaveTime");
        String flag = dataRequest.getString("flag");
        String leaveReason= dataRequest.getString("leaveReason");
        String destination= dataRequest.getString("destination");

        h.setReturnTime(returnTime);
        h.setLeaveTime(leaveTime);
        h.setFlag(flag);
        h.setLeaveReason(leaveReason);
        h.setDestination(destination);

        leaveRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/agree")
    public DataResponse agree(@Valid@RequestBody DataRequest dataRequest){
        Integer leaveId= dataRequest.getInteger("leaveId");
        if(leaveId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Leave> op=leaveRepository.findById(leaveId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Leave h= op.get();
        h.setFlag("是");
        leaveRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/allAgree")
    public DataResponse agreeAll(@Valid@RequestBody DataRequest dataRequest) {
        List<Leave> leaveList=leaveRepository.findAll();
        for(Leave leave:leaveList)
        {
            leave.setFlag("是");
            leaveRepository.save(leave);
        }
        return CommonMethod.getReturnMessageOK();
    }

    public void localStudentDelete(Integer studentId){
        List<Leave> studentList=leaveRepository.findByStudentStudentId(studentId);
        leaveRepository.deleteAll(studentList);
    }

    @PostMapping("getByFlag")
    public DataResponse getByFlag(@Valid@RequestBody DataRequest dataRequest){
        String num= dataRequest.getString("num");
        if(num==null){
            num="";
        }
        String name= dataRequest.getString("name");
        if(name==null){
            name="";
        }
        String flag= dataRequest.getString("flag");
        if(flag==null){
            flag="";
        }
        List<Leave> leaveList=leaveRepository.findByNumNameFlag(num,name,flag);
        List dataList=new ArrayList<>();
        Map m;
        for(Leave s:leaveList){
            if (s != null) {
                m = new HashMap();
                m.put("leaveId", s.getLeaveId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("leaveTime",s.getLeaveTime());
                m.put("returnTime",s.getReturnTime());
                m.put("flag",s.getFlag());
                m.put("leaveReason",s.getLeaveReason());
                m.put("destination",s.getDestination());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/searchLeave")
    public DataResponse searchLeave(@Valid@RequestBody DataRequest dataRequest){
        List<Leave> leaveList=leaveRepository.findAll();
        boolean flag=false;
        for(Leave l:leaveList){
            if(l.getFlag().equals("待批准")){
                flag=true;
                break;
            }
        }
        return CommonMethod.getReturnMessageOK(flag?"yes":"no");
    }
    @PostMapping("/negate")
    public DataResponse negate(@Valid@RequestBody DataRequest dataRequest){
        Integer leaveId= dataRequest.getInteger("leaveId");
        if(leaveId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Leave> op=leaveRepository.findById(leaveId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Leave h= op.get();
        h.setFlag("否");
        leaveRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }

    public void localDeleteStudent(Integer studentId){
        List<Leave> leaveList=leaveRepository.findByStudentStudentId(studentId);
        leaveRepository.deleteAll(leaveList);
    }
}
