package com.example.SpringBootDemo.HomeController;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.SpringBootDemo.Repo.DATABASE;
import com.example.SpringBootDemo.Repo.RESULTDATABASE;
import com.example.SpringBootDemo.Repo.Result;
import com.example.SpringBootDemo.Repo.STUDENT;
import com.example.SpringBootDemo.Repo.SUBDATABASE;
import com.example.SpringBootDemo.Repo.Subject;
import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
public class HomeController {
	
	@Autowired
	DATABASE database;
	@Autowired
	SUBDATABASE subdatabase;
	@Autowired
	RESULTDATABASE resultdatabase;
	
	
	@RequestMapping("/")
	String getHomePage() {
		return "login";
	}
//	@RequestMapping("/save")
//	String getBack(STUDENT student) {
//		System.out.println(student.getName());
//		System.out.println(student.getStudentId());
//		database.save(student);
//		return "login";
//	}
	@RequestMapping("/validate")
	String validateUser(User user,Model model) {
		System.out.println(user.getName());
		System.out.println(user.getPassword());
		String name=user.getName();
		String password=user.getPassword();
		if(user.getName().equalsIgnoreCase("gsd")&&user.getPassword().equals("123")){
			return "redirect:/dashboard";
		}
		model.addAttribute("error","Either username or password");
		return "login";
	}
	
	@RequestMapping("/dashboard")
	String dashBoard(Model model) {
		long studentnumber=database.count();
		long subjectnumber=subdatabase.count();
		long resultCount=resultdatabase.count();
		model.addAttribute("studentNumber",studentnumber);
		model.addAttribute("subjectCount",subjectnumber);
		model.addAttribute("resultCount",resultCount);
		model.addAttribute("active","");
		
		
		return "dashboard";
	};
	@RequestMapping("/student/dashboard")
	String studentDashBoard(Model model) {
		long number=database.count();
		model.addAttribute("studentNumber",number);
		return "studentDashboard";
	};
	@RequestMapping("/student/all")
	String studentDetail(Model model) {
		List<STUDENT> data=(List<STUDENT>) database.findAll();
		System.out.print(data.get(1).getName());
		model.addAttribute("student",new STUDENT());
		return "student-detail";
	};
	
	@RequestMapping("/subjects")
	String subjects(Model model) {
		
		return "subjects";
	}
	@RequestMapping("/results")
	String getResults(Model model) {
		int count=(int) resultdatabase.count();
		model.addAttribute("resultCount",count);
		return "results";
	}
	@RequestMapping("/create/student")
		String createStudent(Model model) {
		model.addAttribute("student",new STUDENT());
		return "create-student";
	}
	@PostMapping("/create/student/validate")
	String validateStudent(@ModelAttribute("student")STUDENT student,Model model) {
		List<STUDENT> students=(List<STUDENT>)database.findAll();
		int largest=0;
		for(int i=0;i<students.size();i++) {
			if(students.get(i).getStudentId()>largest) {
				largest=students.get(i).getStudentId();
			}
			
		};
	student.setStudentId(largest+1);
	System.out.println(student.getName());
	System.out.println(student.getStudentId());
	System.out.println(student.getGender());
	System.out.println(student.getAddress());
	System.out.println(student.getAge());
	System.out.println(student.getDob());
	try {
		database.save(student);
		
		return "create-student";
		
	}catch(Exception error){
		model.addAttribute("error","Error in creating database please check the primary key value or other datatype");
		return "create-student";
	}
	
}
	@RequestMapping("/student/detail")
	String getStudentDetail(Model model){
		List<STUDENT> students=(List<STUDENT>) database.findAll();
		model.addAttribute("students",students);
		return "students";
	}
	
	@GetMapping("/student/{id}")
	String viewStudentDetail(@PathVariable(value="id")Integer studentId,Model model) {
		Optional<STUDENT> item=database.findById(studentId);
		
		STUDENT _student=new STUDENT();
		
		model.addAttribute("student",item.get());
		return "student-detail";
		
	}
	@GetMapping("/student/update/{id}")
	String updateStudent(@PathVariable(value="id")Integer studentId,Model model) {
		Optional<STUDENT> item=database.findById(studentId);
		System.out.println(studentId+item.get().getName());
		model.addAttribute("student",item.get());
		return "student-update";
	}
	@PostMapping("/student/update")
	String updateStudent(@ModelAttribute("student")STUDENT student,Model model) {
		database.save(student);
		model.addAttribute("student",student);
		return "student-update";
	}
	@GetMapping("/student/delete/{id}")
	String deleteStudent(@ModelAttribute("id")Integer studentId,Model model){
		try {
			database.deleteById(studentId);

			model.addAttribute("message","Student deleted with given id");
			List<STUDENT> student=(List<STUDENT>) database.findAll(); 
			model.addAttribute("students",student);
			
		}catch(Exception e) {
			System.out.println(e);
			List<STUDENT> student=(List<STUDENT>) database.findAll(); 
			model.addAttribute("students",student);
			model.addAttribute("message","Error in Student deleted with given id");
			
		}
		return "students";
	}
	
