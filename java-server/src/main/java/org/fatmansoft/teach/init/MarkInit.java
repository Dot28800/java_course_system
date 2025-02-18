package org.fatmansoft.teach.init;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.repository.CourseChooseRepository;
import org.fatmansoft.teach.repository.HomeworkRepository;
import org.fatmansoft.teach.repository.MarkRepository;
import org.fatmansoft.teach.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarkInit {
    @Autowired
    private CourseChooseRepository courseChooseRepository;
    @Autowired
    private MarkRepository markRepository;
    @Autowired
    private HomeworkRepository homeworkRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    int maxId=1;
    public void initialize(){
        List<CourseChoose> courseChooseList=courseChooseRepository.findByStudentCourse(0,0);
        for(CourseChoose courseChoose:courseChooseList){
            Mark mark=new Mark();
            mark.setCourseChoose(courseChoose);
            Optional<Mark> op=markRepository.getByCourseChooseId(courseChoose.getId());
            if(!op.isPresent()){
                markRepository.save(mark);
            }

        }
        operate();

    }
    void operate(){
        List<Mark> markList=markRepository.findAll();
        for(Mark info:markList){
           // List<CourseChoose> chooseList=courseChooseRepository.
            CourseChoose c= info.getCourseChoose();
            List<Homework> homeworkList=homeworkRepository.findByStudentCourse(info.getCourseChoose().getStudent().getStudentId(),info.getCourseChoose().getCourse().getCourseId());

            Optional<Score> score=scoreRepository.findOpByStudentCourse(info.getCourseChoose().getStudent().getStudentId(),info.getCourseChoose().getCourse().getCourseId());
            Double homeworkMarkAvg;
            Integer mark=0;
            if(!homeworkList.isEmpty()){
                homeworkMarkAvg=getAvg(homeworkList);
            }else{
                homeworkMarkAvg=0.0;
            }

            if(score.isPresent()){
                mark=score.get().getMark();
            }
            if(homeworkMarkAvg==null){
                homeworkMarkAvg=0.0;

            }
            if(mark==null){
                mark=0;
            }

            Double totalMark=homeworkMarkAvg*0.35+mark*0.65;
            info.setMark(totalMark);
            Double GPA=(totalMark-50)/10;
            if(totalMark<60){
                GPA=0.0;
            }
            info.setGPA(GPA);
            markRepository.save(info);


        }
        List<CourseChoose> courseChooseList=courseChooseRepository.findAll();
        List<Course> courseList=new ArrayList<>();
        for(CourseChoose c:courseChooseList){
            if(!courseList.contains(c.getCourse()) ){
                courseList.add(c.getCourse());
            }
        }

        for(Course course:courseList){
            List<Mark> marks=markRepository.getByCourseId(course.getCourseId());
            Mark[] rankList=getSortedList(marks);
            for(int i=0;i< rankList.length;i++){
                rankList[i].setRank(String.valueOf(i+1));
                markRepository.save(rankList[i]);
            }

        }
    }


    Double getAvg(List<Homework> arr){
        Integer n=arr.size();
        Double sum=0.0;
        for(Homework homework:arr){
            String m= homework.getWorkMark();
            if(m==null){
                m="0.0";
            }
            sum+=Double.parseDouble(homework.getWorkMark());
        }
        return sum/n;
    }

    Mark[] getSortedList(List<Mark> mapList){
        Mark[] marks= mapList.toArray(new Mark[0]);
        Arrays.sort(marks, new Comparator<Mark>() {
            @Override
            public int compare(Mark o1, Mark o2) {
                return o2.getMark().compareTo(o1.getMark());
            }


        });
        return marks;
    }



}
