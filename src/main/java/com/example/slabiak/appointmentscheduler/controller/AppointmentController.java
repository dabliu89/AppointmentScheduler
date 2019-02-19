package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    WorkService workService;

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;


    @GetMapping("/select_service")
  public String selectService(Model model) {
        model.addAttribute("works", workService.findAll());
        model.addAttribute("appointmentForm", new AppointmentRegisterForm());
        return "appointments/select-service";
    }

   @PostMapping("/select_provider")
    public String selectProvider(@ModelAttribute("appointmentForm") AppointmentRegisterForm appointmentForm, Model model) {
        model.addAttribute("providers", userService.findByWorks(workService.findById(appointmentForm.getWorkId())));
        model.addAttribute(appointmentForm);
        return "appointments/select-provider";
    }

    @PostMapping("/select_date")
    public String selectDate(@ModelAttribute("appointmentForm") AppointmentRegisterForm appointmentForm, Model model){
        model.addAttribute(appointmentForm);
        return "appointments/select-date";
    }

    @PostMapping("/save")
    public String selectDate(@ModelAttribute("appointmentForm") AppointmentRegisterForm appointmentForm, Authentication authentication){
        Appointment appointment = new Appointment();
        appointment.setCustomer(userService.findByUserName(authentication.getName()));
        appointment.setProvider(userService.findById(appointmentForm.getProviderId()));
        appointment.setWork(workService.findById(appointmentForm.getWorkId()));
        appointment.setStart(appointmentForm.getStart());
        appointment.setEnd(appointmentForm.getEnd());
        appointmentService.save(appointment);
        return "redirect:/customers/";
    }

}