	@GetMapping("/subject/create")
	String createSubject(Model model) {
		model.addAttribute("subject",new Subject());
		return "create-subject";
	}
	@PostMapping("/subject/validate")
	String validateSubject(@ModelAttribute("subject")Subject subject,Model model) {
		try {
			int maxId=getSubMaxId();
			
			subject.setSubjectId(maxId+1);
			subdatabase.save(subject);
			model.addAttribute("success","Data Saved to database");
		}catch(Exception e) {
			model.addAttribute("error","Error in saving data to the database");
		}
		
		return "create-subject";
	}
	@GetMapping("/subject/view")
	String viewSubject(Model model) {
		List<Subject> data=(List<Subject>) subdatabase.findAll();
		model.addAttribute("subjects",data);
		model.addAttribute("delete","");
		return "subject-view";
	}
	@GetMapping("/subject/update/{id}")
	String getUpdate(@PathVariable(value="id")Integer id,Model model) {
		Optional<Subject> student=subdatabase.findById(id);
		model.addAttribute("subject",student.get());
		System.out.print(id);
		return "subject-update";
	}
	@PostMapping("/subject/update")
	String getUpdateValidat(@ModelAttribute("subject")Subject subject,Model model) {
		System.out.print(subject.getSubjectId());
		subdatabase.save(subject);
		Optional<Subject> subject1=subdatabase.findById(subject.getSubjectId());
		model.addAttribute("subject",subject1.get());
		
		return "subject-update";
	}
	@GetMapping("/delete/subject/{id}")
	String deleteSubject(@PathVariable(value="id")Integer id) {
		subdatabase.deleteById(id);
		return "redirect:/subject/view";
	}
	@GetMapping("/result/entry")
	String enterResult(Model model) {
		model.addAttribute("result",new Result());
		return "result-entery";
	}
	@PostMapping("/result/entry/save")
	String saveResult(@ModelAttribute("result")Result result,Model model) {
		try {
			
			int maxid=getResultMaxId();
			result.setId(maxid+1);
			System.out.println(maxid);
			resultdatabase.save(result);
			model.addAttribute("result",new Result());
			model.addAttribute("success","Data Stored in database sucess");
			return "result-entery";
		}catch(Exception e) {
			System.out.print(e);
		
			model.addAttribute("error","Error in storing data to the database");
			return "result-entery";
		}
		
	}
	@GetMapping("/result/view/all")
	String viewResult(Model model) {
		try {
			List<Result> results=(List<Result>) resultdatabase.findAll();
			model.addAttribute("results",results);
			return "result-view-all";
		}catch(Exception e) {
			return "result-view-all";
		}
	}
	
	@GetMapping("/result/update/{id}")
	String updateResult(@PathVariable(value="id")Integer id,Model model) {
		try {
			Optional<Result> data=resultdatabase.findById(id);
			model.addAttribute("result",data.get());
			
			return "result-update";
		}catch(Exception e) {
			
			return "result-update";
		}
	}
	@PostMapping("/result/entry/update")
	String updateResult(@ModelAttribute("result")Result result,Model model) {
		try {
			resultdatabase.save(result);
			model.addAttribute("success","Successfully Updated data to the database");
			return "result-update";
		}catch(Exception e) {
			model.addAttribute("error","Error in updating value to the database");
			return "result-update";
		}
	}
	@GetMapping("/result/delete/{id}")
	String deleteResult(@PathVariable("id")Integer id) {
		try {
			resultdatabase.deleteById(id);
			return "redirect:/result/view/all";
		}catch(Exception e) {
			
			return "redirect:/result/view/all";
		}
	}
	@GetMapping("/result/show/individual")
	String showResultIndividualget(Model model) {
		
		model.addAttribute("studentid",new Studentid());
		//model.addAttribute("student",new STUDENT());
		return "result-view-individual";
	}
	@PostMapping("/result/show/individual")
	String showResultIndividual(@ModelAttribute("studentid")Studentid id,Model model) {
		System.out.print(id.getId());
		Optional<STUDENT> studentitem=database.findById(id.getId());
		if(studentitem.isPresent()) {
		model.addAttribute("student",studentitem.get());
		//math-1,science-2,english-3,computer-4
		List<Result> items=(List<Result>) resultdatabase.findAll();
		Integer math=getMarksForStudent(items,id.getId(),1);
		Integer science=getMarksForStudent(items,id.getId(),2);
		Integer english=getMarksForStudent(items,id.getId(),3);
		Integer computer=getMarksForStudent(items,id.getId(),4);
		
		
		model.addAttribute("math",math);
		model.addAttribute("science",science);
		model.addAttribute("english",english);
		model.addAttribute("computer",computer);
		}else {
			model.addAttribute("error","No data found for given id");
		}
		return "result-view-individual";
	}
	
	int getSubMaxId() {
		List<Subject> data=(List<Subject>) subdatabase.findAll();
		int largest=0;
		for(int i=0;i<data.size();i++) {
			if(data.get(i).getSubjectId()>largest) {
				largest=data.get(i).getSubjectId();
			}
		};
		return largest;
	}
	int getResultMaxId() {
		List<Result> data=(List<Result>) resultdatabase.findAll();
		int largest=0;
		for(int i=0;i<data.size();i++) {
			if(data.get(i).getId()>largest) {
				largest=data.get(i).getId();
			}
		}
		return largest;
	}
	
	int getMarksForStudent(List<Result> result,Integer id,Integer subcode) {
		for(int i=0;i<result.size();i++) {
			if(result.get(i).getStudentId()==id) {
				if(result.get(i).getSubjectId()==subcode) {
					return result.get(i).getMark();
				}
			}
		}
		
		return 0;
	}
}

class Studentid{
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
