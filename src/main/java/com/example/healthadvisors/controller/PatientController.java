package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Address;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.service.AddressService;
import com.example.healthadvisors.service.MailService;
import com.example.healthadvisors.service.PatientService;
import com.example.healthadvisors.service.UserService;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AddressService addressService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;

    @Value(value = "E:\\HealthAdvisors\\upload")
    String path;


    @GetMapping("/register")
    public String addUser() {
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute @Valid CreateUserRequest createUserRequest, BindingResult bindingResult,
                          @ModelAttribute CreatePatientRequest createPatientRequest,
                          @ModelAttribute CreateAddressRequest createAddressRequest,
                          @RequestParam("picture") MultipartFile[] uploadedFiles, ModelMap map, Locale locale) throws IOException, MessagingException {

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError allError : bindingResult.getAllErrors()) {
                errors.add(allError.getDefaultMessage());
            }
            map.addAttribute("errors", errors);
            return "register";
        }

        User user = modelMapper.map(createUserRequest, User.class);
        user.setActive(false);
        user.setToken(UUID.randomUUID().toString());
        user.setTokenCreatedDate(LocalDateTime.now());
        User newUser = userService.saveUserAsPatient(user, uploadedFiles);

        Address newAddress = addressService.save(modelMapper.map(createAddressRequest, Address.class));

        Patient patient = modelMapper.map(createPatientRequest, Patient.class);
        patient.setUser(newUser);
        patient.setAddress(newAddress);
        patientService.save(patient);


        String subject = "WELCOME TO OUR WEBSITE";
        String message = "Dear " + newUser.getName() + " " + newUser.getSurname() + ", you are successfully registered" + " For activation please click on this URL: http://localhost:8080/user/activate?token="+newUser.getToken();

        //mailService.sendHtmlEmail(newUser.getEmail(), subject, newUser, "http://localhost:8080/user/activate?token=" + newUser.getToken(), "activateUser", locale);
        mailService.sendEmail(newUser.getEmail(), subject, message);
        return "redirect:/loginPage";
    }

    @GetMapping("/user/activate")
    public String activateUser(ModelMap map,
                               @RequestParam(name = "token") String token) {

        Optional<User> user = userService.findByToken(token);
        if (!user.isPresent()) {
            map.addAttribute("message", "User does not exists");
            return "activateUser";
        }
        User userFromDb = user.get();
        if (userFromDb.isActive()) {
            map.addAttribute("message", "User already active");
            return "activateUser";
        }
        userFromDb.setActive(true);
        userFromDb.setToken(null);
        userFromDb.setTokenCreatedDate(null);
        userService.saveUser(userFromDb);
        map.addAttribute("message", "Your account has been activated. \n Please sign in");
        return "login";
    }


    @GetMapping(value = "/getPatientImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(path, picName);
    }

}