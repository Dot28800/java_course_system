package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.repository.UserTypeRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/secure/user")
public class UserController {
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @PostMapping("/getUser")
    public DataResponse getUserType(@Valid@RequestBody DataRequest dataRequest) {
        Integer userId = CommonMethod.getUserId();
        User u;

        Optional<User> op = userRepository.findByUserId(userId);
        Integer typeId;
        if (op.isPresent()) {
            u = op.get();
            typeId = u.getUserType().getId();
        } else {
            return CommonMethod.getReturnMessageError("该用户不存在！");
        }
        String personNum =u.getPerson().getNum();
        Map m=new HashMap<>();
        m.put("typeId",typeId.equals(2)?"学生":"管理员");
        m.put("personNum",personNum);
        m.put("userName",u.getPerson().getName());
        Integer personId=u.getPerson().getPersonId();
        if(personId==null){
            personId=0;
        }
        m.put("personId",personId);
        if(typeId.equals(2)){
            Optional<Student> op1=studentRepository.findByPersonId(personId);
            Student student=op1.isPresent()?op1.get():new Student();
            //Integer studentId= op1.isPresent() ?op1.get().getStudentId():0;
            m.put("student",student);
        }
        return CommonMethod.getReturnData(m);
    }

}
