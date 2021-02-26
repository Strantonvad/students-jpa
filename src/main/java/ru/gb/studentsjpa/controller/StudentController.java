package ru.gb.studentsjpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.gb.studentsjpa.entity.Student;
import ru.gb.studentsjpa.repository.StudentRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

  @Autowired
  private StudentRepository studentRepository;

  @GetMapping
  public String allStudents(Model model, @RequestParam(value = "name", required = false) String name) {

    List<Student> allStudents;
    if(name == null || name.isEmpty()) {
      allStudents = studentRepository.findAll();
    } else {
      allStudents = studentRepository.findByNameLike("%" + name + "%");
    }
    model.addAttribute("students", allStudents);
    return "students";
  }

  @GetMapping("/{id}/edit")
  public String editStudent(@PathVariable("id") Integer id, Model model) {
    Student student = studentRepository.findById(id).orElseThrow(() -> new NotFoundException("User"));
    model.addAttribute("student", student);
    return "student";
  }

  @GetMapping("/new")
  public String newStudent(Model model) {
    Student student = new Student();
    model.addAttribute("student", student);
    return "student";
  }

  @PostMapping("/update")
  public String updateStudent(@Valid Student student, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "student";
    }

    studentRepository.save(student);
    return "redirect:/student";
  }

  @DeleteMapping("/{id}/delete")
  public String deleteStudent(@PathVariable("id") Integer id) {
    studentRepository.deleteById(id);
    return "redirect:/student";
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelAndView notFoundExceptionHandler(NotFoundException exception) {
    ModelAndView modelAndView = new ModelAndView("not_found");
    modelAndView.getModel().put("entity_name", exception.getMessage());
    return modelAndView;
  }
}
